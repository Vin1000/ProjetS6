package SearchUs.server.engine;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 20/05/2015.
 */
public class ElasticManager {

    private String serviceUrl;

    public ElasticManager() {

        this.serviceUrl = "http://staging.gostamp.ca/api/";
    }

    private HttpEntity httpPost(String endpoint, String data )
    {
        HttpEntity entity = null;

        try
        {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            System.out.println("Post request; Endpoint: " + endpoint + " ,Data: " + data);

            HttpPost httpPost = new HttpPost(this.serviceUrl+"_search");

            httpPost.setEntity(new StringEntity(data));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
               // System.out.println("Search result: " + response2.getStatusLine());
                entity = response2.getEntity();

            } finally {
                response2.close();
            }
        }
        catch (java.io.IOException ex){
            System.out.println("Error2 : " + ex.toString());

        }

        return entity;
    }

    public void search(String query) {

        String jsonQuery = query;
        InputStream inputStream = null;
        HttpEntity queryResult = httpPost("_search",jsonQuery);

        byte[] buffer = new byte[1024];
        if (queryResult != null) {
            try {
                inputStream = queryResult.getContent();
                int bytesRead = 0;
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                while ((bytesRead = bis.read(buffer)) != -1) {
                    String chunk = new String(buffer, 0, bytesRead);
                    System.out.println("Chunk: "+ chunk);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try { inputStream.close(); } catch (Exception ignore) {}
            }
        }

    }
}