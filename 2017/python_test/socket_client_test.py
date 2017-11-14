#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

# Echo client program
import socket

HOST = 'localhost'    # The remote host
PORT = 50007              # The same port as used by the server

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))
s.sendall('Hello, world'.encode())
data = s.recv(1024)
s.close()
print(type(data))
print(repr(data))