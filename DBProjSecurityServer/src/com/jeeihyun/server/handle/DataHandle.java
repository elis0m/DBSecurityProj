package com.jeeihyun.server.handle;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.jeeihyun.server.security.AES;
import com.jeeihyun.server.security.SHA256;
import com.jeeihyun.server.utils.kMap;

public class DataHandle {
   
    SHA256 sha256 = new SHA256();
    AES aes = new AES();
    
    static kMap[][] keyMap = new kMap[128][128];
    
    
     /* 초기화  */
    
    public void init() {
	    for(int i = 0; i < 128; i++){
	        for(int j = 0; j < 128; j++) {
	        	keyMap[i][j] = new kMap();
	        }
	    }
    }
    
    public void setTset(String msg){
        ArrayList<String> index = new ArrayList<>();
        StringTokenizer list = new StringTokenizer(msg);
        while(list.hasMoreTokens()){
            index.add(list.nextToken("%"));
        }
        
        
        int i = Integer.parseInt(index.get(0),16);
        int j = Integer.parseInt(index.get(1),16);
              
        keyMap[i][j].setLabel(index.get(2));
       
        if(index.size()> 3) keyMap[i][j].setValue(index.get(3));
        
        
        System.out.println("********************************");
        System.out.println("TS[" + i + "][" + j + "] ==> ");
        System.out.println("Key(Label):" + keyMap[i][j].getLabel());
        System.out.println("Value : " + keyMap[i][j].getValue());
        
        
    }
   
    
    public String Sendlist(String keyword){
        String strList = "";
        String beta = "1";
        int i = 1;

        while(beta.equals("1") && i < 256){
            BigInteger blk = new BigInteger(sha256.SHA256Encryption(aes.Functionbar(keyword,Integer.toString(i))),16);
            String blkbin = blk.toString(2);
            String b = blkbin.substring(0,7);
            String l = blkbin.substring(7,87);
            String k = blkbin.substring(87,216);
            int bint = Integer.parseInt(b,2);
                        
            for(int j = 0; j < 128; j++){
                if(keyMap[bint][j].getLabel().equals(l)){
                    BigInteger kk = new BigInteger(k,2);
                    BigInteger one = new BigInteger(keyMap[bint][j].getValue(),2);
                    
                    String v = one.xor(kk).toString(2);
            
                    while(v.length()<129) v = "0"+v;
                    
                    beta = v.substring(0,1);
                    int len = v.length();
                    String tmp = v.substring(1,len);
                    BigInteger two = new BigInteger(tmp,2);
                    tmp = two.toString(16);
                    if(tmp.length()==31) tmp = "0"+tmp;
                    strList = strList+"@"+tmp;
                }
            }
            i++;
        }
       return strList;
    }
    
    

}
