package com.jeeihyun.client.handle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import com.jeeihyun.client.net.Client;
import com.jeeihyun.client.security.AES;

public class ClientDataHandle {
    
    public static String keyword = "";

    public ClientDataHandle(){
      
        int[][] b = new int[3][3];
        int a = b.length;
    }
    
    public void print() {
    	System.out.println("1. Generate Tset");
    	System.out.println("2. Send Tset");
    	System.out.println("3. Keyword Search");
    	System.out.println("4. Exit");
    	System.out.println();
    	System.out.println("Command : ");
    }
    
    public void SendTSet(DataOutputStream output, ArrayList<String> list){
        try{
            for(int i = 0; i < list.size(); i++){
                output.writeUTF(list.get(i));
            }
            output.writeUTF("$");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void SendKeyword(DataOutputStream output){
        FileHandler fh = new FileHandler();
    	AES aes = new AES();
        ArrayList<String> kword = fh.getKeyword();
        String keyt = fh.getKey(0);
        Scanner sc = new Scanner(System.in);
        String key;

        System.out.println("Available search keywords (KEYWORD) : ");
        
        for(int i = 0; i < kword.size(); i++) {
        	System.out.print(kword.get(i)+"   ");
        }
        
        System.out.println();
        
        while(true){
            System.out.println("Enter a keyword to search : ");
            key = sc.next();
            if(kword.contains(key)) break;
            else System.out.println("!! Check search keywords !!");
        }
        keyword = key;
        String keyword = aes.Function(key,keyt);
        try {
            output.writeUTF("*"+keyword);
            output.flush();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Keyword sent.");
        
        System.out.println(keyword);
    }

    public void createList(String msg){
    	
        FileHandler fh = new FileHandler();
        ArrayList<String> filename = new ArrayList<>();
        StringTokenizer list = new StringTokenizer(msg);
        
        while (list.hasMoreTokens()) {
            filename.add(list.nextToken("@"));
        }
        for(int i = 0; i < filename.size(); i++){
            fh.getEncFile(filename.get(i),keyword);
        }

    }
}
