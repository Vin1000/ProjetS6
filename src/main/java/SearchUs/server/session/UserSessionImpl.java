package SearchUs.server.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.validation.Assertion;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Created by Marc-Antoine on 2015-05-19.
 */

public class UserSessionImpl {

    private Provider<HttpServletRequest> provider = null;
    private HttpSession session = null;

    @Inject
    public UserSessionImpl(Provider<HttpServletRequest> requestProvider) {
        provider = requestProvider;
    }

    public UserSessionImpl(HttpServletRequest request) {
        if (request.isRequestedSessionIdValid() && request.isRequestedSessionIdFromCookie()) {
            session = request.getSession(false);
        }
    }

    public UserSessionImpl(HttpSession session) {
        this.session = session;
    }

    private HttpSession getSession() {
        try {
            if (session != null) {
                return session;
            }
            else if (provider.get().isRequestedSessionIdValid()) {
                return provider.get().getSession(false);
            }
            else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isValid() {
        return getSession() != null;
    }

    public String getUserId(){
        String userId = null;
        HttpSession session = getSession();

        if(session != null){
            Assertion assertion = (Assertion) session.getAttribute(org.jasig.cas.client.util.AbstractCasFilter.CONST_CAS_ASSERTION);

            if (assertion != null) {
                userId =  assertion.getPrincipal().getName();
            }
        }

        return userId;
    }

}
