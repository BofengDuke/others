#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

import threading,time,random
from queue import Queue
import os

nums = []
lock = threading.Lock()

class ThreadRun(threading.Thread):
	"""docstring for ThreadRun"""
	def __init__(self, name,queue):
		super(ThreadRun, self).__init__(name=name)
		self.name = str(name)
		self.queue = queue

	def run(self):
		global nums
		while not self.queue.empty():
			print('name %s (%s) is running'%(threading.current_thread().name,os.getpid()))
			tmp = self.queue.get()
			start = time.time()
			process(start,str(tmp),self.name)
			print('name %s (%s) end.'%(self.name,os.getpid()))


def abc(tmp,name):
	print('name: %s tmp: %s '%(name,tmp))
	time.sleep(1)

def process(start,tmp,name):
	abc(tmp,name)
	
	lock.acquire()
	try:
		runtime = time.time() - start
		nums.append(runtime)
	finally:
		lock.release()

def run():
	queue = Queue()
	for i in range(20):
		queue.put(i)

	threads = []

	for i in range(10):
		thread = ThreadRun("thread_"+str(i),queue)
		threads.append(thread)
		thread.start()

	for thread in threads:
		thread.join()


if __name__ == '__main__':
	run()







