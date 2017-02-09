package com.java.utils.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
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

public class testXml {
	
	@Test
	public  void main() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream in = new FileInputStream("D:/ws/PA18Shopauto1.42.0/src/config/config-dmz/web/web-context-payment.xml");
			InputSource is = new InputSource(in);
			Document doc=builder.parse(is);
			Element root=doc.getDocumentElement();
			
			List<Element> nodes=testXml.getElement(root, "bean");
			for(int i=0;i<nodes.size(); i++) {
				Element bean=nodes.get(i);
				String beanid = bean.getAttribute("name");
				if(beanid==null) {
					beanid = bean.getAttribute("id");
				}
				System.out.println(beanid);
				List<Element> properties =testXml.getElement(bean, "property");
				for(int j=0;j<properties.size();j++) {
					Element property = properties.get(j);
					List<Element> refs = testXml.getElement(property, "ref");
					
					if(refs.size()==0) {
						continue;
					}
					String attr = refs.get(0).getAttribute("bean");
					System.out.println("++"+attr);
				}
				
			}
			
			
		} catch (ParserConfigurationException e) {
			
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Element> getElement(Element root,String elementName) {
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
	
	@Test
	public void testLoaderLog4j() {
		try {
			URL[] urls= {
					new File("D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/APP-INF/lib/log4j-1.2.15.jar").toURI().toURL(),
					new File("D:/ws/PA18Shopauto1.42.0/dist/weblogic/pa18shopauto/APP-INF/lib/commons-logging.jar").toURI().toURL()};
			
			//and if set the classloader in contextclassloader,
			//if the class was loaded by 
			
			//如果是junit 测试 ，会自动将 lib下的commons-logging.jar 添加到默认的 classpath下。
			//启动 junit 测试时，这些类将会被 System AppClassLoader 加载 。 fuck。
			
			MyClassLoader loader = new MyClassLoader(urls);

			//first delegate to parent classloader to load this class ,if loaded ,then return ;
			//if all parent classloader loaded failed ,finally invoke the 
			//user-defined calssloader's method findClass(String clazz) to find and load  it .
			//
			Class clazz=loader.loadClass("org.apache.commons.logging.impl.LogFactoryImpl");

			Thread.currentThread().setContextClassLoader(loader);
			System.out.println(clazz.getClassLoader()+";;classpath="+clazz.getClassLoader().getResource(""));

			Class[] parameterTypes = new Class[1];
			parameterTypes[0] = String[].class;
			//the getMethod only returns the public method
			//Method m= clazz.getMethod("newInstance", parameterTypes);
			
			Method m = clazz.getDeclaredMethod("newInstance", String.class);
			m.setAccessible(true);
			m.invoke(clazz.newInstance(),clazz.getCanonicalName());
			
		}
		catch(Exception e) {
			e.printStackTrace();
			//if not setted ,it will be null
			//System.out.println(e.getMessage());
		}
		
	}
	
	//user-defined classloader must override the loadClass to change the load rules.
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
		
		public URL[] getURL() {
			return super.getURLs();
		}
		
		
		// ordinary the method need not to be overrided,
		// it just will be called by yourself classloader
		/**
		@Override
		public Class<?> findClass(String clazz) throws ClassNotFoundException{
				Class<?> aClass = findLoadedClass(clazz);
				System.out.println("the clazz name="+clazz);
				if(aClass!=null) {
					System.out.println(clazz+"is already loaded by " + aClass.getClassLoader());
					return aClass;
				}
				if(!clazz.startsWith("org.apache")) {
					System.out.println(clazz+"is  loaded by  parent loader ");
					return super.loadClass(clazz);
				}
				//find and load the class
				InputStream is = this.getResourceAsStream(clazz);
				if(is==null) {
					System.out.println("is  = null");
					throw new ClassNotFoundException(clazz);
				}
				try {
					byte[] b = new byte[is.available()];
					is.read(b);
					defineClass(clazz, b, 0, b.length);
					
					
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				return null;
			} **/
		}
	
	
	

}
