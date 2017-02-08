package com.java.utils.searchFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FileSearchUtil {
	
	/**
	 * 搜索正则表达式
	 */
	private String[] Regex=null;
	
	private String configFile = "file.properties";
	
	private String matchMode = null;
	
	private String filePath = null;
	
	private File file = null;
	
	private int alertON ;
	
	private int corePoolSize=100;
	
	private int maximumPoolSize=100;
	
	private int searchDeep =15;
	
	private String escapedirectory = null;
	
	private String commentCountMode = null;
	
	private int blocksAlertONLines ;
	
	private ThreadPoolExecutor executor ;
	
	private static Map cache = new ConcurrentHashMap();
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String[] getRegex() {
		return Regex;
	}

	public void setRegex(String[] regex) {
		Regex = regex;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getMatchMode() {
		return matchMode;
	}

	public void setMatchMode(String matchMode) {
		this.matchMode = matchMode;
	}
	
	public FileSearchUtil() {
		setProperties();
		if(this.filePath==null) {
			System.out.println("file path must be inited.");
			return;
		}
		
		if(this.filePath.startsWith("./")) {
			String classPath = this.getClass().getClassLoader().getResource("").toString();
			if(classPath.startsWith("file:/")) {
				classPath = classPath.substring(6);
			}
			this.filePath =  classPath + this.filePath.substring(2);
		}
		
		this.file = new File(this.filePath);
		BlockingQueue<Runnable> searchQueue = new LinkedBlockingQueue<Runnable>();
		this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS, searchQueue);
	}
	
	private void setProperties() {
		
		java.util.Properties config = new Properties();
		//获取当前classLoader
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFile);
		try {
			if(input==null) {
				System.out.println("file.properties must be put at " + classLoader.getResource(""));
				return ;
			}
			config.load(input);
		} catch (IOException e) {
			e.printStackTrace();
			return ;
		}
		
		this.filePath = config.getProperty("file.path");
		
		String deep = config.getProperty("file.search.deep");
		if(deep!=null && !deep.isEmpty()) {
			this.searchDeep = Integer.parseInt(deep);
		}
		
		String escapedirectory = config.getProperty("escape.directory.match");
		if(escapedirectory!=null && !escapedirectory.isEmpty()) {
			this.escapedirectory = escapedirectory;
		}
		
		String countMode = config.getProperty("comment.countLines.mode");
		if(countMode!=null && !countMode.isEmpty()) {
			this.commentCountMode = countMode;
		}
		
		String blocksAlertONLines =  config.getProperty("comment.blocks.alertON.lines");
		if(blocksAlertONLines!=null && !blocksAlertONLines.isEmpty()) {
			this.blocksAlertONLines = Integer.parseInt(blocksAlertONLines);
		}
	}
	
	//搜索注释的字符串
	/**
	 * //开头，忽略本行，直接注释
	 */
	public static int checkComment(String src) {
		
		if(src !=null && src.length()>1) {
			if(src.startsWith("//")) {
				return 1;
			}
			if(src.indexOf("/*")!=-1) {
				
				if(src.indexOf("*/")!=-1) {
					return 1;
				}
				return 2;
			}
			if(src.indexOf("*/")!=-1) {
				return 3;
			}
		} else {
			return -1;
		}
		return 0;
	}
	
	/**单线程版
	 * 返回String[] 超过阈值的files
	 * @return
	 */
	public String[] countComment(File file) {
		//todo
		return null;
	}
	
	/**
	 * 
	 * @param file
	 * @param results ,.java任务文件列表
	 * @return
	 */
	public void countComment2(File file,List<Future<Integer>> results) {
		
		if(!file.exists()) {
			System.out.println("file not exist");
			return ;
		}
		System.out.println("file="+file.getName());
		//主线程判断是否文件，文件则交给线程池；
		//目录则 递归解析，深度搜索模式，
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			
			for(int i=0;i<files.length;i++) {
				
				if(files[i].isFile() && files[i].getName().endsWith(".java")) {
					results.add(executor.submit(new Processor(files[i])));
				} else if(files[i].isDirectory()){
					countComment2(files[i],results);
				}
			}
		}
		else if(file.isFile()&& file.getName().endsWith(".java")) {
			results.add(executor.submit(new Processor(file)));
		}

		return ;
	}
	
	public List getCommentList(){
		List<Future<Integer>> results = new ArrayList<Future<Integer>>();
		List<Integer> result = new ArrayList<Integer>();
		countComment2(file, results);
		for(Future<Integer> erFuture : results) {
			try {
				result.add(erFuture.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	
	private void leafSearch(List<FileDTO> leaves,File file,int deep) {

		if(file ==null || ++deep > this.searchDeep) {
			return;
		}
		if(file.getName().endsWith(this.escapedirectory)) {
			return;
		}
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(int i=0;i<files.length;i++) {
				leafSearch(leaves , files[i] , deep);
			}
		}
		else if(file.isFile()) {
			leaves.add(new FileDTO(file));
		}
		
		return;
	}
	
	public List<FileDTO> countFileConmment() {
		
		if(this.filePath == null) {
			return  null;
		}
		
		if(cache.containsKey(this.filePath)) {
			return (List<FileDTO>)cache.get(this.filePath);
		}
		
		List<FileDTO> fileList = new ArrayList<FileDTO>();
		
		List<FileDTO> resultList = new ArrayList<FileDTO>();
		
		leafSearch(fileList, this.file,0);
		
		List<Future<int[]>> t_result = new ArrayList<Future<int[]>>();
		if(!fileList.isEmpty()) {
			for(int i=0;i<fileList.size();i++) {
				t_result.add(this.executor.submit(new Processor(fileList.get(i).getFile(),this.commentCountMode)));
			}
		}
		
		//这里转换一下，不对外暴露底层的数据细节
		for(int i=0; i<t_result.size();i++) {
			try {
				int[] lines = t_result.get(i).get();
				FileDTO fileDTO = fileList.get(i);
				if(lines[0]> this.alertON) {
					fileDTO.setCommentLines(lines[0]);
				}
				if(lines[1]>= this.blocksAlertONLines) {
					fileDTO.setMaxBlockLines(lines[1]);
					resultList.add(fileDTO);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		//使concurrentHashMap 是否还要同步
		if(!cache.containsKey(this.filePath)) {
			cache.put(this.filePath, resultList);
		}
		
		return resultList;
	}
	
	//默认扫描java代码
	private static int[] checkLines(File file) {
		return checkLines(file,"0");
	}
	
	private static int[] checkLines(File file , String commentCountMode) {
		int commentCount=0;
		int maxBlockLines=0;
		int blockLines=0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String src=null;
			boolean waitCommentEnd = false;
			int lastcount=0;
			while((src = br.readLine())!=null) {
				
				//过滤空白字符
				//src = filterBlank(src);
				src=src.trim();
				if(src==null || src.isEmpty()) {
					continue;
				}
				int comment = checkComment(src);
				
				if(comment==2 && !waitCommentEnd) {
					waitCommentEnd=true;
					//commentCount--;
				}
				if(comment==3) {
					waitCommentEnd=false;
				}
				
				lastcount=commentCount;
				
				if((waitCommentEnd && comment!=-1) || comment==1) {
					
					// 如果/* 与  */ 中间的代码块，则应该认为是连续的块，如果中间有java行，则应该认为是注释的java
					
					if(!"0".equals(commentCountMode) || 
							("0".equals(commentCountMode) && 
									src.matches("[a-zA-Z0-9\\.\\(\\)\\*\\/;\\{\\}\"-]+"))) {
						if(!src.matches("[\\*\\/]+")) {
							commentCount++;
							blockLines++;
						}
					}
				}
				if(blockLines>0 && commentCount==lastcount) {
					maxBlockLines=Math.max(maxBlockLines, blockLines);
					blockLines=0;
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int[] result = new int[2];
		result[0] = commentCount;
		result[1] = maxBlockLines;
		return result;
	}
	
	private static String filterBlank(String src) {
		String result = null;
		//行首过滤
		for(int i=0;i<src.length();i++) {
			if(!Character.isWhitespace(src.charAt(i))) {
				result=src.substring(i);
				break;
			}
		}
		//行尾过滤
		if(result==null) {
			return null;
		}
		int length = result.length();
		for(int i=0;i<length;i++) {
			if(!Character.isWhitespace(result.charAt(length-i-1))) {
				result=result.substring(0,length-i);
				break;
			}
		}
		
		return result;
	}
	
	
	//搜索线程
	private class Processor implements Callable {
		
		private File file ;
		
		private String searchMode;
		
		public Processor(String filepath) {
			 this(new File(filepath));
		}
		public Processor(File file) {
			this.file = file;
			this.searchMode = "0";
		}
		
		public Processor(File file,String mode) {
			this.file = file;
			this.searchMode = mode;
		}

		public Object call() throws Exception {
			
			int[] result = FileSearchUtil.checkLines(file,searchMode);
			return result;
		}
		
	}
	
	
	public static void main(String[] args) {
		FileSearchUtil fsutil =new FileSearchUtil();
		
		long start = System.currentTimeMillis();
		List<FileDTO> fileDTOList = fsutil.countFileConmment();
		if(fileDTOList==null || fileDTOList.isEmpty()) {
			System.out.print("no result");
			return;
		}
		System.out.println("mutiThread mode time cost "+ (System.currentTimeMillis()-start) + "ms");
		
		// search in single thread mode
		
		for(FileDTO f : fileDTOList) {
			System.out.println(f.getFile().getPath()+ " comments = " + f.getCommentLines());
		}
		System.out.println(fileDTOList.size());
		//List result = fsutil.getCommentList();
		
	}

}
