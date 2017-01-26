package game.tapticalwarfare.persistence.beans;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

//method to get by username
//method to get number of users with username

@Entity
@NamedQueries({
	@NamedQuery(name="Profile.findByName", query="SELECT p FROM Profile p WHERE p.username = :username")
})
public class Profile {

	@Id
	private String username;
	private String password;
	private int win;
	private int lose;
	
	public Profile() {
	}

	public Profile(String username, String password, int win, int lose) {
		this.username = username;
		this.password = password;
		this.win = win;
		this.lose = lose;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getWin() {
		return win;
	}

	public int getLose() {
		return lose;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setWin(int win) {
		this.win = win;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}
	
	
}
