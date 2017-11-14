#!/usr/bin/python3
#-*- coding:UTF-8 -*-
import socket
from commonFun import server
from commonFun import binMAC2hex
from commonFun import binIP2Hex
from commonFun import getChecksum



def physicalLayer_parse(host,port):
	print("== Physical Layer Parse")
	data = server(host,port).decode()
	print("## recv data:")
	print(data)
	return data

def dataLinkLayer_parse(data):
	# data = physicalLayer_parse()
	print("\n== Datalink Layer Parse")
	
	# Ethernet Header
	EthHeaderStr = data.split("||",1)[0]
	# print(EthHeaderStr)
	
	# Parse Eth header
	EthHeaderList = list(EthHeaderStr)
	destMACList = EthHeaderList[0:48]
	sourMACList = EthHeaderList[48:48*2]
	ethTypelist = EthHeaderList[48*2::]
	destMAC = "".join(destMACList)
	sourMAC = "".join(sourMACList)
	ethType = "".join(ethTypelist)
	destMAC = binMAC2hex(destMAC)
	sourMAC = binMAC2hex(sourMAC)
	ethType = int(ethType,2)

	print("Parse result:")
	print("Dest   MAC: ",destMAC)
	print("Source MAC: ",sourMAC)
	print("Eth type: ",ethType)

	return (data.split("||",1)[1])

def networkLayer_parse(data):
	# data = dataLinkLayer_parse()
	print("\n== Network Layer Parse")
	IPHeaderlist = list(data.split("||",1)[0])
	
	# network later header
	version        = int("".join(IPHeaderlist[0:4]),2)
	headLen        = int("".join(IPHeaderlist[4:8]),2)
	TOS            = int("".join(IPHeaderlist[8:16]),2)
	totalLen       = int("".join(IPHeaderlist[16:32]),2)
	identification = int("".join(IPHeaderlist[32:32+16]),2)
	sliceFlag      = int("".join(IPHeaderlist[32+16:32+16+3]),2)
	sliceOffset    = int("".join(IPHeaderlist[32+16+3:32*2]),2)
	TTL            = int("".join(IPHeaderlist[32*2:32*2+8]),2)
	protocal       = int("".join(IPHeaderlist[32*2+8 : 32*2+8+8]),2)
	checksum       = getChecksum("".join(IPHeaderlist[32*2+16 : 32*3]))
	sourceIP       = binIP2Hex("".join(IPHeaderlist[32*3 : 32*4]))
	destIP         = binIP2Hex("".join(IPHeaderlist[32*4 : 32*5]))

	print("version: ",version)
	print("header len: ",headLen)
	print("type of server: ",TOS)	
	print("total len: ",totalLen)
	print("identification: ",identification)
	print("flag: ",sliceFlag)
	print("offset: ",sliceOffset)
	print("TTL: ",TTL)
	print("protocal: ",protocal)
	print("checksum: ",checksum)
	print("source  ",sourceIP)
	print("destination: ",destIP)

	return (data.split("||",1)[1])

def transportLayer_parse(data):
	# data = networkLayer_parse()
	print("\n== Transport Layer Parse")
	UDPHeaderList = data.split("||")[0]

	# UDP header
	sourPort = int("".join(UDPHeaderList[0:16]),2)
	destPort = int("".join(UDPHeaderList[16:32]),2)
	dataLen  = int("".join(UDPHeaderList[32:32+16]),2)
	checksum = getChecksum("".join(UDPHeaderList[32+16 : 32*2]))

	print("source port: ",sourPort)
	print("destination port: ",destPort)
	print("data len: ",dataLen)
	print("checksum: ",checksum)
	return (data.split("||",1)[1])

def applicationLayer_parse(data):
	# data = transportLayer_parse()
	print("\n== Application Layer")
	data= data.rstrip('\n')
	dataList = data.split("\n")
	sendData = dataList[-1]
	print("## Http header:")
	for i in range(len(dataList)-1):
		print(i,dataList[i])
	print("## Sending data:")
	print(sendData)
	return sendData

if __name__ == '__main__':
	# physicalLayer_parse()
	# dataLinkLayer_parse()
	# networkLayer_parse()
	# transportLayer_parse()
	# applicationLayer_parse()
	
	host = '127.0.0.1'
	port = 5561

	data = physicalLayer_parse(host,port)
	data = dataLinkLayer_parse(data)
	data = networkLayer_parse(data)
	data = transportLayer_parse(data)
	data = applicationLayer_parse(data)


	