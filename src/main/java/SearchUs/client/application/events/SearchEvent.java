package SearchUs.client.application.events;

import SearchUs.shared.data.SearchDetails;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Created by Louis on 19/05/2015.
 */
public class SearchEvent extends GwtEvent<SearchEvent.GlobalHandler> {
    public interface HasGlobalHandlers extends HasHandlers {
        void onGlobalEvent(SearchEvent event);
    }

    public interface GlobalHandler extends EventHandler {
        public void onGlobalEvent(SearchEvent event);
    }
    public static void fire(HasHandlers source, SearchDetails searchDetails) {
        source.fireEvent(new SearchEvent(searchDetails));
    }

    public static void fire(HasHandlers source, SearchEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    private static final Type<GlobalHandler> TYPE = new Type<GlobalHandler>();

    private SearchDetails searchDetails;

    public SearchEvent(SearchDetails searchDetails) {
        this.searchDetails = searchDetails;
    }

    public static Type<GlobalHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<GlobalHandler> getAssociatedType() {
        return TYPE;
    }

    public SearchDetails getSearchDetails() {
        return this.searchDetails;
    }

    @Override
    protected void dispatch(GlobalHandler handler) {
        handler.onGlobalEvent(this);
    }
}
