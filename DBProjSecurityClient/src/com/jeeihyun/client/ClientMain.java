package com.jeeihyun.client;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import com.jeeihyun.client.handle.ClientDataHandle;
import com.jeeihyun.client.handle.KeyGenerator;
import com.jeeihyun.client.net.Client;


public class ClientMain {
    public static void main(String[] args) {

    	ClientDataHandle cDataHandle = new ClientDataHandle();      
    	 
        ArrayList<String> list = new ArrayList<>();
        //KeyGenerator keyGen = new KeyGenerator();

        int command;
        boolean loop = true;        
       
        
        Client client = new Client();
        client.start();
                
        Scanner sc = new Scanner(System.in);
        System.out.println("- GraduateProject -");

        cDataHandle.print();        
        
        
        while(loop){
            command = sc.nextInt();
            switch(command){
                case 1:
                    list = KeyGenerator.make();
                    System.out.println("Tset Creation completed.");
                    System.out.println();
                    cDataHandle.print();
                    break;
                case 2:                	
                	if(list ==null || list.isEmpty()) {
                		list = KeyGenerator.make();
                	}
                	cDataHandle.SendTSet(client.getOutputStream(),list);
                    System.out.println("Send Tset to server");
                    cDataHandle.print();
                    break;
                case 3:                	  
                	cDataHandle.SendKeyword(client.getOutputStream());        	  	
                    break;
                case 4:
                    loop = false;
                    break;
                default:
            }
        }
        System.out.println("Done");
        System.exit(0);
    }
    
    
     private static String getKeyword() {
    	 String str = null;
    	 System.out.println("Enter keyword : ");
    	 
    	 BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	 
    	 try {
			str = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	 return str;
     }
    
    
}
