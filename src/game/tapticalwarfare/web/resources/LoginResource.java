package game.tapticalwarfare.web.resources;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.tapticalwarfare.persistence.beans.Profile;
import game.tapticalwarfare.web.constants.Headers;
import game.tapticalwarfare.workflow.ProfileWorkflow;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginResource() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null){
			String username = request.getHeader(Headers.USERNAME);
			Profile p = ProfileWorkflow.getProfileByUsername(username);
			if(p != null){
				response.setHeader(Headers.GAME_WIN, "" +p.getWin());
				response.setHeader(Headers.GAME_LOSE, "" +p.getLose());
			}
			response.setStatus(204);
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
		if(response.getStatus() == 204){
			HttpSession session = request.getSession(false);
			session.invalidate();
		}
	}
}
