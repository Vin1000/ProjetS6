package SearchUs.client.application.home.searchbar;

import SearchUs.shared.data.FileType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import javax.inject.Inject;
import java.util.ArrayList;


public class SearchBarView extends ViewWithUiHandlers<SearchBarUiHandlers> implements SearchBarPresenter.MyView {
    interface Binder extends UiBinder<Widget, SearchBarView> {
    }

    ArrayList<FileType> fileTypes;

    @UiField
    TextBox textBox;

    @UiField
    Button sendSearchButton;

    @UiField
    Image sideImage;

    @UiField
    Image topImage;

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

    @Inject
    SearchBarView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        textBox.ensureDebugId("searchTextBox");
        sendSearchButton.ensureDebugId("sendSearchButton");
        topImage.getElement().getStyle().setProperty("paddingBottom", "10px");
        topImage.getElement().getStyle().setProperty("paddingTop", "100px");
        topImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getStyle().setProperty("display", "none");

        fileTypes = new ArrayList<FileType>();
    }

    @UiHandler("sendSearchButton")
    void onSendSearch(ClickEvent event) {
        ClickEvent();
    }

    @UiHandler("textBox")
    void onKeyPress(KeyPressEvent event){
        if(KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode()){
            ClickEvent();
        }
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

    void ClickEvent()
    {
        topImage.getElement().getStyle().setProperty("display", "none");
        sideImage.getElement().getStyle().setProperty("display", "initial");
        sideImage.getElement().getParentElement().setAttribute("align", "left");
        sideImage.getElement().getParentElement().getStyle().setProperty("paddingLeft", "6px");

        getUiHandlers().sendSearch(textBox.getText(), fileTypes);
    }

    Boolean IsAllTypeChecked()
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

    void CheckedAllType(Boolean check)
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
