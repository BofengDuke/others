#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

import threading,time,random

num = []
lock = threading.Lock()

def count(a):
	global num
	num.append(a)
	print(a)

def getNum():
	start = time.time()
	time.sleep(random.random()*3)
	runtime1 = time.time() - start
	start = time.time()
	time.sleep(random.random()*3)
	runtime2 = time.time() - start
	return round(runtime1,2),round(runtime2,2)

def loop(n):
	
	tmp = getNum()

	lock.acquire()
	try:
		print('thread %s is running'%threading.current_thread().name)
		count(tmp)
	finally:
		print('thread %s end.'%threading.current_thread().name)
		lock.release()
		
	

if __name__ == '__main__':

	print('thread %s is running'%threading.current_thread().name)

	threads = []
	for i in range(20):
		thread = threading.Thread(target=loop,name='thread_loop_'+str(i),args=(i,))
		threads.append(thread)
		thread.start()

	for thread in threads:
		thread.join()

	print(num)
	print('thread %s end.'%threading.current_thread().name)

