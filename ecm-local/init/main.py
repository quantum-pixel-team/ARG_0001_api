import os

import psycopg2

folder_path = os.getcwd()
print("Current directory:")
print(folder_path)
print("Connecting to database...")
conn = psycopg2.connect(database="e-commerce",
                        host='postgres',
                        user="ecm",
                        password="ecm",
                        port=5432)
cur = conn.cursor()

files = os.listdir(folder_path)
files = [x for x in files if x.split(".")[0].isnumeric()]
files.sort(key=lambda x: int(x.split(".")[0]))
print(files)
print("started to insert data in DB")
for file in files:
    if file.split(".")[0].isnumeric():
        full_path_and_name = os.path.join(folder_path, file)
        content = open(full_path_and_name, "r").read()
        cur.execute(content)
conn.commit()
cur.close()
conn.close()

print("finished inserting data")
