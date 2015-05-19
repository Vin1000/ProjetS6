package SearchUs.shared.dispatch.search;

import com.gwtplatform.dispatch.rpc.shared.UnsecuredActionImpl;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchAction extends UnsecuredActionImpl<SearchResult> {
    private String searchText;

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public SearchAction(){}

    public SearchAction(String searchText) {
        this.searchText = searchText;
    }
}
