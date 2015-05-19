package SearchUs.server.logic;

import SearchUs.server.session.UserSessionImpl;
import SearchUs.shared.data.SearchResultData;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchManager {

    private UserSessionImpl session;

    @Inject
    public SearchManager(UserSessionImpl session) {
        this.session = session;
    }

    public ArrayList<SearchResultData> getSearchResults(String searchText){

        ArrayList<SearchResultData> results = new ArrayList<SearchResultData>();
        results.add(new SearchResultData(searchText + " first Result", "No_URL", "This is the first result of your search"));
        results.add(new SearchResultData(searchText + " second Result", "No_URL", "This is the second result of your search"));
        results.add(new SearchResultData(searchText + " third Result", "No_URL", "This is the third result of your search"));

        return results;
    }
}
