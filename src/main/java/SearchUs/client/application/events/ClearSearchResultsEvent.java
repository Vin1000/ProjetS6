package SearchUs.client.application.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Created by Louis on 11/06/2015.
 */
public class ClearSearchResultsEvent extends GwtEvent<ClearSearchResultsEvent.ClearSearchResultsEventHandler> {
    public interface HasGlobalHandlers extends HasHandlers {
        void onClearSearchResultsEvent(ClearSearchResultsEvent event);
    }

    public interface ClearSearchResultsEventHandler extends EventHandler {
        public void onClearSearchResultsEvent(ClearSearchResultsEvent event);
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ClearSearchResultsEvent());
    }

    public static void fire(HasHandlers source, ClearSearchResultsEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    private static final GwtEvent.Type<ClearSearchResultsEventHandler> TYPE = new GwtEvent.Type<ClearSearchResultsEventHandler>();

    public ClearSearchResultsEvent() {
    }

    public static GwtEvent.Type<ClearSearchResultsEventHandler> getType() {
        return TYPE;
    }

    @Override
    public GwtEvent.Type<ClearSearchResultsEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ClearSearchResultsEventHandler handler) {
        handler.onClearSearchResultsEvent(this);
    }
}
