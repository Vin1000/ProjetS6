package SearchUs.server.DataAccess;

import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Marc-Antoine on 2015-06-09.
 */
public class DatabaseConnection {

    private String url;
    private String username;
    private String password;

    @Inject
    public DatabaseConnection()
    {
        this.url = "jdbc:postgresql://45.55.206.156:5432/Opus";
        this.username = "postgres";
        this.password = "S6 postgres";

        try {
            String driver = "org.postgresql.Driver";
            Class.forName(driver).newInstance();
        } catch (Exception e1) {
            System.out.println("Echec du chargement du pilote postgres");
            e1.printStackTrace();
        }

    }

    public ResultSet execute(String query)
    {
        Connection connection = null;
        ResultSet result = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            Statement select = connection.createStatement();
            result = select.executeQuery(query);
            System.out.println("Les resultats obtenus:");

        } catch (Exception e2) {
            e2.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }

            return result;
        }
    }
}

