package SearchUs.shared.data;

import java.io.Serializable;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchResultData implements Serializable {
    private String downloadUrl;
    private String title;

    public SearchResultData(){ }

    public SearchResultData(String title, String downloadUrl) {
        this.title = title;
        this.downloadUrl = downloadUrl;
    }

    public SearchResultData(String title, String downloadUrl, String description) {

        this.downloadUrl = downloadUrl;
        this.title = title;
        this.description = description;
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

    private String description;
}
