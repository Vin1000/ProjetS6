package SearchUs.client.application.home;

import javax.inject.Inject;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.home.searchbar.SearchBarPresenter;
import SearchUs.client.application.home.searchresult.SearchResultPresenter;
import SearchUs.shared.data.SearchDetails;
import SearchUs.client.place.NameTokens;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> implements HomeUiHandler {
    public interface MyView extends View, HasUiHandlers<HomeUiHandler> {
    }

    public static final Object SLOT_SEARCHBAR = new Object();
    public static final Object SLOT_SEARCHRESULTS = new Object();

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject protected SearchBarPresenter searchBarPresenter;
    @Inject protected SearchResultPresenter searchResultPresenter;

    @Inject
    HomePagePresenter(EventBus eventBus,
                      MyView view,
                      MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

    }

    @Override
    protected  void onBind()
    {
        super.onBind();

        addToSlot(SLOT_SEARCHBAR, searchBarPresenter);
        addToSlot(SLOT_SEARCHRESULTS, searchResultPresenter);
    }

    @Override
    public void prepareFromRequest(PlaceRequest placeRequest) {
        super.prepareFromRequest(placeRequest);
        SearchDetails searchDetails = new SearchDetails();
        searchDetails.GetUrlParameters(placeRequest.getParameterNames().toArray(), placeRequest);
        searchBarPresenter.UpdateFromUrl(searchDetails.getSearchString());
        searchResultPresenter.UpdateFromUrl(searchDetails);
    }
}
