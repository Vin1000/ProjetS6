



package SearchUs.client.application.searchresult;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.SearchEvent;
import SearchUs.client.place.NameTokens;
import SearchUs.shared.data.SearchDetails;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import java.util.ArrayList;
import java.util.logging.Logger;

public class SearchResultPresenter extends Presenter<SearchResultPresenter.MyView, SearchResultPresenter.MyProxy> implements SearchResultUiHandlers, SearchEvent.GlobalHandler {
    interface MyView extends View, HasUiHandlers<SearchResultUiHandlers> {
        void setLabel(String text);
    }

    @NameToken(NameTokens.SearchResult)
    @ProxyStandard
    public interface MyProxy extends ProxyPlace<SearchResultPresenter> {
    }

    private DispatchAsync dispatcher;
    private SearchDetails searchDetails;

    @Inject
    public SearchResultPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            DispatchAsync dispatcher) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    protected void sendSearchAction()
    {
        getView().setLabel((searchDetails.getSearchString()));
        SearchAction searchAction = new SearchAction(searchDetails);
        dispatcher.execute(searchAction, new AsyncCallback<SearchResult>() {
            @Override //TODO: Change actions done here
            public void onSuccess(SearchResult result) {
                ArrayList<SearchResultData> searchResults = result.getSearchResults();
                getView().setLabel(searchResults.get(0).getTitle());
            }

            @Override
            public void onFailure(Throwable caught) {
                Logger logger = java.util.logging.Logger.getLogger("Error Log variable");
                logger.log(java.util.logging.Level.SEVERE, "La recherche a échoué, veuillez réessayer plus tard");
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(SearchEvent.getType(), this);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public void onGlobalEvent(SearchEvent event) {
        this.searchDetails = new SearchDetails(event.getSearchDetails());
        sendSearchAction();
    }
}
