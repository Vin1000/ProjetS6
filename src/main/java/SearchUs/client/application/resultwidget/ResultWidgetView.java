
package SearchUs.client.application.resultwidget;

import SearchUs.shared.data.SearchResultData;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import java.util.List;

public class ResultWidgetView extends ViewImpl implements ResultWidgetPresenter.MyView {
    public interface Binder extends UiBinder<HTMLPanel, ResultWidgetView> {
    }

    @UiField
    HTMLPanel panel;

    @UiField
    HTML htmlFile;

    @UiField
    Label lblAuthor;

    @UiField
    Label lblDate;

    @UiField
    Label lblDescription;

    @UiField
    HTML htmlOtherKeywords;

    @Inject
    ResultWidgetView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    public void SetContent(SearchResultData result, String imageUrl)
    {
        lblAuthor.setText(result.getAuthor());
        lblDate.setText(result.getDate());
        lblDescription.setText(result.getDescription());

        String a = "<a href=\"" + result.getDownloadUrl() + "\" target=\"_blank\">";
        String img = "<img class=\"gwt-Image\" src=\"" + imageUrl + "\">";
        String filename = "<span class=\"gwt-InlineHTML\">" + result.getFilename() + "</span>";
        String a2 = "</a>";
        htmlFile.setHTML(a + img + filename + a2);

        boolean first = true;
        String keys = "";
        List<String> keywords = result.getKeywords();
        for(String key : keywords)
        {
            if(first)
            {
                first = false;
                keys = AddLink(key);
            }
            else
            {
                keys += ", " + AddLink(key);
            }
        }
        htmlOtherKeywords.setHTML(keys);
    }

    public String AddLink(String keyword)
    {
        return "<a href=\"#\">" + keyword + "</a>";
    }
}
