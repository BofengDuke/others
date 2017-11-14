#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description:  test class
'''



class Parent():
	"""docstring for Parent"""
	def __init__(self, arg):
		self.arg = arg

	def bar(self):
		print('p: '+self.arg)


class Child(Parent):
	"""docstring for Child"""
	def __init__(self, arg,name):
		self.arg = arg
		self.name = name


class Girl(Parent):
	"""docstring for Girl"""
	def __init__(self, arg):
		self.arg = arg

	def bar(self):
		print('girl: '+self.arg)
		


if __name__ == '__main__':
	# cClass = CClass("hello")
	# cClass.test()
	
	child = Child("a","c")
	child.bar()

	girl = Girl("b")
	girl.bar()


