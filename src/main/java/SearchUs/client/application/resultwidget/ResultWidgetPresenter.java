
package SearchUs.client.application.resultwidget;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import java.util.ArrayList;

public class ResultWidgetPresenter extends PresenterWidget<ResultWidgetPresenter.MyView> {
    public interface MyView extends View
    {
        void SetContent(String title, String imageUrl, String downloadUrl, String author, String date, String description, ArrayList<String> keywords);
    }

    @Inject
    ResultWidgetPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);

    }

    public void SetContent(String title, String downloadUrl, String author, String date, String description, ArrayList<String> keywords)
    {
        String ext = title.substring(title.lastIndexOf('.') + 1);
        getView().SetContent(title, FileExtension.getInstance().getIconUrl(ext), downloadUrl, author, date, description, keywords);
    }

}
