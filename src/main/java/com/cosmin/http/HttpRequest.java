package com.cosmin.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cosminoprea on 5/10/19.
 */
public class HttpRequest {
    private String method;
    private String url;
    private String protocol;

    private String requestURI;
    private String queryString;
    private Map<String, String> parameters = new HashMap<>();

    private Map<String, String> headers = new HashMap<>();

    private Socket socket;
    private InputStream in;

    public HttpRequest(Socket socket) throws EmptyRequestException {
        try {
            this.socket = socket;
            this.in = socket.getInputStream();
            parseRequestLine(); // 1
            parseHeaders(); // 2
            parseContent(); // 3
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EmptyRequestException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseRequestLine() throws EmptyRequestException {
        String line = readLine();
        if ("".equals(line)) {
            throw new EmptyRequestException();
        }
        String[] data = line.split("\\s");
        method = data[0];
        url = data[1];
        protocol = data[2];
        parseURL();
    }

    private void parseURL() {
        if (url.indexOf("?") != -1) {
            String[] data = url.split("\\?");
            this.requestURI = data[0];
            // �ж�"?"�����Ƿ���ʵ�ʵĲ�������
            if (data.length > 1) {
                this.queryString = data[1];
                try {
                    parseParameters(this.queryString);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            this.requestURI = url;
        }
    }

    private void parseParameters(String line) throws UnsupportedEncodingException {
        line = decode(line);
        String[] paraArr = line.split("&");
        for (String string : paraArr) {
            String[] arr = string.split("=");
            if (arr.length > 1) {
                parameters.put(arr[0], arr[1]);
            } else {
                parameters.put(arr[0], null);
            }
        }

    }

    private String decode(String line) throws UnsupportedEncodingException {
        return URLDecoder.decode(line, "utf-8");
    }

    private void parseHeaders() {
        while (true) {
            String line = readLine();
            if ("".equals(line)) {
                break;
            }

            String[] data = line.split(": ");
            headers.put(data[0], data[1]);
        }
    }

    private void parseContent() {
        try {
            if (headers.containsKey("Content-Length")) {
                int len = Integer.parseInt(headers.get("Content-Length"));
                byte[] data = new byte[len];
                in.read(data);
                String contentType = headers.get("Content-Type");
                if ("application/x-www-form-urlencoded".equals(contentType)) {
                    String line = new String(data, "ISO8859-1");
                    line = URLDecoder.decode(line, "utf-8");
                    try {
                        parseParameters(line);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLine() {
        StringBuilder sb = new StringBuilder();
        try {
            int pre = -1;
            int cur;
            while ((cur = in.read()) != -1) {
                if (pre == 13 && cur == 10) {
                    break;
                }
                sb.append((char) cur);
                pre = cur;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString().trim();
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return this.parameters.get(name);
    }
}
