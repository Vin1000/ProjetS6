
package SearchUs.client.application.home.searchbar.searchoption;

import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;

import java.util.ArrayList;

public class SearchOptionView extends PopupViewWithUiHandlers<SearchOptionUiHandlers> implements SearchOptionPresenter.MyView {
    public interface Binder extends UiBinder<PopupPanel, SearchOptionView> {
    }

    ArrayList<FileType> fileTypes;

    @UiField
    CheckBox cbTypeALL;

    @UiField
    CheckBox cbTypePDF;

    @UiField
    CheckBox cbTypeDOCX;

    @UiField
    CheckBox cbTypeTXT;

    @UiField
    CheckBox cbTypeXLS;

    @UiField
    Button btOK;

    @Inject
    SearchOptionView(Binder uiBinder, EventBus eventBus) {
        super(eventBus);

        initWidget(uiBinder.createAndBindUi(this));
        fileTypes = new ArrayList<FileType>();
        CheckedAllType(true);
    }

    @UiHandler("btOK")
    void onOkClicked(ClickEvent event)
    {
        SearchDetails searchDetails = new SearchDetails();
        searchDetails.setSearchFor(fileTypes);
        getUiHandlers().onOkClicked(searchDetails);
        hide();
    }

    @UiHandler("cbTypeALL")
    void onCheckBoxClickedALL(ClickEvent event)
    {
        CheckedAllType(cbTypeALL.getValue());
    }

    @UiHandler("cbTypePDF")
    void onCheckBoxClickedPDF(ClickEvent event)
    {
        monitorTotalCheckbox();
    }

    @UiHandler("cbTypeDOCX")
    void onCheckBoxClickedDOCX(ClickEvent event)
    {
        monitorTotalCheckbox();
    }

    @UiHandler("cbTypeTXT")
    void onCheckBoxClickedTXT(ClickEvent event)
    {
        monitorTotalCheckbox();
    }

    @UiHandler("cbTypeXLS")
    void onCheckBoxClickedXLS(ClickEvent event)
    {
        monitorTotalCheckbox();
    }

    void monitorTotalCheckbox()
    {
        if(cbTypePDF.getValue() && cbTypeDOCX.getValue() && cbTypeTXT.getValue() && cbTypeXLS.getValue())
        {
            cbTypeALL.setValue(true);
            fileTypes.clear();
            fileTypes.add(FileType.ALL);
        }
        else
        {
            cbTypeALL.setValue(false);
            fileTypes.clear();

            if(cbTypeXLS.getValue())
            {
                fileTypes.add(FileType.XLS);
            }
            if(cbTypeTXT.getValue())
            {
                fileTypes.add(FileType.TXT);
            }
            if(cbTypeDOCX.getValue())
            {
                fileTypes.add(FileType.DOCX);
            }
            if(cbTypePDF.getValue())
            {
                fileTypes.add(FileType.PDF);
            }

        }

    }

    void CheckedAllType(Boolean check)
    {
        cbTypePDF.setValue(check);
        cbTypeDOCX.setValue(check);
        cbTypeTXT.setValue(check);
        cbTypeXLS.setValue(check);
        cbTypeALL.setValue(check);

        if(check)
        {
            fileTypes.clear();
            fileTypes.add(FileType.ALL);
        }
        else
        {
            fileTypes.clear();
        }
    }
}
