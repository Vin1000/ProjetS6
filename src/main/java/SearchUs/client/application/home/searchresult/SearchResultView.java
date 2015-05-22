package SearchUs.client.application.home.searchresult;

import SearchUs.client.application.home.resultwidget.ResultWidgetPresenter;
import SearchUs.shared.data.SearchResultData;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.util.tools.shared.StringUtils;
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

    public void addNoResultMessage()
    {
        HTML lbl = new HTML("<hr><div style=\"padding-left:6px;\">Aucun résultat pour la recherche.</div>");
        this.resultPanel.add(lbl);
    }

    public void addTook_totalHits(int took, int totalHits) {
        if (totalHits <= 1)
        {
            timeElapsed_totalHits_Label.setText(totalHits + " résultat en " + took + " ms.");
        }
        else
        {
            timeElapsed_totalHits_Label.setText(totalHits + " résultats en " + took + " ms.");
        }
    }

    public void clearResults()
    {
        this.resultPanel.clear();
    }

    interface Binder extends UiBinder<Widget, SearchResultView> {
    }

    @UiField
    HTMLPanel resultPanel;

    @UiField
    Label timeElapsed_totalHits_Label;

    @Inject
    SearchResultView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        timeElapsed_totalHits_Label.getElement().getStyle().setProperty("paddingLeft", "7px");
    }

    @Inject
    Provider<ResultWidgetPresenter> resultWidgetProvider;

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        super.setInSlot(slot, content);
    }
}
