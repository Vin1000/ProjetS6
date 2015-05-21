package SearchUs.server.engine;


import com.google.gwt.thirdparty.json.JSONException;
import com.google.gwt.thirdparty.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 20/05/2015.
 */
public class ElasticManager {

    private String serviceUrl;


    public ElasticManager(String url) {

        this.serviceUrl = url+":9200/files/"; //todo: verifier url
    }
    /*private HttpEntity httpPost(String endpoint, String data )
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
    }*/

    private String makePost(String endpoint, String data )
    {
        String responseBody = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(this.serviceUrl+"_search");
            httpPost.setEntity(new StringEntity(data));

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse( final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpPost, responseHandler);

        }
        catch (IOException ex)
        {

        }
        finally {
            try { httpclient.close(); } catch (Exception ignore) {}
        }
        return responseBody;
    }


    public JSONObject search(String queryString) {

        String query;
        if(queryString.equals("*"))
        {
            query=  "{\n" +
                    "  \"query\" : {\n" +
                    "    \"match_all\" : {}\n" +
                    "  }\n" +
                    "}";
        }
        else
        {
            JSONObject jsonQuery = new JSONObject();
            JSONObject match = new JSONObject();
            JSONObject completeQuery = new JSONObject();

            try {
                match.put("_all",queryString);
                jsonQuery.put("match",match);
                completeQuery.put("query",jsonQuery);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            query = completeQuery.toString();
        }




        String searchResult = makePost("_search",query);
        System.out.println("Search result: "+searchResult);
        JSONObject jsonObj = null;
        System.out.println("query: "+query);

        if(searchResult != null)
        {
            try
            {
                jsonObj = new JSONObject(searchResult);
            }
            catch(com.google.gwt.thirdparty.json.JSONException ex)
            {
                System.out.println("Json decoding exception: "+ex.toString());
            }
        }


        return jsonObj;

    }
}