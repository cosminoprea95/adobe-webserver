package com.cosmin.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class ServerContext {
	private static final Map<String, String> SERVLET_MAPPING = new HashMap<String, String>();
	static{
		initServletMapping();
	}
	private static void initServletMapping(){
		try {
			SAXReader sax = new SAXReader();
			Document doc = sax.read("conf/servlets.xml");
			Element root = doc.getRootElement();
			List<Element> eleList = root.elements("servlet");
			for(Element e : eleList){
				String url = e.attributeValue("url");
				String className = e.attributeValue("className");
				SERVLET_MAPPING.put(url, className);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getServletName(String url){
		return SERVLET_MAPPING.get(url);
	}

	public static void main(String[] args) {
		System.out.println(getServletName("/myweb/login"));
	}
}
