#!/usr/bin/python3
#-*- coding:UTF-8 -*-

import socket

def client(host,port,data):
	client = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	client.connect((host,port))
	data = data.encode('utf-8')
	client.sendall(data)
	getData = client.recv(2048)
	print("client get replay: ",end='')
	print(getData.decode('utf-8'))
	client.close()

def server(host,port):
	retData = 'success'
	retData = retData.encode('utf-8')
	server = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	server.bind((host,port))
	server.listen(3)
	
	while 1:
		print("## Wait data from client: ")
		conn,addr = server.accept()
		print("\nConnect by: ",addr)
		
		data = conn.recv(2048)
		# print("server get data: ")
		# print(data)
		conn.sendall(retData)
		return data
	server.close()

# 字符数字转为二进制,并填0
def strNum2bin(strNum,width=1):
	if(strNum.isdigit()):
		num = bin(int(strNum)).lstrip("0b")
		num = num.rjust(width,"0")
		return num


# IP字符串转为32位二进制
def strIP2bin(strNumIp):
	width = 8		
	num = strNumIp.split('.')
	IP = ""
	for i in range(len(num)):
		IP += strNum2bin(num[i],width)
	return IP

def binIP2Hex(IPbin):
	IPlist = []
	tmp = ''
	for i in range(len(IPbin)):
		tmp = IPbin[i]
		if (i+1)%8 == 0:
			IPlist.append(tmp)
			tmp = ''
	IP = ".".join(IPlist)
	return IP

# MAC 16进制转48位bit
def strMAC2bin(strMAC):
	width = 8
	num = strMAC.split(':')
	MAC = ""
	for i in range(len(num)):
		tmpMAC = bin(int(num[i],16)).lstrip('0b').rjust(width,'0')
		MAC += tmpMAC
	return MAC

# MAC 48位bit转16进制
def binMAC2hex(MACbin):
	MAClist = []
	tmp = ''
	for i in range(len(MACbin)):
		tmp += MACbin[i]
		if (i+1)%8 == 0:
			MAClist.append(hex(int(tmp,2)).lstrip('0x').rjust(2,"0"))
			tmp = ''
	MAC = ":".join(MAClist)
	return MAC

		
# @param header: 传入头部的二进制字符串
# @param len ，以16位为单位，总共求和个数
def  Checksum(header,size):
	data = []
	j = 0
	tmp = ""
	for i in range(len(header)):
		tmp += header[i]
		if (i+1) % 16 == 0:
			data.append(tmp)
			tmp = ""

	# 每16位求和
	sum = 0
	for i in range(size):
		sum = sum + int(data[i],2)

	# 得到的数，高16位（溢出）和低16位求和，重复两次，
	checksum = (sum>>16) + (sum & 0xffff)
	checksum = checksum + (checksum>>16)
	# print("checksum : ",checksum)
	checksum = (~checksum)
	checksum = bin(checksum).lstrip("-0b").rjust(16,"0")
	return checksum



# 将校验和恢复成原来的数字
def getChecksum(checksum):
	checksum = checksum.lstrip("0")
	checksum = "-0b"+checksum
	checksum = int(checksum,2)
	return checksum



