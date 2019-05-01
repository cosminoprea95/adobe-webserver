package application;


import request.HttpRequest;
import response.HttpResponse;

/**
 * Created by cosminoprea on 5/1/19.
 */
public interface Application {

    HttpResponse handle(HttpRequest request);
}
