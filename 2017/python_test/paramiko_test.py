#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

import paramiko,time

linuxhost = "120.77.212.197"
linuxport = 29756
username = "abc"
password = "123456"
rootpasswd = "bofeng96OGC"


client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(linuxhost,linuxport,username,password)

print('Login success!')

channel = client.invoke_shell()

channel.send("ifconfig -a | grep -E -o 'inet addr:([0-9]{1,3}\.){3}[0-9]{1,3}' | awk '{print $2}' | sed '/127\.0\..\../d'")
time.sleep(0.2)

while True:
	result = channel.recv(2048)
	print(repr(result))
	if result.endswith(b'$'):
		break

	# status = channel.recv_exit_status()
	# print(status)
# while True:
# 	result = channel.recv(4096)
# 	print(repr(result))
