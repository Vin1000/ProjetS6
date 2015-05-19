package SearchUs.server.engine;


    import java.security.KeyStore;
    import java.util.List;

    import SearchUs.shared.data.SearchResultData;
    import org.fusesource.restygwt.client.Defaults;
    import org.fusesource.restygwt.client.Method;
    import org.fusesource.restygwt.client.MethodCallback;

    import SearchUs.server.engine.ElasticInterface;

    import com.google.gwt.core.client.EntryPoint;
    import com.google.gwt.core.client.GWT;
    import com.google.gwt.user.client.ui.Label;
    import com.google.gwt.user.client.ui.RootLayoutPanel;
    import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by Paulo on 19/05/2015.
 */
public class ElasticManager {

    //Defaults.setServiceRoot("127.0.0.1");

    ElasticInterface searchEngine = GWT.create(ElasticInterface.class);


    public void queryString(String query, List<SearchResultData> response)
    {
        String q = "{\n" +
                "        \"query_string\": {\n" +
                "            \"query\": \" " + query + "\"\n" +
                "        }\n" +
                "    }";

        searchEngine.postRawQuery(q,new MethodCallback<List<SearchResultData>>() {

            public void onSuccess(Method method, List<SearchResultData> r) {
                /* VerticalPanel panel = new VerticalPanel();
                for (SearchResultData hello : response) {
                    Label label = new Label(hello.getName());
                    panel.add(label);
                }
                RootLayoutPanel.get().add(panel);*/

            }

        public void onFailure(Method method, Throwable exception) {
            Label label = new Label("Error");
            RootLayoutPanel.get().add(label);
        }
        });

    }

}
