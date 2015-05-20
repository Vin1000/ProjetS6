package SearchUs.server.engine;

/**
 * Created by Paulo on 19/05/2015.
 */
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import SearchUs.shared.data.SearchResultData;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;



@Path("/api")
public interface ElasticInterface extends RestService  {

    /*@GET
    public void getHellos( MethodCallback<List<SearchResultData>> callback);*/
    @POST
    @Path("/_search")
    void postRawQuery(@PathParam("query") String query, MethodCallback<SearchResultData> callback);
}

