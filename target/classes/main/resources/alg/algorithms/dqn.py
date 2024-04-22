#!/usr/bin/python
# -*- coding: UTF-8 -*-
import os
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
from sklearn.impute import KNNImputer
from sklearn.model_selection import train_test_split
from sqlalchemy import create_engine
import json
warnings.filterwarnings("ignore")
from sklearn.metrics import f1_score, accuracy_score, confusion_matrix, precision_score, recall_score


config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]

MODEL_SAVE_DICTORY = config["MODEL_SAVE_DICTORY"]
FIG_SAVE_DICTORY = config["FIG_SAVE_DICTORY"]

INPUT_SIZE = config["INPUT_SIZE"]
HIDDEN_SIZE = config["HIDDEN_SIZE"]
OUTPUT_SIZE = config["OUTPUT_SIZE"]
SHAP_TRAINSIZE = config["SHAP_TRAINSIZE"]

modename = config["modename"]
test_size = config["test_size"]

# 常量和数据库参数设置
# db_params = {
#     "dbname": "software10",
#     "user": "pg",
#     "password": "111111",
#     "host": "10.16.48.219",
#     "port": 5432
# }

# 模型保存目录
# MODEL_SAVE_DICTORY = "/home/data/WorkSpace/software10/Arithmetic/trainedModel/"
# MODEL_SAVE_DICTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'
# FIG_SAVE_DICTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\fig\model'

# DQN_NET 强化学习DQN网络输入参数
# INPUT_SIZE = 8
# HIDDEN_SIZE = 64
# OUTPUT_SIZE = 2

# 训练shap解释器所用的样本数，一般在100就能近似
# SHAP_TRAINSIZE = 50



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


# 从数据库中查询知识表
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
    sql = f"select * from {modename}.{table_name}"
    pd_data = getDF(db_params, sql)
    X = imputeKnn(pd_data, cols)
    Y = pd_data[labels]
    X_train, X_test, y_train, y_test = train_test_split(X, Y, test_size=test_size,
                                                        random_state=42)  # 添加random_state以保持可复现性

    # 确保y_train和y_test是整数类型
    y_train = y_train.astype(int)
    y_test = y_test.astype(int)

    y_train = np.array(y_train)
    X_train = np.array(X_train)
    y_test = np.array(y_test)
    X_test = np.array(X_test)

    # feature_names = X.columns.tolist()
    # X_train = torch.tensor(X_train, dtype=torch.float32)
    # y_train = torch.tensor(y_train, dtype=torch.int)
    #
    # X_test = torch.tensor(X_test, dtype=torch.float32)
    # y_test =torch.tensor(y_test, dtype=torch.int)

    # 将NumPy数组转换为torch Tensor
    X_train = torch.tensor(X_train, dtype=torch.float32)
    X_test = torch.tensor(X_test, dtype=torch.float32)
    y_train = torch.tensor(y_train, dtype=torch.int64)
    y_test = torch.tensor(y_test, dtype=torch.int64)

    feature_names = X.columns.tolist()

    return X_train, X_test, y_train, y_test, feature_names


# def plot_loss_reward(total_losses, total_rewards, rate):
#
#     file_path = os.path.join(FIG_SAVE_DICTORY, f"DQN_{rate}_loss.png")
#
#     epoch = len(total_losses)
#     X = range(epoch)
#     plt.plot(X, total_losses, label = 'total_losses')
#     # 添加图例
#     plt.legend()
#     # 添加标题和坐标轴标签
#     plt.title('Loss Curve')
#     plt.xlabel('epoch')
#     # plt.ylabel('value')
#     # 在显示图表之前保存它
#     # 这里你可以指定不同的文件格式，比如png, pdf, svg等
#     plt.savefig(file_path, dpi=300, bbox_inches='tight')
#     # 清除当前图形
#     plt.clf()
#
#     file_path = os.path.join(FIG_SAVE_DICTORY, f"DQN_{rate}_reward.png")
#     plt.plot(X, total_rewards, label = 'total_rewards')
#     # 添加图例
#     plt.legend()
#     # 添加标题和坐标轴标签
#     plt.title('Reward Curve')
#     plt.xlabel('epoch')
#     # plt.ylabel('value')
#     # 在显示图表之前保存它
#     # 这里你可以指定不同的文件格式，比如png, pdf, svg等
#     plt.savefig(file_path, dpi=300, bbox_inches='tight')
#     # 清除当前图形
#     plt.clf()

class Environment1:
    """
    配置一个强化学习环境
    """
    def __init__(self, X, Y,rewardValue, feature_names, history_t=90):
        self.data = X
        self.data_label = Y
        self.current_sample_idx = 0
        self.done = False
        self.reset()
        self.rate = "model"
        self.rewardValue = rewardValue
        self.feature_names = feature_names

    def reset(self):
        self.current_sample_idx = 0
        return self.data[self.current_sample_idx]

    def step(self, act):
        reward = 0
        cols = self.feature_names

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
            #     col = li.get('risk_factors')
            #     if col in cols:
            #         position = cols.index(col)
            #         if li["is_exception"] == 1:
            #             #                     print(one_sample[lst.index(li)] )
            #             if li['exception_low'] == -1 and one_sample[position] > li["exception_up"]:
            #                 reward += li[mode] * self.rewardValue
            #             elif li['exception_up'] == -1 and one_sample[position] < li["exception_low"]:
            #                 reward += li[mode] * self.rewardValue
            #             else:
            #                 if one_sample[position] > li["exception_up"] or one_sample[position] < li[
            #                     "exception_low"]:
            #                     reward += li[mode] * self.rewardValue

        else:
            reward -= self.rewardValue


            # for li in lst:
            #     col = li.get('risk_factors')
            #     if col in cols:
            #         position = cols.index(col)
            #         if li["is_exception"] == 1:
            #             #                     print(one_sample[lst.index(li)] )
            #             if li['exception_low'] == -1 and one_sample[position] > li["exception_up"]:
            #                 reward -= li[mode] * self.rewardValue
            #             elif li['exception_up'] == -1 and one_sample[position] < li["exception_low"]:
            #                 reward -= li[mode] * self.rewardValue
            #             else:
            #                 if one_sample[position] > li["exception_up"] or one_sample[position] < li[
            #                     "exception_low"]:
            #                     reward -= li[mode] * self.rewardValue

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
            if len(pobs) == INPUT_SIZE:
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
    train_env = Environment1(X_train, y_train, reward, feature_names = cols)
    train_env.rate = rate
    Q, total_losses, total_rewards = train_dqn(train_env,iter,gamma, lr, X_train.shape[1])

    # 创建shap解释器
    if X_train.shape[0] > SHAP_TRAINSIZE:
        explain_data = X_train[:SHAP_TRAINSIZE]
    else:
        explain_data = X_train
    new_sample = [2, 138, 35, 0, 33.6, 0.127, 47]  # 新样本的特征值列表，没用，只是占位
    new_sample_tensor = torch.tensor([new_sample], dtype=torch.float32)
    explainer = shap.DeepExplainer(Q, explain_data)
    samples = explain_data
    samples = samples.detach().cpu()
    samples = samples.numpy()
    explanation = shap.Explanation(values=new_sample,
                     base_values=explainer.expected_value[0],
                     data=samples,
                     feature_names=feature_names)
    # 将解释器对象保存到文件
    # 使用os.path.join来构建文件路径，确保路径在不同操作系统下都能正确工作
    file_path1 = os.path.join(MODEL_SAVE_DICTORY , "explainer", f"DQN_{rate}.pkl")
    file_path2 = os.path.join(MODEL_SAVE_DICTORY , "explanation", f"DQN_{rate}.pkl")

    # 使用with语句和'wb'模式打开文件，确保文件会被正确关闭
    with open(file_path1, 'wb') as f:
        pickle.dump(explainer, f)
    with open(file_path2, 'wb') as f:
        pickle.dump(explanation, f)

    # 解释总体样本
    shap_values = explainer.shap_values(explain_data)
    avg_shapvalue = np.sum(shap_values[...,1], axis=0)
    avg_shapvalue = avg_shapvalue.tolist()

    # 解释新样本
    # shap_values = explainer.shap_values(new_sample_tensor)
    # shap.initjs()
    # # 可视化解释结果
    # shap_html = shap.force_plot(explainer.expected_value[0], shap_values[..., 0], show=False, matplotlib=True)
    # plt.show()

    Q.feature_names = feature_names
    file_path3 = os.path.join(MODEL_SAVE_DICTORY, "trained", f"DQN_{rate}.pt")
    # torch.save(Q, MODEL_SAVE_DICTORY + "\DQN_" + rate + ".pt")
    torch.save(Q, file_path3)

    test_env = Environment1(X_test, y_test, reward, feature_names=cols)
    # test
    test_env.rate = rate
    pobs = test_env.reset()
    test_acts = []
    test_rewards = []

    pred = []
    for _ in range(len(test_env.data)):
        pobs_tensor = torch.tensor(pobs, dtype=torch.float32).reshape(1, -1)  # Convert to PyTorch tensor
        pact = Q(pobs_tensor)
        pact = np.argmax(pact.data)
        pred.append(pact)
        test_acts.append(pact)

        obs, reward, done = test_env.step(pact)
        test_rewards.append(reward)

        pobs = obs

    pred = torch.tensor(pred)

    accuracy = accuracy_score(pred, test_env.data_label)
    precision = precision_score(pred, test_env.data_label)
    recall = recall_score(pred, test_env.data_label)
    f1 = f1_score(pred, test_env.data_label)
    matrix = confusion_matrix(pred, test_env.data_label)
    TN = int(matrix[0, 0])
    FP = int(matrix[0, 1])
    FN = int(matrix[1, 0])
    TP = int(matrix[1, 1])

    return accuracy, precision, recall, f1, TP, FN, FP, TN, total_losses, total_rewards, avg_shapvalue


def get_main():
    """
        从后台获取参数（是谁传递给模型）
        得到模型预测的准确率并打印给后台
    """
    import argparse

    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--reward", type=float, default=1)
    parser.add_argument("--epoch", type=int, default=30)
    parser.add_argument("--gamma", type=float, default=0.4)
    parser.add_argument("--learning_rate", type=float, default=0.01)
    parser.add_argument("--modelName", type=str, default="test")
    parser.add_argument("--table_name", type=str, default="data_diabetes23")
    parser.add_argument("--cols", type=str,
                        default="pregnancies,glucose,skinthickness,insulin,bmi,diabetespedigreefunction,age")
    parser.add_argument("--labels", type=str, default="outcome")
    args = parser.parse_args()



    table_name = args.table_name
    reward = args.reward
    iter = args.epoch
    gamma = args.gamma
    lr = args.learning_rate
    modelName = args.modelName
    cols = args.cols
    labels = args.labels

    cols = cols.split(",")
    labels = labels.split(",")

    accuracy, precision, recall, f1, TP, FN, FP, TN, total_losses, total_rewards, avg_shapvalue = main(modelName, reward, iter, gamma, lr, table_name, cols, labels)


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

    total_losses = {"total_losses": 'p'.join(map(str, total_losses))}
    total_losses = json.dumps(total_losses, ensure_ascii=False)

    total_rewards = {"total_rewards": 'p'.join(map(str, total_rewards))}
    total_rewards = json.dumps(total_rewards, ensure_ascii=False)

    avg_shapvalue = {"avg_shapvalue": 'p'.join(map(str, avg_shapvalue))}
    avg_shapvalue = json.dumps(avg_shapvalue, ensure_ascii=False)

    print([accuracy, precision, recall, f1, TP, FN, FP, TN, total_losses, total_rewards, avg_shapvalue])


if __name__ == '__main__':
    get_main()
