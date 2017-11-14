#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column,Integer,String,Sequence,create_engine
from sqlalchemy import and_,or_
from sqlalchemy.orm import sessionmaker
from urllib.request import urlopen,Request
import json,re

from config import DB_URI
from config import WEIBO_URL

engine = create_engine(DB_URI)
Base = declarative_base()

class Crawler_weibo(Base):
	__tablename__ = 'crawler_weibo_sample'
	id = Column(Integer,primary_key=True,autoincrement=True)
	wtext = Column(String(255))
	wlink = Column(String(255))
	wdate = Column(String(255))

Session = sessionmaker(bind=engine)

def init_db():
	Base.metadata.create_all(bind=engine)
def drop_db():
	Base.metadata.drop_all(bind=engine)

header = {
	'Connection': 'Keep-Alive',
    'Accept': 'text/html, application/xhtml+xml, */*',
    'Accept-Language': 'en-US,en;q=0.8,zh-Hans-CN;q=0.5,zh-Hans;q=0.3',
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko'
}

def main():
	page = 1
	count = 0
	exit_flag = 0
	start_flag = 0
	drop_db()
	init_db()
	session = Session()
	while(True):
		s_page = str(page)
		url = WEIBO_URL+"&page="+ s_page
		print(url)
		req = Request(url,headers = header)
		oper = urlopen(req)
		data = oper.read().decode("utf-8")
		data = json.loads(data)
		for card in data['cards']:
			wtext = card['mblog']['text']
			wlink = card['scheme']
			wdate = card['mblog']['created_at']

			wtext = re.sub(r'<(.+?)>',"",wtext)
			day = wdate.split()[0]

			# 2016-12-31 -- 2014-9-1 ，范围外退出，不将数据存如存入数据库中
			day = wdate.split()[0]
			if day == "2016-12-31":
				start_flag = 1

			if start_flag != 1:
				continue	

			count += 1	
			if day == "2014-08-31" or day == "2014-08-30":
				print("end day: %s  count: %d" % (day,count))
				exit_flag = 1
				break

			wlist = Crawler_weibo(wtext=wtext,wlink=wlink,wdate=wdate)
			session.add(wlist)
			session.commit()

		if exit_flag == 1:
			break

		page = page + 1
		
	session.close()
	print("success end")

if __name__ == '__main__':
	# drop_db()
	# init_db()
	main()
# 
