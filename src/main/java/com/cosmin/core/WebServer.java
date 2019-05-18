package com.cosmin.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class WebServer {
	private ServerSocket server;
	private ExecutorService threadPool;
	private WebServer() {
		try {
			server = new ServerSocket(8080);
			threadPool = Executors.newFixedThreadPool(100);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void start() {
		try {
			while (true) {
				Socket socket = server.accept();
				ClientHandler handler = new ClientHandler(socket);
				threadPool.execute(handler);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WebServer server = new WebServer();
		server.start();
	}

}
