import torch
import numpy as np
from dqn1 import Q_Network
import pymysql
import json


# 数据库信息
DB_HOST = "10.16.48.219"
DB_USER = "root"
DB_PASSWORD = "111111"
DB_USE_DATABASE = "ten_Diabetes"

# DQN_NET 强化学习DQN网络输入参数
INPUT_SIZE = 8
HIDDEN_SIZE = 64
OUTPUT_SIZE = 2

# 模型保存目录
MODEL_SAVE_DICTORY = "C:/Users/pc/PycharmProjects/pythonProject/"

def Find():
    """
    查询保存的病人数据
    :return:
    """
    db = pymysql.connect(host=DB_HOST, user=DB_USER, password=DB_PASSWORD, database=DB_USE_DATABASE)
    cur = db.cursor()
    sqlQuery = "SELECT * FROM person where id = 2"
    try:
        cur.execute(sqlQuery)
        results = cur.fetchall()
        lst = []
        for row in results:
            # print(list(row))
            lst.append(list(row)[1: len(row) - 1])
        # print(lst[0])
    except pymysql.Error as e:
        print("数据查询失败：" + str(e))
    finally:
        db.close()

    return lst[0]


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
    Q = Q_Network(input_size=INPUT_SIZE, hidden_size=HIDDEN_SIZE, output_size=OUTPUT_SIZE)
    Q.load_state_dict(torch.load(MODEL_SAVE_DICTORY + "dqn_" +model+".pt"))

    get_one_pred(Q, pobs)


if __name__ == '__main__':
    # pobs = [6, 0, 0, 35, 0, 33.6, 0.627, 50]
    # get_one_result(pobs)

    # 病人向量
    pobs = Find()

    # 解析传递的参数，调用相应算法
    model = "model"
    import argparse
    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--model", type=str, default=None)
    args = parser.parse_args()

    model = args.model
    if model == None:
        model = "model"

    get_one_result(pobs, model)

