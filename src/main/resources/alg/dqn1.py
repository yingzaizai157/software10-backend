#!/usr/bin/python
# -*- coding: UTF-8 -*-
import random
import time
import copy
import numpy as np
import pandas as pd
import torch
import torch.nn as nn
import torch.nn.functional as F
from sklearn.model_selection import train_test_split
import pymysql
import json

# from plotly.subplots import make_subplots
# from plotly import tools
# from plotly.graph_objs import *

# 数据库信息
DB_HOST = "10.16.48.219"
DB_USER = "root"
DB_PASSWORD = "111111"
DB_USE_DATABASE = "ten_Diabetes"

# 损失图像保存地址
LOSSPNG_PATH = 'D:/software10/png/lossreward.png'

# 训练数据集： 糖尿病数据集
TRAIN_SETS = "C:/Users/33125/IdeaProjects/software-software_backend/src/main/resources/alg/diabetes.csv"

# 模型保存目录
MODEL_SAVE_DICTORY = "D:\software10\model"

# DQN_NET 强化学习DQN网络输入参数
INPUT_SIZE = 8
HIDDEN_SIZE = 64
OUTPUT_SIZE = 2


def Insert():
    db = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, database=DB_USE_DATABASE)
    cur = db.cursor()
    sqlQuery = "INSERT INTO Medical_knowledge_base_reflect (id, en_col, cn_col) VALUE (%s,%s,%s) "
    value = (9, "new_col", "新字段")
    try:
        cur.execute(sqlQuery, value)
        db.commit()
        # print('数据插入成功！')
    except pymysql.Error as error:
        # print("数据插入失败：" + str(error))
        db.rollback()
    finally:
        db.close()


def Update(rate, type):
    db = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, database=DB_USE_DATABASE)
    cur = db.cursor()
    sqlQuery = "UPDATE model_info SET performance= %s WHERE reward_type=%s"
    performance = rate
    reward = type
    value = performance, reward
    try:
        cur.execute(sqlQuery, value)
        db.commit()
        # print('数据更新成功！')
    except pymysql.Error as e:
        # print("数据更新失败：" + str(e))
        # 发生错误时回滚
        db.rollback()
    finally:
        db.close()


def Find():
    db = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, database=DB_USE_DATABASE)
    cur = db.cursor()
    sqlQuery = "SELECT * FROM knowledge_features"
    try:
        cur.execute(sqlQuery)
        results = cur.fetchall()
        lst = []
        for row in results:
            dit = {
                "encol": row[1],
                "is_except": row[3],
                "low": row[4],
                "upper": row[5],
                "model_rate": row[8],
                "doctor_rate": row[9]
            }
            lst.append(dit)
    except pymysql.Error as e:
        print("数据查询失败：" + str(e))
    finally:
        db.close()

    return lst


lst = Find()


def get_torch_data(filePath):
    """
    将数据转换为torch可以处理的tensor格式
    :param filePath:
    :return:
    """
    pd_data = pd.read_csv(filePath)
    pd_data.head()

    X = pd_data.iloc[:, 0:8]
    Y = pd_data.iloc[:, 8]
    test_size = 0.33
    X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=test_size,
                                                        random_state=42)  # 添加random_state以保持可复现性

    y_train = np.array(y_train)
    y_train.reshape((len(X_train), 1))

    X_train = np.array(X_train)
    X_train.reshape((len(X_train), 8))

    y_test = np.array(y_test)
    y_test.reshape((len(X_test), 1))

    X_test = np.array(X_test)
    X_test.reshape((len(X_test), 8))

    X_train = torch.tensor(X_train, dtype=torch.float32)
    y_train = torch.tensor(y_train, dtype=torch.int)

    X_test = torch.tensor(X_test, dtype=torch.float32)
    y_test =torch.tensor(y_test, dtype=torch.int)

    return X_train, X_test, y_train, y_test


class Environment1:
    """
    配置一个强化学习环境
    """
    def __init__(self, X, Y, history_t=90):
        self.data = X
        self.data_label = Y
        self.current_sample_idx = 0
        self.done = False
        self.reset()
        self.rate = "model"

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
            reward += 1

            for li in lst:
                if li["is_except"] == 1:
                    #                     print(one_sample[lst.index(li)] )
                    if li['low'] == -1 and one_sample[lst.index(li)] > li["upper"]:
                        reward += li[mode] * 1
                    elif li['upper'] == -1 and one_sample[lst.index(li)] < li["low"]:
                        reward += li[mode] * 1
                    else:
                        if one_sample[lst.index(li)] > li["upper"] or one_sample[lst.index(li)] < li["low"]:
                            reward += li[mode] * 1
        else:
            reward -= 1
            for li in lst:
                if li["is_except"] == 1:
                    #                     print(one_sample[lst.index(li)] )
                    if li['low'] == -1 and one_sample[lst.index(li)] > li["upper"]:
                        reward -= li[mode] * 1
                    elif li['upper'] == -1 and one_sample[lst.index(li)] < li["low"]:
                        reward -= li[mode] * 1
                    else:
                        if one_sample[lst.index(li)] > li["upper"] or one_sample[lst.index(li)] < li["low"]:
                            reward -= li[mode] * 1

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


def train_dqn(env):
    """
    1. 设置Q网络，设置强化学习超参数
    2. 迭代强化学习
    :param env:  强化学习环境
    :return: 网络、损失、奖励
    """
    Q = Q_Network(input_size=INPUT_SIZE, hidden_size=HIDDEN_SIZE, output_size=OUTPUT_SIZE)
    Q_ast = copy.deepcopy(Q)
    optimizer = torch.optim.Adam(params=Q.parameters())
    optimizer.zero_grad()

    epoch_num = 5
    step_max = len(env.data) - 1
    memory_size = 200
    batch_size = 20
    epsilon = 1.0
    epsilon_decrease = 1e-3
    epsilon_min = 0.1
    start_reduce_epsilon = 200
    train_freq = 10
    update_q_freq = 20
    gamma = 0.97
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
            # print('/t'.join(map(str, [epoch + 1, epsilon, total_step, log_reward, log_loss, elapsed_time])))
            start = time.time()

    return Q, total_losses, total_rewards


def main(rate):
    """
    1.将数据集划分
    2.获取DQN模型，画损失和奖励图
    3.保存模型
    4.用测试集进行验证获取模型准确度
    :param rate: 谁传递的参数
    :return: 模型预测准确度
    """
    X_train, X_test, y_train, y_test = get_torch_data(TRAIN_SETS)
    train_env = Environment1(X_train, y_train)
    train_env.rate = rate
    Q, total_losses, total_rewards = train_dqn(train_env)
    plot_loss_reward(total_losses, total_rewards)
    # 保存
    # with open("C:/Users/pc/PycharmProjects/pythonProject/dqn_"+rate+".pt", 'wb') as f:
    #     torch.save(Q, f)
    torch.save(Q.state_dict(), MODEL_SAVE_DICTORY + "dqn_" + rate + ".pt")
    # torch.save(Q, "C:/Users/pc/PycharmProjects/pythonProject/dqn_"+rate+".pt")
    # 读取
    # Q = torch.load(PATH)

    test_env = Environment1(X_test, y_test)
    # test
    test_env.rate = rate
    pobs = test_env.reset()
    test_acts = []
    test_rewards = []

    acc = 0
    for _ in range(len(test_env.data) - 1):
        pobs_tensor = torch.tensor(pobs, dtype=torch.float32).reshape(1, -1)  # Convert to PyTorch tensor
        pact = Q(pobs_tensor)
        pact = np.argmax(pact.data)
        if pact == test_env.data_label[_]:
            acc += 1
        test_acts.append(pact)

        obs, reward, done = test_env.step(pact)
        test_rewards.append(reward)

        pobs = obs

    return acc / len(test_env.data)


def get_main():
    """
        从后台获取参数（是谁传递给模型）
        得到模型预测的准确率并打印给后台
    """
    rate = "model"
    type = "异常指标+模型"
    import argparse

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--rate", type=str, default=None)
    args = parser.parse_args()
    # print(type(rate))

    rate = args.rate
    if rate == None:
        rate = "model"
    else:
        type = "异常指标+医生"
    # print("======================/n rate = ", rate)

    acc = main(rate)

    # print(type, "acc", round(acc, 2))
    Update(round(acc * 100, 2), type)
    accuracy = {"accuracy": acc}
    accuracy = json.dumps(accuracy, ensure_ascii=False)
    print([accuracy])


# 画图
# def plot_loss_reward(total_losses, total_rewards):
#     figure = tools.make_subplots(rows=1, cols=2, subplot_titles=('loss', 'reward'), print_grid=False)
#     figure.append_trace(Scatter(y=total_losses, mode='lines', line=dict(color='skyblue')), 1, 1)
#     figure.append_trace(Scatter(y=total_rewards, mode='lines', line=dict(color='orange')), 1, 2)
#     figure['layout']['xaxis1'].update(title='epoch')
#     figure['layout']['xaxis2'].update(title='epoch')
#     figure['layout'].update(height=400, width=900, showlegend=False)
#
#     figure.write_image(LOSSPNG_PATH)
from plotly.subplots import make_subplots
from plotly.graph_objs import Scatter

def plot_loss_reward(total_losses, total_rewards):
    figure = make_subplots(rows=1, cols=2, subplot_titles=('loss', 'reward'), print_grid=False)
    figure.add_trace(Scatter(y=total_losses, mode='lines', line=dict(color='skyblue')), row=1, col=1)
    figure.add_trace(Scatter(y=total_rewards, mode='lines', line=dict(color='orange')), row=1, col=2)
    figure.update_xaxes(title_text='epoch', row=1, col=1)
    figure.update_xaxes(title_text='epoch', row=1, col=2)
    figure.update_layout(height=400, width=900, showlegend=False)

    # figure.write_image(LOSSPNG_PATH)




if __name__ == '__main__':
    get_main()
