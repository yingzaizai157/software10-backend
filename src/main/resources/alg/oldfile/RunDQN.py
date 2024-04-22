import os
import pickle

import torch
import numpy as np
import psycopg2
from shap import Explanation

from dqn1 import Q_Network
import pymysql
import json
import random
import time
import copy
import warnings
import numpy as np
import pandas as pd
import shap
import torch
import torch.nn as nn
import torch.nn.functional as F
from sklearn.model_selection import train_test_split
from sqlalchemy import create_engine
import json
import psycopg2
warnings.filterwarnings("ignore")
import matplotlib.pyplot as plt




# 设置数据库连接参数
db_params = {
    "dbname": "medical",
    "user": "pg",
    "password": "111111",
    "host": "10.16.48.219",
    "port": 5432
}



# 模型保存目录
# MODEL_SAVE_DICTORY = "/home/data/WorkSpace/software10/Arithmetic/trainedModel/"
MODEL_SAVE_DICTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'

# DQN_NET 强化学习DQN网络输入参数
INPUT_SIZE = 8
HIDDEN_SIZE = 64
OUTPUT_SIZE = 2


def Find():
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    sqlQuery = "SELECT * FROM software10.knowledge_features"

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


def getDF(db_params, sql):
    # 使用 SQLAlchemy 创建数据库连接引擎
    engine_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    engine = create_engine(engine_url)

    # 使用 SQLAlchemy 引擎执行 SQL 查询，获取 DataFrame
    data = pd.read_sql(sql, engine)
    return data


def get_torch_data(table_name, cols, labels):
    """
    将数据转换为torch可以处理的tensor格式
    :param filePath:
    :return:
    """
    modename = "software10"
    sql = f"select * from {modename}.{table_name}"
    pd_data = getDF(db_params, sql)

    # X = pd_data.iloc[:, 0:8]
    # Y = pd_data.iloc[:, 8]
    X = pd_data[cols]
    Y = pd_data[labels]
    test_size = 0.2
    X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=test_size,
                                                        random_state=42)  # 添加random_state以保持可复现性

    y_train = np.array(y_train)
    # y_train.reshape((len(X_train), 1))

    X_train = np.array(X_train)
    # X_train.reshape((len(X_train), 8))

    y_test = np.array(y_test)
    # y_test.reshape((len(X_test), 1))

    X_test = np.array(X_test)
    # X_test.reshape((len(X_test), 8))

    feature_names = X.columns.tolist()

    X_train = torch.tensor(X_train, dtype=torch.float32)
    y_train = torch.tensor(y_train, dtype=torch.int)

    X_test = torch.tensor(X_test, dtype=torch.float32)
    y_test =torch.tensor(y_test, dtype=torch.int)

    return X_train, X_test, y_train, y_test, feature_names


class Environment1:
    """
    配置一个强化学习环境
    """
    def __init__(self, X, Y,rewardValue, history_t=90):
        self.data = X
        self.data_label = Y
        self.current_sample_idx = 0
        self.done = False
        self.reset()
        self.rate = "model"
        self.rewardValue = rewardValue

    def reset(self):
        self.current_sample_idx = 0
        return self.data[self.current_sample_idx]

    def step(self, act):
        reward = 0

        current_sample = self.data[self.current_sample_idx]
        true_label = self.data_label[self.current_sample_idx]
        self.current_sample_idx += 1

        one_sample = current_sample.tolist()
        #         print("one_sample", one_sample)

        rate = "model"
        mode = "model_rate"
        if rate == "doctor":
            mode = "doctor_rate"

        # act = 0: no, 1: yes
        if act == true_label:
            reward += self.rewardValue

            # for li in lst:
            #     if li["is_exception"] == 1:
            #         #                     print(one_sample[lst.index(li)] )
            #         if li['exception_low'] == -1 and one_sample[lst.index(li)] > li["exception_up"]:
            #             reward += li[mode] * self.rewardValue
            #         elif li['exception_up'] == -1 and one_sample[lst.index(li)] < li["exception_low"]:
            #             reward += li[mode] * self.rewardValue
            #         else:
            #             if one_sample[lst.index(li)] > li["exception_up"] or one_sample[lst.index(li)] < li["exception_low"]:
            #                 reward += li[mode] * self.rewardValue

        else:
            reward -= self.rewardValue

            # for li in lst:
            #     if li["is_exception"] == 1:
            #         #                     print(one_sample[lst.index(li)] )
            #         if li['exception_low'] == -1 and one_sample[lst.index(li)] > li["exception_up"]:
            #             reward -= li[mode] * self.rewardValue
            #         elif li['exception_up'] == -1 and one_sample[lst.index(li)] < li["exception_low"]:
            #             reward -= li[mode] * self.rewardValue
            #         else:
            #             if one_sample[lst.index(li)] > li["exception_up"] or one_sample[lst.index(li)] < li["exception_low"]:
            #                 reward -= li[mode] * self.rewardValue

        if self.current_sample_idx == len(self.data):
            self.done = True
        else:
            self.done = False
            self.next_sample = self.data[self.current_sample_idx]

        return self.next_sample, reward, self.done  # next_state, reward, done


class Q_Network(nn.Module):
    """
    定义Q网络
    """
    def __init__(self, input_size, hidden_size, output_size):
        super(Q_Network, self).__init__()
        self.fc1 = nn.Linear(input_size, hidden_size)
        self.fc2 = nn.Linear(hidden_size, hidden_size)
        self.fc3 = nn.Linear(hidden_size, output_size)

    def __call__(self, x):
        h = F.relu(self.fc1(x))
        h = F.relu(self.fc2(h))
        y = self.fc3(h)
        return y

    def reset(self):
        self.zero_grad()


def get_one_pred(Q, pobs):
    """
    从Q网络中处理传递的向量，得到策略动作，患不患病
    :param Q: Q网络
    :param pobs: 病人信息变量
    :return: 策略动作，患不患病
    """
    pact = Q(torch.tensor(pobs, dtype=torch.float32).reshape(1, -1))
    pact = np.argmax(pact.data)
    if pact == 0:
        res = {"res": 0}
    else:
        res = {"res": 1}

    res = json.dumps(res, ensure_ascii=False)
    print([res])


def get_one_result(pobs, model):
    """
    读取Q网络，调用预测动作函数
    :param pobs:
    :param model:
    :return:
    """
    # Q = Q_Network()
    # Q.load_state_dict(torch.load(MODEL_SAVE_DICTORY + model +".pt"))
    MODEL_SAVE_DICTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'
    Q = torch.load(os.path.join(MODEL_SAVE_DICTORY, f"DQN_{model}.pt"))
    get_one_pred(Q, pobs)

    # shap部分
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, "explainer", f"DQN_{model}.pkl")
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, "explanation", f"DQN_{model}.pkl")
    # 使用with语句和'rb'模式打开文件
    with open(file_path1, 'rb') as f:
        # 从文件中加载（反序列化）对象
        explainer = pickle.load(f)
    with open(file_path2, 'rb') as f:
        # 从文件中加载（反序列化）对象
        explanation = pickle.load(f)
    new_sample_tensor = torch.tensor([pobs], dtype=torch.float32)
    # 解释新样本
    # shap_values = explainer(new_sample_tensor)
    shap_values = explainer.shap_values(new_sample_tensor)

    core = 0 # 解释那一类？
    # shap.initjs()
    # 可视化解释结果

    # 计算每个特征的平均绝对SHAP值（作为特征重要性的估计）
    feature_importances = np.abs(shap_values[...,0]).mean(axis=0)

    # 得到特征重要性排序的索引
    sorted_indices = np.argsort(feature_importances)[::-1]
    # 选择前 N 个最重要的特征的索引
    N = 5  # 你希望显示的特征数量
    selected_indices = sorted_indices[:N]
    # 过滤SHAP值和特征名
    filtered_shap_values = shap_values[:, selected_indices]
    feature_names = Q.feature_names
    filtered_feature_names = np.array(feature_names)[selected_indices]

    shap.force_plot(explainer.expected_value[core], shap_values[..., selected_indices, core], show=False, matplotlib=True,
                                feature_names=filtered_feature_names)
    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    plt.savefig(r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\fig\shap1.png',
                bbox_inches='tight')

    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


    explanation.values = shap_values[..., core]
    explanation.base_values = explainer.expected_value[core]
    explanation.data = np.array(pobs).reshape((1, len(pobs)))
    shap.plots.waterfall(explanation[0], show=False, max_display=N)

    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    plt.savefig(r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\fig\shap2.png',
                bbox_inches='tight')

    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()

    # plt.show()








if __name__ == '__main__':

    # 病人向量

    # 解析传递的参数，调用相应算法
    import argparse
    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--model", type=str, default="test1")
    parser.add_argument("--onedata", type=str, default="3,100,40,100.8,37,1,20")

    args = parser.parse_args()
    model = args.model
    onedata = args.onedata
    onedata = onedata.split(",")
    onedata = [int(x) if x.isdigit() else float(x) for x in onedata]

    get_one_result(onedata, model)

