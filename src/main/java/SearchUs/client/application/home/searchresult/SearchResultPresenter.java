package SearchUs.client.application.home.searchresult;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.ChangePageEvent;
import SearchUs.client.application.events.ClearSearchResultsEvent;
import SearchUs.shared.data.SearchDetails;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.rpc.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;

import java.util.ArrayList;
import java.util.Date;

public class SearchResultPresenter extends Presenter<SearchResultPresenter.MyView, SearchResultPresenter.MyProxy> implements SearchResultUiHandlers, ClearSearchResultsEvent.ClearSearchResultsEventHandler, ChangePageEvent.ChangePageEventHandler {

    interface MyView extends View, HasUiHandlers<SearchResultUiHandlers>
    {
        void addResult(SearchResultData result);
        void addNoResultMessage();
        void addTimeElapsed_totalHits(int processingTime, int timeElapsed, int totalHits);
        void clearResults();
        void clearTimeElapsed();
        void clearPager();
        void addPager(int numberOfPages, int currentPage);
    }

    ArrayList<SearchResultData> searchResults = new ArrayList<>();
    int currentPage = 1;
    int numberOfPages;
    int resultsPerPage = 1;

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchResultPresenter> {
    }

    private DispatchAsync dispatcher;
    private Date clearTime;
    PlaceManager placeManager;

    @Inject
    public SearchResultPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            DispatchAsync dispatcher,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(ClearSearchResultsEvent.getType(), this);
        addRegisteredHandler(ChangePageEvent.getType(), this);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public void onClearSearchResultsEvent(ClearSearchResultsEvent event)
    {
        clearResults();
    }

    public void clearResults()
    {
        clearTime = new Date();
        searchResults.clear();
        getView().clearResults();
        getView().clearTimeElapsed();
        getView().clearPager();
    }

    public void SendSearch(SearchDetails searchDetails)
    {
        if(searchDetails.getSearchWithGoogle())
        {
            Window.open("https://www.google.ca/search?q=" + searchDetails.getSearchString(), "google", "");
        }

        SearchAction searchAction = new SearchAction(searchDetails);
        dispatcher.execute(searchAction, new AsyncCallback<SearchResult>() {
            @Override //TODO: Change actions done here
            public void onSuccess(SearchResult result)
            {
                if(clearTime != null && (new Date().getTime() - clearTime.getTime() < 100))
                {
                    return;
                }
                getView().clearResults();

                searchResults = result.getSearchResults();
                currentPage = 1; //todo: get from url if refresh
                numberOfPages = (int)Math.ceil((double)searchResults.size()/(double)resultsPerPage);

                if(currentPage < 1 || currentPage > numberOfPages) //to prevent bad input from url
                {
                    currentPage = 1;
                }
                if(!searchResults.isEmpty())
                {
                    getView().addPager(numberOfPages, currentPage);
                }
                getView().addTimeElapsed_totalHits(result.getProcessingTime(), result.getTimeElapsed(), result.getTotalHits());

                displayResults();
            }

            @Override
            public void onFailure(Throwable caught) {
                //fail
            }
        });
    }

    public void UpdateFromUrl(SearchDetails searchDetails)
    {
        SendSearch(searchDetails);
    }

    public void displayResults()
    {
        getView().clearResults();
        if(!searchResults.isEmpty())
        {
            for(int i = (currentPage-1)*resultsPerPage; i < currentPage*resultsPerPage; i++) //for paging
            {
                if(i < searchResults.size()) //safe check
                {
                    getView().addResult(searchResults.get(i));
                }
            }
        }
        else
        {
            getView().addNoResultMessage();
        }
    }

    @Override
    public void onChangePageEvent(ChangePageEvent event)
    {
        int pageNumber = event.getPageNumber();
        int previousPage = currentPage;
        if(pageNumber == 0) //precedant
        {
            if(currentPage > 1)
            {
                currentPage--;
            }
        }
        else if(pageNumber == -1) //suivant
        {
            if(currentPage < numberOfPages)
            {
                currentPage++;
            }
        }
        else //chiffre
        {
            currentPage = pageNumber;
        }

        if(previousPage != currentPage)
        {
            displayResults();
        }
    }
}
