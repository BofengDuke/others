package org.mytetris.control;

import java.io.*;
import java.net.Socket;

import org.mytetris.entities.Shape;



/**
 * ����ͨ���߳�
 * Created by Fang
 */

public class SocketThread implements Runnable {
    private Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;

    public static boolean isNum(String str){
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public SocketThread(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
        new Thread(this).start();
    }
    public void run() {
        try {
            while(true) {
                // ���︺���
                String mess = bufferedReader.readLine();
                if(isNum(mess)) {
                    int type = Integer.parseInt(mess);
                    if(LanController.getShape() == null){
                        LanController.setShape(new Shape(type));
                    }
                }
                switch (mess){
                	case "startGame":
//                		LanController.lanController.startGame();
                		break;
                    case "rotate":
                    	LanController.lanController.rotate();
                        break;
                    case "down":
                    	LanController.lanController.moveDown();
                        break;
                    case "left":
                    	LanController.lanController.moveLeft();
                        break;
                    case "right":
                    	LanController.lanController.moveRight();
                        break;
                    case "isput":
                    	LanController.lanController.accpetShape();
                        break;
                    case "gameover":
                    	LanController.lanController.gameover();
                        break;
                    case "gamestop":
                    	LanController.lanController.gameover();
//                        GameController.localController.resume();
                        break;
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            System.out.println("������ run �쳣: " + e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (Exception e) {
                    socket = null;
                    System.out.println("����� finally �쳣:" + e.getMessage());
                }
            }
        }
    }

    public void sendMessage(String str){
        // ���︺��д
        try {
            bufferedWriter.write(str);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}