package SearchUs.client.application.events;

import com.gargoylesoftware.htmlunit.Page;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Window;

/**
 * Created by Vincent on 02/07/2015.
 */
public class ChangePageEvent extends GwtEvent<ChangePageEvent.ChangePageEventHandler> {
    public interface HasGlobalHandlers extends HasHandlers {
        void onChangePageEvent(ChangePageEvent event);
    }

    public interface ChangePageEventHandler extends EventHandler {
        public void onChangePageEvent(ChangePageEvent event);
    }

    private int PageNumber;

    public int getPageNumber()
    {
        return PageNumber;
    }

    public static void fire(HasHandlers source) {
        source.fireEvent(new ChangePageEvent());
    }

    public static void fire(HasHandlers source, ChangePageEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    private static final Type<ChangePageEventHandler> TYPE = new Type<ChangePageEventHandler>();

    public ChangePageEvent() { }

    public ChangePageEvent(int number) {
        PageNumber = number;
    }

    public static Type<ChangePageEventHandler> getType() { return TYPE; }

    @Override
    public Type<ChangePageEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ChangePageEventHandler handler) {
        handler.onChangePageEvent(this);
    }
}
