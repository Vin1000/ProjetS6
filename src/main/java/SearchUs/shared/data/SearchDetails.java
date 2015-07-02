package SearchUs.shared.data;

import SearchUs.client.place.NameTokens;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Louis on 19/05/2015.
 */
public class SearchDetails implements IsSerializable{

    HashMap<String, String> map;

    public SearchDetails(){
        map = new HashMap<String, String>();
    }

    public SearchDetails(String searchString)
    {
        map = new HashMap<String, String>();
        map.put("searchString", searchString);
    }

    public SearchDetails(SearchDetails searchDetails)
    {
        map = new HashMap<String, String>();
        map.put("searchString", searchDetails.getSearchString());
    }

    public void setSearchString(String searchString)
    {
        map.put("searchString", searchString);
    }
    public void setSearchFor(ArrayList<FileType> searchFor){

        String hashmapString = new String();
        for(int i = 0; i < searchFor.size(); i++)
        {
            hashmapString = searchFor.get(i).toString() + ",";
        }

        map.put("searchFor", hashmapString);
        }
    public void setSearchInFields(ArrayList<FieldType> searchInFields){
        String hashmapString = new String();
        for(int i = 0; i < searchInFields.size(); i++)
        {
            hashmapString = searchInFields.get(i).toString() + ",";
        }

        map.put("searchInFields", hashmapString);
        }
    public void setSearchDate(String searchDate){
        map.put("searchDate", searchDate);
        }
    public void setSearchWithGoogle(Boolean searchWithGoogle){
        map.put("searchWithGoogle", String.valueOf(searchWithGoogle));
        }


    public String getSearchString() {return ((String)map.get("searchString"));}
    public String getSearchDate(){return ((String)map.get("searchDate"));}
    public ArrayList<FileType> getSearchFor(){
        ArrayList<FileType> fileTypes = new ArrayList<FileType>();

        String hashmapString = ((String)map.get("searchFor"));
        String hashmapArray[]  = hashmapString.split(",");
        for(int i = 0; i < hashmapArray.length; i++)
        {
            fileTypes.add(i, FileType.valueOf(hashmapArray[i]));
        }
        return fileTypes;
    }
    public ArrayList<FieldType> getSearchInFields(){
        ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();

        String hashmapString = ((String)map.get("searchInFields"));
        String hashmapArray[]  = hashmapString.split(",");
        for(int i = 0; i < hashmapArray.length; i++)
        {
            fieldTypes.add(i, FieldType.valueOf(hashmapArray[i]));
        }

        return fieldTypes;
    }
    public Boolean getSearchWithGoogle(){

        if(((String)map.get("searchWithGoogle")).equals("true"))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public PlaceRequest ToUrlString()
    {
        PlaceRequest.Builder builder = new PlaceRequest.Builder()
                .nameToken(NameTokens.getHome())
                .with(map);
        return builder.build();
    }

    public void GetUrlParameters(Object urlParameters[], PlaceRequest request)
    {
        for(int i = 0; i < urlParameters.length; i++) {
            map.put(urlParameters[i].toString(), request.getParameter(urlParameters[i].toString(), ""));
        }
    }
}
