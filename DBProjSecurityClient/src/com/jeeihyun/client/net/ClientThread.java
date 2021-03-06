package com.jeeihyun.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.jeeihyun.client.handle.ClientDataHandle;


public class ClientThread extends Thread implements Runnable {

	 	DataInputStream input;
	    DataOutputStream output;
	    ClientDataHandle cDataHandle;
	    
	    public ClientThread(Socket Socket, DataInputStream input, DataOutputStream output) {
	        try {
	                this.input = input;
	                this.output = output;
	                cDataHandle = new ClientDataHandle();           
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
	    }
	
    public void run() {    	
    	String msg="";    
            while (true) {
                try {
                    msg = input.readUTF();
                    if ((msg.charAt(0))=='@'){
                    	System.out.println("----------------------------------------------");
                        System.out.println("Successfully receiving file list from Server");
                        cDataHandle.createList(msg);
                        cDataHandle.print();
                    	System.out.println("----------------------------------------------");
                    }else {
                    	 System.out.println("[ERROR]");
                    	 System.out.println(msg);                            	 
                         cDataHandle.print();
                    }
                } catch (IOException e) {
                    System.out.println("Disconnect server and socket .");
                    break;
                }
            }
     }
}