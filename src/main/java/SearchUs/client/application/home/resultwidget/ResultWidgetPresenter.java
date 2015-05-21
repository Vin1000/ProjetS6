
package SearchUs.client.application.home.resultwidget;

import SearchUs.shared.data.SearchResultData;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import java.util.List;

public class ResultWidgetPresenter extends PresenterWidget<ResultWidgetPresenter.MyView> {
    public interface MyView extends View
    {
        void SetContent(SearchResultData result, String imageUrl);
    }

    @Inject
    ResultWidgetPresenter(EventBus eventBus, MyView view) {
        super(eventBus, view);

    }

    public void SetContent(SearchResultData result)
    {
        String ext = result.getFilename().substring(result.getFilename().lastIndexOf('.') + 1);
        getView().SetContent(result, FileExtension.getInstance().getIconUrl(ext));
    }

}
