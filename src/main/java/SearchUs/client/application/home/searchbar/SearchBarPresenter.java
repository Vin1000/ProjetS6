



package SearchUs.client.application.home.searchbar;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.ClearSearchResultsEvent;
import SearchUs.client.application.events.OptionEvent;
import SearchUs.client.application.home.searchbar.searchoption.SearchOptionPresenter;
import SearchUs.client.place.NameTokens;
import SearchUs.shared.data.FieldType;
import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import java.util.ArrayList;


public class SearchBarPresenter extends Presenter<SearchBarPresenter.MyView, SearchBarPresenter.MyProxy> implements SearchBarUiHandlers, OptionEvent.OptionEventHandler {
    interface MyView extends View, HasUiHandlers<SearchBarUiHandlers> {
        void ClickEvent(String searchString);
        void SetSearchText(String searchText);
    }

    SearchDetails searchDetails;
    PlaceManager placeManager;

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchBarPresenter> {
    }

    @Inject protected SearchOptionPresenter searchOptionPresenter;

    @Inject
    public SearchBarPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        this.placeManager = placeManager;

        getView().setUiHandlers(this);
        searchDetails = new SearchDetails();
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(OptionEvent.getType(), this);
    }

    @Override
    protected void onReveal(){
        super.onReveal();
    }

    @Override
    public void sendSearch(String searchText) {
        searchDetails.setSearchString(searchText);

        PlaceRequest request = new PlaceRequest.Builder()
                .nameToken(NameTokens.getHome())
                .build();

        placeManager.revealPlace(searchDetails.ToUrlString());
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

    @Override
    public void LogoClick()
    {
        searchDetails = new SearchDetails();
        ClearSearchResultsEvent.fire(this);
    }

    public void UpdateFromUrl(String searchText)
    {
        getView().SetSearchText(searchText);
    }
}
