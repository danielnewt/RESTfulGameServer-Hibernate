package game.tapticalwarfare.persistence.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import game.tapticalwarfare.persistence.beans.Game;
import game.tapticalwarfare.persistence.beans.Profile;
import game.tapticalwarfare.util.Util;

public class DAO {
	
	private static final SessionFactory sessionFactory = new Configuration().configure("game/tapticalwarfare/persistence/config/hibernate.cfg.xml").buildSessionFactory();
	
	private static final int MAX_PENDING_SESSIONS = 5;
	private static List<Session> availableSessions = new ArrayList<Session>();
	
	public static synchronized Session getSession(){
		Session session = null;
		if(availableSessions.isEmpty()){
			session = sessionFactory.openSession();
		} else {
			int lastIndex = availableSessions.size() - 1;
			session = availableSessions.get(lastIndex);
			availableSessions.remove(lastIndex);
		}
		session.beginTransaction();
		return session;
	}
	
	public static synchronized void closeSession(Session session){
		session.getTransaction().commit();
		if(availableSessions.size() < MAX_PENDING_SESSIONS){
			availableSessions.add(session);
		}else{
			session.close();
		}
	}
	
	public static boolean saveNewProfile(String username, String password, Session session){
		if(Util.IsEmptyString(username) || Util.IsEmptyString(password) || session == null) return false;
		
		Profile p = getProfileByName(username, session);
		if(p != null) return false;
		
		p = new Profile(username, password, 0, 0);
		session.save(p);
		
		return true;
	}
	
	public static Profile getProfileByName(String name, Session session){
		TypedQuery<Profile> query = session.createNamedQuery("Profile.findByName", Profile.class);
		query.setParameter("username", name);
		List<Profile> results = query.getResultList();
		if(results.isEmpty()) return null;
		return results.get(0);
	}
	
	public static int saveNewGame(String player1, String gamestate, String password, Session session){
		if(Util.IsEmptyString(player1) || Util.IsEmptyString(gamestate)) return -1;
		
		Profile p = getProfileByName(player1, session);
		if(p == null) return -1;
		
		Game g = new Game();
		g.setPlayer1(p);
		g.setCurrentTurn(0);
		g.setGamestate(gamestate);
		g.setPassword(password);
		session.save(g);
		
		return g.getGameId();
	}
	
	public static boolean joinGame(int gameId, String player2, String password, Session session){
		if(Util.IsEmptyString(player2)) return false;
		
		Profile p = getProfileByName(player2, session);
		if(p == null) return false;
		
		Game g = getGameById(gameId, session);
		if(g == null || g.getPlayer2() != null || (g.getPassword() != null && !g.getPassword().equals(password))) return false;
		
		g.setPlayer2(p);
		session.update(g);
		
		return true;
	}
	
	public static String getGamestate(int gameId, Session session){
		Game g = getGameById(gameId, session);
		if(g == null) return null;
		return g.getGamestate();
	}
	
	public static boolean setGamestate(int gameId, int currentTurn, String gamestate, Session session){
		if(Util.IsEmptyString(gamestate)) return false;
		
		Game g = getGameById(gameId, session);
		if(g == null) return false;
		
		g.setCurrentTurn(currentTurn);
		g.setGamestate(gamestate);
		session.update(g);
		
		return true;
	}
	
	public static Game getGameById(int id, Session session){
		TypedQuery<Game> query = session.createNamedQuery("Game.findById", Game.class);
		query.setParameter("id", id);
		List<Game> results = query.getResultList();
		if(results.isEmpty()) return null;
		return results.get(0);
	}
	
	public static List<Game> getAllGames(Session session){
		TypedQuery<Game> query = session.createNamedQuery("Game.findAll", Game.class);
		List<Game> results = query.getResultList();
		return results;
	}
}