#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

import traceback



try:
	a = b"123"
	a = a.split('2')
	print(a)
except Exception as e:
	print(e)
	print("---------------------")
	traceback.print_exc()

