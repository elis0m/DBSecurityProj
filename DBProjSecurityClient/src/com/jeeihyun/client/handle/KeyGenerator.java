package com.jeeihyun.client.handle;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import com.jeeihyun.client.security.AES;
import com.jeeihyun.client.security.SHA256;
import com.jeeihyun.client.utils.kMap;

public class KeyGenerator {
    	

  public static ArrayList make(){
        Random ran = new Random();
        AES aes = new AES();
        SHA256 sha256 = new SHA256();
        
        
        FileHandler fl = new FileHandler();
        ArrayList<ArrayList<String>> containvector;
        
        kMap[][] keyMap = new kMap[128][128];

        for(int i = 0; i < 128; i++){
            for(int j = 0; j < 128; j++) {
            	keyMap[i][j] = new kMap();
            }
        }
        
        // key generate
        String Keys = makeRandomHex(20);
        String Keyt = makeRandomHex(20);
        
        
        System.out.println("Key generation result : Random HEXcode KeyS and KeyT are created successfully !");

        containvector = fl.getList();

        ArrayList<Integer> al;
        ArrayList<ArrayList> x = new ArrayList<>();
        for(int i = 0; i < 128; i++){
            x.add(new ArrayList<Integer>());
        }
        for(int i = 0; i < 128; i++){
            al = x.get(i);
            for(int j = 0; j < 128; j++) {
                al.add(j + 1);
            }
        }

        for(int i = 0; i < containvector.size(); i++){
            ArrayList<String> contain = containvector.get(i);
            String Keye = aes.Function(contain.get(0),Keys);
            String stag = aes.Function(contain.get(0),Keyt);

            for(int j = 0; j < contain.size() - 1; j++){
                String msg = contain.get(j+1);
                contain.set(j+1,aes.encryptAES(msg,Keye));
                BigInteger blk = new BigInteger(sha256.SHA256Encryption(aes.Functionbar(stag,Integer.toString(j+1))),16);
                String blkbin = blk.toString(2);
                String b = blkbin.substring(0,7);
                String l = blkbin.substring(7,87);
                String k = blkbin.substring(87,216);

                int bint = Integer.parseInt(b,2);
                ArrayList<Integer> y = x.get(bint);
                int random;
                int num;
                while(true) {
                    random = ran.nextInt(128);
                    num = y.get(random);
                    if (!(num==1000)) break;
                }
                y.set(random,1000);

                keyMap[bint][num-1].setLabel(l);
                BigInteger one = new BigInteger(contain.get(j+1),16);
                BigInteger bik = new BigInteger(k,2);

                
                if(j==contain.size()-2){
                    one = one.xor(bik);
                    keyMap[bint][num-1].setValue(one.toString(2));
                }else{
                    String tmp = one.toString(2);
                    while(tmp.length()<128){
                        tmp="0"+tmp;
                    }
                    BigInteger two = new BigInteger("1"+tmp,2);
                    two = two.xor(bik);
                    keyMap[bint][num-1].setValue(two.toString(2));
                }
            }

        }
        fl.saveKey(Keyt,1);
        fl.saveKey(Keys,0);

        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i < 128; i++){
            for(int j = 0; j < 128; j++) {
                if (!keyMap[i][j].getLabel().equals("")){
                    String b = Integer.toHexString(i);
                    if(i<16) b="0"+b;
                    String c = Integer.toHexString(j);
                    if(j<16) c="0"+c;
                    String a = "%"+b+"%"+c+"%"+keyMap[i][j].getLabel()+"%"+keyMap[i][j].getValue();
                    list.add(a);
                }
            }
        }

        return list;
    }
    
    
	private static String makeRandomHex(int numchars){
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        
        while(sb.length() < numchars){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }
    
}
