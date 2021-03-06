package com.jeeihyun.client.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client{
	 	private Socket socket;
	    private final String ip="127.0.0.1";
	    private final int port=9870;
	    
	    private DataOutputStream output;
	    private DataInputStream input;
	    
	    public void start(){
	        try {
                socket = new Socket(ip, port);
                
                output = new DataOutputStream(socket.getOutputStream());
                input = new DataInputStream(socket.getInputStream());

                ClientThread rt = new ClientThread(socket, input, output);
                rt.start();
	        }catch (IOException e) {
	            
	            System.exit(0);
	        }
     }
	    
	  public DataOutputStream getOutputStream() {
		  return output;
	  }
	  
	  public DataInputStream getInputStream() {
		  return input;
	  }
	  
	    
	    
	    
}