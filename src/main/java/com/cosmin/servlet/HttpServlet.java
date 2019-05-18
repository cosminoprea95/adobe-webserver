package com.cosmin.servlet;

import java.io.File;

import com.cosmin.http.HttpRequest;
import com.cosmin.http.HttpResponse;
/**
 * Created by cosminoprea on 5/10/19.
 */
public abstract class HttpServlet {

	public abstract void service(HttpRequest request, HttpResponse response);

	public void forward(String path, HttpRequest request, HttpResponse response){
		File file = new File("webapps/" + path);
		response.setEntity(file);
	}
	
}
