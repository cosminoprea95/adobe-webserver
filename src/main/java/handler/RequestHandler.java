package handler;

import application.Application;
import application.FileApplication;
import request.HttpRequest;
import request.RequestParser;
import response.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.logging.Logger;

/**
 * Created by cosminoprea on 5/1/19.
 */
public class RequestHandler implements Runnable {

    private Application app;
    private Socket connection;

    public RequestHandler(Socket connection){
        this.app = new FileApplication();
        this.connection = connection;
    }

    @Override
    public void run() {
        {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = connection.getInputStream();
                out = connection.getOutputStream();

                HttpRequest request = RequestParser.parse(in);

                if (request != null) {

                    HttpResponse response = app.handle(request);

                    response.getHeaders().put("Server", "SERVER_NAME");
                    response.getHeaders().put("Date", Calendar.getInstance().getTime().toString());
                    response.getHeaders().put("Connection", "close");

                    response.write(out);
                } else {
                    Logger.getGlobal().info( "Server accepts only HTTP protocol.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
