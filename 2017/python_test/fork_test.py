#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

import os

print('Process %s is start'%os.getpid())

for i in range(3):

	print("#### "+str(i))

	pid = os.fork()

	if pid == 0 :
		print("I am a child process ( %s ) and my parent is %s"%(os.getpid(),os.getppid()))
	else:
		print('I ( %s ) just created a child process %s'%(os.getpid(),pid))

	print('\n')

