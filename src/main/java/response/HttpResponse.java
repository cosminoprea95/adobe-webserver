package response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Created by cosminoprea on 5/1/19.
 */
public class HttpResponse {

    private HashMap<String, String> headers;

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void write(OutputStream out) {
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write("\r\n");

                for (String key: headers.keySet()) {
                    writer.write(key + ":" + headers.get(key));
                    writer.write("\r\n");
                }
                writer.write("\r\n");



                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();

        }
    }
}
