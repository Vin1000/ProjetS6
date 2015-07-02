
package SearchUs.client.application.home.pagerwidget; 

import SearchUs.client.application.events.ChangePageEvent;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

public class PagerWidgetPresenter extends PresenterWidget<PagerWidgetPresenter.MyView> implements PagerWidgetUiHandlers {
    public interface MyView extends View, HasUiHandlers<PagerWidgetUiHandlers> {
        void setPageNumber(int pageNumber);
        void setBold();
    }

    @Inject
    PagerWidgetPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }

    public void setPageNumber(int pageNumber)
    {
        getView().setPageNumber(pageNumber);
    }

    public void onChangePageClick(int number)
    {
        ChangePageEvent.fire(this, new ChangePageEvent(number));
    }
}
