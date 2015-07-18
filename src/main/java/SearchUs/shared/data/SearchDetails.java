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

    public void setSearchString(String searchString)
    {
        map.put("searchString", searchString);
    }

    public void setSearchFor(ArrayList<FileType> searchFor){

        String hashmapString = new String();
        for(int i = 0; i < searchFor.size(); i++)
        {
            hashmapString += searchFor.get(i).toString() + ",";
        }

        map.put("searchFor", hashmapString);
    }

    public void setSearchInFields(ArrayList<FieldType> searchInFields){
        String hashmapString = new String();
        for(int i = 0; i < searchInFields.size(); i++)
        {
            hashmapString += searchInFields.get(i).toString() + ",";
        }

        map.put("searchInFields", hashmapString);
    }

    public void setSearchDate(String searchDate){
        map.put("searchDate", searchDate);
    }

    public void setSearchWithGoogle(Boolean searchWithGoogle){
        map.put("searchWithGoogle", String.valueOf(searchWithGoogle));
    }

    public void setResultsPerPage(int resultsPerPage){
        map.put("resultsPerPage", resultsPerPage + "");
    }

    public String getSearchString() {
        if(map.containsKey("searchString")) {
            return ((String) map.get("searchString"));
        }

        return "";
    }

    public String getSearchDate() {
        if (map.containsKey("searchDate")) {
            return ((String) map.get("searchDate"));
        }

        return "1900-01-01";
    }

    public ArrayList<FileType> getSearchFor(){
        ArrayList<FileType> fileTypes = new ArrayList<FileType>();

        if(map.containsKey("searchFor")) {
            String hashmapString = ((String) map.get("searchFor"));
            String hashmapArray[] = hashmapString.split(",");
            for (int i = 0; i < hashmapArray.length; i++) {
                fileTypes.add(FileType.valueOf(hashmapArray[i]));
            }
        }
        else {
            fileTypes.add(FileType.ALL);
        }

        return fileTypes;
    }

    public ArrayList<FieldType> getSearchInFields(){
        ArrayList<FieldType> fieldTypes = new ArrayList<FieldType>();

        if(map.containsKey("searchInFields")) {
            String hashmapString = ((String) map.get("searchInFields"));
            String hashmapArray[] = hashmapString.split(",");
            for (int i = 0; i < hashmapArray.length; i++) {
                fieldTypes.add(FieldType.valueOf(hashmapArray[i]));
            }
        }

        else
        {
            fieldTypes.add(FieldType.Content);
        }
        return fieldTypes;
    }

    public Boolean getSearchWithGoogle()
    {
        if(map.containsKey("searchWithGoogle")) {
            return ((String) map.get("searchWithGoogle")).equals("true");
        }

        return false;
    }

    public int getResultsPerPage() {

        if(map.containsKey("resultsPerPage")) {
            return Integer.parseInt(map.get("resultsPerPage"));
        }

        return 5;
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
