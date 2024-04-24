import json

import numpy as np
import pandas as pd
import shap
from sqlalchemy import create_engine
from sklearn.preprocessing import MinMaxScaler
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, confusion_matrix

# 数据库参数
db_params = {
    "dbname": "medical",
    "user": "pg",
    "password": "111111",
    "host": "10.16.48.219",
    "port": 5432
}


# 从数据库加载数据的函数
def getDF(db_params, sql):
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    # 使用 SQLAlchemy 引擎执行 SQL 查询，获取 DataFrame
    data = pd.read_sql(sql, engine)
    return data






def Q_learning(modelName, lr, epsilon, gamma, declay, episodes, table_name, cols, labels):

    # 用于加载数据的SQL查询
    modename = "software10"
    sql = f"select * from {modename}.{table_name}"
    # sql = "SELECT Pregnancies, Glucose, BloodPressure, SkinThickness, Insulin, BMI, DiabetesPedigreeFunction, Age, Outcome FROM software10.data_diabetes_imputed"

    # 加载数据
    pd_data = getDF(db_params, sql)

    # X = pd_data.iloc[:, 0:8]
    # y = pd_data.iloc[:, 8]
    X = pd_data[cols]
    y = pd_data[labels]
    scaler = MinMaxScaler()
    X_scaled = scaler.fit_transform(X)

    # 划分数据集
    X_train, X_test, y_train, y_test = train_test_split(X_scaled, y, test_size=0.2
                                                        # , random_state=42
                                                        )

    n_states = len(X_train)  # 状态数为训练集的样本数
    n_actions = 2  # 动作数，假设是二分类问题

    # 初始化Q-table
    Q = np.zeros((n_states, n_actions))



    # Q-learning训练过程
    for episode in range(episodes):
        for state_index in range(n_states):
            # 探索 or 利用
            if np.random.rand() < epsilon:
                action = np.random.choice([0, 1])  # 随机选择动作
                epsilon -= declay
            else:
                action = np.argmax(Q[state_index, :])  # 选择Q值最高的动作

            # 假设奖励机制
            reward = 1 if action == y_train.iloc[state_index].item() else -1

            # 更新Q-table
            next_state_index = (state_index + 1) % n_states
            Q[state_index, action] += lr * (reward + gamma * np.max(Q[next_state_index, :]) - Q[state_index, action])

    # 使用训练好的Q-table进行预测
    predictions = np.array([np.argmax(Q[state_index, :]) for state_index in range(len(X_test))])

    def q_model_predict(features):
        predictions = []
        for i in range(features.shape[0]):
            if i >= Q.shape[0]:  # 检查索引是否有效
                print(f"Warning: Index {i} is out of bounds for Q-table with size {Q.shape[0]}.")
                continue  # 跳过这个索引，或者你可以选择其他的处理方式
            action = np.argmax(Q[i, :])
            predictions.append(action)
        return np.array(predictions)

    # shap总体解释
    # 假设 model 是已经训练好的SVM或KNN模型
    if X_train.shape[0] > 50:
        explain_data = X_train[:50]
    else:
        explain_data = X_train
    explainer = shap.KernelExplainer(q_model_predict, explain_data)
    shap_values = explainer.shap_values(explain_data)
    avg_shapvalue = np.sum(shap_values, axis=0)
    avg_shapvalue = avg_shapvalue.tolist()



    # 计算性能指标
    accuracy = accuracy_score(y_test, predictions)
    precision = precision_score(y_test, predictions)
    recall = recall_score(y_test, predictions)
    f1 = f1_score(y_test, predictions)
    matrix = confusion_matrix(y_test, predictions)
    TN, FP, FN, TP = matrix.ravel()

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
    parser.add_argument("--modelName", type=str, default="test1")
    parser.add_argument("--table_name", type=str, default="diabetes10")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    args = parser.parse_args()

    table_name = args.table_name
    table_name = "data_" + table_name + "_imputed"
    lr = args.lr
    epsilon = args.epsilon
    gamma = args.gamma
    declay = args.declay
    episodes = args.episodes
    modelName = args.modelName
    cols = args.cols
    labels = args.labels

    cols = cols.split(",")
    labels = labels.split(",")

    accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue = Q_learning(modelName, lr, epsilon, gamma, declay,episodes, table_name, cols, labels)

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





