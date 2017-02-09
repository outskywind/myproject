package com.java.utils.searchxml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class XmlSearch2 {
	
	private static HashMap<String, String> dependtransilation = new HashMap<String, String>();
	private static HashMap<String, String> dependedby = new HashMap<String, String>();
	private static HashSet<String> web2Controllers = new HashSet<String>();
	private static HashSet<String> removes = new HashSet<String>();
	
	private static HashMap<String,String> reserved = new HashMap<String,String>();
	private static HashSet<String> reserved_cache = new HashSet<String>();
	
	private static HashMap<String, String> web2BeanDefinition = new HashMap<String, String>();

	private static HashMap<String, String> appBeanDefinition = new HashMap<String, String>();
	private static HashMap<String, String> appBeanDefinition_diff = new HashMap<String, String>();
	
	private static HashMap<String, String> appdependtransilation = new HashMap<String, String>();
	private static MyClassLoader classLoader = null;
	private static String classPath = "D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/APP-INF/classes";
	private static String classPath_ejb = "D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/pa18shopauto-ejb.jar";
	private static String classLib = "D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/APP-INF/lib";
	private static String web2classpath = "D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/pa18shopauto-web2.war/WEB-INF/classes";
	
	@Test
	public void testGetWebxmldo() {

		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			//从classpath 加载
			//InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("");
			//the App first
			classLoader = getClassLoader();
			
			File base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-app/biz");
			findAllBeans(builder,base,"biz-");

			//the dmz
			base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-dmz/web");
			//findAllBeans(builder,base,"web-context");
			findWebBeans(builder,base,"dmz");
			//the web2
			base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-web2/web");
			findWebBeans(builder,base,"web2");

			//processDepends();

			//dmz用到的bean 或者class 是不可删除的，要从待删除列表中 dependtransilation 去掉
			
			//processfinalResult();
			//formatXmlOutPut();
			
			formatController();
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}
	
	private void formatController() {
		Iterator iterator = web2Controllers.iterator();
		while(iterator.hasNext()) {
			String value = (String)iterator.next();
			System.out.println(value);
		}
	}
	
	private void formatXmlOutPut() {
		Iterator<Entry<String, String>> it = dependtransilation.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String,String> e = (Entry<String, String>) it.next();
			String depenString = e.getValue();
			String[] ddStrings = depenString.split(",");
			String bean = (String) e.getKey();
			//web2
			String dp = web2BeanDefinition.get(bean);
			//app
			if(dp!=null && !dp.isEmpty()) {
				System.out.println(bean+"="+dp);
			}
			
			else{
				dp = appBeanDefinition.get(bean);
				if(dp!=null && !dp.isEmpty()) {
				System.out.println(bean+"="+dp);
				}
				else
					System.out.println(bean+"=");
			}
			for(int i=0;i<ddStrings.length;i++) {
				
				//是一个bean id ,
				String d_bean = ddStrings[i];
				String clazz =appBeanDefinition.get(d_bean);
				if(clazz==null || clazz.isEmpty()) {
					clazz = web2BeanDefinition.get(d_bean);
				}
				if(clazz!=null && !clazz.isEmpty()) {
					d_bean = d_bean+"="+clazz;
				}
				
				System.out.println("          "+ d_bean);
			}
			
			//System.out.println(controller+"="+e.getValue());
		}
	}
	
	private void formatXmlOutPutapp() {
		Iterator it = appdependtransilation.entrySet().iterator();
		while(it.hasNext()) {
			Entry e = (Entry) it.next();
			String depenString = (String) e.getValue();
			String[] ddStrings = depenString.split(",");
			String controller = (String) e.getKey();
			String con = appBeanDefinition.get(controller);
			
			System.out.println(controller+"="+con);
			for(int i=0;i<ddStrings.length;i++) {
				
				//是一个bean id ,
				String d_bean = ddStrings[i];
				String clazz =appBeanDefinition.get(d_bean);
				if(clazz!=null && !clazz.isEmpty()) {
					d_bean = d_bean+"="+clazz;
				}
				
				System.out.println("          "+ d_bean);
			}
			
			//System.out.println(controller+"="+e.getValue());
		}
	}

	private void findWebBeans(DocumentBuilder builder,File base ,String flag) {
		
		File[] xmls = base.listFiles();
		List<String> list= new ArrayList<String>();
		for(int i=0;i<xmls.length;i++) {
			File xmlFile = xmls[i];
			String name = xmlFile.getName();
			if(name.startsWith("web-context")) {
				list.add(xmlFile.getPath());
			}
		}
		
		//step2. resovle xml path as inputStream
		parse(builder,list,flag);
		
		//处理dmz传递出来的所有依赖
		//step3. cache all depends
		if("dmz".equals(flag)) {
			Iterator it = reserved.entrySet().iterator();
			while(it.hasNext()) {
				Entry<String,String> entry =(Entry) it.next();
				//
				String depend= entry.getValue();
				if(depend==null) {
					return;
				}
				
				String[] depends = depend.split(",");
				for(int i=0;i<depends.length;i++) {
					reserved_cache.add(depends[i]);
				}
			}
		}
	}
	
	
	
	private void findAllBeans(DocumentBuilder builder,File base ,String xmlpre) {
		
		File[] xmls = base.listFiles();
		List<String> list= new ArrayList<String>();
		for(int i=0;i<xmls.length;i++) {
			File xmlFile = xmls[i];
			String name = xmlFile.getName();
			if(name.startsWith(xmlpre)) {
				list.add(xmlFile.getPath());
			}
		}
		//step2. resovle xml path as inputStream
		parseDirect(builder, list , xmlpre);
	}
	
	private List<Element> getElement(Element root,String elementName) {
		List<Element> elements = new ArrayList<Element>(); 
		NodeList nodes = root.getChildNodes();
		for(int i=0;i<nodes.getLength();i++) {
			Node node = nodes.item(i);
			if(node instanceof Element) {
				Element e = (Element)node;
				if(e.getNodeName().equals(elementName)) {
					elements.add(e);
				}
			}
		}
		return elements;
	}
	
	
	private void buildDependedBy(String bean_id, Element bean) {
		
		List<Element> properties = getElement(bean, "property");
		
		for(int i=0;i<properties.size();i++) {
			Element depend = properties.get(i);
			List<Element> ref = getElement(depend, "ref");
			if(ref.size()==0) {
				continue;
			}
			Element refbean = ref.get(0);
			String dbean =refbean.getAttribute("bean");
			if(bean.equals("")) {
				continue;
			}
			String dependedbys=dependedby.get(dbean);
			if(dependedbys!=null) {
				StringBuffer sb = new StringBuffer(dependedbys);
				sb.append(",").append(bean_id);
				dependedbys = sb.toString();
			} else {
				dependedbys=bean_id;
			}
			dependedby.put(dbean, dependedbys);
		}
	}
	
	
	private void parseDirect(DocumentBuilder builder, List<String> list , String xmlpre) {
		
		for(int i=0;i<list.size();i++) {
			String xmlpath = list.get(i);
			try {
				InputStream inputStream = new FileInputStream(xmlpath);
				InputSource is = new InputSource(inputStream);
				
				//doc root
				Document doc = builder.parse(is);
				
				Element root =  doc.getDocumentElement();
				List<Element> beans = getElement(root,"bean");

				//获取所有注册的bean，对应的class，这些class都是被依赖的
				for(int j=0;j<beans.size();j++) {
					Element bean = beans.get(j);
					parseReserved(bean,xmlpre);
				}
				
			}
				catch (SAXException e) {
					System.out.println(xmlpath);
					e.printStackTrace();
				} catch (IOException e) {
					System.out.println(xmlpath);
					e.printStackTrace();
				}
		}
		
	}
	
	
	private void parseReserved(Element bean,String xmlpre) {
		//没有值返回""空串
		String id = bean.getAttribute("id");
		if(id.equals("")) {
			id = bean.getAttribute("name");
		}
		if(id.equals("")) {
			return;
		}
		
		String clazz = bean.getAttribute("class");
		if(xmlpre.equals("biz-")) {
			appBeanDefinition.put(id, clazz);
		}
		//
		if(clazz==null || clazz.isEmpty() || clazz.startsWith("org.springframework")
				|| clazz.contains("FacadeFactoryBean") ||clazz.contains("endowment")) {
			return;
		}
		List<Element> properties = getElement(bean,"property");
		//1.
		
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		for(int i=0;i<properties.size();i++) {
			Element depend = properties.get(i);
			
			//property name
			//如果 属性名 和bean 定义名称不一致，需要放到缓存表记录下来
			String properName = depend.getAttribute("name");
			
			
			List<Element> refs = getElement(depend, "ref");
			if(refs.size()==0) {
				continue;
			}
			Element dBean = refs.get(0);
			String attr =dBean.getAttribute("bean");
			if(attr.equals("")) {
				continue;
			}
			if(!properName.equals(attr)) {
				appBeanDefinition_diff.put(properName, attr);
			}
			if(flag) {
				sb.append(",");
			}
			sb.append(attr);
			flag=true;
		}
		
		//没有找到 xml 依赖，看是否autowire = byname
		if(sb.toString().isEmpty()) {
			//如果是自动注入依赖
			String autowire = bean.getAttribute("autowire");
			if(autowire!=null && autowire.equals("byName")) {
				sb = new StringBuffer("byName");
			}
		}
		
		if(xmlpre.equals("biz-")) {
			appdependtransilation.put(id, sb.toString());
			scanDependClasses(id, clazz,"app");
		}
		//buildDependedBy(id,bean);
	}

	private void parse(DocumentBuilder builder, List<String> list, String flag) {
		for(int i=0;i<list.size();i++) {
			String xmlpath = list.get(i);
			try {
				InputStream inputStream = new FileInputStream(xmlpath);
				InputSource is = new InputSource(inputStream);
				//doc root
				Document doc = builder.parse(is);
				Element root =  doc.getDocumentElement();
				List<Element> beans = getElement(root,"bean");
				/**
				System.out.println(root.getNodeName());
				NamedNodeMap attrs = root.getAttributes();
				for(int j=0;j<attrs.getLength();j++) {
					Node attr = attrs.item(j);
					System.out.println(attr.getNodeName()+"="+attr.getNodeValue());
				}
				**/
				for(int j=0;j<beans.size();j++) {
					Element bean=beans.get(j);
					processBean(bean,flag);
				}
			}
			catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void scanDependClasses(String id,String clazz, String flag) {
		
		if(!clazz.startsWith("com.paic.pa18shop")) {
			return;
		}
		
		//需要处理 文件读取时的 facade,用classloader 处理好.
		List<String> importsList = searchbyfile(id, clazz, flag);
		
		//dmz 不需要classloader 处理 byName;
		if("app".equals(flag)) {
			searchbyclassloader(id,importsList,clazz);
		}
		
	}

	private List<String> searchbyfile(String id, String clazz, String flag) {
		File file = new File("D:/ws/PA18Shopauto1.42.0/src/java/"+clazz.replace(".", "/")+".java");
		//System.out.println(file.getParent());
		if(file==null || !file.exists()) {
			return null;
		}
		String tmpLine = null;
		try {
			BufferedReader bf = new BufferedReader(new FileReader(file));
			
			List<String> class_imports = new ArrayList<String>();
			HashMap<String,String> esbs = new HashMap<String,String>();
			
			StringBuffer totalLine=new StringBuffer();
			while((tmpLine=bf.readLine())!=null) {
				tmpLine=tmpLine.trim();
				if(tmpLine==null || tmpLine.isEmpty()) {
					continue;
				}
				
				totalLine.append(tmpLine);
				if(!tmpLine.endsWith(";")) {
					continue;
				}
				else{
					tmpLine = totalLine.toString();
					totalLine =new StringBuffer();
				}
				
				if(tmpLine.startsWith("import")) {
					String clazz_import = tmpLine.substring(tmpLine.indexOf("import")+6,tmpLine.indexOf(";")).trim();
					//只记录shop里的代码
					if(clazz_import.startsWith("com.paic.pa18shop")&&
							!clazz_import.equals("com.paic.pa18shop.country.web.util.ServiceRequestID")) {
						
						if(isClass(clazz_import)){
							recorddepened(id,clazz_import,flag);
							class_imports.add(clazz_import);
						}
					}
				}
				else if(tmpLine.indexOf("setRequestedServiceID")!=-1) {
					String clazz_esb = tmpLine.substring(tmpLine.indexOf("setRequestedServiceID")+"setRequestedServiceID".length()+1,tmpLine.indexOf(")")).trim();
					esbs.put(clazz_esb, "");
				}
			}
			if(!esbs.isEmpty()) {
				recordDepenedesb(id,class_imports , esbs ,flag);
			}
			//返回接口列表和DTO等列表
			return class_imports;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			System.out.println(tmpLine);
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean isClass(String clazz) {
		boolean isclazz = false;
		try {
			classLoader.loadClass(clazz);
			isclazz =  true;
		} catch(Exception e){
		}
		return isclazz;
	}
	
	private void searchbyclassloader(String id,List<String> importsList, String clazz) {
		try {
			Class clazz_c =classLoader.loadClass(clazz);
			
			Field[] fields = clazz_c.getDeclaredFields();
			String[] depends =  appdependtransilation.get(id).split(",");
			for(int i=0;i<fields.length;i++) {
				String var_name = fields[i].getName();
				
				String bean = appBeanDefinition_diff.get(var_name);
				if(bean!=null && !bean.isEmpty()) {
					var_name = bean;
				}
				Class var_clazz = fields[i].getType();
				if(var_clazz.isInterface()) {
					//如果是接口，查找替换接口,此时已经在 {@link app appdependtransilation}记录了，需要替换成 var_name 对应的bean name
					//---查找bean 定义:在app解析过程中调用时，appBeanDefinition 还没解析完成，有些bean 找不到
					//记录bean
					for(String clazz_import:importsList) {
						
						try {
							String var_clazz_name = var_clazz.getCanonicalName();
							if(clazz_import.equals(var_clazz_name)) {
								for(int j=0;j<depends.length;j++) {
									if(clazz_import.equals(depends[j])) {
										//System.out.println(clazz_import+" to= "+var_name);
										depends[j] = var_name;
									}
								}
							}
						} catch(Exception e){
							//the import is not a class
							for(int j=0;j<depends.length;j++) {
								if(clazz_import.equals(depends[j])) {
									depends[j] = null;
								}
							}
						}
					}
				}
			}
			StringBuffer sb = new StringBuffer();
			for(int i=0;i<depends.length;i++) {
				if(depends[i]==null) {
					continue;
				}
				if(sb.length()>0) {
					sb.append(",");
				}
				sb.append(depends[i]);
			}
			appdependtransilation.put(id, sb.toString());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
	}
	
	
	
	private void recordDepenedesb(String id, List<String> imports , HashMap esbs ,String flag) {
		
		for(Iterator it =  esbs.keySet().iterator();it.hasNext();) {
			String esb = (String)it.next();
			if(esb.contains("\"")) {
				esb = esb.substring(1,esb.length()-1);
			}
			else {
				String clazz = esb.substring(0,esb.indexOf("."));
				String feild = esb.substring(esb.indexOf(".")+1);
				for(String clazz_name : imports) {
					if(clazz_name.endsWith(clazz)) {
						try {
							Class clazz_c =classLoader.loadClass(clazz_name);
							Field f = clazz_c.getDeclaredField(feild);
							String value = (String) f.get(null);
							recorddepened(id,value,flag);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
	
	
	private void recorddepened(String id,String clazz_import, String flag) {
		
		if("web2".equals(flag)) {
			if(reserved_cache.contains(clazz_import)) {
				//System.out.println(clazz_import);
				return;
			}
			record(dependtransilation,id,clazz_import);
		}
		else if("app".equals(flag)){
			record(appdependtransilation,id,clazz_import);
		}
		else if("dmz".equals(flag)) {
			record(reserved,id,clazz_import);
		}
	}
	
	private void record(HashMap<String,String> map , String id , String clazz_import) {
		String clazz = clazz_import;
		String dependeds = map.get(id);
		if(dependeds!=null && !dependeds.isEmpty()) {
			StringBuffer sb=new StringBuffer(dependeds);
			clazz = sb.append(",").append(clazz_import).toString();
		}
		//bean id 或者 class限定名
		map.put(id, clazz);
	}
	
	
	private void processBean(Element bean ,String flag) {
		
		//step1  check attributes
		//NamedNodeMap attrs = bean.getAttributes();
		Map controller = new HashMap();
		String key = "";
		String value ="";
		
		String id = bean.getAttribute("id");
		if(id.equals("")) {
			id = bean.getAttribute("name");
		}
		String clazz = bean.getAttribute("class");
		
		if(clazz==null || clazz.isEmpty() || clazz.startsWith("org.springframework")
				|| clazz.contains("FacadeFactoryBean") ||clazz.contains("endowment")) {
			return;
		}
		if(flag.equals("dmz")) {
			reserved.put(id, clazz);
		} else {
			web2BeanDefinition.put(id, clazz);
			if(id.endsWith(".do")|| id.endsWith("screen")|| clazz.endsWith("Controller")) {
				web2Controllers.add(id);
			}
		}

		//step2  check depend beans
		//1.查找到依赖的bean A 放入依赖set
		//2.查找bean A是否被DMZ Bean X依赖，如果没有， 放入 可删除map
		//3.如果有其他Bean X依赖，查找X 是否在可删除map中，如果X不可删除，则 A查找结束；如果X可删除 则继续查找所有 bean 集合,
		//直到所有bean查完，还没有，则 A 可删除。
		//需要一个所有的bean 依赖map
		//more effectively, just need the .do and action bean depend-beans-map。
		List<Element> properties = getElement(bean,"property");
		//1.
		
		StringBuffer sb = new StringBuffer();
		boolean isok = false;
		for(int i=0;i<properties.size();i++) {
			Element depend = properties.get(i);
			List<Element> refs = getElement(depend, "ref");
			if(refs.size()==0) {
				continue;
			}
			Element dBean = refs.get(0);
			String attr =dBean.getAttribute("bean");
			if(attr.equals("") || (flag.equals("web2") && reserved_cache.contains(attr))) {
				//System.out.println(attr);
				continue;
			}
			if(isok) {
				sb.append(",");
			}
			sb.append(attr);
			isok=true;
		}
		
		//从class 文件再查，补充DTO 和caller 等不在bean依赖xml里配置的
		
		//StringBuffer toString() 如果没有任何值，返回的是一个"".
		//这里所有 xml 里的bean 依赖
		if(flag.equals("dmz")) {
			reserved.put(id, sb.toString());
			scanDependClasses(id, clazz,"dmz");
		}
		else {
			dependtransilation.put(id, sb.toString());
			scanDependClasses(id,clazz,"web2");
		}
	}
	
	
	public class MyClassLoader extends URLClassLoader{

		public MyClassLoader(URL[] urls) {
			super(urls);
		}
		
		public void addURL(URL[] urls) {
			for(int i=0;i<urls.length;i++) {
				addURL(urls[i]);
			}
		}
		
		public void addURL(URL url) {
			super.addURL(url);
		}
		
	}
	
	
	private MyClassLoader getClassLoader() {
		
		File file =  new File(classPath);
		File ejb =  new File(classPath_ejb);
		File lib = new File(classLib);
		File[] libjars = lib.listFiles();
		
		File web2 = new File(web2classpath);
		try {
			URL[] urls={file.toURI().toURL(),lib.toURI().toURL(),web2.toURI().toURL(),ejb.toURI().toURL()};
			MyClassLoader classLoader = new MyClassLoader(urls);
			for(int i=0;i<libjars.length;i++) {
				URL url = libjars[i].toURI().toURL();
				classLoader.addURL(url);
			}
			return classLoader;
			}
		catch (MalformedURLException e){
		e.printStackTrace();
		}
		return null;
	}

	private void processfinalResult() {
		
		//1.
		processAllDepends("dmz");
		reserved_cache=new HashSet<String>();
		
		Iterator<Entry<String, String>> iterator = reserved.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String,String> e = iterator.next();
			String clazzorbean = e.getValue();
			String[] bean = clazzorbean.split(",");
			for(int i=0;i<bean.length;i++) {
				reserved_cache.add(bean[i]);
			}
		}
		processAllDepends("web2");

	}
	
	
	//1.处理dmz所有的依赖，这些是不能被删除的，放入 reserved_cache
	private void processAllDepends(String flag) {
		
		//待处理队列
		List<String> chained = new ArrayList<String>();
		//待处理结果集
		HashSet<String> cache = new HashSet<String>();
		//初始化待处理队列
		Iterator<Entry<String, String>> iterator = 
				flag.equals("web2")?dependtransilation.entrySet().iterator():reserved.entrySet().iterator();
		while(iterator.hasNext()) {
			Entry<String,String> e = iterator.next();
			String clazzorbean = e.getValue();
			String[] bean = clazzorbean.split(",");
			for(int i=0;i<bean.length;i++) {
				if(!cache.contains(bean[i])) {
					chained.add(bean[i]);
					cache.add(bean[i]);
				}
			}
		}
		//初始化队列完成
		//开始解析队列
		for(int i=0;i<chained.size();i++) {
			String bean = chained.get(i);
			getAppDepends(bean,chained,cache , flag);
		}
	}
	
	private void getAppDepends(String bean , List<String> chained , HashSet<String> cache , String flag) {
		
		//1.先查找app的依赖，如果是空的，表示这个被依赖的class 全名不是 app的bean，是web2 或者dmz的class如caller。
		String finded = appBeanDefinition.get(bean);
		//是service facade ；以及action
		HashMap<String, String> map = flag.equals("web2")? dependtransilation : reserved;
		
		if(flag.equals("web2")) {
			if(finded!=null) {
				String dep = appdependtransilation.get(bean);
				if(dep!=null) {
					//dep 需要过滤掉不能删除的 依赖
					StringBuffer sb = new StringBuffer();
					String[] deps = dep.split(",");
					for(int i=0;i<deps.length;i++) {
						if(reserved_cache.contains(deps[i])) {
							continue;
						}
						if(sb.length()>0) {
							sb.append(",");
						}
						sb.append(deps[i]);
						if(!cache.contains(deps[i])) {
							chained.add(deps[i]);
							cache.add(deps[i]);
						}
					}
					map.put(bean, sb.toString());
				}
			}
		}
		else {
			if(finded!=null) {
				String dep = appdependtransilation.get(bean);
				if(dep!=null) {
					map.put(bean, dep);
					String[] deps = dep.split(",");
					for(int i=0;i<deps.length;i++) {
						if(!cache.contains(deps[i])) {
							chained.add(deps[i]);
							cache.add(deps[i]);
						}
					}
				}
			}
		}
		//非app 的bean,比如dto 和util 之类，以及caller之类（dmz）
		//不解析
		
	}
	
	private void processRemoves(String controllerDepend, List<String> chained) {
		chained.add(controllerDepend);
		if(appBeanDefinition.get(controllerDepend)==null 
				|| dependedby.get(controllerDepend)==null) {
			//the bean only defined in web2 or used in web2
			removes.add(controllerDepend);
		}
		else {
			try {
				recurse(controllerDepend,chained);}
			catch(Exception e) {
				System.out.println(controllerDepend);
			}
		}
	}

	
	private int recurse(String bean_id, List<String> chained) {
		
		//if this bean can be removed
		//1.this bean has not been defined in app ,or if defined in app , only used in web2,i.e. not used in dmz
		//2. not depended  by other bean , or only depended by other removable bean .
		//3. caller is not defined in xml bean , so  triverse the controllers get out the caller , and find out the caller depends
		String dependedbys = dependedby.get(bean_id);
		//System.out.println("="+dependedbys);
		int removesCount = 0;
		//none dependeds
		if(dependedbys==null ) {
			removes.add(bean_id);
			return 1;
		} else if(removes.contains(bean_id)) {
			return 1;
		}
		else  {
			String[] beans = dependedbys.split(",");
			
			//如果标记不可删除，直接返回 0;
			for(int i=0;i<beans.length;i++) {
				if(reserved.containsKey(beans[i])) {
					return 0;
				}
			}
			for(int i=0;i<beans.length;i++) {
				//表明这个依赖项是之前的，出现循环依赖
				if(chained.contains(beans[i])) {
					return 1;
				}
				if(removes.contains(beans[i]) || web2BeanDefinition.containsKey(beans[i])) {
					removesCount++;
				} else {
					chained.add(beans[i]);
					removesCount+=recurse(beans[i],chained);
				}
			}
			if(removesCount==beans.length) {
				removes.add(bean_id);
				return 1;
			} else {
				return 0;
			}
		}
	}
	

}
