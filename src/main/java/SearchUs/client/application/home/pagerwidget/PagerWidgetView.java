
package SearchUs.client.application.home.pagerwidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import java.util.ArrayList;

public class PagerWidgetView extends ViewWithUiHandlers<PagerWidgetUiHandlers> implements PagerWidgetPresenter.MyView {
    public interface Binder extends UiBinder<HTMLPanel, PagerWidgetView> {
    }

    @UiField
    HTMLPanel panel;

    @Inject
    PagerWidgetView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    ArrayList<Button> buttons = new ArrayList<Button>();
    int currentButton = 1;

    public void setPageNumber(int numberOfPages)
    {
        panel.clear();
        Button btPrevious = new Button();
        btPrevious.setText("<");
        btPrevious.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                getUiHandlers().onChangePageClick(0);
                if(currentButton > 1)
                {
                    currentButton--;
                }
                setBold();
            }
        });
        Button btNext = new Button();
        btNext.setText(">");
        btNext.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                getUiHandlers().onChangePageClick(-1);
                if(currentButton < numberOfPages)
                {
                    currentButton++;
                }
                setBold();
            }
        });

        panel.add(btPrevious);

        for(int i = 1; i <= numberOfPages; i++)
        {
            Button bt = new Button();
            bt.setText(i + "");
            bt.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent clickEvent) {
                    int pageNumber = Integer.parseInt(((Button) clickEvent.getSource()).getText());
                    getUiHandlers().onChangePageClick(pageNumber);
                    currentButton = pageNumber;
                    setBold();
                }
            });
            buttons.add(bt);
            panel.add(bt);
        }
        setBold();
        panel.add(btNext);
    }

    public void setBold()
    {
        for(Button bt : buttons)
        {
            bt.getElement().getStyle().setProperty("fontWeight", "normal");
            bt.getElement().getStyle().setProperty("textDecoration", "none");
        }
        buttons.get(currentButton-1).getElement().getStyle().setProperty("fontWeight", "bold");
        buttons.get(currentButton-1).getElement().getStyle().setProperty("textDecoration", "underline");
    }
}
