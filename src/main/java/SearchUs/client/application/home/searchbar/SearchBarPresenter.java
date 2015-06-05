



package SearchUs.client.application.home.searchbar;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.OptionEvent;
import SearchUs.client.application.events.SearchEvent;
import SearchUs.client.application.home.HomePagePresenter;
import SearchUs.client.application.home.searchbar.searchoption.SearchOptionPresenter;
import SearchUs.client.place.NameTokens;
import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;


public class SearchBarPresenter extends Presenter<SearchBarPresenter.MyView, SearchBarPresenter.MyProxy> implements SearchBarUiHandlers, OptionEvent.OptionEventHandler {
    interface MyView extends View, HasUiHandlers<SearchBarUiHandlers> {
    }

    SearchDetails searchDetails;

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchBarPresenter> {
    }

    @Inject protected SearchOptionPresenter searchOptionPresenter;

    @Inject
    public SearchBarPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        getView().setUiHandlers(this);
        ArrayList defaultFileType= new ArrayList<FileType>();
        defaultFileType.add(FileType.ALL);
        searchDetails = new SearchDetails();
        searchDetails.setSearchFor(defaultFileType);
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(OptionEvent.getType(), this);
    }

    @Override
    public void sendSearch(String searchText) {
        searchDetails.setSearchString(searchText);
        SearchEvent.fire(this, searchDetails);
    }

    @Override
    public void onOptionEvent(OptionEvent event) {
        searchDetails = event.getSearchDetails();
    }

    @Override
    public void ShowAdvancedOptions()
    {
        addToPopupSlot(searchOptionPresenter);
    }
}
