package SearchUs.server.guice;

import SearchUs.server.dispatch.SearchActionHandler;
import SearchUs.shared.dispatch.search.SearchAction;
import com.gwtplatform.dispatch.rpc.server.guice.HandlerModule;

public class ServerModule extends HandlerModule {
    @Override
    protected void configureHandlers() {
        bindHandler(SearchAction.class, SearchActionHandler.class);
    }
}
