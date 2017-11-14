
# Database config
HOSTNAME = ''
DATABASE = ''
USERNAME = ''
PASSWORD = ''

DB_URI = 'mysql+pymysql://{}:{}@{}/{}?charset=utf8'.format(USERNAME,PASSWORD,HOSTNAME,DATABASE)

# url=WEIBO_URL = http://m.weibo.cn/container/getIndex?type=uid&value=1875333245&containerid=1076031875333245&page=1
WEIBO_URL = "http://m.weibo.cn/container/getIndex?type=uid&value=1875333245&containerid=1076031875333245"
# http://m.weibo.cn/container/getIndex?type=uid&value=1875333245&containerid=1076031875333245`