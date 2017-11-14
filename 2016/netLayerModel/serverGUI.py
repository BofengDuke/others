#!/usr/bin/python3
#-*- coding:UTF-8 -*-

import tkinter
import time
from netLayerModel_server import physicalLayer_parse
from netLayerModel_server import dataLinkLayer_parse
from netLayerModel_server import networkLayer_parse
from netLayerModel_server import transportLayer_parse
from netLayerModel_server import applicationLayer_parse
from commonFun import server

port = 6666

def setPort():
	port = ePort.get()
	if len(port) == 0:
		port = '6666'
	port = int(port)

def evRecv():
	host = "127.0.0.1"
	

	tParseText.delete('0.0',tkinter.END)

	titleApplication = "\n== Application Layer ==\n"
	titleTransport = "\n== Transport Layer == \n"
	titleNetwork = "\n== Network Layer ==\n"
	titleDatalink = "\n== Datalink Layer ==\n"
	titlePhysical = "== Physical Layer ==\n"

	data = server(host,port).decode()
	if len(data) == 0:
		print("Can't recv any message")
		raise

	tParseText.insert(tkinter.END,titlePhysical)
	phyMsg = "Physical is geting message"
	tParseText.insert(tkinter.END,phyMsg+'\n')

	tParseText.insert(tkinter.END,titleDatalink)
	tParseText.insert(tkinter.END,data+'\n')

	tParseText.insert(tkinter.END,titleNetwork)
	data = dataLinkLayer_parse(data)
	tParseText.insert(tkinter.END,data+'\n')

	tParseText.insert(tkinter.END,titleTransport)
	data = networkLayer_parse(data)
	tParseText.insert(tkinter.END,data+'\n')

	tParseText.insert(tkinter.END,titleApplication)
	data = transportLayer_parse(data)
	tParseText.insert(tkinter.END,data)
	
	strTime = time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())+"\n"
	tRecvText.insert(tkinter.END,strTime)
	data = applicationLayer_parse(data)
	tRecvText.insert(tkinter.END,data+'\n\n')
	


if __name__ == '__main__':
	
	WIDTH = 550

	root = tkinter.Tk()
	root.geometry('1150x560+100+100')
	root.wm_title("Server")

	fRecv = tkinter.Frame(root,width=WIDTH,height=500)
	fParse = tkinter.Frame(root,width=WIDTH,height=550)
	fBtn = tkinter.Frame(root,width=WIDTH,height=30)

	lRecv = tkinter.Label(fRecv,text="Recv/Send")
	lParse = tkinter.Label(fParse,text='Parse:')
	tRecvText = tkinter.Text(fRecv,bg='white',height=350)
	tParseText = tkinter.Text(fParse,bg='white',height=500)
	bRecv = tkinter.Button(fBtn,text='Recv Data',command=evRecv)
	ePort = tkinter.Entry(fBtn,width=15)
	bSetPort = tkinter.Button(fBtn,text='Set Port',command=setPort)

	fRecv.grid(row=0,column=0,padx=5,pady=5,sticky='N')
	fBtn.grid(row=2,column=0,padx=5,pady=5)
	fParse.grid(row=0,rowspan=3,column=1,padx=5,pady=5)
	fRecv.grid_propagate(0)
	fParse.grid_propagate(0)
	fBtn.grid_propagate(0)

	lRecv.grid(sticky='W')
	lParse.grid(sticky='W')
	bRecv.grid(row=0,column=0,sticky='W',padx=10)
	ePort.grid(row=0,column=1,sticky='W',padx=5)
	bSetPort.grid(row=0,column=2,sticky='W',padx=5)
	tRecvText.grid(ipadx=10,ipady=5)
	tParseText.grid(ipadx=10,ipady=5)
	tParseText.grid(ipadx=10,ipady=5)

	root.mainloop()



