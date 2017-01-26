package game.tapticalwarfare.web.resources;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import game.tapticalwarfare.util.Util;
import game.tapticalwarfare.web.constants.Headers;
import game.tapticalwarfare.workflow.ProfileWorkflow;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class RegisterResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterResource() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getHeader(Headers.USERNAME);
		String password = request.getHeader(Headers.PASSWORD);
		
		if(Util.IsEmptyString(username) || Util.IsEmptyString(password)){
			response.setStatus(400);
			response.getWriter().write("Missing username or password.");
			return;
		}
		
		try{
			boolean success = ProfileWorkflow.saveNewProfile(username, password);
			if(!success){
				response.setStatus(400);
				response.getWriter().write("Username exists already.");
				return;
			}
			response.setStatus(204);
		}catch (Exception e){
			e.printStackTrace();
			response.setStatus(500);
			response.getWriter().write("Failed to create new profile. Please try again later.");
		}
	}

}
