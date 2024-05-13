import json
import math
import os
import pickle
import warnings
import numpy as np
import pandas as pd
import shap
from sklearn.impute import KNNImputer
from sqlalchemy import create_engine
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, confusion_matrix
warnings.filterwarnings("ignore")


config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]
MODEL_SAVE_DICTORY = config["MODEL_SAVE_DICTORY"]
SHAP_TRAINSIZE = config["SHAP_TRAINSIZE"]
modename = config["modename"]
test_size = config["test_size"]




def Find():
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    sqlQuery = "SELECT * FROM software10.knowledge"

    try:
        # 使用 SQLAlchemy 引擎执行 SQL 查询，获取 DataFrame
        df = pd.read_sql(sqlQuery, engine)

        # 将 DataFrame 转换为字典列表
        lst = df.to_dict(orient='records')
    except Exception as e:
        print("数据查询失败：" + str(e))
        lst = []

    return lst


lst = Find()




def imputeKnn(df, feature_cols):
    # 初始化KNN插值器，设置要使用的邻居数量
    imputer = KNNImputer(n_neighbors=5)

    # 使用KNN插值填充缺失值
    df_imputed = imputer.fit_transform(df[feature_cols])

    # 将填充后的数据转换回DataFrame
    df_imputed = pd.DataFrame(df_imputed, columns=feature_cols)

    # 将填充后的数据合并到原始DataFrame中
    # df[feature_cols] = df_imputed
    return df_imputed



# 从数据库加载数据的函数
def getDF(db_params, sql):
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    # 使用 SQLAlchemy 引擎执行 SQL 查询，获取 DataFrame
    data = pd.read_sql(sql, engine)
    return data


def Q_learning(modelName, lr, epsilon, gamma, declay, episodes, table_name, cols, labels, features_label, features_doctorRate):

    # 用于加载数据的SQL查询
    sql = f"select * from {modename}.{table_name}"
    # sql = "SELECT Pregnancies, Glucose, BloodPressure, SkinThickness, Insulin, BMI, DiabetesPedigreeFunction, Age, Outcome FROM software10.data_diabetes_imputed"

    # 加载数据
    pd_data = getDF(db_params, sql)
    X = imputeKnn(pd_data, cols)
    y = pd_data[labels]
    scaler = MinMaxScaler()
    X_scaled = scaler.fit_transform(X)

    # 划分数据集
    X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=test_size, random_state=42)

    # 确保y_train和y_test是整数类型
    y_train = y_train.astype(int)
    y_test = y_test.astype(int)

    n_states = len(X_train)  # 状态数为训练集的样本数
    n_actions = 2  # 动作数，假设是二分类问题

    # 初始化Q-table
    Q = np.zeros((n_states, n_actions))

    for episode in range(episodes):
        for state_index in range(n_states):
            current_state = state_index
            if np.random.rand() < epsilon:
                action = np.random.choice([0, 1])
            else:
                action = np.argmax(Q[current_state, :])

            next_state_index = (state_index + 1) % n_states
            next_state = next_state_index

            if action == y_train.iloc[state_index].item():
                reward = 1
            else:
                reward = -1

            old_value = Q[current_state, action]
            future_max = np.max(Q[next_state, :])
            Q[current_state, action] = old_value + lr * (reward + gamma * future_max - old_value)

        # Epsilon decay at the end of each episode
        epsilon = max(0.01, epsilon - declay)  # Ensure epsilon never goes below 0.01

    predictions = np.array([np.argmax(Q[i, :]) for i in range(len(X_test))])


    def q_model_predict(features):
        Q_expanded = np.vstack([Q] * (math.ceil(len(features)/len(Q)) + 1))
        return np.array([np.argmax(Q_expanded[i, :]) for i in range(len(features))])

    # shap总体解释
    # 假设 model 是已经训练好的SVM或KNN模型
    if X_train.shape[0] > SHAP_TRAINSIZE:
        explain_data = X_train[:SHAP_TRAINSIZE]
    else:
        explain_data = X_train
    explainer = shap.Explainer(q_model_predict, explain_data)
    shap_values = explainer.shap_values(explain_data)
    avg_shapvalue = np.sum(shap_values, axis=0)
    avg_shapvalue = avg_shapvalue.tolist()
    avg_shapvalue = [abs(number) for number in avg_shapvalue]

    # 将值转化为百分比
    total = sum(avg_shapvalue)
    avg_shapvalue = [(x / total) * 100 for x in avg_shapvalue]

    # 保存模型
    # 保存模型到文件
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, 'trained', f"QLearning_{modelName}.pkl")
    with open(file_path1, 'wb') as file:
        pickle.dump(Q, file)
    # 保存SHAP
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, 'explainer', f"QLearning_{modelName}.pkl")
    with open(file_path2, 'wb') as file:
        pickle.dump(explain_data, file)
    # 保存数据预处理模块
    file_path3 = os.path.join(MODEL_SAVE_DICTORY, 'scaler', f"QLearning_{modelName}.pkl")
    with open(file_path3, 'wb') as f:
        pickle.dump(scaler, f)



    # 计算性能指标
    accuracy = accuracy_score(y_test, predictions)
    precision = precision_score(y_test, predictions)
    recall = recall_score(y_test, predictions)
    f1 = f1_score(y_test, predictions)
    conf_matrix = confusion_matrix(y_test, predictions)
    TN = int(conf_matrix[0, 0])
    FP = int(conf_matrix[0, 1])
    FN = int(conf_matrix[1, 0])
    TP = int(conf_matrix[1, 1])

    return accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue



def get_main():
    """
            从后台获取参数（是谁传递给模型）
            得到模型预测的准确率并打印给后台
        """
    import argparse

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--lr", type=float, default=0.1)
    parser.add_argument("--epsilon", type=int, default=0.99)
    parser.add_argument("--gamma", type=float, default=0.6)
    parser.add_argument("--declay", type=float, default=1e-3)
    parser.add_argument("--episodes", type=float, default=1000)
    parser.add_argument("--modelName", type=str, default="test")
    parser.add_argument("--table_name", type=str, default="heart2")
    parser.add_argument("--cols", type=str,
                        default="age,sex,cp,trestbps,chol,fbs,restecg,thalach,exang,oldpeak,slope,ca,thal")
    parser.add_argument("--labels", type=str, default="target")
    parser.add_argument("--features_label", type=str, default="cp,ca,thal,sex,exang,chol,oldpeak,age,restecg,trestbps,thalach,slope,fbs")
    parser.add_argument("--features_doctorRate", type=str, default="24.7,17.1,11.4,10.9,7.199999999999999,4.8,4.8,4.7,4.7,3.4000000000000004,3.3000000000000003,2.1,0.6")
    args = parser.parse_args()

    table_name = args.table_name
    lr = args.lr
    epsilon = args.epsilon
    gamma = args.gamma
    declay = args.declay
    episodes = args.episodes
    modelName = args.modelName
    cols = args.cols
    labels = args.labels
    features_label = args.features_label
    features_doctorRate = args.features_doctorRate

    cols = cols.split(",")
    labels = labels.split(",")
    features_label = features_label.split(",")
    features_doctorRate = features_doctorRate.split(",")

    accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue = Q_learning(modelName, lr, epsilon, gamma, declay,episodes, table_name, cols, labels, features_label, features_doctorRate)

    accuracy = {"accuracy": accuracy}
    accuracy = json.dumps(accuracy, ensure_ascii=False)
    precision = {"precision": precision}
    precision = json.dumps(precision, ensure_ascii=False)
    recall = {"recall": recall}
    recall = json.dumps(recall, ensure_ascii=False)
    f1 = {"f1": f1}
    f1 = json.dumps(f1, ensure_ascii=False)
    TP = {"TP": TP}
    TP = json.dumps(TP, ensure_ascii=False)
    FN = {"FN": FN}
    FN = json.dumps(FN, ensure_ascii=False)
    FP = {"FP": FP}
    FP = json.dumps(FP, ensure_ascii=False)
    TN = {"TN": TN}
    TN = json.dumps(TN, ensure_ascii=False)

    # total_losses = {"total_losses": 'p'.join(map(str, total_losses))}
    # total_losses = json.dumps(total_losses, ensure_ascii=False)
    #
    # total_rewards = {"total_rewards": 'p'.join(map(str, total_rewards))}
    # total_rewards = json.dumps(total_rewards, ensure_ascii=False)

    avg_shapvalue = {"avg_shapvalue": 'p'.join(map(str, avg_shapvalue))}
    avg_shapvalue = json.dumps(avg_shapvalue, ensure_ascii=False)

    print([accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue])







if __name__ == '__main__':
    get_main()





