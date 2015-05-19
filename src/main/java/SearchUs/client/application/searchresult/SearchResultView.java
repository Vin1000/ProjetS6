package SearchUs.client.application.searchresult;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;


public class SearchResultView extends ViewWithUiHandlers<SearchResultUiHandlers> implements SearchResultPresenter.MyView {
    @Override
    public void setLabel(String text) {
        this.label.setText(text);
    }

    interface Binder extends UiBinder<Widget, SearchResultView> {
    }

    @UiField
    Label label;

    @Inject
    SearchResultView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
    }
}
