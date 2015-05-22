package SearchUs.client.application.home.searchbar;

import com.gwtplatform.mvp.client.UiHandlers;

public interface SearchBarUiHandlers extends UiHandlers {
    void sendSearch(String searchText,int pageNumber);
}
