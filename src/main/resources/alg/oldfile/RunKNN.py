import json
import os
import pickle
import warnings

import numpy as np
import shap
from matplotlib import pyplot as plt
warnings.filterwarnings("ignore")



MODEL_SAVE_DICTORY = r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\models'
N = 5  # 你希望显示的特征数量


def get_one_result(onedata, model):
    # 从文件加载模型
    file_path1 = os.path.join(MODEL_SAVE_DICTORY, f"KNN_{model}.pkl")
    file_path2 = os.path.join(MODEL_SAVE_DICTORY, 'explainer', f"KNN_{model}.pkl")
    file_path3 = os.path.join(MODEL_SAVE_DICTORY, 'scaler', f"KNN_{model}.pkl")
    with open(file_path1, 'rb') as file:
        KNN = pickle.load(file)
    with open(file_path2, 'rb') as file:
        explainer = pickle.load(file)
    with open(file_path3, 'rb') as file:
        scaler = pickle.load(file)

    # 预测
    # 首先将onedata转换为NumPy数组
    onedata_np = np.array(onedata)
    # 然后重塑onedata为2D数组
    onedata_reshaped = onedata_np.reshape(1, -1)
    onedata_reshaped = scaler.transform(onedata_reshaped)
    result = KNN.predict(onedata_reshaped).item()
    if result == 0:
        res = {"res": 0}
    else:
        res = {"res": 1}
    res = json.dumps(res, ensure_ascii=False)
    print([res])

    # shap
    shap_values = explainer(onedata_reshaped)
    shap_values.base_values = np.round(shap_values.base_values, decimals=2)
    shap_values.values = np.round(shap_values.values, decimals=2)
    # shap.force_plot(explainer.expected_value, shap_values, show=False, matplotlib=True)

    # 假设 shap_values 是已经计算好的 SHAP 值
    # shap_values = explainer(X_test)

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
    plt.savefig(r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\fig\shap1.png',
                bbox_inches='tight')
    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()


    shap.plots.waterfall(shap_values[0], max_display=N, show=False)
    # 保存图像到文件。这里可以指定不同的格式，比如PNG, PDF, SVG等。
    plt.savefig(r'D:\Code\Java\software10\software-software_backend\src\main\resources\alg\fig\shap2.png',
                bbox_inches='tight')
    # 清除当前的figure，避免在后续的plot中出现重叠
    plt.clf()











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