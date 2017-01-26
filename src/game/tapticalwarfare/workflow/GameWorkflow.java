package game.tapticalwarfare.workflow;

import java.util.List;

import org.hibernate.Session;

import game.tapticalwarfare.model.json.GameJson;
import game.tapticalwarfare.model.json.SerializableList;
import game.tapticalwarfare.persistence.beans.Game;
import game.tapticalwarfare.persistence.dao.DAO;
import game.tapticalwarfare.util.Util;

public class GameWorkflow {

	public static int saveNewGame(String player1, String gamestate, String password){
		Session s = null;
		try{
			s = DAO.getSession();
			password = Util.IsEmptyString(password) ? null : password;
			
			return DAO.saveNewGame(player1, gamestate, password, s);
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static boolean joinGame(int gameId, String player2, String password){
		Session s = null;
		try{
			s = DAO.getSession();
			password = Util.IsEmptyString(password) ? null : password;
			
			return DAO.joinGame(gameId, player2, password, s);
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static GameJson getGameById(int id){
		Session s = null;
		try{
			s = DAO.getSession();
			Game game = DAO.getGameById(id, s);
			return convertToGameJson(game);
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static SerializableList getAllGames(){
		Session s = null;
		try{
			s = DAO.getSession();
			List<Game> games = DAO.getAllGames(s);
			if(games != null && !games.isEmpty()){
				SerializableList jsonList = new SerializableList();
				for(Game g : games){
					jsonList.add(convertToGameJson(g));
				}
				return jsonList;
			}
			return null;
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static String getGamestate(int gameId){
		Session s = null;
		try{
			s = DAO.getSession();
			return DAO.getGamestate(gameId, s);
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static boolean setGamestate(int gameId, int currentTurn, String gamestate){
		Session s = null;
		try{
			s = DAO.getSession();
			return DAO.setGamestate(gameId, currentTurn, gamestate, s);
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static GameJson convertToGameJson (Game g){
		if(g == null) return null;
		GameJson gj = new GameJson();
		gj.setGameId(g.getGameId());
		gj.setCurrentTurn(g.getCurrentTurn());
		gj.setPlayer1(g.getPlayer1().getUsername());
		if(g.getPlayer2() != null){
			gj.setPlayer2(g.getPlayer2().getUsername());
		} else {
			gj.setPlayer2(null);
		}
		gj.setPassword(g.getPassword());
		return gj;
	}
	
	public static void RemoveGameById(int gameId){
		Session s = null;
		try{
			s = DAO.getSession();
			Game game = DAO.getGameById(gameId, s);
			if(game != null){
				s.delete(game);
			}
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static void RemoveUnusedGamesByAge(long maxAgeInMillis) throws Exception{
		Session s = null;
		try{
			s = DAO.getSession();
			List<Game> games = DAO.getAllGames(s);
			if(games != null && !games.isEmpty()){
				for(Game g : games){
					if(g.getTimeSinceUpdated() >= maxAgeInMillis){
						s.delete(g);
					}
				}
			}
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
}
