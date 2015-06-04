package SearchUs.shared.data;

import java.util.List;

/**
 * Created by Paulo on 2015-05-28.
 */
public class SearchResultBoard extends SearchResultData {
    private String linkUrl;
    private String boardName;

    public SearchResultBoard(){
        this.setType(ResultType.board);
    }


    public SearchResultBoard(String boardName, String linkUrl, String description, String author, String title,
                             String date, List<String> keywords) {
        this.setType(ResultType.file);

        this.linkUrl = linkUrl;
        this.title = title;
        this.description = description;
        this.author = author;
        this.boardName = boardName;
        this.date = date;
        this.keywords = keywords;

    }


    public String getLinkUrl() {return linkUrl;}
    public String getBoardName(){ return boardName;}


    public void setLinkUrl(String linkUrl) {this.linkUrl = linkUrl;}
    public void setBoardName(String boardName) {   this.boardName = boardName; }


}
