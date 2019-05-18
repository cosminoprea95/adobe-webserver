package com.cosmin.servlet;

import java.io.RandomAccessFile;
import java.util.Arrays;

import com.cosmin.http.HttpRequest;
import com.cosmin.http.HttpResponse;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class RegServlet extends HttpServlet {

    public void service(HttpRequest request, HttpResponse response) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        int age = Integer.parseInt(request.getParameter("age"));
        System.out.println(username + "," + password + "," + nickname + "," + age);

        try (RandomAccessFile raf = new RandomAccessFile("user.dat", "rw")) {
            raf.seek(raf.length());
            byte[] data = username.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = password.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = nickname.getBytes("utf-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            raf.writeInt(age);
        } catch (Exception e) {
            e.printStackTrace();
        }

        forward("myweb/reg_success.html", request, response);

    }

}
