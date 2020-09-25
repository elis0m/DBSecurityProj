package com.jeeihyun.server;

import com.jeeihyun.server.net.Server;
import com.jeeihyun.server.security.AES;
import com.jeeihyun.server.security.SHA256;
import com.jeeihyun.server.utils.kMap;


public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
       
        Server server = new Server();  
        server.StartServer();
    
        System.out.println("- GraduateProject -");
        System.out.println("Waiting for client command ...");
    }
}
