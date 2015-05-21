package SearchUs.client.application.home.searchresult;

import SearchUs.client.application.home.resultwidget.ResultWidgetPresenter;
import SearchUs.shared.data.SearchResultData;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Provider;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;
import java.util.ArrayList;


public class SearchResultView extends ViewWithUiHandlers<SearchResultUiHandlers> implements SearchResultPresenter.MyView {

    public void addResult(SearchResultData result)
    {
        ResultWidgetPresenter rw = resultWidgetProvider.get();
        rw.SetContent(result);
        this.resultPanel.add(rw);
    }

    public void clearResults()
    {
        this.resultPanel.clear();
    }

    interface Binder extends UiBinder<Widget, SearchResultView> {
    }

    @UiField
    HTMLPanel resultPanel;

    @Inject
    SearchResultView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Inject
    Provider<ResultWidgetPresenter> resultWidgetProvider;

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
    }
}
