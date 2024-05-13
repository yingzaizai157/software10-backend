import json
import math
import os
import pickle
import warnings
import numpy as np
import shap
from matplotlib import pyplot as plt
warnings.filterwarnings("ignore")

config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
# 读取常量
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]

MODEL_SAVE_DICTORY = config["MODEL_SAVE_DICTORY"]
SHAP_SAVE_PATH = config["SHAP_SAVE_PATH"]
N = config["N"]  # 你希望显示的特征数量
modename = config["modename"]


def get_one_result(onedata, modelname, taskname):
    # 从文件加载模型
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, "trained", f"QLearning_{taskname}.pkl")
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, 'explainer', f"QLearning_{taskname}.pkl")
    file_path3 = os.path.join(MODEL_SAVE_DICTORY, 'scaler', f"QLearning_{taskname}.pkl")
    with open(file_path1, 'rb') as file:
        Q = pickle.load(file)
    with open(file_path2, 'rb') as file:
        explain_data = pickle.load(file)
    with open(file_path3, 'rb') as file:
        scaler = pickle.load(file)

    def q_model_predict(features):
        Q_expanded = np.vstack([Q] * (math.ceil(len(features)/len(Q)) + 1))
        return np.array([np.argmax(Q_expanded[i, :]) for i in range(len(features))])

    # 预测
    # 首先将onedata转换为NumPy数组
    onedata_np = np.array(onedata)
    # 然后重塑onedata为2D数组
    onedata_reshaped = onedata_np.reshape(1, -1)
    onedata_reshaped = scaler.transform(onedata_reshaped)
    result = q_model_predict(onedata_reshaped).item()
    if result == 0:
        res = {"res": 0}
    else:
        res = {"res": 1}
    res = json.dumps(res, ensure_ascii=False)
    print([res])

    # shap
    explainer = shap.Explainer(q_model_predict, explain_data)
    shap_values = explainer(onedata_reshaped)
    shap_values.base_values = np.round(shap_values.base_values, decimals=2)
    shap_values.values = np.round(shap_values.values, decimals=2)

    # 计算每个特征的平均绝对SHAP值（作为特征重要性的估计）
    feature_importances = np.abs(shap_values.values).mean(axis=0)

    # 得到特征重要性排序的索引
    sorted_indices = np.argsort(feature_importances)[::-1]

    # 选择前 N 个最重要的特征的索引

    selected_indices = sorted_indices[:N]
    # 过滤SHAP值和特征名
    filtered_shap_values = shap_values[:, selected_indices]
    filtered_feature_names = np.array(shap_values.feature_names)[selected_indices]

    shap.force_plot(
        shap_values.base_values,
        filtered_shap_values.values,
        np.round(onedata_reshaped[:, selected_indices], decimals=2),
        feature_names=filtered_feature_names,
        show=False,
        matplotlib=True
    )
    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    file_path4 = os.path.join(SHAP_SAVE_PATH, f"{taskname}_{modelname}_shap1.png")
    plt.savefig(file_path4, bbox_inches='tight')
    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


    shap.plots.waterfall(shap_values[0], max_display=N, show=False)
    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    file_path5 = os.path.join(SHAP_SAVE_PATH, f"{taskname}_{modelname}_shap2.png")
    plt.savefig(file_path5, bbox_inches='tight')
    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


if __name__ == '__main__':
    import argparse
    parser = argparse.ArgumentParser(description='参数')
    parser.add_argument("--modelname", type=str, default="QLearning")
    parser.add_argument("--taskname", type=str, default="test")
    parser.add_argument("--onedata", type=str, default="63,1,1,145,233,1,2,150,0,2.3,3,0,6")

    args = parser.parse_args()
    modelname = args.modelname
    taskname = args.taskname
    onedata = args.onedata
    onedata = onedata.split(",")
    onedata = [int(x) if x.isdigit() else float(x) for x in onedata]

    get_one_result(onedata, modelname, taskname)