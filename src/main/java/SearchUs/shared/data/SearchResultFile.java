package SearchUs.shared.data;

import SearchUs.shared.dispatch.search.SearchResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Paulo on 2015-05-28.
 */
public class SearchResultFile extends SearchResultData {
    private String downloadUrl;
    private String filename;

    public SearchResultFile(){
        this.setType(ResultType.file);
    }


    public SearchResultFile(String filename, String downloadUrl, String description, String author, String title,
                            String date, List<String> keywords) {
        this.setType(ResultType.file);

        this.downloadUrl = downloadUrl;
        this.title = title;
        this.description = description;
        this.author = author;
        this.filename = filename;
        this.date = date;
        this.keywords = keywords;

    }


    public String getDownloadUrl() {return downloadUrl;}
    public String getFilename(){ return filename;}


    public void setDownloadUrl(String downloadUrl) {this.downloadUrl = downloadUrl;}
    public void setFilename(String filename) {   this.filename = filename; }


}
