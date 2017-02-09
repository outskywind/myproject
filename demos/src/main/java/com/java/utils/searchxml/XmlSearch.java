package com.java.utils.searchxml;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.Set;
import java.util.Vector;

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

import sun.reflect.generics.scope.ClassScope;

public class XmlSearch {
	
	private static HashMap<String, String> dependtransilation = new HashMap<String, String>();
	private static HashMap<String, String> dependedby = new HashMap<String, String>();
	private static HashSet<String> web2Controllers = new HashSet<String>();
	private static HashSet<String> removes = new HashSet<String>();
	private static HashMap<String,String> reserved = new HashMap<String,String>();
	private static HashMap<String, String> web2BeanDefinition = new HashMap<String, String>();
	private static HashMap<String, String> dmzBeanDefinition = new HashMap<String, String>();
	private static HashMap<String, String> appBeanDefinition = new HashMap<String, String>();
	
	private static String classPath = "D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/APP-INF/classes";
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
			File base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-app/biz");
			findAllBeans(builder,base,"biz-");
			
			//the dmz
			base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-dmz/web");
			findAllBeans(builder,base,"web-context");
			
			//the web2
			base = new File("D:/ws/PA18Shopauto1.42.0/src/config/config-web2/web");
			findWebBeans(builder,base);
			//
			processDepends();
			/**
			Set<Entry<String,String>> entries=dependedby.entrySet();
			for(Iterator<Entry<String,String>> it= entries.iterator(); it.hasNext();) {
				Entry<String,String> entry = it.next();
				System.out.println(entry.getKey()+"="+entry.getValue());
			}
			**/
			//
			for(String bean : web2Controllers) {
				
				if(web2BeanDefinition.get(bean)!=null) {
					String beanClass = web2BeanDefinition.get(bean);
					System.out.println(bean+"="+beanClass);
				}
				if(appBeanDefinition.get(bean)!=null) {
					String beanClass = appBeanDefinition.get(bean);
					System.out.println(bean+"="+beanClass);
				}
			}
			System.out.println(removes.size());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		//依赖删除
		

	}
	

	private void findWebBeans(DocumentBuilder builder,File base ) {
		
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
		parse(builder, list);
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
					//.do 或者 action 自己对应的class 也算被依赖的
					//没有值返回""空串
					String id = bean.getAttribute("id");
					if(id.equals("")) {
						id = bean.getAttribute("name");
					}
					String clazz = bean.getAttribute("class");
					if(xmlpre.equals("biz-")) {
						appBeanDefinition.put(id, clazz);
					}else {
						//这是web的bean id=对应的class
						dmzBeanDefinition.put(id, clazz);
					}
					buildDependedBy(id,bean);
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
	
	private void parse(DocumentBuilder builder, List<String> list ) {
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
					processBean(bean);
				}
			}
			catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	private void processBean(Element bean ) {
		
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
		web2BeanDefinition.put(id, clazz);
		
		//controller
		if(id.endsWith(".do")|| id.endsWith("screen")|| clazz.endsWith("Controller")) {
			web2Controllers.add(id);
			//to do : resolve the caller depends
			//resolveCallers(clazz);
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
		boolean flag = false;
		for(int i=0;i<properties.size();i++) {
			Element depend = properties.get(i);
			List<Element> refs = getElement(depend, "ref");
			if(refs.size()==0) {
				continue;
			}
			Element dBean = refs.get(0);
			String attr =dBean.getAttribute("bean");
			if(attr.equals("")) {
				continue;
			}
			if(flag) {
				sb.append(",");
			}
			sb.append(attr);
			flag=true;
		}
		dependtransilation.put(id, sb.toString());
	}
	
	private void resolveCallers(String clazz) {
		try {
			File file =  new File(classPath);
			File lib = new File(classLib);
			File[] libjars = lib.listFiles();
			
			
			File web2 = new File(web2classpath);
			URL[] urls= {file.toURI().toURL(),lib.toURI().toURL(),web2.toURI().toURL()};
			MyClassLoader classLoader = new MyClassLoader(urls);
			for(int i=0;i<libjars.length;i++) {
				URL url = libjars[i].toURI().toURL();
				classLoader.addURL(url);
				
			}
			URL[] urls2 =classLoader.getURLs();
			for(int i=0;i<urls2.length;i++) {
				System.out.println(urls2[i]);
			}
			
			//添加资源路径
			Thread.currentThread().setContextClassLoader(classLoader);
			
			classLoader.loadClass(clazz).newInstance();
			Field f = classLoader.getClass().getDeclaredField("classes");
			f.setAccessible(true);
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Vector<Class> classes = (Vector<Class>) f.get(classLoader);
			
			for(@SuppressWarnings("rawtypes")
			Iterator<Class> it = classes.iterator();it.hasNext();) {
				@SuppressWarnings("rawtypes")
				Class clazz_ = it.next();
				String classname = clazz_.getCanonicalName();
				System.out.println(classname);
			}
			
		} catch(MalformedURLException e) {
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SecurityException e) {
			
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
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
	
	
	
	private void processDepends(){
		
		for(String controller : web2Controllers) {
			removes.add(controller);
			String[] controllerDepends = dependtransilation.get(controller).split(",");
			for(String controllerDepend :controllerDepends) {
				if(appBeanDefinition.get(controllerDepend)==null 
						|| dependedby.get(controllerDepend)==null) {
					//the bean only defined in web2 or used in web2
					removes.add(controllerDepend);
				}
				else {
					recurse(controllerDepend);
				}
			}
		}
	}

	
	private int recurse(String bean_id) {
		
		//if this bean can be removed
		//1.this bean has not been defined in app ,or if defined in app , only used in web2,i.e. not used in dmz
		//2. not depended  by other bean , or only depended by other removable bean .
		//3. caller is not defined in xml bean , so  triverse the controllers get out the caller , and find out the caller depends

		String dependedbys = dependedby.get(bean_id);
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
			for(int i=0;i<beans.length;i++) {
				if(removes.contains(beans[i]) || web2BeanDefinition.containsKey(beans[i])) {
					removesCount++;
				} else {
					removesCount+=recurse(beans[i]);
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
