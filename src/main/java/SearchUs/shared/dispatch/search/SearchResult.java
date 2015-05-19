package SearchUs.shared.dispatch.search;

import SearchUs.shared.data.SearchResultData;
import com.gwtplatform.dispatch.rpc.shared.Result;

import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchResult implements Result {
    private ArrayList<SearchResultData> searchResults;

    public ArrayList<SearchResultData> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<SearchResultData> searchResults) {
        this.searchResults = searchResults;
    }
}
