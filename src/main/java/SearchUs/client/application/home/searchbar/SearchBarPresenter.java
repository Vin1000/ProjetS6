



package SearchUs.client.application.home.searchbar;

import SearchUs.client.application.events.SearchEvent;
import SearchUs.client.application.home.HomePagePresenter;
import SearchUs.client.place.NameTokens;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;


public class SearchBarPresenter extends Presenter<SearchBarPresenter.MyView, SearchBarPresenter.MyProxy> implements SearchBarUiHandlers {
    interface MyView extends View, HasUiHandlers<SearchBarUiHandlers> {
    }

    //private final PlaceManager placeManager;

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchBarPresenter> {
    }

    @Inject
    public SearchBarPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy/*,
            PlaceManager placeManager*/) {
        super(eventBus, view, proxy);

        getView().setUiHandlers(this);
        //this.placeManager = placeManager;
    }

    @Override
    public void sendSearch(String searchText) {
        /*PlaceRequest responsePlaceRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.getSearchResult())
                .build();
        placeManager.revealPlace(responsePlaceRequest);*/

        SearchEvent.fire(this, new SearchDetails(searchText));
    }
}
