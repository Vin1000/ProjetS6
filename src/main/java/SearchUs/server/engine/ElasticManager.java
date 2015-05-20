package SearchUs.server.engine;


    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.MalformedURLException;
    import java.net.URL;
    import java.net.URLConnection;
    import java.security.KeyStore;
    import java.util.List;

    import SearchUs.shared.data.SearchResultData;
    import com.google.gwt.json.client.JSONParser;
    import com.google.gwt.json.client.JSONValue;
    //import com.google.gwt.user.client.Window;

    import org.fusesource.restygwt.client.*;
    import org.fusesource.restygwt.client.Defaults;


    import SearchUs.server.engine.ElasticInterface;

    import com.google.gwt.core.client.EntryPoint;
    import com.google.gwt.core.client.GWT;




/**
 * Created by Paulo on 19/05/2015.
 */
public class ElasticManager {


    //Defaults.serviceRoot  = "127.0.0.1/";

    ElasticInterface searchEngine;

    public ElasticManager(){
       /*searchEngine = new ElasticInterface() {
           @Override
           public void postRawQuery(String query, MethodCallback<SearchResultData> callback) {

           }
       };*/
        searchEngine = Class.forName("ElasticInterface");
        Defaults.setServiceRoot("http://staging.gostamp.ca/api/_search");

    }

    public void queryStringREST(String query)
    {
        System.out.println("REST Search request made, query: " + query);

        Resource resource = new Resource("http://staging.gostamp.ca/api/_search");

        //JSONValue request = JSONParser.parseStrict("query");

        Method request =  resource.post();
        request.addData("query", query);
        request.send(
                new JsonCallback() {
                    public void onSuccess(Method method, JSONValue response) {
                        System.out.println("REST RESPONSE: " + response);
                    }

                    public void onFailure(Method method, Throwable exception) {
                        System.out.println("Error: " + exception);
                    }
                });
    }

    public void queryString(String query) {
        System.out.println("Search request made, query: " + query);

        String q = "{\n" +
                "        \"query_string\": {\n" +
                "            \"query\": \" " + query + "\"\n" +
                "        }\n" +
                "    }";

        searchEngine.postRawQuery(q, new MethodCallback<SearchResultData>() {

            public void onSuccess(Method method, SearchResultData r) {
                /* VerticalPanel panel = new VerticalPanel();
                for (SearchResultData hello : response) {
                    Label label = new Label(hello.getName());
                    panel.add(label);
                }
                RootLayoutPanel.get().add(panel);*/

                System.out.println("Result: titre: " + r.getTitle());

            }

            public void onFailure(Method method, Throwable exception) {
                System.out.println("Error!!!");

            }
        });

    }

    public void rawQuery()
    {
        try
        { // get URL content
            URL url = new URL("http://staging.gostamp.ca/api/_search");
            URLConnection conn = url.openConnection();
            //open the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            while ((inputLine = br.readLine()) != null)
            {
                System.out.println(inputLine);
            }
            br.close();
            System.out.println("Done");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
