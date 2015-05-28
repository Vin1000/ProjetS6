
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
        if (cbTypeALL.getValue()) {
            CheckedAllType(true);
        }
        else{
            CheckedAllType(false);
        }
    }

    @UiHandler("cbTypePDF")
    void onCheckBoxClickedPDF(ClickEvent event)
    {
        if(IsAllTypeChecked())
        {
            cbTypeALL.setValue(true);
            fileTypes.add(FileType.PDF);
        }

        else
        {
            cbTypeALL.setValue(false);
            fileTypes.remove(FileType.PDF);
        }
    }

    @UiHandler("cbTypeDOCX")
    void onCheckBoxClickedDOCX(ClickEvent event)
    {
        if(IsAllTypeChecked())
        {
            cbTypeALL.setValue(true);
            fileTypes.add(FileType.DOCX);
        }

        else
        {
            cbTypeALL.setValue(false);
            fileTypes.remove(FileType.DOCX);
        }
    }

    @UiHandler("cbTypeTXT")
    void onCheckBoxClickedTXT(ClickEvent event)
    {
        if(IsAllTypeChecked())
        {
            cbTypeALL.setValue(true);
            fileTypes.add(FileType.TXT);
        }

        else
        {
            cbTypeALL.setValue(false);
            fileTypes.remove(FileType.TXT);
        }
    }

    @UiHandler("cbTypeXLS")
    void onCheckBoxClickedXLS(ClickEvent event)
    {
        if(IsAllTypeChecked())
        {
            cbTypeALL.setValue(true);
            fileTypes.add(FileType.XLS);
        }

        else
        {
            cbTypeALL.setValue(false);
            fileTypes.remove(FileType.XLS);
        }
    }

    public Boolean IsAllTypeChecked()
    {
        if(cbTypePDF.getValue() && cbTypeDOCX.getValue() && cbTypeTXT.getValue() && cbTypeXLS.getValue())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void CheckedAllType(Boolean check)
    {
        cbTypePDF.setValue(check);
        cbTypeDOCX.setValue(check);
        cbTypeTXT.setValue(check);
        cbTypeXLS.setValue(check);

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
