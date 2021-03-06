package com.jeeihyun.client.handle;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.ArrayList;

import com.jeeihyun.client.security.AES;

public class FileHandler {
    private ArrayList<String> filelist; 
    private ArrayList<String> kword;
    private ArrayList<String> plaintext; 
    private ArrayList<ArrayList<String>> containArrayList;
    
    private String FileBasePath= "D:\\workspsace_graduate\\DBProjSecurityClient\\src";	// C:\\Users\\jeeihyun\\Desktop\\workspace_graduate\\Client\\src
    
    public FileHandler() {    	
    	 	filelist = new ArrayList<String>();
    	 	kword = new ArrayList<String>();
    	 	plaintext = new ArrayList<String>();
    	    containArrayList = new ArrayList<>();    	
    }
        
 	public FileHandler(ArrayList<String> filelist, ArrayList<String> kword,
			ArrayList<String> plaintext,
			ArrayList<ArrayList<String>> containArrayList, String fileBasePath) {
			super();
			this.filelist = filelist;
			this.kword = kword;
			this.plaintext = plaintext;
			this.containArrayList = containArrayList;
			FileBasePath = fileBasePath;
	}

	public ArrayList<ArrayList<String>> getList() {
        String path = FileBasePath + "\\fileListDir";
        File dirFile = new File(path);
        File[] fileList = dirFile.listFiles();
       
        
        for (File tempFile : fileList) {
            if (tempFile.isFile()) {
                String tempFileName = tempFile.getName();
                tempFileName = tempFileName.toLowerCase();
                //System.out.println(tempFileName+"�쓣 李얠븯�뒿�땲�떎.");
                if(tempFileName.endsWith(".txt")&&tempFileName.length()==36) filelist.add(tempFileName);
            }
        }
        
        System.out.println("Number of files to be sent to the Server : "+filelist.size());
        
        loadKeyword();
        
        loadPlainTxt();
        
        keyUpdate();
        return containArrayList;
    }
    
    
    public void loadKeyword(){
        String data="";
        Path path = Paths.get(FileBasePath+"\\kword.txt");

        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            Charset charset = Charset.defaultCharset();

            int byteCount;

            while (true) {
                byteCount = fileChannel.read(byteBuffer);
                if (byteCount == -1) break;
                byteBuffer.flip();
                data += charset.decode(byteBuffer).toString();
                byteBuffer.clear();
            }
            fileChannel.close();
            System.out.println("Read kword.txt");
            StringTokenizer list = new StringTokenizer(data);
            while (list.hasMoreTokens()) {
                kword.add(list.nextToken("|"));
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    

    
    public void loadPlainTxt(){
        String data="";
        for(int i = 0; i <filelist.size(); i++){
            try {
                Path path = Paths.get(FileBasePath + "\\fileListDir\\" + filelist.get(i));
                FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
                ByteBuffer byteBuffer = ByteBuffer.allocate(100);
                Charset charset = Charset.defaultCharset();

                int byteCount;

                while (true) {
                    byteCount = fileChannel.read(byteBuffer);
                    if (byteCount == -1) break;
                    byteBuffer.flip();
                    data += charset.decode(byteBuffer).toString();
                    byteBuffer.clear();
                }
                fileChannel.close();
                System.out.println("Read "+filelist.get(i));
                plaintext.add(data);
                data = "";
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    
    
    
    public void keyUpdate(){
        String word = "";
        String msg = "";

        for (int i = 0; i < kword.size(); i++){
            ArrayList<String> contain = new ArrayList<>();
            word = kword.get(i);
            contain.add(word);
            for (int j = 0; j < plaintext.size(); j++){
                msg = " "+plaintext.get(j)+" ";

                if(msg.contains(" "+word+" ")){
                    contain.add(filelist.get(j).substring(0,32));
                }
            }
            containArrayList.add(contain);
        }
        
    }
    public void saveKey(String key, int i){
        String keyt = "keyt.txt";
        String keys = "keys.txt";
        Path path = null;
        if (i == 1){
            path = Paths.get(FileBasePath +"\\"+keyt);
        }
        else{
            path = Paths.get(FileBasePath + "\\"+keys);
        }
        try {
            Files.createDirectories(path.getParent());

            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);

            Charset charset = Charset.defaultCharset();
            ByteBuffer byteBuffer = charset.encode(key);
            int byteCount = fileChannel.write(byteBuffer);
            fileChannel.close();
            System.out.println("Save success in key.txt !");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public ArrayList<String> getKeyword(){
        loadKeyword();
        return kword;
    }
    public String getKey(int i){
        String data="";
        Path path = null;
        if(i == 0) path = Paths.get(FileBasePath + "\\keyt.txt");
        else path = Paths.get(FileBasePath + "\\keys.txt");

        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            Charset charset = Charset.defaultCharset();

            int byteCount;

            while (true) {
                byteCount = fileChannel.read(byteBuffer);
                if (byteCount == -1) break;
                byteBuffer.flip();
                data += charset.decode(byteBuffer).toString();
                byteBuffer.clear();
            }
            fileChannel.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public void getEncFile(String cipher, String keyword){
        AES aes = new AES();
        String data="";
        String ks = getKey(1);
        String ke = aes.Function(keyword,ks);
        String id = aes.decryptAES(cipher,ke);
        Path path = Paths.get(FileBasePath +"\\fileListDir\\"+id+".txt");
        try {
            FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            Charset charset = Charset.defaultCharset();

            int byteCount;

            while (true) {
                byteCount = fileChannel.read(byteBuffer);
                if (byteCount == -1) break;
                byteBuffer.flip();
                data += charset.decode(byteBuffer).toString();
                byteBuffer.clear();
            }
            fileChannel.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        System.out.println(data);
    }
}
