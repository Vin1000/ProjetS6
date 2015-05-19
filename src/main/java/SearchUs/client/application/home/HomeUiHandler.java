package SearchUs.client.application.home;

import com.gwtplatform.mvp.client.UiHandlers;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public interface HomeUiHandler extends UiHandlers {
    void sendSearch(String searchText);
}
