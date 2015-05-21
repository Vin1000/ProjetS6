package SearchUs.client.application.home;

import javax.inject.Inject;

import SearchUs.client.application.ApplicationPresenter;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewImpl;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HomePageView extends ViewWithUiHandlers<HomeUiHandler> implements HomePagePresenter.MyView {
    public interface Binder extends UiBinder<Widget, HomePageView> {
    }

    @UiField
    HTMLPanel dock;
    @UiField
    FlowPanel searchBar;
    @UiField
    FlowPanel searchResults;

    @Inject
    HomePageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));

        /*dock = new DockLayoutPanel(Style.Unit.PX);
        dock.addNorth(new HTML("north"), 2);
        dock.add(new HTML("center"));

        RootLayoutPanel rp = RootLayoutPanel.get();
        rp.add(dock);*/
    }

    /*public void onModuleLoad() {
        // Attach five widgets to a DockLayoutPanel, one in each direction. Lay
        // them out in 'em' units.
        DockLayoutPanel p = new DockLayoutPanel(Style.Unit.EM);
        p.addNorth(new HTML("north"), 2);
        p.add(new HTML("center"));

        // Attach the LayoutPanel to the RootLayoutPanel. The latter will listen for
        // resize events on the window to ensure that its children are informed of
        // possible size changes.
        RootLayoutPanel rp = RootLayoutPanel.get();
        rp.add(p);
    }*/

    @Override
    public void addToSlot(Object slot, IsWidget content) {
        super.addToSlot(slot, content);

        if (slot == HomePagePresenter.SLOT_SEARCHBAR) {
            searchBar.add(content);
        } else if (slot == HomePagePresenter.SLOT_SEARCHRESULTS) {
            searchResults.add(content);
        }
    }

    @Override
    public void removeFromSlot(Object slot, IsWidget content) {
        super.removeFromSlot(slot, content);

        if (slot == HomePagePresenter.SLOT_SEARCHBAR) {
            searchBar.remove(content);
        } else if (slot == HomePagePresenter.SLOT_SEARCHRESULTS) {
            searchResults.remove(content);
        }
    }
}
