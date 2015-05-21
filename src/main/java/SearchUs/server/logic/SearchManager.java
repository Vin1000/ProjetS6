package SearchUs.server.logic;

import SearchUs.server.engine.ElasticManager;
import SearchUs.server.session.UserSessionImpl;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchResult;

import com.google.gwt.thirdparty.json.JSONArray;
import com.google.gwt.thirdparty.json.JSONException;
import com.google.gwt.thirdparty.json.JSONObject;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchManager {

    private UserSessionImpl session;

    public static final String SERVER_URL = "http://45.55.164.162";

    @Inject
    public SearchManager(UserSessionImpl session) {
        this.session = session;
    }

    public SearchResult getSearchResults(String searchText){


        SearchResult result = new SearchResult();

        ArrayList<SearchResultData> listResults = new ArrayList<SearchResultData>();

        //todo: injecter l'objet.
        ElasticManager searchEngine = new ElasticManager(SERVER_URL);

        JSONObject queryResult =  searchEngine.search(searchText);


        try {
            JSONObject hits = queryResult.getJSONObject("hits");

            int total = hits.getInt("total");

            JSONArray resultsArray =  hits.getJSONArray("hits");

            JSONObject hit;
            JSONObject hitSource;
            JSONObject file;
            String filename ;
            String url;
            String description;

            for(int i=0;i<total;i++)
            {
                hit = resultsArray.getJSONObject(i);
                System.out.println(hit.getString("_type"));
                if(hit.getString("_type").equals("doc") )//todo: enforce
                {
                    System.out.println("if begin");
                    hitSource = hit.getJSONObject("_source");
                    file = hitSource.getJSONObject("file");
                    filename = file.getString("filename");
                    url = hitSource.getJSONObject("path").getString("real").replace("/var/www/html", SERVER_URL);
                    description = hitSource.getString("content");

                    listResults.add(new SearchResultData(filename,url,description));
                    System.out.println("New element created");

                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*results.add(new SearchResultData(result.);*/
        /*listResults.add(new SearchResultData(searchText + ".pdf", "http://www.gel.usherbrooke.ca/s6info/e15", "PDF result"));
        listResults.add(new SearchResultData(searchText + ".docx", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        listResults.add(new SearchResultData(searchText + ".babinlenoob", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        listResults.add(new SearchResultData(searchText + ".png", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        listResults.add(new SearchResultData(searchText + ".java", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        listResults.add(new SearchResultData(searchText + ".zip", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        listResults.add(new SearchResultData(searchText + ".doc", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));*/

        result.setSearchResults(listResults);
        return result;
    }
}
