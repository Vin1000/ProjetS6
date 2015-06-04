package SearchUs.client.application.events;

import SearchUs.shared.data.SearchDetails;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Created by Louis on 28/05/2015.
 */
public class OptionEvent extends GwtEvent<OptionEvent.OptionEventHandler> {
    public interface HasGlobalHandlers extends HasHandlers {
        void onOptionEvent(OptionEvent event);
    }

    public interface OptionEventHandler extends EventHandler {
        public void onOptionEvent(OptionEvent event);
    }
    public static void fire(HasHandlers source, SearchDetails searchDetails) {
        source.fireEvent(new OptionEvent(searchDetails));
    }

    public static void fire(HasHandlers source, OptionEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    private static final Type<OptionEventHandler> TYPE = new Type<OptionEventHandler>();

    private SearchDetails searchDetails;

    public OptionEvent(SearchDetails searchDetails) {
        this.searchDetails = searchDetails;
    }

    public static Type<OptionEventHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<OptionEventHandler> getAssociatedType() {
        return TYPE;
    }

    public SearchDetails getSearchDetails() {
        return this.searchDetails;
    }

    @Override
    protected void dispatch(OptionEventHandler handler) {
        handler.onOptionEvent(this);
    }
}
