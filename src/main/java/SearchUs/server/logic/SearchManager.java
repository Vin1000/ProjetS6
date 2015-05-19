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
        results.add(new SearchResultData(searchText + ".txt", "http://www.gel.usherbrooke.ca/s6info/e15", "Text result"));
        results.add(new SearchResultData(searchText + ".pdf", "http://www.gel.usherbrooke.ca/s6info/e15", "PDF result"));
        results.add(new SearchResultData(searchText + ".docx", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));

        return results;
    }
}
