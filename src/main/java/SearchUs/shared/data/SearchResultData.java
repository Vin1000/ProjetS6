package SearchUs.shared.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchResultData implements Serializable {
    private String downloadUrl;
    private String filename;
    private String description;

    private String author;
    private String title;
    private String date;
    private List<String> keywords;


    public SearchResultData(){ }

    public SearchResultData(String filename, String downloadUrl) {
        this.title = "";
        this.downloadUrl = downloadUrl;
        this.description = "";
        this.author = "";
        this.filename = filename;
        this.date = "";
        this.keywords = null;
    }

    public SearchResultData(String filename, String downloadUrl, String description) {

        this.downloadUrl = downloadUrl;
        this.title = "";
        this.description = description;
        this.author = "";
        this.filename = filename;
        this.date = "";
        this.keywords = null;
    }

    public SearchResultData(String filename, String downloadUrl, String description, String author, String title,
                            String date, List<String> keywords) {

        this.downloadUrl = downloadUrl;
        this.title = title;
        this.description = description;
        this.author = author;
        this.filename = filename;
        this.date = date;
        this.keywords = keywords;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getAuthor(){ return author; }
    public String getFilename(){ return filename;}
    public String getDate(){ return date;}
    public List<String> getKeywords(){return keywords;}

    public void setAuthor(String author) {   this.author = author; }
    public void setFilename(String filename) {   this.filename = filename; }
    public void setDate(String date) {   this.date = date; }
    public void setKeywords(List<String> keywords) {   this.keywords = keywords; }


}
