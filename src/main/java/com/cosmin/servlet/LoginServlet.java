package com.cosmin.servlet;

import java.io.RandomAccessFile;

import com.cosmin.http.HttpRequest;
import com.cosmin.http.HttpResponse;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class LoginServlet extends HttpServlet{
	
	public void service(HttpRequest request, HttpResponse response) {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "r")) {
			byte[] b = new byte[32];
			boolean check = false;
			for (int i = 0; i < raf.length() / 100; i++) {
				raf.seek(i * 100);
				raf.read(b);
				String dbUserName = new String(b, "utf-8").trim();

				if (dbUserName.equals(username)) {

					raf.read(b);
					String dbPassword = new String(b, "utf-8").trim();

					if (dbPassword.equals(password)) {
						forward("myweb/login_success.html", request, response);
						check = true;
						break;
					}
				}
			}

			if (!check) {
				forward("myweb/login_fail.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
