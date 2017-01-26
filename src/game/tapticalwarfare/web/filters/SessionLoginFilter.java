package game.tapticalwarfare.web.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.tapticalwarfare.util.Util;
import game.tapticalwarfare.web.constants.Headers;
import game.tapticalwarfare.workflow.ProfileWorkflow;

/**
 * Servlet Filter implementation class SessionLogin
 */
@WebFilter("/SessionLogin")
public class SessionLoginFilter implements Filter {

	public static final String[] ignoreURIs = {"/TapticalWarfareServer/Register"};
	
    /**
     * Default constructor. 
     */
    public SessionLoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String username = httpRequest.getHeader(Headers.USERNAME);
		String password = httpRequest.getHeader(Headers.PASSWORD);
		
		
		if(!ignoreAuthorization(httpRequest)){
			HttpSession session = httpRequest.getSession();
			if(session.isNew()){
				try{
					if(!validateLogin(username, password)){
						session.invalidate();
						httpResponse.setStatus(401);
						httpResponse.getWriter().write("Unauthorized request.");
					}
				} catch (Exception e){
					e.printStackTrace();
					session.invalidate();
					httpResponse.setStatus(500);
					httpResponse.getWriter().write("Failed to Login. Please try again later.");
				}
			}
		}
		
		chain.doFilter(request, httpResponse);
	}

	private boolean validateLogin(String username, String password){
		if(Util.IsEmptyString(username) || Util.IsEmptyString(password)) return false;
		
		boolean success = ProfileWorkflow.login(username.trim(), password.trim());
		return success;
	}
	
	private boolean ignoreAuthorization(HttpServletRequest httpRequest){
		for(String uri : ignoreURIs){
			if(httpRequest.getRequestURI().equals(uri)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
