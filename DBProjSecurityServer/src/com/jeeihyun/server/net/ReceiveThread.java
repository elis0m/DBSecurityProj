package com.jeeihyun.server.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.jeeihyun.server.handle.DataHandle;

class ReceiveThread extends Thread implements Runnable{
    //private Socket Socket;
    DataInputStream input;
    DataOutputStream output;
    DataHandle dHandle = null;
    
    
    public ReceiveThread(Socket Socket) {
     //this.Socket = Socket;
        try {
	            input = new DataInputStream(Socket.getInputStream());  // 클라이언트의 input data 받기
	            output = new DataOutputStream(Socket.getOutputStream()); // 서버에서 클라이언트로 data 출력                                                      
                
                dHandle = new DataHandle();          
                dHandle.init();                    
                    
        } catch (IOException e) {
                e.printStackTrace();
        }
    }
    
    
    int schLastPos = 33;
    
    public synchronized void run() {
    	
            while (input != null) {
                try {
                    String msg = input.readUTF();
                    if ((msg.charAt(0)) == '%') {
                        dHandle.setTset(msg);
                    } else if ((msg.charAt(0)) == '$') {
                        System.out.println("Tset reception");
                        System.out.println("Waiting for Client's command ...");

                    } else if ((msg.charAt(0)) == '*') {
                        System.out.println("Receive keyword from client");
                        String  strFileList = dHandle.Sendlist(msg.substring(1,schLastPos));
                        
                        if(strFileList == null || strFileList.length()==0) {
                        	strFileList = "No corresponding file !";
                        }
                        Thread.sleep(500);                        
                        output.writeUTF(strFileList);
                        
                        output.flush();
                        
                        System.out.println("File transfer");
                        System.out.println("Waiting for Client's command ...");
                    }                   
                                        
                } catch (IOException e) {
                    System.out.println("Client shutdown.");
                    break;
                } catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
    }
}