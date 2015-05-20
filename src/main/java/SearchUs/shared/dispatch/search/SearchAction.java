package SearchUs.shared.dispatch.search;

import SearchUs.shared.data.SearchDetails;
import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchAction extends UnsecuredActionImpl<SearchResult> {
    private SearchDetails searchDetails;

    public SearchDetails getSearchDetails() {
        return searchDetails;
    }

    public void setSearchDetails(SearchDetails searchDetails) {
        this.searchDetails = searchDetails;
    }

    public SearchAction(){}

    public SearchAction(SearchDetails searchDetails) {
        this.searchDetails = searchDetails;
    }
}
