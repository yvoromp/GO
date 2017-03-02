package Players;

import goGame.Board.Status;
import goGame.Board;

public abstract class Player {
	
	private String name;
	private Status status;
	private int boardSize;
	
	/**
	 * creates a new player instance;
	 * @param name
	 * @param status
	 * @param boardSize
	 */
	public Player(String name, Status status, int boardSize){
		this.boardSize = boardSize;
		this.name = name;
		this.status = status;
	}
	
	//returns the name of the player
	public String getName(){
		return name;
	}
	
	//returns the stone of the player
	public Status getStone(){
		return status;
	}
	
	//returns the boardsize of the player
	public int getBoardSize(){
		return boardSize;
	}
	
	/**
	 * checks if player passes or plays
	 * @param b
	 * @return
	 */
	public abstract boolean passOrPlay(Board b);
		
	/**
	 * determines the position for the next move of the player
	 * @param board
	 * @return
	 */
	public abstract String determineMove(Board board);
	
	
	public abstract String webMove(Board board);
	
	/**
	 * places a stone on the board
	 * @param board
	 */
//	public void makeMove(Board board){
//		if(passOrPlay(board)){
//			board.pass();
//		}else{
//			Index chosenIndex = determineMove(board);
//			board.blackPassed = false;
//			board.rightStage = true;
//			board.setStone(chosenIndex);
//			board.rightStage = false;
//		}
//		
//	}

}
