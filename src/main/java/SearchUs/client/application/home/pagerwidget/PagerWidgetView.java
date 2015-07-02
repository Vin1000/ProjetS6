
package SearchUs.client.application.home.pagerwidget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PagerWidgetView extends ViewWithUiHandlers<PagerWidgetUiHandlers> implements PagerWidgetPresenter.MyView {
    public interface Binder extends UiBinder<HTMLPanel, PagerWidgetView> {
    }

    @UiField
    HTMLPanel panel;

    @Inject
    PagerWidgetView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    public void setPageNumber(int pageNumber)
    {
        HTML html = new HTML();
        html.setHTML("< " + pageNumber + " >");
        panel.add(html);
    }
}
