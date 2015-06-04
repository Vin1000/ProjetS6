package SearchUs.client.application.home.searchbar;

import SearchUs.shared.data.FileType;
import com.gwtplatform.mvp.client.UiHandlers;

import java.util.ArrayList;

public interface SearchBarUiHandlers extends UiHandlers {
    void sendSearch(String searchText);
    void ShowAdvancedOptions();
}
