package SearchUs.client.application.searchresult;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class SearchResultModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(SearchResultPresenter.class, SearchResultPresenter.MyView.class, SearchResultView.class, SearchResultPresenter.MyProxy.class);
    }
}
