package SearchUs.server;

import SearchUs.server.DataAccess.DatabaseConnection;
import SearchUs.server.DataAccess.LocalRepository;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc-Antoine on 2015-05-28.
 */
public class TestDB {

    @Test
    public void testQuerry(){
        DatabaseConnection db = new DatabaseConnection();
        ResultSet result = db.execute("SELECT path FROM file.get_AllowedFilesFromList('babm2002','''C:\\temp\\TestFilev2'',''C:\\temp\\TestFileAdminv2''');");
        Assert.assertNotNull(result);
    }
}
