package SearchUs.server.engine;


import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.thirdparty.json.JSONArray;
import com.google.gwt.thirdparty.json.JSONException;
import com.google.gwt.thirdparty.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Paulo on 20/05/2015.
 */
public class ElasticManager {

    private String serviceUrl;

    public ElasticManager(String url) {

        this.serviceUrl = url+":9200/files/doc/"; //todo: verifier url
    }


    private String makePost(String endpoint, String data )
    {
        String responseBody = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpPost httpPost = new HttpPost(this.serviceUrl+endpoint);
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
            ex.printStackTrace();
        }
        finally {
            try { httpclient.close(); } catch (Exception ignore) {}
        }
        return responseBody;
    }

    //Not used
    /*private String makeGet(String endpoint)
    {
        String responseBody =  null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try
        {
            HttpGet httpget = new HttpGet(this.serviceUrl+endpoint);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
             responseBody = httpclient.execute(httpget, responseHandler);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return responseBody;
    }*/



    public JSONObject search(SearchDetails searchInfo) {

        String query = buildFileSearchQuery(searchInfo);

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

    private String buildFileSearchQuery(SearchDetails searchInfo)
    {
        // Champs de texte entré par l'utilisateur pour la recherche
        String queryString = searchInfo.getSearchString();
        String queryContent; // la partie query de la requête
        String query;   // requête complete, ce qu'on retourne


        /* Section types de fichier */
        ArrayList<FileType> searchInDocTypes = searchInfo.getSearchFor();
        String fileTypes = "[";
        Integer numberTypes = 0;

        System.out.print("DOCTYPES: " + (searchInDocTypes.get(0)).toString());

        for(FileType type : searchInDocTypes)
        {
            if(numberTypes != 0)
                fileTypes += ",";
            fileTypes += "\""+type.toString().toLowerCase() +"\"";
            numberTypes++;
        }

        fileTypes += "]";


        /* Section queryString */
        if(queryString.contains("*"))
        {
            queryContent = "                  \"wildcard\":\n" +
                    "                  {\n" +
                    "                      \"_all\":\"*\"\n" +
                    "                  }\n";
        }
        else {
            queryContent = "\"match\":{\"_all\": \""+ queryString+"\"}";
        }

        /* Section filters */
        String searchFilters;

        //S'il n'y a pas de date specifiée
        if(searchInfo.getSearchDate() == null)
        {
            searchFilters = "                \"script\" :\n" +
                    "                {\n" +
                    "                  \"file\" : \"tamtamfi\",\n" +
                    "                  \"lang\" : \"javascript\",\n" +
                    "                  \"params\" :\n" +
                    "                  {\n" +
                    "                      \"fileType\" : " + fileTypes +",\n" +
                    "                      \"numberTypes\" : "+ numberTypes.toString().toLowerCase() +"\n" +
                    "                  }\n" +
                    "                }\n";
        }
        else
        {
            searchFilters = "              \t\"bool\" :\n" +
                    "                {\n" +
                    "                \t\"must\" : \n" +
                    "                      [\n" +
                    "                      \t{\n" +
                    "                          \"script\" :\n" +
                    "                          {\n" +
                    "                            \"file\" : \"tamtamfi\",\n" +
                    "                            \"lang\" : \"javascript\",\n" +
                    "                            \"params\" :\n" +
                    "                            {\n" +
                    "                                \"fileType\" : " + fileTypes +",\n" +
                    "                                \"numberTypes\" : "+ numberTypes.toString().toLowerCase() +"\n" +
                    "                            }\n" +
                    "                          }\n" +
                    "                        },\n" +
                    "                        {\n" +
                    "                          \"range\":\n" +
                    "                          {\n" +
                    "                              \"date\" : \n" +
                    "                              {\n" +
                    "                                  \"gte\" : \""+searchInfo.getSearchDate() +"\"\n" +  //
                    "                              }\n" +
                    "                          }\n" +
                    "                         }\n" +
                    "                       ]\n" +
                    "                }";
        }


        /* Finalement on bati la query */
        query = "{\n" +
                "\"size\" : 100,"+
                "  \t\"query\":\n" +
                "    {\n" +
                "          \"filtered\" :\n" +
                "          {\n" +
                "              \"query\" :\n" +
                "              {\n" + queryContent +
                "              },\n" +
                "              \"filter\" :\n" +
                "              {\n" +
                searchFilters +
                "              }\n" +
                "          }\n" +
                "  \t}\n" +
                "}";
        return query;

    }
}