#!/usr/bin/python3
#-*- coding:UTF-8 -*-
import socket
from commonFun import strNum2bin
from commonFun import strIP2bin
from commonFun import strMAC2bin
from commonFun import Checksum
from commonFun import client




# use HTTP
def applicationLayer(sendData):
	# print("== Application Layer:\n")
	# HTTP header
	method   = "GET"
	host     = "localhost"
	fileType = "HTTP/1.1"

	HttpHeader = method+" "+"http://"+host+"/"+" "+fileType+"\n"
	HttpHeader = HttpHeader+"Host:"+host+"\n"
	HttpHeader = HttpHeader+"\n"
	allData    = HttpHeader+sendData

	allData = "||"+allData
	# print(allData)
	return allData

# use UDP
def transportLayer(data):
	# print("== Transport Layer:")
	
	# UDP header
	sourPort = "5555"   # 16bit
	destPort = "5556"	# 16bit
	dataLen  = "123"	# 16bit
	checksum = "0"      # 16bit

	sourPort = strNum2bin(sourPort,16)
	destPort = strNum2bin(destPort,16)
	dataLen  = strNum2bin(dataLen,16)
	checksum = strNum2bin(checksum,16)

	UDPHeaderOld = sourPort+destPort+dataLen+checksum
	checksum     = Checksum(UDPHeaderOld,4)
	UDPHeader    = sourPort+destPort+dataLen+checksum
	allData      = UDPHeader+data

	allData = "||"+allData
	# print(allData)
	return allData

# IPv4
def networkLayer(data):
	# print("== Network Layer:")

	# IPv4 Header
	version  = "4"	# 4bit
	headLen  = "5"	# 4bit
	TOS      = "0"		# 8bit
	totalLen = str(len(data)+40 % 8)	# 16bit
	identification = "56593"	# 16bit
	sliceFlag   = "2"		# 3bit
	sliceOffset = "0"	# 13bit
	TTL         = "64"		# 8bit
	protocal    = "17"	# 8bit  UDP
	checksum    = "0"	# 16bit
	sourceIP    = "127.0.0.1"   # 32bit 
	destIP      = "127.0.0.1"    # 32bit
	option      = "0"    # max 40bit

	version  = strNum2bin(version,4)
	headLen  = strNum2bin(headLen,4)
	TOS      = strNum2bin(TOS,8)
	totalLen = strNum2bin(totalLen,16)
	identification = strNum2bin(identification,16)
	sliceFlag   = strNum2bin(sliceFlag,3)
	sliceOffset = strNum2bin(sliceOffset,13)
	TTL         = strNum2bin(TTL,8)
	protocal    = strNum2bin(protocal,8)
	checksum    = strNum2bin(checksum,16)
	sourceIP    = strIP2bin(sourceIP)
	destIP      = strIP2bin(destIP)

	# 求checksum
	IPheaderOld = version+headLen+TOS+totalLen+identification+sliceFlag
	IPheaderOld = IPheaderOld+sliceOffset+TTL+protocal+checksum
	IPheaderOld = IPheaderOld+sourceIP+destIP
	checksum    = Checksum(IPheaderOld,10)
	# print("checksum: ",checksum)
	
	# 重组 IP Header
	IPHeader = version+headLen+TOS+totalLen+identification+sliceFlag
	IPHeader = IPHeader+sliceOffset+TTL+protocal+checksum+sourceIP+destIP
	allData  = IPHeader+data
	allData  = "||"+allData

	# print(allData)
	return allData

# Ethernet II
def dataLinkLayer(data):
	# print("== Data Link Layer:")

	# Ethernet header
	destMAC = "c8:3a:35:3a:3c:10"   # 48bit
	sourMAC = "60:02:b4:e3:8e:b3"	# 48bit
	ethType = "2048"				# 16bit IPv4

	destMAC = strMAC2bin(destMAC)
	sourMAC = strMAC2bin(sourMAC)
	ethType = strNum2bin(ethType,16)

	EthHeader = destMAC+sourMAC+ethType
	allData   = EthHeader+data
	allData   = allData
	# print(allData)
	return allData


# Physical Layer: send data and receive reply
def physicalLayer(host,port,data):
	# print("== Physical Layer:")
	print(data)
	client(host,port,data)



if __name__ == '__main__':

	host = "127.0.0.1"
	port = 5560
	sendData = "This is the sending data"

	data = applicationLayer(sendData)
	data = transportLayer(data)
	data = networkLayer(data)
	data = dataLinkLayer(data)
	physicalLayer(host,port,data)

	# applicationLayer()
	# transportLayer()
	# networkLayer()
	# dataLinkLayer()
	
	# physicalLayer(host,port)


