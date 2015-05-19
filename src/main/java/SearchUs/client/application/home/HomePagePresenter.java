package SearchUs.client.application.home;

import javax.inject.Inject;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.place.NameTokens;
import SearchUs.client.place.TokenParameters;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> implements HomeUiHandler {
    public interface MyView extends View, HasUiHandlers<HomeUiHandler> {
    }

    private final PlaceManager placeManager;

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject
    HomePagePresenter(EventBus eventBus,
                      MyView view,
                      MyProxy proxy,
                      PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
        getView().setUiHandlers(this);
        this.placeManager=placeManager;
    }

    @Override
    public void sendSearch(String searchText) {
        PlaceRequest responsePlaceRequest = new PlaceRequest.Builder()
                .nameToken(NameTokens.getSearchResult())
                .with(TokenParameters.SEARCH_STRING, searchText)
                .build();

        placeManager.revealPlace(responsePlaceRequest);
    }
}
