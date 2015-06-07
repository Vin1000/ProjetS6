package SearchUs.shared.data;

import com.google.gwt.user.client.rpc.IsSerializable;


import java.util.ArrayList;


/**
 * Created by Louis on 19/05/2015.
 */
public class SearchDetails implements IsSerializable{

    String searchString;
    ArrayList<FileType> searchFor; // types de fichier (Babillard est un type de fichier)
    ArrayList<FieldType> searchInFields; //Champs dans lesquels on doit chercher
    String searchDate;  //Date pour la recherche


    public SearchDetails(){

    }

    public SearchDetails(String searchString)
    {
        this.searchString = searchString;
    }

    public SearchDetails(SearchDetails searchDetails)
    {
        searchString = searchDetails.getSearchString();
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }
    public void setSearchFor(ArrayList<FileType> searchFor){this.searchFor = searchFor;}
    public void setSearchInFields(ArrayList<FieldType> searchInFields){this.searchInFields = searchInFields;}
    public void setSearchDate(String searchDate){this.searchDate = searchDate;}


    public String getSearchString() {return searchString;}
    public String getSearchDate(){return searchDate;}
    public ArrayList<FileType> getSearchFor(){return searchFor;}
    public ArrayList<FieldType> getSearchInFields(){return searchInFields;}


}
