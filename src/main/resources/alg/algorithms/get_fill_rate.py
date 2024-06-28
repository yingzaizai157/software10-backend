import warnings
import pandas as pd
from sqlalchemy import create_engine
import sys
import json

warnings.filterwarnings("ignore")

config_file = r"/root/keti3/backend/software10/alg/algorithms/config.json"
# config_file = r"D:\Code\Java\software10\software-software_backend\src\main\resources\alg\algorithms\config.json"
with open(config_file) as json_file:
    config = json.load(json_file)
db_params = config["db_params"]


# dbname = db_params["dbname"]
# user = db_params["user"]
# password = db_params["password"]
# host = db_params["host"]
# port = db_params["port"]

def get_fill_rate():
    # 数据库配置：请根据你的数据库信息进行修改
    # database_url = f"postgresql://pg:111111@10.16.48.219/software10"
    database_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@{db_params['host']}:{db_params['port']}/{db_params['dbname']}"
    # database_url = f"postgresql+psycopg2://{db_params['user']}:{db_params['password']}@10.16.48.219:{db_params['port']}/{db_params['dbname']}"
    table_name = sys.argv[1]

    # 创建数据库引擎
    engine = create_engine(database_url)

    # 通过SQLAlchemy引擎读取数据表到DataFrame
    with engine.connect() as conn, conn.begin():
        df = pd.read_sql_table(table_name, conn)

    # 计算缺失值统计信息
    missing_counts = df.isnull().sum()  # 缺失值计数
    total_counts = df.shape[0]  # 总行数
    missing_percentages = 100 - (missing_counts / total_counts) * 100  # 缺失比例

    # 将结果整合到DataFrame中
    missing_stats = pd.DataFrame({
        'column_name': df.columns,
        'missing_percentage': missing_percentages
    })
    return missing_stats


if __name__ == '__main__':
    stats = get_fill_rate()
    data = {
        'column_name': stats['column_name'].tolist(),
        'miss_rate': stats['missing_percentage'].tolist()
    }
    json_string = json.dumps(data, ensure_ascii=False)
    print(json_string)
