import json
import pandas as pd
import numpy as np
from sqlalchemy import create_engine, text
import argparse
import warnings
warnings.filterwarnings("ignore")

config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"

# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]
modename = config["modename"]




def getInfos(df, feature_cols):
    df[feature_cols] = df[feature_cols].replace(to_replace=[0], value=[np.nan])
    # 计算每列的缺失值比率
    missing_rate = df.isnull().mean()

    # 转换为字典格式
    missing_rate_dict = missing_rate.to_dict()
    return missing_rate_dict


# def imputeKnn(df, feature_cols, db_params):
#     # 初始化KNN插值器，设置要使用的邻居数量
#     imputer = KNNImputer(n_neighbors=5)
#
#     # 使用KNN插值填充缺失值
#     df_imputed = imputer.fit_transform(df[feature_cols])
#
#     # 将填充后的数据转换回DataFrame
#     df_imputed = pd.DataFrame(df_imputed, columns=feature_cols)
#
#     # 将填充后的数据合并到原始DataFrame中
#     df[feature_cols] = df_imputed
#     return df


def getDF(db_params, sql):
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    # 获取数据库连接对象
    with engine.connect() as connection:
        # 使用连接执行 SQL 查询
        result = connection.execute(sql)

        # 将查询结果转换为 DataFrame
        data = pd.DataFrame(result.fetchall(), columns=result.keys())
        data = data.astype(float)

    feature_cols = []
    temp = data.columns
    for i in range(temp.shape[0]):
        if i != temp.shape[0]-1:
            feature_cols.append(temp[i])



    # 获取各列数据缺失值比例
    # feature_cols = [col for col in data.columns if col not in labels_col]
    missing_rate_dict = getInfos(data, feature_cols)
    # missing_rate_dict = [{key: value} for key, value in missing_rate_dict.items()]
    feature_missing_rate = [{key: 1-value} for key, value in missing_rate_dict.items() if key in feature_cols]
    labels_missing_rate = [{key: 1-value} for key, value in missing_rate_dict.items() if key not in feature_cols]
    missing_rate_dict = []
    missing_rate_dict.append(feature_missing_rate)
    missing_rate_dict.append(labels_missing_rate)
    print(missing_rate_dict)

    # # 缺失填充并建立新的表
    # df = imputeKnn(data, feature_cols, db_params)
    # # 将填充后的数据写回数据库
    # if mode == "public":
    #     table_name = "data_" + table_name
    # with engine.connect() as connection:
    #     # 使用连接执行 SQL 查询
    #     df.to_sql(table_name + "_imputed", connection, schema='software10', if_exists='replace', index=False)

    # 将填充后的数据写回数据库
    # df.to_sql("data_diabetes_imputed", engine, schema='software10', if_exists='replace', index=False)


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--table_name", type=str, default="heart2")
    parser.add_argument("--modename", type=str, default="public")
    args = parser.parse_args()

    table_name = args.table_name

    str = "select * from " + modename + "." + table_name
    sql = text(str)
    getDF(db_params, sql)
