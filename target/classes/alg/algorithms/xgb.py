import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score  # 精度值
import argparse
import json
import psycopg2
from xgboost import XGBClassifier
import warnings
warnings.filterwarnings("ignore")

# 设置数据库连接参数
dbname = 'medical'
user = 'pg'
password = '111111'
host = '10.16.48.219'
port = '5432'

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


def getAllData(tablename, cols, labels, mode):

    columns = '","'.join(cols)
    sql = f'SELECT "{columns}" FROM {mode}.{tablename}'
    tableData = Find(sql)
    cols_data = pd.DataFrame(data=tableData, columns=cols)

    tlabels = '","'.join(labels)
    sql = f'SELECT "{tlabels}" FROM {mode}.{tablename}'
    tableData = Find(sql)
    labels_data = pd.DataFrame(data=tableData, columns=labels)

    return cols_data, labels_data, cols


if __name__ == '__main__':

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--table_name", type=str, default="diabetes10")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    parser.add_argument("--mode", type=str, default="public")
    args = parser.parse_args()
    mode = "software10"
    table_name = args.table_name
    table_name = "data_" + table_name + "_imputed"

    cols = args.cols
    labels = args.labels

    cols = cols.split(",")
    labels = labels.split(",")


    # columns = getColumnNames(table_name)
    data, labels, columns = getAllData(table_name, cols, labels, mode)
    seed = 42
    test_size = 0.33
    X_train, X_test, y_train, y_test = train_test_split(data, labels, test_size=test_size, random_state=seed)

    # 随机森林训练
    model = XGBClassifier()
    # print(y_train, type(y_train))
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    predictions = [value for value in y_pred]  # round() 返回浮点数x的四舍五入
    # 比较得到精度值
    accuracy = accuracy_score(y_test, predictions)
    # print(float(accuracy))
    # print(', Accuracy: %.f%%' % (accuracy * 100.0))

    # 获取特征重要性值
    importances = model.feature_importances_

    # 创建特征重要性和特征名称的元组列表
    feature_importances = [{key: round(value, 3)} for key, value in zip(columns, importances)]
    # feature_importances = list(zip(columns, importances))
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
