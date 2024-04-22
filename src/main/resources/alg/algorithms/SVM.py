import os
import pickle
import pandas as pd
from sklearn.impute import KNNImputer
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score, \
    confusion_matrix
import warnings
import json
import shap
import numpy as np
from sqlalchemy import create_engine
warnings.filterwarnings("ignore")


config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]

modename = config["modename"]
MODEL_SAVE_DICTORY = config["MODEL_SAVE_DICTORY"]
SHAP_TRAINSIZE = config["SHAP_TRAINSIZE"]
test_size = config["test_size"]




# 缺失值补齐
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


def getDF(db_params, sql):
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    # 使用 SQLAlchemy 引擎执行 SQL 查询，获取 DataFrame
    data = pd.read_sql(sql, engine)
    return data


def runSVM(modelName, data, kernel, random_state, paramRange, cv, cols, labels):
    X = imputeKnn(data, cols)
    y = data[labels]

    # 划分数据集为训练集和测试集
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size, random_state=random_state)

    # 确保y_train和y_test是整数类型
    y_train = y_train.astype(int)
    y_test = y_test.astype(int)

    # 特征缩放
    scaler = StandardScaler()
    X_train_scaled = scaler.fit_transform(X_train)
    X_test_scaled = scaler.transform(X_test)

    # 创建SVM分类器
    svm_clf = SVC(kernel=kernel, random_state=random_state, C=paramRange)


    # # 创建一个包含不同C值的列表，这些值将用于正则化
    # param_grid = {'C': list(range(1, paramRange, 1))}
    #
    # # 创建网格搜索对象
    # grid_search = GridSearchCV(estimator=svm_clf, param_grid=param_grid, cv=cv, scoring='accuracy')
    #
    # # 在训练集上执行网格搜索
    # grid_search.fit(X_train_scaled, y_train)
    # # 使用最佳参数的模型进行预测
    # best_clf = grid_search.best_estimator_

    svm_clf.fit(X_train_scaled, y_train)
    y_pred = svm_clf.predict(X_test_scaled)

    # shap总体解释
    # 假设 model 是已经训练好的SVM或KNN模型
    if X_train_scaled.shape[0] > SHAP_TRAINSIZE:
        explain_data = X_train_scaled[:SHAP_TRAINSIZE]
    else:
        explain_data = X_train_scaled
    explainer = shap.Explainer(svm_clf.predict, explain_data)
    explainer.feature_names = cols
    shap_values = explainer(explain_data)
    avg_shapvalue = np.sum(shap_values.values, axis=0)
    avg_shapvalue = avg_shapvalue.tolist()

    # 保存模型
    # 保存模型到文件
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, 'trained', f"SVM_{modelName}.pkl")
    with open(file_path1, 'wb') as file:
        pickle.dump(svm_clf, file)
    # 保存SHAP
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, 'explainer', f"SVM_{modelName}.pkl")
    with open(file_path2, 'wb') as file:
        pickle.dump(explainer, file)
    # 保存数据预处理模块
    file_path3 = os.path.join(MODEL_SAVE_DICTORY, 'scaler', f"SVM_{modelName}.pkl")
    with open(file_path3, 'wb') as f:
        pickle.dump(scaler, f)

    # 计算准确率和分类报告
    accuracy = accuracy_score(y_test, y_pred)
    precision = precision_score(y_test, y_pred)
    recall = recall_score(y_test, y_pred)
    f1 = f1_score(y_test, y_pred)

    conf_matrix = confusion_matrix(y_test, y_pred)
    # 提取 TP、TN、FP、FN
    TN = int(conf_matrix[0, 0])
    FP = int(conf_matrix[0, 1])
    FN = int(conf_matrix[1, 0])
    TP = int(conf_matrix[1, 1])

    return accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue



if __name__ == '__main__':
    import argparse

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--kernel", type=str, default='linear')
    parser.add_argument("--random_state", type=int, default=45)
    parser.add_argument("--paramRange", type=int, default=5)
    parser.add_argument("--cv", type=int, default=5)
    parser.add_argument("--modelName", type=str, default="test")
    parser.add_argument("--table_name", type=str, default="data_diabetes23")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    args = parser.parse_args()

    kernel, random_state, paramRange, cv, modelName, table_name, cols, labels = args.kernel, args.random_state, \
        args.paramRange, args.cv, args.modelName, args.table_name, args.cols, args.labels

    cols = cols.split(",")
    labels = labels.split(",")


    sql = f"select * from {modename}.{table_name}"
    data = getDF(db_params, sql)
    accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue = runSVM(modelName, data, kernel, random_state, paramRange, cv, cols, labels)
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

    avg_shapvalue = {"avg_shapvalue": 'p'.join(map(str, avg_shapvalue))}
    avg_shapvalue = json.dumps(avg_shapvalue, ensure_ascii=False)

    print([accuracy, precision, recall, f1, TP, FN, FP, TN, avg_shapvalue])
