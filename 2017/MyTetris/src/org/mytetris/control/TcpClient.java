package org.mytetris.control;

import java.io.IOException;
import java.net.Socket;

public class TcpClient {

	private static SocketThread socketThread;
	
	    public static void Init(int PORT){
	        try {
	            Socket socket = new Socket("localhost",PORT);
	        System.out.println("�ͻ���IP:"+socket.getLocalAddress()+"�˿�"+socket.getPort());
	        // ���������߳�
	        socketThread=new SocketThread(socket);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	public static SocketThread getSocketThread(){
	    return socketThread;
	}
	
}
