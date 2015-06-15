
package SearchUs.client.application.home.searchbar.searchoption;

import SearchUs.shared.data.FieldType;
import SearchUs.shared.data.FileType;
import SearchUs.shared.data.SearchDetails;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PopupViewWithUiHandlers;
import java.util.ArrayList;
import java.util.Date;

public class SearchOptionView extends PopupViewWithUiHandlers<SearchOptionUiHandlers> implements SearchOptionPresenter.MyView {
    public interface Binder extends UiBinder<PopupPanel, SearchOptionView> {
    }

    ArrayList<FileType> fileTypes;
    ArrayList<FieldType> fieldTypes;

    @UiField
    PopupPanel popupPanel;

    @UiField
    CheckBox cbFileTypeALL;

    @UiField
    CheckBox cbFileTypePDF;

    @UiField
    CheckBox cbFileTypeDOCX;

    @UiField
    CheckBox cbFileTypeTXT;

    @UiField
    CheckBox cbFileTypeXLS;

    @UiField
    CheckBox cbFieldTypeContent;

    @UiField
    CheckBox cbFieldTypeAuteur;

    @UiField
    DateBox dateBox;

    @UiField
    Button btClearDate;

    @UiField
    Button btOK;

    @UiField
    CheckBox cbSearchWithGoogle;

    @UiField
    Image imgGoogle;

    @Inject
    SearchOptionView(Binder uiBinder, EventBus eventBus) {
        super(eventBus);

        initWidget(uiBinder.createAndBindUi(this));

        fileTypes = new ArrayList<FileType>();
        CheckedAllFileType(true);

        fieldTypes = new ArrayList<FieldType>();
        fieldTypes.add(FieldType.Content);
        cbFieldTypeContent.setValue(true);

        imgGoogle.getElement().getStyle().setProperty("marginTop", "0px");
        imgGoogle.getElement().getStyle().setProperty("paddingRight", "4px");
        imgGoogle.getElement().getStyle().setProperty("paddingLeft", "2px");

        popupPanel.getElement().getStyle().setProperty("border", "2px solid black");
        popupPanel.getElement().getStyle().setProperty("padding", "0px");
        popupPanel.setAnimationEnabled(true);
        popupPanel.setAnimationType(PopupPanel.AnimationType.CENTER);
        popupPanel.setGlassEnabled(true);
        popupPanel.setAutoHideOnHistoryEventsEnabled(true);
        popupPanel.setAutoHideEnabled(true);

        dateBox.getElement().getStyle().setProperty("width", "76px");
        dateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
    }

    @UiHandler("btOK")
    void onOkClicked(ClickEvent event)
    {
        SearchDetails searchDetails = new SearchDetails();
        searchDetails.setSearchFor(fileTypes);
        searchDetails.setSearchInFields(fieldTypes);

        if(dateBox.getValue() != null)
        {
            searchDetails.setSearchDate(dateBox.getValue().toString());
        }

        searchDetails.setSearchWithGoogle(cbSearchWithGoogle.getValue());

        getUiHandlers().onOkClicked(searchDetails);
        hide();
    }

    @UiHandler("btClearDate")
    void onClearDateClicked(ClickEvent event)
    {
        dateBox.setValue(null);
    }

    @UiHandler("cbFileTypeALL")
    void onCheckBoxClickedALL(ClickEvent event)
    {
        CheckedAllFileType(cbFileTypeALL.getValue());
    }

    @UiHandler("cbFileTypePDF")
    void onCheckBoxClickedPDF(ClickEvent event)
    {
        monitorFileTypeCheckbox();
    }

    @UiHandler("cbFileTypeDOCX")
    void onCheckBoxClickedDOCX(ClickEvent event)
    {
        monitorFileTypeCheckbox();
    }

    @UiHandler("cbFileTypeTXT")
    void onCheckBoxClickedTXT(ClickEvent event)
    {
        monitorFileTypeCheckbox();
    }

    @UiHandler("cbFileTypeXLS")
    void onCheckBoxClickedXLS(ClickEvent event)
    {
        monitorFileTypeCheckbox();
    }

    @UiHandler("cbFieldTypeContent")
    void onCheckBoxClickedContent(ClickEvent event)
    {
        monitorFieldTypeCheckbox();
    }

    @UiHandler("cbFieldTypeAuteur")
    void onCheckBoxClickedAuteur(ClickEvent event)
    {
        monitorFieldTypeCheckbox();
    }

    void monitorFileTypeCheckbox()
    {
        if(cbFileTypePDF.getValue() && cbFileTypeDOCX.getValue() && cbFileTypeTXT.getValue() && cbFileTypeXLS.getValue())
        {
            cbFileTypeALL.setValue(true);
            fileTypes.clear();
            fileTypes.add(FileType.ALL);
        }
        else
        {
            cbFileTypeALL.setValue(false);
            fileTypes.clear();

            if(cbFileTypeXLS.getValue())
            {
                fileTypes.add(FileType.XLS);
            }
            if(cbFileTypeTXT.getValue())
            {
                fileTypes.add(FileType.TXT);
            }
            if(cbFileTypeDOCX.getValue())
            {
                fileTypes.add(FileType.DOCX);
            }
            if(cbFileTypePDF.getValue())
            {
                fileTypes.add(FileType.PDF);
            }
        }
    }

    void monitorFieldTypeCheckbox()
    {
        fieldTypes.clear();

        if(cbFieldTypeContent.getValue())
        {
            fieldTypes.add(FieldType.Content);
        }
        if(cbFieldTypeAuteur.getValue())
        {
            fieldTypes.add(FieldType.Auteur);
        }
    }

    void CheckedAllFileType(Boolean check)
    {
        cbFileTypePDF.setValue(check);
        cbFileTypeDOCX.setValue(check);
        cbFileTypeTXT.setValue(check);
        cbFileTypeXLS.setValue(check);
        cbFileTypeALL.setValue(check);

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
