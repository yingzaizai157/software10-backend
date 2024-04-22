import pandas as pd
from sklearn.impute import KNNImputer
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score  # 精度值
import argparse
import json
import psycopg2
from xgboost import XGBClassifier
import warnings
warnings.filterwarnings("ignore")



config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]

modename = config["modename"]
test_size = config["test_size"]

dbname = db_params["dbname"]
user = db_params["user"]
password = db_params["password"]
host = db_params["host"]
port = db_params["port"]

seed = 42




# 连接到数据库
db = psycopg2.connect(dbname=dbname, user=user, password=password, host=host, port=port)

# 创建游标对象
cur = db.cursor()


# def updateContribute(table_name, col_name, contribute):
#     sqlQuery = f"update table_cols_contribute SET model_featrues_contributions={contribute} where table_name='{table_name}' and en_col='{col_name}'"
#     try:
#         cur.execute(sqlQuery)
#         db.commit()
#         # print('数据更新成功！')
#     except psycopg2.Error as e:
#         # print("数据更新失败：" + str(e))
#         # 发生错误时回滚
#         db.rollback()
#
#
# def Insert(table_name, en_col, cn_col=None):
#
#     sqlQuery = f"INSERT INTO table_cols_contribute (table_name, en_col, cn_col) VALUE ( '{table_name}','{en_col}','{cn_col}') "
#     try:
#         cur.execute(sqlQuery)
#         db.commit()
#         # print('数据插入成功！')
#     except psycopg2.Error as error:
#         # print("数据插入失败：" + str(error))
#         db.rollback()



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

def Find(sqlQuery):
    # results=[]

    cur.execute(sqlQuery)
    results = cur.fetchall()  # tuple
    # print(results)


    return results


def getColumnNames(tablename):
    sql = f"SELECT column_name FROM information_schema.columns where table_name='{tablename}'"
    queryTuple = Find(sql)
    columns = []

    for row in queryTuple:
        columns.append(row[0])
    return columns


# def getAllData(tablename, columns, label=-1):
#     cols = '","'.join(columns)
#     sql = f'SELECT "{cols}" FROM {paradigm}.{tablename}'
#
#     tableData = Find(sql)
#     data = pd.DataFrame(data=tableData, columns=columns)
#
#
#     if label == -1:
#         labels = data[columns[-1]]
#         data.drop(columns = [columns[-1]], inplace=True)
#         columns.pop()
#     else:
#         labels = data[label]
#         data.drop(columns = [label], inplace=True)
#         columns.remove(label)
#
#     return data, labels, columns


def getAllData(tablename, cols, labels):

    columns = '","'.join(cols)
    sql = f'SELECT "{columns}" FROM {modename}.{tablename}'
    tableData = Find(sql)
    cols_data = pd.DataFrame(data=tableData, columns=cols)
    cols_data = imputeKnn(cols_data, cols)


    tlabels = '","'.join(labels)
    sql = f'SELECT "{tlabels}" FROM {modename}.{tablename}'
    tableData = Find(sql)
    labels_data = pd.DataFrame(data=tableData, columns=labels)

    return cols_data, labels_data, cols


if __name__ == '__main__':

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--table_name", type=str, default="data_diabetes23")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    parser.add_argument("--mode", type=str, default="public")
    args = parser.parse_args()
    table_name = args.table_name

    cols = args.cols
    labels = args.labels

    cols = cols.split(",")
    labels = labels.split(",")


    # columns = getColumnNames(table_name)
    data, labels, columns = getAllData(table_name, cols, labels)
    X_train, X_test, y_train, y_test = train_test_split(data, labels, test_size=test_size, random_state=seed)

    # 确保y_train和y_test是整数类型
    y_train = y_train.astype(int)
    y_test = y_test.astype(int)

    # 随机森林训练
    model = XGBClassifier()
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    predictions = [value for value in y_pred]  # round() 返回浮点数x的四舍五入
    # 比较得到精度值
    accuracy = accuracy_score(y_test, predictions)

    # 获取特征重要性值
    importances = model.feature_importances_

    # 创建特征重要性和特征名称的元组列表
    feature_importances = [{key: round(value, 3)} for key, value in zip(columns, importances)]
    print(feature_importances)

    # for col_name, val in feature_importances:
    #     res = Find(f"select * from table_cols_contribute where table_name = '{table_name}' and en_col='{col_name}'")
    #     if len(res) == 0:
    #         Insert(table_name, col_name, "null")
    #
    #     # print(round(val, 2))
    #
    #     updateContribute(table_name, col_name, round(val, 2))

    db.close()
