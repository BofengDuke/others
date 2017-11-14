#!/usr/bin/python3
#-*- coding:utf-8 -*-

import socket
import sys

Route_1 = {
	'name': 'Route_1',
	'ip': '127.0.10.1',
	'port': 55550,
}

Route_2 = {
	'name': 'Route_2',
	'ip': '127.0.20.1',
	'port': 55560,
}
Route_3 = {
	'name': 'Route_3',
	'ip': '127.0.30.1',
	'port': 55570,
}

Host_1 = {
	'name': 'host1',
	'ip': '127.0.10.2',
	'port': 55010,
	'nextRoute': Route_1	
}
Host_2 = {
	'name': 'host2',
	'ip': '127.0.20.2',
	'port':  55020,
	'nextRoute': Route_2
}
Host_3 = {
	'name': 'host3',
	'ip': '127.0.30.2',
	'port': 55030,
	'nextRoute': Route_3
}



# R1 路由器
global routeTable

def send(curr_route,nextIp,port,data):
	'''
	 	@param curr_route: 
  		Example: Route_1 = {'name': 'Route_3','ip': '127.0.30.1','port': 55570,}
		@param data:
		Example: data[destHost,destPort,data]
	'''
	client = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
	try:
		client.sendto(data.encode('utf-8'),(nextIp,port))
		response,clientaAddr = client.recvfrom(4096)
		if response.decode('utf-8') == 'ACK':
			print("\n[%s] Send data to %s:%d success!"%(curr_route['name'],nextIp,port))
	except Exception as err:
		print(err)
	client.close()

def recv(curr_route):
	server = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)
	server.bind((curr_route['ip'],curr_route['port']))

	# 接收数据
	while True:
		reMsg = 'ACK'
		data,addr = server.recvfrom(4096)
		server.sendto(reMsg.encode('utf-8'),addr)
		data = data.decode('utf-8').split('|')
		print("\n[%s] recv from %s:%d "%(curr_route['name'],addr[0],addr[1]))

		'''
		data = [destIp,destPort,sourIp,sourPort,msg]
		查路由表，转发
		@routeTable = {[destHost,nextHop_ip,nextHop_port,nextHop_name]}
		Example H1 to H3
			 path:  H1 -> R1 -> R2 -> R3 -> H3
		'''
		# routeTable = [
		# 	[Host_1['ip'],Host_1['ip'],Host_1['port'],Host_1['name']],
		# 	[Host_2['ip'],Route_2['ip'],Route_2['port'],Route_2['name']],
		# 	[Host_3['ip'],Route_2['ip'],Route_2['port'],Route_2['name']]

		# ]

		for route in routeTable:
			if data[0] == route[0]:
				if data[0] == route[1]:
					print('直接发送给主机')
				else:
					print('转发给下一个路由')
				data = '|'.join(data)
				print("  [%s] Dest:%s  Next:%s:%d  next_name: %s"%(curr_route['name'],route[0],route[1],route[2],route[3]))
				send(currRoute,route[1],int(route[2]),data)


if __name__ == '__main__':

	if len(sys.argv) == 2:
		if sys.argv[1] == '1':
			currRoute  = Route_1
			routeTable = [
				[Host_1['ip'],Host_1['ip'],Host_1['port'],Host_1['name']],
				[Host_2['ip'],Route_2['ip'],Route_2['port'],Route_2['name']],
				[Host_3['ip'],Route_2['ip'],Route_2['port'],Route_2['name']]

			]
		elif sys.argv[1] == '2':
			currRoute  = Route_2
			routeTable = [
				[Host_1['ip'],Route_1['ip'],Route_1['port'],Route_1['name']],
				[Host_2['ip'],Host_2['ip'],Host_2['port'],Host_2['name']],
				[Host_3['ip'],Route_3['ip'],Route_3['port'],Route_3['name']]

			]

		elif sys.argv[1] == '3':
			currRoute  = Route_3
			routeTable = [
				[Host_1['ip'],Route_2['ip'],Route_2['port'],Route_2['name']],
				[Host_2['ip'],Route_2['ip'],Route_2['port'],Route_2['name']],
				[Host_3['ip'],Host_3['ip'],Host_3['port'],Host_3['name']]

			]
		else:
			print('[Usage] Only three route: 1-3')
			sys.exit(1)
		print('======== Current Route %s  %s:%d ======' %(sys.argv[1],currRoute['ip'],currRoute['port']))
		print('** Waiting ...')
		recv(currRoute)
	else:
		print('[Usage] python3 route.py [curr_route_num]')
		sys.exit(1)
	



