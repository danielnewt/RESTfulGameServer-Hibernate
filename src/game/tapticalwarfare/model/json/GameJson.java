package game.tapticalwarfare.model.json;

import java.io.Serializable;

public class GameJson implements Serializable{

	private static final long serialVersionUID = -9192513058689458893L;

	private int gameId;
	private String player1;
	private String player2;
	private int currentTurn;
	private String gameState;
	private String password;
	
	public GameJson(){
		
	}
	
	public GameJson(int gameId, String player1, String player2, int currentTurn, String gameState) {
		this.gameId = gameId;
		this.player1 = player1;
		this.player2 = player2;
		this.currentTurn = currentTurn;
		this.gameState = gameState;
	}

	public int getGameId() {
		return gameId;
	}

	public String getPlayer1() {
		return player1;
	}

	public String getPlayer2() {
		return player2;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}
	
	public String getGameState(){
		return gameState;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public void setPlayer1(String player1) {
		this.player1 = player1;
	}

	public void setPlayer2(String player2) {
		this.player2 = player2;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}
	
	public void setGameState(String gameState){
		this.gameState = gameState;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
