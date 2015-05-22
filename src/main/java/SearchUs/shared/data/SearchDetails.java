package SearchUs.shared.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Louis on 19/05/2015.
 */
public class SearchDetails implements IsSerializable{

    String searchString;
    int pageNumber;

    public SearchDetails(){

    }

    public SearchDetails(String searchString, int pageNumber)
    {
        this.searchString = searchString;
        this.pageNumber = pageNumber;
    }

    public SearchDetails(SearchDetails searchDetails)
    {
        searchString = searchDetails.getSearchString();
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }

    public String getSearchString()
    {
        return searchString;
    }

    public void setPageNumber(int pageNumber){this.pageNumber = pageNumber;}
    public int getPageNumber(){return pageNumber;}
}
