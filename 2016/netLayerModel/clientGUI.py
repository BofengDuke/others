#!/usr/bin/python3
#-*- coding:UTF-8 -*-

import tkinter
import time
from netLayerModel_client import applicationLayer
from netLayerModel_client import transportLayer
from netLayerModel_client import networkLayer
from netLayerModel_client import dataLinkLayer
from commonFun import client

# def evRecv():
# 	data = "0102241125522asdasdasdasdasds"
# 	tRecvText.insert(tkinter.END,data,'green')

host = "127.0.0.1"
port = 6666

def setHostPort():
	host = eHost.get()
	if len(host) == 0:
		host = '127.0.0.1'
	port = ePort.get()
	if len(port) == 0:
		port = '6666'
	port = int(port)

def evSend():
	
	# sendData = "This is the sending data"
	sendData = tInput.get('0.0',tkinter.END)
	strTime = time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())+"\n"
	tRecvText.insert(tkinter.END,strTime)
	tRecvText.insert(tkinter.END,sendData+'\n')
	tInput.delete('0.0',tkinter.END)
	tParseText.delete('0.0',tkinter.END)

	titleApplication = "== Application Layer ==\n"
	titleTransport = "\n== Transport Layer == \n"
	titleNetwork = "\n== Network Layer ==\n"
	titleDatalink = "\n== Datalink Layer ==\n"
	titlePhysical = "\n== Physical Layer ==\n"

	tParseText.insert(tkinter.END,titleApplication)
	data = applicationLayer(sendData)
	tParseText.insert(tkinter.END,data)

	tParseText.insert(tkinter.END,titleTransport)
	data = transportLayer(data)
	tParseText.insert(tkinter.END,data)

	tParseText.insert(tkinter.END,titleNetwork)
	data = networkLayer(data)
	tParseText.insert(tkinter.END,data)

	tParseText.insert(tkinter.END,titleDatalink)
	data = dataLinkLayer(data)
	tParseText.insert(tkinter.END,data)

	tParseText.insert(tkinter.END,titlePhysical)
	client(host,port,data)
	strData = "Physical Layer is sending data"
	tParseText.insert(tkinter.END,strData)

if __name__ == '__main__':
	
	
	WIDTH = 550

	root = tkinter.Tk()
	root.geometry('1140x560+100+100')
	root.wm_title("Client")

	fRecv = tkinter.Frame(root,width=WIDTH,height=350)
	fParse = tkinter.Frame(root,width=WIDTH,height=550)
	fInput = tkinter.Frame(root,width=WIDTH,height=150)
	fSendBtn = tkinter.Frame(root,width=WIDTH,height=30)

	lRecv = tkinter.Label(fRecv,text="Recv/Send")
	lParse = tkinter.Label(fParse,text='Parse:')
	lSend = tkinter.Label(fInput,text='Send:')
	tRecvText = tkinter.Text(fRecv,bg='white',height=350)
	tParseText = tkinter.Text(fParse,bg='white',height=500)
	tInput = tkinter.Text(fInput,bg='white',height=150)
	bSend = tkinter.Button(fSendBtn,text="Send Data",command=evSend)
	eHost = tkinter.Entry(fSendBtn,width=15)
	ePort = tkinter.Entry(fSendBtn,width=15)
	bSet = tkinter.Button(fSendBtn,text='Host/Port',command=setHostPort)

	fRecv.grid(row=0,column=0,padx=5,pady=5,sticky='N')
	fInput.grid(row=1,column=0,padx=5,pady=5)
	fSendBtn.grid(row=2,column=0,padx=5,pady=5)
	fParse.grid(row=0,rowspan=3,column=1,padx=5,pady=5)
	fRecv.grid_propagate(0)
	fParse.grid_propagate(0)
	fInput.grid_propagate(0)
	fSendBtn.grid_propagate(0)

	lRecv.grid(sticky='W')
	lParse.grid(sticky='W')
	lSend.grid(sticky='W')
	bSend.grid(sticky='W',padx=10)
	eHost.grid(row=0,column=2,sticky='E',padx=5)
	ePort.grid(row=0,column=3,sticky='E',padx=5)
	bSet.grid(row=0,column=4,sticky='E',padx=5)
	tRecvText.grid()
	tParseText.grid()
	tInput.grid()


	root.mainloop()