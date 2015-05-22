package SearchUs.client.application.home.searchbar;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SearchBarModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SearchBarPresenter.class, SearchBarPresenter.MyView.class, SearchBarView.class, SearchBarPresenter.MyProxy.class);
    }
}
