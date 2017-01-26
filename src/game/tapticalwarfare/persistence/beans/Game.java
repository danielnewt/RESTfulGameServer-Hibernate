package game.tapticalwarfare.persistence.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@NamedQueries({
	@NamedQuery(name="Game.findAll", query="SELECT g FROM Game g"),
	@NamedQuery(name="Game.findById", query="SELECT g FROM Game g WHERE g.gameId = :id")
})
public class Game implements Serializable{
	
	private static final long serialVersionUID = -8636411129812577612L;

	@Id
	@GeneratedValue
	private int gameId;
	
	@ManyToOne
	@JoinColumn(name="player1")
	private Profile player1;
    
	@ManyToOne
	@JoinColumn(name="player2")
	private Profile player2;
	
	private int currentTurn;
	
	@Type(type="text")
	private String gamestate;
	private String password;
	
	@UpdateTimestamp
	private Date lastUpdated;

	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	public Game() {
	}

	public int getGameId() {
		return gameId;
	}

	public Profile getPlayer1() {
		return player1;
	}

	public Profile getPlayer2() {
		return player2;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public String getGamestate() {
		return gamestate;
	}

	public String getPassword() {
		return password;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setPlayer1(Profile player1) {
		this.player1 = player1;
	}

	public void setPlayer2(Profile player2) {
		this.player2 = player2;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public void setGamestate(String gamestate) {
		this.gamestate = gamestate;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
    public long getTimeSinceUpdated(){
    	Date current = new Date();
    	return current.getTime() - lastUpdated.getTime();
    }
    
}
