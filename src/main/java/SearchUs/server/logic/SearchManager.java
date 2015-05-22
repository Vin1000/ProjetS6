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
import java.util.List;

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

    public SearchResult getSearchResults(String searchText)
    {
        SearchResult result = new SearchResult();

        ArrayList<SearchResultData> listResults = new ArrayList<SearchResultData>();

        //todo: injecter l'objet.
        ElasticManager searchEngine = new ElasticManager(SERVER_URL);

        JSONObject queryResult =  searchEngine.search(searchText);

        if(queryResult != null)
        {
            try {
                JSONObject hits = queryResult.getJSONObject("hits");

                int totalHits = hits.getInt("total");
                int took = queryResult.getInt("took");

                JSONArray resultsArray =  hits.getJSONArray("hits");

                JSONObject hit;
                JSONObject hitSource;
                JSONObject file;
                JSONObject meta;
                String filename ;
                String url;
                String description;
                String author;
                String title;
                String date;
                List<String> keywords = null;

                for(int i=0;i<totalHits;i++)
                {
                    hit = resultsArray.getJSONObject(i);
                    //System.out.println(hit.getString("_type"));
                    if(!hit.getString("_type").equals("folder") )//todo: enforce
                    {
                        hitSource = hit.getJSONObject("_source");
                        file = hitSource.getJSONObject("file");
                        filename = file.getString("filename");
                        url = hitSource.getJSONObject("path").getString("real").replace("/var/www/html", SERVER_URL);
                        description = hitSource.getString("content");

                        meta = hitSource.getJSONObject("meta");
                        author = meta.getString("author");
                        title = meta.getString("title");
                        date = meta.getString("date");

                        listResults.add(new SearchResultData(filename, url, description,author,title,date,keywords));

                    }

                }

                result.setTimeElapsed(took);
                result.setTotalHits(totalHits);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        result.setSearchResults(listResults);
        return result;
    }
}
