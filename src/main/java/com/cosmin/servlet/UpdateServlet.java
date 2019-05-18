package com.cosmin.servlet;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import com.cosmin.http.HttpRequest;
import com.cosmin.http.HttpResponse;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class UpdateServlet extends HttpServlet{

	@Override
	public void service(HttpRequest request, HttpResponse response) {
		String username = request.getParameter("username");
		String oldpwd = request.getParameter("oldpwd");
		String newpwd = request.getParameter("newpwd");

		try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")){
			boolean isUpdate = false;
			for(int i = 0; i < raf.length()/100; i++){
				raf.seek(i*100);
				byte[] data = new byte[32];
				raf.read(data);
				String user = new String(data, "utf-8").trim();

				if(user.equals(username)){
					data = new byte[32];
					raf.read(data);
					String pwd = new String(data, "utf-8").trim();
					if(pwd.equals(oldpwd)){

						raf.seek(i*100+32);
						byte[] bytes = newpwd.getBytes("utf-8");
						bytes = Arrays.copyOf(bytes, 32);
						raf.write(bytes);
						isUpdate = true;
					}
					break;
				}
			}
			
			if(isUpdate){
				forward("myweb/update_success.html", request, response);
			}else{
				forward("myweb/update_fail.html", request, response);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
