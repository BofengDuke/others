#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

from multiprocessing import Pool
import os,time,random

def long_time_task(name):
	print('Run task %s (%s)'%(name,os.getpid()))
	start = time.time()
	time.sleep(random.random() * 3)
	runtime = time.time() - start
	print('Task %s runs %0.2f seconds'%(name,runtime))

if __name__ == '__main__':
	print('Process %s running'%os.getpid())

	pool = Pool(20)
	for i in range(21):
		pool.apply_async(long_time_task,args=(i,))

	print('Waiting all subprocessing done.')
	pool.close()
	pool.join()
	print('All subprocessing done.')



