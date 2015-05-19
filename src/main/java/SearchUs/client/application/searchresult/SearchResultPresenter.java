



package SearchUs.client.application.searchresult;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.place.NameTokens;
import SearchUs.client.place.TokenParameters;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

public class SearchResultPresenter extends Presenter<SearchResultPresenter.MyView, SearchResultPresenter.MyProxy> implements SearchResultUiHandlers {
    interface MyView extends View, HasUiHandlers<SearchResultUiHandlers> {
        void setLabel(String text);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> SLOT_searchResult = new Type<RevealContentHandler<?>>();

    @NameToken(NameTokens.SearchResult)
    @ProxyStandard
    public interface MyProxy extends ProxyPlace<SearchResultPresenter> {
    }

    private DispatchAsync dispatcher;

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

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String searchString = request.getParameter(TokenParameters.SEARCH_STRING, null);
        SearchAction searchAction = new SearchAction(searchString);

        dispatcher.execute(searchAction, new AsyncCallback<SearchResult>() {
            @Override //TODO: Change actions done here
            public void onSuccess(SearchResult result) {
                ArrayList<SearchResultData> searchResults = result.getSearchResults();
                getView().setLabel(searchResults.get(0).getTitle() + " " + searchResults.get(1).getTitle());
            }

            @Override
            public void onFailure(Throwable caught) {
                Logger logger = java.util.logging.Logger.getLogger("Error Log variable");
                logger.log(java.util.logging.Level.SEVERE, "La recherche a échouée, veuillez réessayer plus tard");
            }
        });
    }


    protected void onReset() {
        super.onReset();
    }


}
