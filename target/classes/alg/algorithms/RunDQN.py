import os
import pickle
from dqn import Q_Network
import warnings
import numpy as np
import pandas as pd
import shap
import torch
import torch.nn as nn
import torch.nn.functional as F
from sqlalchemy import create_engine
import json
warnings.filterwarnings("ignore")
import matplotlib.pyplot as plt



# config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"

config_file = r"/root/keti3/backend/software10/alg/algorithms/config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]

MODEL_SAVE_DICTORY = config["MODEL_SAVE_DICTORY"]
SHAP_SAVE_PATH = config["SHAP_SAVE_PATH"]

INPUT_SIZE = config["INPUT_SIZE"]
HIDDEN_SIZE = config["HIDDEN_SIZE"]
OUTPUT_SIZE = config["OUTPUT_SIZE"]

modename = config["modename"]
core = config["core"] # 解释哪一类？0，1




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


def get_one_result(pobs, modelname, taskname):
    """
    读取Q网络，调用预测动作函数
    :param pobs:
    :param model:
    :return:
    """
    # Q = Q_Network()
    # Q.load_state_dict(torch.load(MODEL_SAVE_DICTORY + model +".pt"))
    Q = torch.load(os.path.join(MODEL_SAVE_DICTORY, "trained", f"DQN_{taskname}.pt"))
    get_one_pred(Q, pobs)

    # shap部分
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, "explainer", f"DQN_{taskname}.pkl")
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, "explanation", f"DQN_{taskname}.pkl")
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

    file_path3 = os.path.join(SHAP_SAVE_PATH, f"{taskname}_{modelname}_shap1.png")

    plt.savefig(file_path3, bbox_inches='tight')

    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


    explanation.values = shap_values[..., core]
    explanation.base_values = explainer.expected_value[core]
    explanation.data = np.array(pobs).reshape((1, len(pobs)))
    shap.plots.waterfall(explanation[0], show=False, max_display=N)

    file_path4 = os.path.join(SHAP_SAVE_PATH, f"{taskname}_{modelname}_shap2.png")
    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    plt.savefig(file_path4, bbox_inches='tight')

    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


if __name__ == '__main__':

    # 病人向量

    # 解析传递的参数，调用相应算法
    import argparse
    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--modelname", type=str, default="dqn")
    parser.add_argument("--taskname", type=str, default="test")
    parser.add_argument("--onedata", type=str, default="3,100,40,100.8,37,1,20,12")

    args = parser.parse_args()
    modelname = args.modelname
    taskname = args.taskname
    onedata = args.onedata
    onedata = onedata.split(",")
    onedata = [int(x) if x.isdigit() else float(x) for x in onedata]

    get_one_result(onedata, modelname, taskname)

