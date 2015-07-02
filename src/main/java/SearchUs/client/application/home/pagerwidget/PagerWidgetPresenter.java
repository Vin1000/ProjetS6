
package SearchUs.client.application.home.pagerwidget; 

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PagerWidgetPresenter extends PresenterWidget<PagerWidgetPresenter.MyView> implements PagerWidgetUiHandlers {
    public interface MyView extends View, HasUiHandlers<PagerWidgetUiHandlers> {
    }

    @Inject
    PagerWidgetPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }


}
