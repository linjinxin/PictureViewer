package utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {
	//¸´ÖÆ
	  public static boolean copyFile(String destination, String source) throws Exception {
	        File sourceFile=new File(source);
	        File targetFile=new File(destination);
	        if(!sourceFile.exists()){
	            throw new Exception("");
	        }
	        if(targetFile.exists()){
	            throw new Exception("");
	        }
	        BufferedInputStream input=new BufferedInputStream(new FileInputStream(sourceFile));
	        BufferedOutputStream output=new BufferedOutputStream(new FileOutputStream(targetFile));
	        int r;
	        while((r=input.read())!=-1){
	            output.write((byte)r);
	        }
	        input.close();
	        output.close();
	        return true;
	    }

	   
	    public static boolean moveFile(String destination, String source) throws Exception {
	        File sourceFile=new File(source);
	        File targetFile=new File(destination);
	        if(!sourceFile.exists()){
	            throw new Exception("");
	        }
	        if(targetFile.exists()){
	            throw new Exception("");
	        }
	        FileUtils.copyFile(destination, source);
	        FileUtils.deleteFile(source);
	        return true;
	    }

	   
	    public static boolean deleteFile(String source) throws Exception {
	        File sourceFile=new File(source);
	        if(!sourceFile.exists()){
	            throw new Exception("");
	        }
	        sourceFile.delete();
	        return true;
	    }

	    
	    public static boolean renameFile(String filePath,String newName) throws Exception {
	        File sourceFile=new File(filePath);
	        if(!(sourceFile.exists())){
	            throw new Exception("");
	        }
	        int index=filePath.lastIndexOf("\\");
	        filePath=filePath.substring(0,index+1)+newName;
	        File newFile=new File(filePath);
	        if(newFile.exists()){
	            throw new Exception("");
	        }
	        sourceFile.renameTo(newFile);
	        return true;
	    }
}
