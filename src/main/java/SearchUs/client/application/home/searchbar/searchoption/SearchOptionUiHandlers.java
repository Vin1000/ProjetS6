package SearchUs.client.application.home.searchbar.searchoption;

import SearchUs.shared.data.SearchDetails;
import com.gwtplatform.mvp.client.UiHandlers;

public interface SearchOptionUiHandlers extends UiHandlers {
    public void onOkClicked(SearchDetails searchDetails);
}
