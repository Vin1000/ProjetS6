package SearchUs.server.logic;

import SearchUs.server.engine.ElasticManager;
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

        //todo: injecter l'objet.
        ElasticManager searchEngine = new ElasticManager();

        searchEngine.search(searchText);

        ArrayList<SearchResultData> results = new ArrayList<SearchResultData>();
        results.add(new SearchResultData(searchText + ".txt", "http://www.gel.usherbrooke.ca/s6info/e15", "Text result"));
        results.add(new SearchResultData(searchText + ".pdf", "http://www.gel.usherbrooke.ca/s6info/e15", "PDF result"));
        results.add(new SearchResultData(searchText + ".docx", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        results.add(new SearchResultData(searchText + ".babin", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        results.add(new SearchResultData(searchText + ".png", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        results.add(new SearchResultData(searchText + ".java", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        results.add(new SearchResultData(searchText + ".zip", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));
        results.add(new SearchResultData(searchText + ".doc", "http://www.gel.usherbrooke.ca/s6info/e15", "Word result"));

        return results;
    }
}
