#!-#-coding: utf8-*-
import os
import json
import MySQLdb
from xml.etree.ElementTree import parse
from xml.etree.ElementTree import ParseError
from collections import Counter

mysqlHost = "localhost"
mysqlPort = 3306
mysqlUser = "root"
mysqlPassword = "lvpd"
dbName = "irs_data"

con = MySQLdb.connect(host=mysqlHost, port=mysqlPort, user=mysqlUser, passwd=mysqlPassword, db=dbName, charset='utf8')
cursor = con.cursor(MySQLdb.cursors.DictCursor)

counter = Counter(['사회', '경제', '문화', '스포츠', '정치'])

sql = "select id, category, path FROM irs_data.news  where category in ('사회', '경제', '문화', '스포츠', '정치') order by category desc"
cursor.execute(sql)

recs = cursor.fetchall()
for rec in recs: 

    sid = rec['id'] 
    category = rec['category'] 
    path = rec['path']
    text = ""
	
    # counter[category] += 1
    # if counter[category] > 1000:
    #     continue

    try: 
        tree = parse(path)
        document = tree.getroot()
        title = document.find("title").text.encode('utf-8')
        content = document.find("content").text.encode('utf-8')
        text = title + '\n' + content
    except BaseException as e:
        print path, e

    newpath = '../raw/' + category 
    if not os.path.exists(newpath):
        os.makedirs(newpath)

    filepath = newpath + "/" + str(sid)
    f = open(filepath, 'w')
    f.write(text)
    f.close()

cursor.close()
con.close()


