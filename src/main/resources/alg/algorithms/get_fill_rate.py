import pandas as pd
from sqlalchemy import create_engine
import sys
import json

def get_fill_rate():
    # 数据库配置：请根据你的数据库信息进行修改
    database_url = "postgresql://pg:111111@10.16.48.219/software10"
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
