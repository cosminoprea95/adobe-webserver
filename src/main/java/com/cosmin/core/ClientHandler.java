package com.cosmin.core;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import com.cosmin.http.EmptyRequestException;
import com.cosmin.http.HttpRequest;
import com.cosmin.http.HttpResponse;
import com.cosmin.servlet.HttpServlet;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class ClientHandler implements Runnable {
	private Socket socket;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try{
			HttpRequest request = new HttpRequest(socket);
			HttpResponse response = new HttpResponse(socket);
			String url = request.getRequestURI();
			String servletName = ServerContext.getServletName(url);
			if(servletName != null){
				Class clz = Class.forName(servletName);
				HttpServlet regservlet = (HttpServlet)clz.newInstance();
				regservlet.service(request, response);
			}else{
				File file = new File("webapps" + url);
				if (file.exists()) {
					response.setEntity(file);
				} else {
					File notFoundPage = new File("webapps/root/404.html");
					response.setStatusCode(404);
					response.setEntity(notFoundPage);
				}
			}
			response.flush();
		} catch (EmptyRequestException e) {
			System.out.println(e.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}