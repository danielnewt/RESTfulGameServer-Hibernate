package game.tapticalwarfare.workflow;

import org.hibernate.Session;

import game.tapticalwarfare.persistence.beans.Profile;
import game.tapticalwarfare.persistence.dao.DAO;

public class ProfileWorkflow {

	
	public static boolean saveNewProfile(String username, String password){
		Session s = null;
		try{
			s = DAO.getSession();
			boolean success = DAO.saveNewProfile(username, password, s);
			return success;
		} catch (Exception e){
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static Profile getProfileByUsername(String username){
		Session s = null;
		try{
			s = DAO.getSession();
			Profile p = DAO.getProfileByName(username, s);
			return p;
		} catch (Exception e) {
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static boolean login(String username, String password){
		Session s = null;
		try{
			s = DAO.getSession();
			Profile p = DAO.getProfileByName(username, s);
			boolean success = false;
			if(p != null && p.getPassword().equals(password)){
				success = true;
			}
			return success;
		} catch (Exception e) {
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static void logWin(String username){
		Session s = null;
		try{
			s = DAO.getSession();
			Profile p = DAO.getProfileByName(username, s);
			p.setWin(p.getWin() + 1);
			s.save(p);
		} catch (Exception e) {
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
	
	public static void logLoss(String username){
		Session s = null;
		try{
			s = DAO.getSession();
			Profile p = DAO.getProfileByName(username, s);
			p.setLose(p.getLose() + 1);
			s.save(p);
		} catch (Exception e) {
			throw e;
		} finally {
			if(s != null){
				DAO.closeSession(s);
			}
		}
	}
}
