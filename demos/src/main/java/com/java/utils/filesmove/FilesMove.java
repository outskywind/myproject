package com.java.utils.filesmove;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class FilesMove {
	
	//private static String filepath="D:/Users/quanchengyun970/Desktop/jboss-as-7.2.0.Final";
	private static String filepath="E:/SourceCode/springframework/spring-framework-3.2.5.RELEASE";

	private File  dest = new File("D:/springsource");
	@Test
	public void TestSimplified() throws IOException {
		//1.创建文件对象
		File base = new File(filepath);
		File[] docs = base.listFiles();
		for(int i=0;i<docs.length;i++) {
			String filename = docs[i].getName();
			if(filename.startsWith("spring-")) {
				//File basedoc = new File(base,filename+"/src/main/java/org");
				File basedoc = new File(base,filename+"/src/main/java");
				if(basedoc.exists()) {
					System.out.println("<fileset dir=\""+basedoc.getPath()+"\">" +
							"<include name=\"**/*\"/></fileset>");
				}
			}
		}
		
	}
	
	@Test
	public void test() {
		//1.创建文件对象
		File base = new File(filepath);
		File[] flist = base.listFiles();
		dest.mkdir();
		for(int i=0;i<flist.length;i++){
			String path = flist[i].getName();
			if(path.startsWith("spring-")){
				copyAll(flist[i]);
			}
		}
	}
	
	private void copyAll(File f) {
		
		if(f.isFile()&& f.getName().endsWith("java")) {
			copy(f);
		}
		
		if(!f.isDirectory()) {
			return;
		}
		File[] files = f.listFiles();
		for(int i=0;i<files.length;i++) {
			copyAll(files[i]);
		}
		
	}
	
	private void copy(File file) {
		String path = file.getPath();
		String pkgPath = path.substring(path.indexOf("java") + 5,
				path.lastIndexOf("\\"));
		try {
			String[] dir = pkgPath.split("\\\\");
			StringBuffer sb = new StringBuffer(dest.getPath());
			for (int i = 0; i < dir.length; i++) {
				sb.append("\\").append(dir[i]);
				File pa = new File(sb.toString());
				pa.mkdir();
			}
			sb.append("\\").append(file.getName());
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file));
			byte[] b = new byte[in.available()];
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(new File(sb.toString())));
			in.read(b);
			out.write(b);
			in.close();
			out.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(pkgPath);
		}
	}

}
