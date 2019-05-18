package com.cosmin.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class HttpContext {

	private static final Map<Integer, String> STATUS_MAPPING = new HashMap<Integer, String>();

	private static final Map<String, String> MIME_MAPPING = new HashMap<String, String>();

	static {
		initStatusMapping();
		initMimeMapping();
	}

	private static void initStatusMapping() {
		STATUS_MAPPING.put(200, "OK");
		STATUS_MAPPING.put(201, "Created");
		STATUS_MAPPING.put(202, "Accepted");
		STATUS_MAPPING.put(204, "No Content");
		STATUS_MAPPING.put(301, "Moved Permanently");
		STATUS_MAPPING.put(302, "Moved Temporarily");
		STATUS_MAPPING.put(304, "Not Modified");
		STATUS_MAPPING.put(400, "Bad Request");
		STATUS_MAPPING.put(401, "Unauthorized");
		STATUS_MAPPING.put(403, "Forbidden");
		STATUS_MAPPING.put(404, "Not Found");
		STATUS_MAPPING.put(500, "Internal Server Error");
		STATUS_MAPPING.put(501, "Not Implemented");
		STATUS_MAPPING.put(502, "Bad Gateway");
		STATUS_MAPPING.put(503, "Service Unavailable");
	}
	private static void initMimeMapping(){
		try{
			SAXReader sax = new SAXReader();
			Document doc = sax.read(new File("./conf/web.xml"));
			Element root = doc.getRootElement();
			List<Element> mimetList = root.elements("mime-mapping");
			for(Element mimeElement : mimetList){
				String key = mimeElement.elementTextTrim("extension");
				String value = mimeElement.elementTextTrim("mime-type");
				MIME_MAPPING.put(key, value);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(MIME_MAPPING);
		System.out.println(MIME_MAPPING.size());
	}
	static String getStatusReason(int statusCode) {
		return STATUS_MAPPING.get(statusCode);
	}
	static String getMimeType(String ext){
		return MIME_MAPPING.get(ext);
	}
	
	public static void main(String[] args) {
		String fileName = "XXX.mp4";
		String[] split = fileName.split("\\.");
		System.out.println(getMimeType(split[split.length-1]));
	}
}
