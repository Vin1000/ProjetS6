package SearchUs.server.DataAccess;

import com.google.inject.Inject;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Marc-Antoine on 2015-06-09.
 */
public class LocalRepository {
    @Inject
    private DatabaseConnection db;

    public ArrayList<String> getValidPaths(String cip, ArrayList<String> paths){

        // Temporary patch : If no cip provided (CAS Disabled), Use TEST Cip
        if(cip == null){
            cip = "abdj2702";
        }

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

        ArrayList<String> validPaths = new ArrayList<>();
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
