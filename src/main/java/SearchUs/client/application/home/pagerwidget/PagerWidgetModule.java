
package SearchUs.client.application.home.pagerwidget;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class PagerWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(PagerWidgetPresenter.class, PagerWidgetPresenter.MyView.class, PagerWidgetView.class);
    }
}
