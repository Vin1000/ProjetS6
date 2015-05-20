
package SearchUs.client.application.resultwidget;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import java.util.ArrayList;

public class ResultWidgetView extends ViewImpl implements ResultWidgetPresenter.MyView {
    public interface Binder extends UiBinder<HTMLPanel, ResultWidgetView> {
    }

    @UiField
    HTMLPanel panel;

    @UiField
    Label lblTitle;

    @UiField
    Label lblAuthor;

    @UiField
    Label lblDate;

    @UiField
    Label lblDescription;

    @UiField
    HTML htmlOtherKeywords;

    @UiField
    Image imgIcon;

    @Inject
    ResultWidgetView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    public void SetContent(String title, String imageUrl, String downloadUrl, String author, String date, String description, ArrayList<String> keywords)
    {
        lblTitle.setText(title);
        imgIcon.setUrl(imageUrl);
        lblAuthor.setText(author);
        lblDate.setText(date);
        lblDescription.setText(description);

        //TODO: ajouter le downloadUrl sur l'image et le titre

        boolean first = true;
        String keys = "";
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
