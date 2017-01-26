package game.tapticalwarfare.web.resources;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import game.tapticalwarfare.model.json.GameJson;
import game.tapticalwarfare.model.json.SerializableList;
import game.tapticalwarfare.util.Util;
import game.tapticalwarfare.util.json.JsonHelper;
import game.tapticalwarfare.web.constants.Headers;
import game.tapticalwarfare.workflow.GameWorkflow;
import game.tapticalwarfare.workflow.ProfileWorkflow;

/**
 * Servlet implementation class GameResource
 */
@WebServlet("/Game")
public class GameResource extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GameResource() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null){
			try{
				String gameId = request.getHeader(Headers.GAME_ID);
				if(Util.IsEmptyString(gameId)){
					SerializableList games = GameWorkflow.getAllGames();
					if(games == null || games.getList().isEmpty()){
						response.setStatus(204);
					}else {
						String gamesJson = JsonHelper.convertToJson(games);
						response.setStatus(200);
						response.setContentType("application/json");
						response.getWriter().write(gamesJson);
					}
				} else {
					String gameState = GameWorkflow.getGamestate(Integer.parseInt(gameId));
					response.setStatus(200);
					response.setContentType("application/json");
					response.getWriter().write(gameState);
				}
			}catch(Exception e){
				e.printStackTrace();
				response.setStatus(500);
				response.getWriter().write("Request Failed. Please try again later.");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null){
			try{
				String gameHeader = request.getHeader(Headers.GAME);
				if(Util.IsEmptyString(gameHeader)){
					response.setStatus(400);
					response.getWriter().write("Missing header");
				}else{
					switch(gameHeader){
					case Headers.JOIN:
						join(request, response);
						break;
					case Headers.HOST:
						host(request, response);
						break;
					case Headers.UPDATE:
						update(request, response);
						break;
					case Headers.GAME_WIN:
						gameEnd(request, response);
						break;
					default:
						response.setStatus(400);
						response.getWriter().write("Invalid game header");
						break;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				response.setStatus(500);
				response.getWriter().write("Request Failed. Please try again later.");
			}
		}
	}

	private void gameEnd(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String username = request.getHeader(Headers.USERNAME);
		username = Util.IsEmptyString(username) ? null : username.trim();
		
		String gameIdStr = request.getHeader(Headers.GAME_ID);
		gameIdStr = Util.IsEmptyString(gameIdStr) ? null : gameIdStr.trim();
		
		String losingPlayer = request.getHeader(Headers.GAME_LOSE);
		losingPlayer = Util.IsEmptyString(gameIdStr) ? null : losingPlayer.trim();
		
		int gameId = 0;
		try{
			gameId = Integer.parseInt(gameIdStr);
		}catch(Exception e){
			response.setStatus(400);
			response.getWriter().write("Invalid value for header: " + Headers.GAME_ID);
			return;
		}
		
		if(username != null && losingPlayer != null){
			ProfileWorkflow.logWin(username);
			ProfileWorkflow.logLoss(losingPlayer);
			GameWorkflow.RemoveGameById(gameId);
		} else {
			response.setStatus(400);
			response.getWriter().write("Username is missing from request.");
			return;
		}
	}
	
	private void update(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			String body = Util.getRequestBody(request);
			if(Util.IsEmptyString(body)){
				response.setStatus(400);
				response.getWriter().write("Empty request body");
				return;
			}
			
			GameJson game = null;
			try{
				game = JsonHelper.convertToGameJson(body);
			}catch(Exception e){
				response.setStatus(400);
				response.getWriter().write("Invalid request body");
				return;
			}
			
			if(game == null || Util.IsEmptyString(game.getGameState())){
				response.setStatus(400);
				response.getWriter().write("Missing information in request body");
				return;
			}
			
			boolean success = GameWorkflow.setGamestate(game.getGameId(), game.getCurrentTurn(), game.getGameState());
			
			if(success){
				response.setStatus(204);
			}else{
				response.setStatus(500);
				response.getWriter().write("Failed to update game.");
			}
		} catch(Exception e){
			throw e;
		}
	}
	
	private void host(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			
			String body = Util.getRequestBody(request);
			if(Util.IsEmptyString(body)){
				response.setStatus(400);
				response.getWriter().write("Empty request body");
				return;
			}
			
			GameJson game = null;
			try{
				game = JsonHelper.convertToGameJson(body);
			}catch(Exception e){
				response.setStatus(400);
				response.getWriter().write("Invalid request body: " + body);
				return;
			}
			
			if(game == null || 
					Util.IsEmptyString(game.getPlayer1()) ||
					Util.IsEmptyString(game.getGameState())){
				response.setStatus(400);
				response.getWriter().write("Missing information in request body");
				return;
			}
			
			String gamePassword = game.getPassword();
			gamePassword = Util.IsEmptyString(gamePassword) ? null : gamePassword.trim();
			
			int gameId = GameWorkflow.saveNewGame(game.getPlayer1(), game.getGameState(), gamePassword);
			
			if(gameId > 0){
				response.setStatus(200);
				response.getWriter().write("" + gameId);
			}else{
				response.setStatus(500);
				response.getWriter().write("Failed to save new game.");
			}
		} catch(Exception e){
			throw e;
		}
	}
	
	private void join(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{
			String gamePassword = request.getHeader(Headers.GAME_PASSWORD);
			gamePassword = Util.IsEmptyString(gamePassword) ? null : gamePassword.trim();
			
			String username = request.getHeader(Headers.USERNAME);
			username = Util.IsEmptyString(username) ? null : username.trim();
			
			String gameIdStr = request.getHeader(Headers.GAME_ID);
			gameIdStr = Util.IsEmptyString(gameIdStr) ? null : gameIdStr.trim();
			
			if(username == null || gameIdStr == null){
				response.setStatus(400);
				response.getWriter().write("Missing header information");
				return;
			}
			
			int gameId = 0;
			try{
				gameId = Integer.parseInt(gameIdStr);
			}catch(Exception e){
				response.setStatus(400);
				response.getWriter().write("Invalid value for header: " + Headers.GAME_ID);
				return;
			}
			
			boolean success = GameWorkflow.joinGame(gameId, username, gamePassword);
			
			if(success){
				response.setStatus(204);
			}else{
				response.setStatus(500);
				response.getWriter().write("Failed to join game.");
			}
		} catch(Exception e){
			throw e;
		}
	}
	
}
