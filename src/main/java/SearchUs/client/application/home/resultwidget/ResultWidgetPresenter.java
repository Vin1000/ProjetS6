
package SearchUs.client.application.home.resultwidget;

import SearchUs.shared.data.SearchResultBoard;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.data.SearchResultFile;
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
        if(result instanceof SearchResultFile) {
            SearchResultFile searchResultFile = (SearchResultFile) result;
            String ext = searchResultFile.getFilename().substring(searchResultFile.getFilename().lastIndexOf('.') + 1);
            getView().SetContent(result, FileExtension.getInstance().getIconUrl(ext));
        }

        else if(result instanceof SearchResultBoard)
        {

        }
    }

}
