package SearchUs.client.application.home.searchbar;

import SearchUs.client.application.home.searchbar.searchoption.SearchOptionModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SearchBarModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new SearchOptionModule());
        bindPresenter(SearchBarPresenter.class, SearchBarPresenter.MyView.class, SearchBarView.class, SearchBarPresenter.MyProxy.class);
    }
}
