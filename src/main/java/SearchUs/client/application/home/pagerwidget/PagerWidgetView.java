
package SearchUs.client.application.home.pagerwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
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
        panel.clear();
        Button btPrevious = new Button();
        btPrevious.setText("<");
        btPrevious.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //Window.alert("Previous!");
                getUiHandlers().onChangePageClick(0);
            }
        });
        Button btNext = new Button();
        btNext.setText(">");
        btNext.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //Window.alert("Next!");
                getUiHandlers().onChangePageClick(-1);
            }
        });

        panel.add(btPrevious);

        for(int i = 1; i <= pageNumber; i++)
        {
            Button bt = new Button();
            bt.setText(i + "");
            bt.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    int pageNumber = Integer.parseInt(((Button) clickEvent.getSource()).getText());
                    getUiHandlers().onChangePageClick(pageNumber);
                }
            });
            panel.add(bt);
        }

        panel.add(btNext);
    }
}
