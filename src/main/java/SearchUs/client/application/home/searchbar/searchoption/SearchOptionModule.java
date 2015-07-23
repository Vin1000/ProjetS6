package SearchUs.client.application.home.searchbar.searchoption;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SearchOptionModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(SearchOptionPresenter.class, SearchOptionPresenter.MyView.class, SearchOptionView.class);
    }
}
