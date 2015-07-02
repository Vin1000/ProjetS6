package SearchUs.server.logic;

import SearchUs.server.DataAccess.LocalRepository;
import SearchUs.server.engine.ElasticManager;
import SearchUs.server.session.UserSessionImpl;
import SearchUs.shared.data.SearchDetails;
import SearchUs.shared.data.SearchResultData;
import SearchUs.shared.data.SearchResultFile;
import SearchUs.shared.dispatch.search.SearchResult;

import com.google.gwt.thirdparty.json.JSONArray;
import com.google.gwt.thirdparty.json.JSONException;
import com.google.gwt.thirdparty.json.JSONObject;
import com.google.inject.Inject;

import java.sql.ResultSet;
import java.util.*;

/**
 * Created by Marc-Antoine on 2015-05-18.
 */
public class SearchManager {


    private UserSessionImpl session;

    public static final String SERVER_URL = "http://45.55.72.89/";

    private boolean GETFAKEDATA = false;

    @Inject
    private LocalRepository permisionsManager;

    @Inject
    public SearchManager(UserSessionImpl session) {
        this.session = session;
    }

    public SearchResult getSearchResults(SearchDetails searchInfo)
    {


        SearchResult result = new SearchResult();

        ArrayList<SearchResultData> listResults = new ArrayList<>();


        ArrayList<SearchResultData> permittedResults = new ArrayList<>();

        ArrayList<String> pathList = new ArrayList<>();
        ArrayList<String> permittedPaths;

        int totalHits = 0;

        if(!GETFAKEDATA)
        {
            //todo: injecter l'objet.
            ElasticManager searchEngine = new ElasticManager(SERVER_URL);

            JSONObject queryResult = searchEngine.search(searchInfo);

            if (queryResult != null)
            {
                try
                {
                    JSONObject hits = queryResult.getJSONObject("hits");

                    totalHits = hits.getInt("total");
                    int took = queryResult.getInt("took");

                    JSONArray resultsArray = hits.getJSONArray("hits");

                    JSONObject hit;
                    JSONObject hitSource;
                    JSONObject file;
                    JSONObject meta;
                    String filename;
                    String url;
                    String description;
                    String author;
                    String title;
                    String date;
                    List<String> keywords = null;
                    String realPath;

                    for (int i = 0; i < totalHits; i++)
                    {
                        hit = resultsArray.getJSONObject(i);
                        //System.out.println(hit.getString("_type"));
                        if (!hit.getString("_type").equals("folder"))//todo: enforce
                        {
                            hitSource = hit.getJSONObject("_source");
                            file = hitSource.getJSONObject("file");
                            filename = file.getString("filename");
                            realPath = hitSource.getJSONObject("path").getString("real");
                            url = realPath.replace("/var/www/html", SERVER_URL);
                            description = getFormattedDescription(hitSource.getString("content"), searchInfo.getSearchString());

                            meta = hitSource.getJSONObject("meta");
                            author = meta.getString("author");
                            title = meta.getString("title");
                            date = meta.getString("date");

                            listResults.add(new SearchResultFile(filename, url, description, author, title, date, keywords,realPath));

                            //add path to list
                            pathList.add(realPath);
                        }
                    }
                    result.setTimeElapsed(took);

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else //GetFakeData when server is down!
        {
            listResults.addAll(GetFakeData(3, searchInfo.getSearchString()));
        }

        if(listResults.size() > 0)
        {
            permittedPaths = permisionsManager.getValidPaths(session.getUserId() , pathList);

            for(SearchResultData res:listResults)
            {
                if(res instanceof SearchResultFile)
                {
                    SearchResultFile fileResult = (SearchResultFile) res;
                    if(permittedPaths.contains(fileResult.getRealPath()))
                    {
                        permittedResults.add(res);
                        permittedPaths.remove(fileResult.getRealPath());
                    }
                    else
                    {
                        totalHits--;
                    }
                }
                else
                {
                    permittedResults.add(res);
                }
            }
        }


        result.setTotalHits(totalHits);
        result.setSearchResults(permittedResults);
        return result;
    }

    private String getFormattedDescription(String description, String searchText)
    {
        String beginFormat = "<strong><font color=\"red\">";
        String endFormat = "</font></strong>";
        String punctuation = "[\\p{Punct}\\s]+";

        String[] searchKeywords = searchText.split(punctuation); //split searchtext in keywords

        String[] content = description.split(punctuation); //split description in words
        for(int i = 0; i < content.length; i++)
        {
            content[i] = content[i].toLowerCase(); //lowercase every word
        }
        String formattedDescription = description;
        ArrayList<Integer> indexList = new ArrayList<>();
        Map<Integer, Integer> dictionary = new HashMap<>();

        for(int i = 0; i < searchKeywords.length; i++) //foreach keyword
        {
            //if(description.toLowerCase().matches(".*\\b" + searchKeywords[i].toLowerCase() + "\\b.*")) //if keyword is exact word
            if(Arrays.asList(content).contains(searchKeywords[i].toLowerCase()))
            {
                ArrayList<Integer> list = getAllIndex(formattedDescription.toLowerCase(), searchKeywords[i].toLowerCase());
                indexList.addAll(list);
                for (Integer index : list)
                {
                    dictionary.put(index, searchKeywords[i].length()); //add index + keyword length in dictionary
                }
            }
        }

        Collections.sort(indexList, Collections.reverseOrder()); //start by the end

        //by starting with the last one, the indexes will stay correct.
        for(Integer index : indexList)
        {
            String textBeforeWord = formattedDescription.substring(0, index); //0 to index
            String word = formattedDescription.substring(index, index + dictionary.get(index)); //index to index+length
            String textAfterWord = formattedDescription.substring(index + dictionary.get(index)); //index+length to end
            formattedDescription = textBeforeWord + beginFormat + word + endFormat + textAfterWord;
        }
        return formattedDescription;
    }

    private ArrayList<Integer> getAllIndex(String text, String word)
    {
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = -1; (i = text.indexOf(word, i + 1)) != -1; )
        {
            indexes.add(i);
        }
        return indexes;
    }

    private ArrayList<SearchResultData> GetFakeData(int amount, String searchText) //for when server is down
    {
        ArrayList<SearchResultData> list = new ArrayList<>();
        for(int i = 0; i < amount; i++)
        {
            String filename = "File-" + i + ".txt";
            String url = "http://www.google.ca";
            String description = "ALLO JE DEVRAIS AVOIR DES ATTRIBUTS CUSTOM";
            String author = "fakeAuthor";
            String title = "Fake file " + i;
            String date = new Date().toString();
            ArrayList<String> keywords = new ArrayList<>();
            String realPath = "";
            list.add(new SearchResultFile(filename, url, getFormattedDescription(description, searchText), author, title, date, keywords,realPath));
        }

        return list;
    }
}
