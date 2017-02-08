package com.java.utils.filesmove;

import java.io.File;

import org.junit.Test;

public class FilesRemove {
	
	
	private static String file_path = "E:\\workspace\\PA18ShopAuto1.44.0";
	
	@Test
	public void testRemove(){
		
		File f =  new File(file_path);
		
		remove(f);
		
		
	}
	
	public void remove(File f){
		if(f.getName().endsWith(".svn")){
			deleteDirectory(f);
			System.out.println(f.getAbsolutePath()+" is deleted");
			return ;
		}
		
		if(f.isDirectory()){
			File[] fs = f.listFiles();
			for(File file :fs){
				remove(file);
			}
		}
	}
	
	public void deleteDirectory(File f){
		//文件夹是空的才可以删除
		File[] files = f.listFiles();
		for(File file : files){
			if(file.isFile()){
				if(!file.delete()){
					System.out.println(file.getName()+" delete failed");
				}
				//不要用这种 break或者continue的做法
				//continue;
			}
			else{
				deleteDirectory(file);
			}
		}
		f.delete();
	}
	

}
