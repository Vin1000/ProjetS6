package SearchUs.client.application.home.searchbar;

import com.google.gwt.event.dom.client.ClickEvent;
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
    }

    @UiHandler("sendSearchButton")
    void onSendSearch(ClickEvent event) {
        getUiHandlers().sendSearch(textBox.getText());
    }
}
