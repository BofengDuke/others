#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
Author:	Duke
Description: 
'''

# Echo server program
import socket

HOST = 'localhost'                 # Symbolic name meaning all available interfaces
PORT = 50007              # Arbitrary non-privileged port

DATA = ("eth0: flags=4099<UP,BROADCAST,MULTICAST>  mtu 1500"
        "ether c4:54:44:ab:d4:1d  txqueuelen 1000  (Ethernet)"
        "RX packets 0  bytes 0 (0.0 B)"
        "RX errors 0  dropped 0  overruns 0  frame 0"
        "TX packets 0  bytes 0 (0.0 B)"
        "TX errors 0  dropped 0 overruns 0  carrier 0  collisions 0")

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((HOST, PORT))
s.listen(1)
conn, addr = s.accept()
print(addr)
while 1:
    data = conn.recv(1024)
    if not data: break
    conn.sendall(DATA.encode())
conn.close()