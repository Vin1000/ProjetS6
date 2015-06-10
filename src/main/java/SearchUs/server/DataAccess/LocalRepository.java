package SearchUs.server.DataAccess;

import com.google.inject.Inject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc-Antoine on 2015-06-09.
 */
public class LocalRepository {
    @Inject
    private DatabaseConnection db;

    public List<String> getValidPaths(String cip, List<String> paths){

        String query = "SELECT path FROM file.get_AllowedFilesFromList('" + cip + "','";

        boolean firstPath = true;
        for(String path : paths){
            if(!firstPath){
                query +=',';
            }
            else{
                firstPath = false;
            }
            query += "''" + path + "''";
        }
        query += "');";

        ArrayList<String> validPaths = new ArrayList<String>();
        ResultSet result = db.execute(query);

        try {
            while (result.next()) {
                validPaths.add(result.getString("path"));
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return validPaths;
    }
}
