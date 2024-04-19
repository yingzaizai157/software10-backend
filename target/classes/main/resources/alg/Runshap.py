# import shap
# import torch
# from dqn1 import Q_Network
# import numpy as np
# import matplotlib.pyplot as plt
#
# db_params = {
#     "dbname": "medical",
#     "user": "pg",
#     "password": "111111",
#     "host": "10.16.48.219",
#     "port": 5432
# }
#
# # 加载你的强化学习模型
# MODEL_SAVE_DIRECTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'
# model = torch.load(MODEL_SAVE_DIRECTORY + "\DQN_test1.pt")
#
# # 如果模型在 GPU 上，将其移动到 CPU
# model = model.cpu()
#
# new_sample = [2, 138, 35, 0, 33.6, 0.127, 47]  # 新样本的特征值列表
#
#
# # 准备数据，转换为模型期待的张量格式
# new_sample_tensor = torch.tensor([new_sample], dtype=torch.float32)
#
# # 使用 DeepExplainer
# explainer = shap.DeepExplainer(model, data=new_sample_tensor)
#
# # 解释新样本
# shap_values = explainer.shap_values(new_sample_tensor)
#
#
# shap.initjs()
# # 可视化解释结果
# shap_html = shap.force_plot(explainer.expected_value[0], shap_values[..., 0], show=False, matplotlib=True)
# plt.show()






#!/usr/bin/python
# -*- coding: UTF-8 -*-
import pickle
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
from sklearn.metrics import f1_score, accuracy_score, confusion_matrix, precision_score, recall_score

#
# from plotly import tools
# from plotly.graph_objs import *

# 损失图像保存地址
# LOSSPNG_PATH = 'E:/projects/medical_pro/front/Software-9-wu-main/src/assets/lossreward.png'

# 训练数据集： 糖尿病数据集
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


def train_dqn(env,iter,gamma, lr, INPUT_SIZE):
    """
    1. 设置Q网络，设置强化学习超参数
    2. 迭代强化学习
    :param env:  强化学习环境
    :return: 网络、损失、奖励
    """
    Q = Q_Network(input_size=INPUT_SIZE, hidden_size=HIDDEN_SIZE, output_size=OUTPUT_SIZE)
    Q_ast = copy.deepcopy(Q)
    optimizer = torch.optim.Adam(params=Q.parameters(), lr=lr)
    optimizer.zero_grad()

    epoch_num = iter
    step_max = len(env.data) - 1
    memory_size = 200
    batch_size = 20
    epsilon = 1.0
    epsilon_decrease = 1e-3
    epsilon_min = 0.1
    start_reduce_epsilon = 200
    train_freq = 10
    update_q_freq = 20
    gamma = gamma
    show_log_freq = 5

    memory = []
    total_step = 0
    total_rewards = []
    total_losses = []

    start = time.time()
    for epoch in range(epoch_num):
        pobs = env.reset()
        step = 0
        done = False
        total_reward = 0
        total_loss = 0

        while not done and step < step_max:

            # select act
            pact = np.random.randint(2)
            if np.random.rand() > epsilon:
                pact = Q(torch.tensor(pobs, dtype=torch.float32).reshape(1, -1))
                pact = np.argmax(pact.detach().numpy())

            # act
            obs, reward, done = env.step(pact)

            pobs = np.array(pobs, dtype=np.float32)
            # 以下假设 pobs 的长度是 8，如果不是，请将 8 替换为正确的长度
            if len(pobs) == 8:
                # add memory
                memory.append((torch.tensor(pobs, dtype=torch.float32), pact, reward,
                               torch.tensor(obs, dtype=torch.float32), done))
            if len(memory) > memory_size:
                memory.pop(0)

            # train or update q
            if len(memory) == memory_size:
                if total_step % train_freq == 0:
                    # shuffled_memory = np.random.permutation(memory)
                    random.shuffle(memory)
                    shuffled_memory = memory
                    memory_idx = range(len(shuffled_memory))
                    for i in memory_idx[::batch_size]:

                        batch = shuffled_memory[i:i + batch_size]
                        b_pobs = torch.stack([item[0] for item in batch])  # 使用 torch.stack 沿新维度组合张量
                        b_pact = torch.tensor([item[1] for item in batch], dtype=torch.int64)
                        b_reward = torch.tensor([item[2] for item in batch], dtype=torch.float32)
                        b_obs = torch.stack([item[3] for item in batch])  # 使用 torch.stack 沿新维度组合张量
                        b_done = torch.tensor([item[4] for item in batch], dtype=torch.bool)

                        q = Q(b_pobs)
                        q_a = Q_ast(b_obs).detach()
                        maxq = torch.max(q_a, dim=1).values  # 使用 torch.max 沿指定维度获取最大值
                        target = q.clone().detach()
                        for j in range(batch_size):
                            target[j, b_pact[j]] = b_reward[j] + gamma * maxq[j] * (not b_done[j])

                        Q.reset()
                        loss = F.mse_loss(q, target)
                        total_loss += loss.item()
                        optimizer.zero_grad()
                        loss.backward()
                        optimizer.step()

                if total_step % update_q_freq == 0:
                    Q_ast = copy.deepcopy(Q)

            # epsilon
            if epsilon > epsilon_min and total_step > start_reduce_epsilon:
                epsilon -= epsilon_decrease

            # next step
            total_reward += reward
            pobs = obs
            step += 1
            total_step += 1

        total_rewards.append(total_reward)
        total_losses.append(total_loss)

        if (epoch + 1) % show_log_freq == 0:
            log_reward = sum(total_rewards[((epoch + 1) - show_log_freq):]) / show_log_freq
            log_loss = sum(total_losses[((epoch + 1) - show_log_freq):]) / show_log_freq
            elapsed_time = time.time() - start
            # print('\t'.join(map(str, [epoch + 1, epsilon, total_step, log_reward, log_loss, elapsed_time])))
            start = time.time()

    return Q, total_losses, total_rewards


def main(rate, reward,iter,gamma, lr, table_name, cols, labels):
    """
    1.将数据集划分
    2.获取DQN模型，画损失和奖励图
    3.保存模型
    4.用测试集进行验证获取模型准确度
    :param rate: 谁传递的参数
    :return: 模型预测准确度
    """
    X_train, X_test, y_train, y_test, feature_names = get_torch_data(table_name, cols, labels)
    MODEL_SAVE_DIRECTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'
    Q = torch.load(MODEL_SAVE_DIRECTORY + "\DQN_test1.pt")


    # 创建shap解释器
    new_sample = [2, 138, 35, 0, 33.6, 0.127, 47]  # 新样本的特征值列表
    new_sample_tensor = torch.tensor([new_sample], dtype=torch.float32)
    explain_data = X_train[0:999, :]
    explainer = shap.DeepExplainer(Q, explain_data)
    # 解释新样本
    shap_values = explainer.shap_values(new_sample_tensor)
    shap.initjs()
    # 可视化解释结果
    shap_html = shap.force_plot(explainer.expected_value[0], shap_values[..., 0], show=False, matplotlib=True, feature_names = feature_names)
    # plt.show()

    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    plt.savefig('shap_force_plot.png', bbox_inches='tight')

    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()



    return None


def get_main():
    """
        从后台获取参数（是谁传递给模型）
        得到模型预测的准确率并打印给后台
    """
    import argparse

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--reward", type=float, default=1)
    parser.add_argument("--epoch", type=int, default=100)
    parser.add_argument("--gamma", type=float, default=0.4)
    parser.add_argument("--learning_rate", type=float, default=0.01)
    parser.add_argument("--modelName", type=str, default="test1")
    parser.add_argument("--table_name", type=str, default="diabetes10")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    args = parser.parse_args()



    table_name = args.table_name
    table_name = "data_" + table_name + "_imputed"
    reward = args.reward
    iter = args.epoch
    gamma = args.gamma
    lr = args.learning_rate
    modelName = args.modelName
    cols = args.cols
    labels = args.labels

    cols = cols.split(",")
    labels = labels.split(",")






    a = main(modelName, reward, iter, gamma, lr, table_name, cols, labels)

if __name__ == '__main__':
    get_main()
