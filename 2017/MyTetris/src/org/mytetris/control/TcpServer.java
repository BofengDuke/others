package org.mytetris.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

	private static SocketThread socketThread;

    public static void Init(int PORT){
        try {
            ServerSocket ss = new ServerSocket(PORT);
            System.out.println("端口号"+PORT+",服务器已启动");
            Socket s = ss.accept();
            // 启动交流线程
            socketThread = new SocketThread(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SocketThread getSocketThread(){
        return socketThread;
    }
	
}
