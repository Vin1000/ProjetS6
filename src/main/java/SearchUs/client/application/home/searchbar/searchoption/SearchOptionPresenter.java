
package SearchUs.client.application.home.searchbar.searchoption;

import SearchUs.client.application.events.OptionEvent;
import SearchUs.shared.data.SearchDetails;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class SearchOptionPresenter extends PresenterWidget<SearchOptionPresenter.MyView> implements SearchOptionUiHandlers {
    public interface MyView extends PopupView, HasUiHandlers<SearchOptionUiHandlers> {
    }

    @Inject
    public SearchOptionPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);
        getView().setUiHandlers(this);
    }

    public void onOkClicked(SearchDetails searchDetails)
    {
        OptionEvent.fire(this, searchDetails);
    }
}
