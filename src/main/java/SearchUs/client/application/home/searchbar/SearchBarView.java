package SearchUs.client.application.home.searchbar;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;


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
        topImage.getElement().getStyle().setProperty("paddingTop", "200px");
        topImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getStyle().setProperty("display", "none");
        sideImage.getElement().getStyle().setProperty("cursor", "pointer");
    }

    @UiHandler("sideImage")
    void onLogoClick(ClickEvent event)
    {
        topImage.getElement().getStyle().setProperty("paddingBottom", "10px");
        topImage.getElement().getStyle().setProperty("paddingTop", "200px");
        sideImage.getElement().getParentElement().setAttribute("align", "center");
        topImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getStyle().setProperty("display", "none");
        textBox.setText("");
        getUiHandlers().LogoClick();
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

    public void ClickEvent()
    {
        topImage.getElement().getStyle().setProperty("display", "none");
        sideImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getParentElement().setAttribute("align", "left");
        sideImage.getElement().getParentElement().getStyle().setProperty("paddingLeft", "6px");

        getUiHandlers().sendSearch(textBox.getText());
    }

    public void ClickEvent(String searchString)
    {
        topImage.getElement().getStyle().setProperty("display", "none");
        sideImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getParentElement().setAttribute("align", "left");
        sideImage.getElement().getParentElement().getStyle().setProperty("paddingLeft", "6px");

        textBox.setText(searchString);
        getUiHandlers().sendSearch(textBox.getText());
    }

    public void SetSearchText(String searchText)
    {
        if(searchText != null) {
            topImage.getElement().getStyle().setProperty("display", "none");
            sideImage.getElement().getStyle().setProperty("display", "initial");
            sideImage.getElement().getParentElement().setAttribute("align", "left");
            sideImage.getElement().getParentElement().getStyle().setProperty("paddingLeft", "6px");
        }

        textBox.setText(searchText);
    }
}
