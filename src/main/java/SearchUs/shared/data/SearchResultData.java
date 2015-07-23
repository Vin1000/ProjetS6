package SearchUs.shared.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public abstract class SearchResultData implements Serializable {

    protected String description;
    protected String author;
    protected String date;
    protected String title;
    protected List<String> keywords;

    public SearchResultData(){ }

    public SearchResultData(String filename, String downloadUrl) {
        this.title = "";
        this.description = "";
        this.author = "";
        this.date = "";
        this.keywords = null;
    }

    public SearchResultData(String filename, String downloadUrl, String description) {

        this.title = "";
        this.description = description;
        this.author = "";
        this.date = "";
        this.keywords = null;
    }

    public SearchResultData(String filename, String downloadUrl, String description, String author, String title,
                            String date, List<String> keywords) {

        this.title = title;
        this.description = description;
        this.author = author;
        this.date = date;
        this.keywords = keywords;
    }

    public enum ResultType { file, board }

    private ResultType type;

    public String getAuthor(){ return author; }
    public String getDate(){ return date;}
    public String getDescription(){return description;}
    public List<String> getKeywords(){return keywords;}
    public String getTitle() {return title;}
    public ResultType getType() {return type;}


    public void setAuthor(String author) {   this.author = author; }
    public void setDescription(String description) {this.description = description;}
    public void setDate(String date) {   this.date = date; }
    public void setKeywords(List<String> keywords) {   this.keywords = keywords; }
    public void setTitle(String title) {this.title = title;}

    //Ceci est protege afin que seule la classe enfant puisse s'en servir pour etablir le type de resultat
    protected void setType(ResultType type){this.type = type;}

}
