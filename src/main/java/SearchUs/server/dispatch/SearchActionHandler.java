package SearchUs.server.dispatch;

import SearchUs.server.engine.ElasticManager;
import SearchUs.server.logic.SearchManager;
import SearchUs.shared.dispatch.search.SearchAction;
import SearchUs.shared.dispatch.search.SearchResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.rpc.server.ExecutionContext;
import com.gwtplatform.dispatch.rpc.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchActionHandler implements ActionHandler<SearchAction, SearchResult> {

    @Inject
    SearchManager searchManager;

    @Override
    public SearchResult execute(SearchAction action, ExecutionContext context)
            throws ActionException {
        SearchResult  result = new SearchResult();

        result.setSearchResults(searchManager.getSearchResults(action.getSearchDetails().getSearchString()));
        return result;
    }

    @Override
    public void undo(SearchAction action, SearchResult result,
                     ExecutionContext context) throws ActionException {
        // TODO Auto-generated method stub
    }

    @Override
    public Class<SearchAction> getActionType() {
        // TODO Auto-generated method stub
        return null;
    }
}
