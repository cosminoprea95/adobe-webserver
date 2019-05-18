package com.cosmin.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class HttpResponse {

    private int statusCode = 200;
    private String statusReason = "OK";

    private Map<String, String> headers = new HashMap<>();

    private File entity;

    private OutputStream out;

    public HttpResponse(Socket socket) {
        try {
            out = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        sendStatusLine();
        sendHeaders();
        sendContent();
    }

    public void sendStatusLine() {
        try {
            String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHeaders() {
        try {
            Set<Entry<String, String>> entrySet = headers.entrySet();
            for (Entry<String, String> header : entrySet) {
                String line = header.getKey() + ": " + header.getValue();
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);
                out.write(10);
            }

            out.write(13);
            out.write(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendContent() {

        if (entity == null) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(entity);) {
            byte[] b = new byte[1024 * 10];
            int len;
            while ((len = fis.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        String fileName = entity.getName();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        String contentType = HttpContext.getMimeType(ext);
        this.headers.put("Content-Type", contentType);
        this.headers.put("Content-Length", String.valueOf(entity.length()));
        this.entity = entity;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        this.statusReason = HttpContext.getStatusReason(statusCode);
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public void putHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

}
