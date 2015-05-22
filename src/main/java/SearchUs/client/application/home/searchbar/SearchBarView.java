package SearchUs.client.application.home.searchbar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class SearchBarView extends ViewWithUiHandlers<SearchBarUiHandlers> implements SearchBarPresenter.MyView {
    interface Binder extends UiBinder<Widget, SearchBarView> {
    }

    @UiField
    TextBox textBox;

    @UiField
    Button sendSearchButton;

    @Inject
    SearchBarView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        textBox.ensureDebugId("searchTextBox");
        sendSearchButton.ensureDebugId("sendSearchButton");
    }

    @UiHandler("sendSearchButton")
    void onSendSearch(ClickEvent event) {
        getUiHandlers().sendSearch(textBox.getText());
    }

    @UiHandler("textBox")
    void onKeyPress(KeyPressEvent event){
        if(KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()){
            getUiHandlers().sendSearch(textBox.getText());
        }
    }
}
