package com.jeeihyun.server.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private ServerSocket Socket;
    private Socket Socket1;
    private final int port = 9870;

    public void StartServer(){
            try{
                    Socket = new ServerSocket(port);
                    System.out.println("[Server mode] Waiting for request command from Client (port  : "+port+")");
                   
	                    Socket1 = Socket.accept();
	                    
	                    ReceiveThread ReceiveMsg = new ReceiveThread(Socket1);
	                    ReceiveMsg.start();
	                    System.out.println("Client connection success !");
                                   
                } catch (IOException e){ System.out.println("Port is already in use ."); }
    }

}

