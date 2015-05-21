package SearchUs.client.application.home;

import SearchUs.client.application.home.resultwidget.ResultWidgetModule;
import SearchUs.client.application.home.searchbar.SearchBarModule;
import SearchUs.client.application.home.searchresult.SearchResultModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class HomeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new SearchBarModule());
        install(new SearchResultModule());
        install(new ResultWidgetModule());
        bindPresenter(HomePagePresenter.class, HomePagePresenter.MyView.class, HomePageView.class,
                HomePagePresenter.MyProxy.class);
    }
}
