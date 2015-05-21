
package SearchUs.client.application.home.resultwidget;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class ResultWidgetModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenterWidget(ResultWidgetPresenter.class, ResultWidgetPresenter.MyView.class, ResultWidgetView.class);
    }
}
