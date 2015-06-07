



package SearchUs.client.application.home.searchbar;

import SearchUs.client.application.ApplicationPresenter;
import SearchUs.client.application.events.OptionEvent;
import SearchUs.client.application.events.SearchEvent;
import SearchUs.client.application.home.searchbar.searchoption.SearchOptionPresenter;
import SearchUs.client.application.home.searchresult.SearchResultPresenter;
import SearchUs.shared.data.FieldType;
import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Proxy;

import java.util.ArrayList;
import java.util.Date;


public class SearchBarPresenter extends Presenter<SearchBarPresenter.MyView, SearchBarPresenter.MyProxy> implements SearchBarUiHandlers, OptionEvent.OptionEventHandler {
    interface MyView extends View, HasUiHandlers<SearchBarUiHandlers> {
        void ClickEvent(String cookie);
    }

    SearchDetails searchDetails;

    @ProxyStandard
    public interface MyProxy extends Proxy<SearchBarPresenter> {
    }

    @Inject protected SearchOptionPresenter searchOptionPresenter;

    @Inject
    public SearchBarPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);

        getView().setUiHandlers(this);
        ArrayList defaultFileType= new ArrayList<FileType>();
        defaultFileType.add(FileType.ALL);
        ArrayList defaultFieldType = new ArrayList<FieldType>();
        defaultFieldType.add(FieldType.Content);
        searchDetails = new SearchDetails();
        searchDetails.setSearchFor(defaultFileType);
        searchDetails.setSearchInFields(defaultFieldType);
    }

    @Override
    protected void onBind() {
        super.onBind();
        addRegisteredHandler(OptionEvent.getType(), this);
        Window.addWindowClosingHandler(new Window.ClosingHandler()
        {
            @Override
            public void onWindowClosing(Window.ClosingEvent event)
            {
                if(!searchDetails.getSearchString().isEmpty())
                {
                    Cookies.setCookie("refreshcookie", searchDetails.getSearchString(), new Date(System.currentTimeMillis() + 5000), null, null, false);
                }
            }
        });
    }

    @Override
    protected void onReveal(){
        super.onReveal();
        String c = Cookies.getCookie("refreshcookie");
        if(c != null)
        {
            Cookies.removeCookie("refreshcookie");
            getView().ClickEvent(c);
        }
    }

    @Override
    public void sendSearch(String searchText) {
        searchDetails.setSearchString(searchText);
        SearchEvent.fire(this, searchDetails);
    }

    @Override
    public void onOptionEvent(OptionEvent event) {
        searchDetails = event.getSearchDetails();
    }

    @Override
    public void ShowAdvancedOptions()
    {
        addToPopupSlot(searchOptionPresenter);
    }

    @Inject
    SearchResultPresenter searchResultPresenter;

    @Override
    public void LogoClick()
    {
        searchDetails.setSearchString("");
        Cookies.removeCookie("refreshcookie");
        searchResultPresenter.clearResults();
    }
}
