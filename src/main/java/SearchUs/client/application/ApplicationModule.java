package SearchUs.client.application;

import SearchUs.client.application.home.HomeModule;
import SearchUs.client.application.resultwidget.ResultWidgetModule;
import SearchUs.client.application.searchresult.SearchResultModule;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new HomeModule());
        install(new SearchResultModule());
        install(new ResultWidgetModule());
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
