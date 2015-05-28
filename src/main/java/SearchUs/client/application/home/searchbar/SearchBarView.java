package SearchUs.client.application.home.searchbar;

import com.google.gwt.event.shared.GwtEvent.Type;
import SearchUs.client.application.home.searchbar.searchoption.SearchOptionPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

import javax.inject.Inject;
import java.util.ArrayList;


public class SearchBarView extends ViewWithUiHandlers<SearchBarUiHandlers> implements SearchBarPresenter.MyView {
    interface Binder extends UiBinder<Widget, SearchBarView> {
    }

    @UiField
    TextBox textBox;

    @UiField
    Button sendSearchButton;

    @UiField
    Button AdvancedButton;

    @UiField
    Image sideImage;

    @UiField
    Image topImage;

    @Inject
    SearchBarView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        textBox.ensureDebugId("searchTextBox");
        sendSearchButton.ensureDebugId("sendSearchButton");
        topImage.getElement().getStyle().setProperty("paddingBottom", "10px");
        topImage.getElement().getStyle().setProperty("paddingTop", "100px");
        topImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getStyle().setProperty("display", "none");
    }

    @UiHandler("sendSearchButton")
    void onSendSearch(ClickEvent event) {
        ClickEvent();
    }

    @UiHandler("textBox")
    void onKeyPress(KeyPressEvent event){
        if(KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()){
            ClickEvent();
        }
    }

    @UiHandler("AdvancedButton")
    void onAdvancedClicked(ClickEvent event)
    {
        getUiHandlers().ShowAdvancedOptions();
    }

    void ClickEvent()
    {
        topImage.getElement().getStyle().setProperty("display", "none");
        sideImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getParentElement().setAttribute("align", "left");
        sideImage.getElement().getParentElement().getStyle().setProperty("paddingLeft", "6px");

        getUiHandlers().sendSearch(textBox.getText());
    }
}
