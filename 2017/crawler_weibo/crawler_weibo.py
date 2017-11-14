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
	__tablename__ = 'crawler_weibo_detail'
	id = Column(Integer,primary_key=True,autoincrement=True)
	wtext = Column(String(255))
	wlink = Column(String(255))
	wdate = Column(String(30))
	retweeted_id = Column(String(30)) # 转发信息的id, http://m.weibo.cn/u/retweeted_id
	retweeted_url = Column(String(255))
	retweeted_text = Column(String(255))
	page_info_type = Column(String(30))
	page_info_title = Column(String(100))
	page_info_pageurl = Column(String(255))


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


def crawl_msg():
	page = 1
	count = 0
	exit_flag = 0
	start_flag = 0

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
			wtext = re.sub(r'<(.+?)>',"",wtext)
			wlink = card['scheme']
			wdate = card['mblog']['created_at']
			retweeted_id = ""
			retweeted_url = ""
			retweeted_text = ""
			page_info_type = ""
			page_info_title = ""
			page_info_pageurl = ""

			if 'retweeted_status' in card['mblog'].keys():
				retweeted_id = card['mblog']['retweeted_status']['id']
				retweeted_url = "http://m.weibo.cn/status/"+retweeted_id
				retweeted_text = card['mblog']['retweeted_status']['text']
			
			if 'page_info' in card['mblog'].keys():
				page_info_type = card['mblog']['page_info']['type']
				page_info_title = card['mblog']['page_info']['page_title']
				page_info_pageurl = card['mblog']['page_info']['page_url']

			

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

			# print(count,day)

			wlist = Crawler_weibo(wtext=wtext,wlink=wlink,wdate=wdate,retweeted_id=retweeted_id,retweeted_url=retweeted_url,retweeted_text=retweeted_text,page_info_type=page_info_type,page_info_title=page_info_title,page_info_pageurl=page_info_pageurl)
			session.add(wlist)
			session.commit()

		if exit_flag == 1:
			break

		page = page + 1
		
	session.close()
	print("crawl success")

def test():
	page = 1
	count = 0

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
		if 'page_info' in  card['mblog'].keys():
			page_info_type = card['mblog']['page_info']['type']
			print(day,count,page_info_type)
		
		
		count += 1
			




	print("crawl success")

if __name__ == '__main__':
	# drop_db()
	# init_db()
	crawl_msg()

	# test()

