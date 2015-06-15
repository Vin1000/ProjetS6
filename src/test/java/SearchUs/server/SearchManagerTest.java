package SearchUs.server;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Vincent on 2015-06-15.
 */
public class SearchManagerTest {

    @Test
    public void getFormattedDescriptionTest()
    {
        String searchText = "allo";
        String description = "ALLO je devrais avoir des attributs custom";
        String beginFormat = "<strong><font color=\"red\">";
        String endFormat = "</font></strong>";
        String expectedFormattedDescription = beginFormat + "ALLO" + endFormat + " je devrais avoir des attributs custom";
        Assert.assertTrue(false);//changer
    }
}
