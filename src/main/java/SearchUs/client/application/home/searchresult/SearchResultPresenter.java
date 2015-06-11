package SearchUs.client.application.home.searchresult;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.ClearSearchResultsEvent;
import SearchUs.client.application.events.OptionEvent;
import SearchUs.client.application.events.SearchEvent;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class SearchResultPresenter extends Presenter<SearchResultPresenter.MyView, SearchResultPresenter.MyProxy> implements SearchResultUiHandlers, SearchEvent.GlobalHandler, ClearSearchResultsEvent.ClearSearchResultsEventHandler {

    interface MyView extends View, HasUiHandlers<SearchResultUiHandlers>
    {
        void addResult(SearchResultData result);
        void addNoResultMessage();
        void addTimeElapsed_totalHits(int timeElapsed, int totalHits);
        void clearResults();
        void clearTimeElapsed();
    }

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchResultPresenter> {
    }

    private DispatchAsync dispatcher;
    private Date clearTime;

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
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(SearchEvent.getType(), this);
        addRegisteredHandler(ClearSearchResultsEvent.getType(), this);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public void onGlobalEvent(SearchEvent event) {
        SearchAction searchAction = new SearchAction(event.getSearchDetails());
        dispatcher.execute(searchAction, new AsyncCallback<SearchResult>() {
            @Override //TODO: Change actions done here
            public void onSuccess(SearchResult result)
            {
                if(clearTime != null && (new Date().getTime() - clearTime.getTime() < 100))
                {
                    return;
                }
                getView().clearResults();
                ArrayList<SearchResultData> searchResults = result.getSearchResults();

                getView().addTimeElapsed_totalHits(result.getTimeElapsed(), result.getTotalHits());

                if(!searchResults.isEmpty())
                {
                    for (SearchResultData res : searchResults)
                    {
                        getView().addResult(res);
                    }
                }
                else
                {
                    getView().addNoResultMessage();
                }
            }

            @Override
            public void onFailure(Throwable caught) {
                Logger logger = java.util.logging.Logger.getLogger("Error Log variable");
                logger.log(java.util.logging.Level.SEVERE, "La recherche a échouée, veuillez réessayer plus tard");
            }
        });
    }

    @Override
    public void onClearSearchResultsEvent(ClearSearchResultsEvent event)
    {
        clearResults();
    }

    public void clearResults()
    {
        clearTime = new Date();
        getView().clearResults();
        getView().clearTimeElapsed();
    }
}
