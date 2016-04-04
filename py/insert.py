#!-#-coding: utf8-*-
import os

import json
import MySQLdb
from xml.etree.ElementTree import parse
from xml.etree.ElementTree import ParseError


mysqlHost = "localhost"
mysqlPort = 3306
mysqlUser = "root"
mysqlPassword = "lvpd"
dbName = "irs_data"

matches = []

# global con
# if con == None:

con = MySQLdb.connect(host=mysqlHost, port=mysqlPort, user=mysqlUser, passwd=mysqlPassword, db=dbName, charset='utf8')
cursor = con.cursor(MySQLdb.cursors.DictCursor)


import os
source = "../2016"

for root, dirnames, filenames in os.walk(source):
    for filename in filenames:
        matches.append(os.path.join(root, filename))

# matches.append("../2016/02/27/43629648.xml");

count = 0
for path in matches:  
	if os.path.splitext(path)[1] == ".xml":
		
		try: 
			tree = parse(path)
			document = tree.getroot()


			media_name = document.find("media_name").text.encode('utf-8') 
			article_date = document.find("article_date").text.encode('utf-8')
			category = document.find("category").text.encode('utf-8')
			title = document.find("title").text.encode('utf-8')

			sql = "INSERT INTO irs_data.news (media_name,article_date,category,title,path) VALUES ('{0}', '{1}', '{2}','{3}','{4}')" .format(media_name, article_date, category, title, path)
			cursor.execute(sql)

		except BaseException as e:
			print path, e

		count += 1

		if (count % 1000) == 0:
			print "commit ", count
			con.commit()

con.commit()
print count