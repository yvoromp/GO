package goGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import Gui.GoGUIIntegrator;


/*
 * The Chinese game GO
 */

public class Board {
	public enum Status{
		BLACK, WHITE, NONE
	}

	public static int DIM;
	public Index lastMove;
	public boolean deadStone;
	public HashMap<Index, Status> stones;
	public String allPositions = "";
	public HashSet <String> oldPositions;
	public boolean isBlack = true;
	public boolean blackPassed;
	public boolean gameEnded = false;
	public int blackStoneCounter;
	public int whiteStoneCounter;
	public boolean isBlackArea = false;
	public boolean isWhiteArea = false;
	public boolean rightStage = false;
	private GoGUIIntegrator GUI;



	public Board(int boardSize, GoGUIIntegrator gui){
		DIM = boardSize;
		GUI=gui;
		reset(DIM,GUI);
	}
	
	public Board(int boardSize){
		DIM = boardSize;
		testReset(DIM);
	}

	//deepcopy for valid move
	public HashMap<Index, Status> deepCopy() {
		HashMap <Index, Status> HashMapCopy = new HashMap<Index, Status>(stones);
		return HashMapCopy;
	}


	// method for changing  betweenplayers
	public void changePlayer(){
		isBlack = !isBlack;
	}

	//pass, including pass rule
	public void pass(){
		if( !isBlack && blackPassed){
			gameEnded = true;
			gameOver();
		}
		lastMove = null;
		if(isBlack){
			blackPassed = true;
		}


	}

	//puts current boardstate into list
	public void savePositions(){
		allPositions = "";
		for(int i = 0; i < DIM; i++){
			for(int j = 0; j < DIM; j++){
				String position = stones.get(getPointAt(i,j)).toString();
				allPositions = allPositions + position;
			}
		}
		oldPositions.add(allPositions);
	}

	/**
	 * returns the status of index i
	 * @param i
	 * @return
	 */
	public Status getStatus(Index i){
		Status position = stones.get(i);
		return position;
	}

	public Index getPointAt(int x, int y){
		for (Index index : stones.keySet()) {
			if (index.getX() == x && index.getY() == y) {
				return index;
			}
		}
		return null;
	}

	/**
	 * checks for suicide of stone
	 * @param i
	 * @param s
	 * @return
	 */
	public boolean isHarakiri(Index i, Status s){
		boolean harakiri = false;
		boolean otherStone = false;
		boolean sameStone = false;
		for( Index neighbor : getNeighbors(i)){
			if(getStatus(neighbor) != Status.NONE){
				if (getStatus(neighbor) != s){
					otherStone = true;
				}else{
					sameStone = true;
				}
			}else{
				return false;
			}
		}
		if(otherStone && !sameStone){
			harakiri = true;
		}
		return harakiri;

	}
	
	/**
	 * checks if KO rule is violated
	 * @param index
	 * @param status
	 * @return
	 */
	public boolean isKo(Index index, String status){
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		HashMap <Index, Status> stonesCopy = deepCopy();

		stones.put(index, nowPlaying);
		changeBoardAfterMove(index, status);
		String s = "";
		for (int i = 0; i < DIM; i++) {
			String visual = "";
			for (int j = 0; j < DIM; j++) {	
				visual = visual + stones.get(getPointAt(i,j));	
			}
			s = s + visual;
		}
		String currentBoard = s;
		for(String string : oldPositions){
			if(string.equals(currentBoard)){
				allPositions = "";
				stones = stonesCopy;
				return true;
			}
		}
		stones = stonesCopy;
		return false;
	}
	
	public boolean testIsKo(Index index, String status){
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		HashMap <Index, Status> stonesCopy = deepCopy();

		stones.put(index, nowPlaying);
		testChangeBoardAfterMove(index, status);
		String s = "";
		for (int i = 0; i < DIM; i++) {
			String visual = "";
			for (int j = 0; j < DIM; j++) {	
				visual = visual + stones.get(getPointAt(i,j));	
			}
			s = s + visual;
		}
		String currentBoard = s;
		for(String string : oldPositions){
			if(string.equals(currentBoard)){
				System.out.println(toString());
				System.out.println("position has occured before, voilation of KO rule");
				allPositions = "";
				stones = stonesCopy;
				return true;
			}
		}
		stones = stonesCopy;
		return false;
	}

	/**
	 * chekcs if a move is valid
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isValidMove(int x, int y, String status){
		if ( x < 0 || y < 0 || x > DIM -1 || y > DIM - 1){
			return false;
		}

		if (stones.get(getPointAt(x,y)) != Status.NONE){
			return false;
		}
		Index position = getPointAt(x,y);
		if (isKo(position, status)){
			return false;
		}
		lastMove = position;
		return true;
	}
	
	public boolean testIsValidMove(int x, int y, String status){
		if ( x < 0 || y < 0 || x > DIM -1 || y > DIM - 1){
			return false;
		}

		if (stones.get(getPointAt(x,y)) != Status.NONE){

			return false;
		}
		Index position = getPointAt(x,y);

		if (testIsKo(position, status)){
			return false;
		}
		lastMove = position;
		return true;
	}


	//checks if the game is over
	public boolean gameOver(){
		int count = 0;
		boolean gameOver = false;

		//checks for full board (no emptySpaces)
		for (int i = 0; i < DIM; i++) {
			for (int j = 0; j < DIM; j++) {	
				if(getStatus(getPointAt(j,i)) != Status.NONE){
					count++;
				}
			}
			if(count == DIM * DIM){
				gameOver = true;
			}
		}

		//checks if there was a game end by consecutive passing
		if(gameEnded){
			gameOver = true;
		}
		return gameOver;
	}

	/**
	 * checks which status owns an index
	 * @param i
	 * @param checkedPoints
	 * @return
	 */
	public boolean isNoneArea (Index i, Set<Index> checkedPoints) {
		for (Index neighbor : getNeighbors(i)) {
			if (getStatus(neighbor) == Status.BLACK) {
				isBlackArea = true;
			}
			if (getStatus(neighbor) == Status.WHITE) {
				isWhiteArea = true;
			}
			if (getStatus(neighbor) == Status.NONE && !checkedPoints.contains(neighbor)) {
				checkedPoints.add(neighbor);
				if (!isNoneArea(neighbor, checkedPoints)) {
					return false;
				}
			}
		}
		return true;
	}


	// counts the score at the end of the game
	public String totalScore(){
		int blackStoneCounter = 0;
		int whiteStoneCounter = 0;

		for(int i = 0; i < DIM; i++){
			for(int j = 0; j < DIM; j++){
				if(getStatus(getPointAt(j,i)) == Status.BLACK){
					isBlackArea = true;
				}
				if(getStatus(getPointAt(j,i)) == Status.WHITE){
					isWhiteArea = true;
				}
				if(getStatus(getPointAt(j,i)) == Status.NONE){
					for (Index neighbor : getNeighbors(getPointAt(j,i))){
						if(getStatus(neighbor) == Status.BLACK){
							isBlackArea = true;
						}
						if(getStatus(neighbor) == Status.WHITE){
							isWhiteArea = true;
						}
						if(getStatus(neighbor) == Status.NONE){
							Set<Index> checkedPoints = new HashSet<>();
							if (isNoneArea(neighbor, checkedPoints)) {
								checkedPoints.add(neighbor);
							}
						}

					}
				}
				if(isBlackArea && !isWhiteArea){
					blackStoneCounter++;
				}
				if(isWhiteArea && !isBlackArea){
					whiteStoneCounter++;
				}
				isBlackArea = false;
				isWhiteArea = false;
			}
		}
		this.blackStoneCounter = blackStoneCounter;
		this.whiteStoneCounter = whiteStoneCounter;
		return "CHAT EINDSCORE: Black scores " + blackStoneCounter + " and White scores " + whiteStoneCounter;
	}


	//returns a String representation of the player
	public String toString() {
		String s = "";
		for (int i = 0; i < DIM; i++) {
			String visualRow = "";
			for (int j = 0; j < DIM; j++) {	
				visualRow = visualRow + "    " + stones.get(getPointAt(i,j)) + "    ";

				if (j < DIM - 1) {
					visualRow = visualRow + "|";
				}
			}
			s = s + visualRow ;
			if (i < DIM - 1) {
				s = s + "\n" + "\n";
			}
		}

		return s;

	}

	//resets all the fields to empty
	public void reset(int boardSize, GoGUIIntegrator gui){
		GUI.clearBoard();
		oldPositions = new HashSet <String>();
		stones = new HashMap<Index, Status>();
		DIM = boardSize;
		for (int i = 0; i < DIM; i++){
			for(int j = 0; j < DIM; j++){
				Index index = new Index(i , j);
				stones.put(index, Status.NONE);
			}

		}
	}	
	
	public void testReset(int boardSize){
		oldPositions = new HashSet <String>();
		stones = new HashMap<Index, Status>();
		DIM = boardSize;
		for (int i = 0; i < DIM; i++){
			for(int j = 0; j < DIM; j++){
				Index index = new Index(i , j);
				stones.put(index, Status.NONE);
			}

		}
	}	

	//checks if stone should be removed
	public boolean isDead(Index i, Set<Index> checkedStones){
		for (Index neighbor : getNeighbors(i)){
			if (getStatus(neighbor) == Status.NONE){
				return false;
			}
			if (getStatus(neighbor) == getStatus(i) && !checkedStones.contains(neighbor)){
				checkedStones.add(neighbor);
				if(!isDead(neighbor, checkedStones)){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * gets the  positions surrounding stones
	 * @param i
	 * @return
	 */
	public Set<Index> getNeighbors(Index i){
		Set<Index> neighbors = new HashSet<>();

		if( i.getX() > 0){
			neighbors.add(getPointAt(i.getX() - 1, i.getY()));
		}
		if( i.getY() > 0){
			neighbors.add(getPointAt(i.getX(), i.getY() - 1));
		}
		if( i.getX() < DIM -1){
			neighbors.add(getPointAt(i.getX() + 1, i.getY()));
		}
		if( i.getY() < DIM -1){
			neighbors.add(getPointAt(i.getX(), i.getY() + 1));
		}
		return neighbors;
	}

	/**
	 * change the board after a stone is set
	 * @param i
	 * @param status
	 */
	public void changeBoardAfterMove(Index i, String status){ 
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		boolean white = (nowPlaying.equals(Status.WHITE) ? true : false);
		for( Index neighbor : getNeighbors(i)){
			boolean sameColorAsPlacedStone = false;
			boolean emptySpace = false;
			if(getStatus(neighbor) == getStatus(i)){
				sameColorAsPlacedStone = true;
			}
			if(getStatus(neighbor) == Status.NONE){
				emptySpace = true;
			}
			if(getStatus(neighbor) != Status.NONE && getStatus(neighbor) != nowPlaying){
				removeIfDeadStone(neighbor);
			}

			Set<Index> checkedNeighborStones = new HashSet<>();
			checkedNeighborStones.add(i);
			if(emptySpace && sameColorAsPlacedStone){
				stones.put(i, nowPlaying);
				GUI.addStone(i.getY(),i.getX(), white);
				
			}
			if(isHarakiri(i,nowPlaying)){
				stones.put(i, nowPlaying);
				GUI.addStone(i.getY(),i.getX(), white);
				
			}

		}
		Set<Index> checkedNeighborStones = new HashSet<>();
		if(isDead(i, checkedNeighborStones)){
			stones.put(i, Status.NONE);
			GUI.removeStone(i.getY(), i.getX());
		}
	}
	
	public void testChangeBoardAfterMove(Index i, String status){ 
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		for( Index neighbor : getNeighbors(i)){
			boolean sameColorAsPlacedStone = false;
			boolean emptySpace = false;
			if(getStatus(neighbor) == getStatus(i)){
				sameColorAsPlacedStone = true;
			}
			if(getStatus(neighbor) == Status.NONE){
				emptySpace = true;
			}
			if(getStatus(neighbor) != Status.NONE && getStatus(neighbor) != nowPlaying){
				testRemoveIfDeadStone(neighbor);
			}

			Set<Index> checkedNeighborStones = new HashSet<>();
			checkedNeighborStones.add(i);
			if(emptySpace && sameColorAsPlacedStone){
				stones.put(i, nowPlaying);
				
			}
			if(isHarakiri(i,nowPlaying)){
				stones.put(i, nowPlaying);
				
			}
		}
		Set<Index> checkedNeighborStones = new HashSet<>();
		if(isDead(i, checkedNeighborStones)){
			stones.put(i, Status.NONE);
		}
	}
	
	/**
	 * places the stone on the board
	 * @param i
	 */
	public void setStone(Index i, String status){
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		boolean white = (nowPlaying.equals(Status.WHITE) ? true : false);
		stones.put(i, nowPlaying);
		GUI.addStone(i.getY(),i.getX(), white);
		changeBoardAfterMove(i,status);
		savePositions();
	}
	
	public void testSetStone(Index i, String status){
		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
		stones.put(i, nowPlaying);
		testChangeBoardAfterMove(i,status);
		savePositions();
	}

//	public void tryStone(Index i, String status){
//		Status nowPlaying = (status.equals("black") ? Status.BLACK : Status.WHITE);
//		stones.put(i, nowPlaying);
//	}
	/**
	 * removes enclosed stones
	 * @param i
	 */
	public void removeIfDeadStone(Index i){
		Set<Index> checkedStones = new HashSet<>();
		if(isDead(i, checkedStones)){
			checkedStones.add(i);
			if (!checkedStones.isEmpty()){
				deadStone = true;
			}
			for(Index deadStones : checkedStones){
				stones.put(deadStones, Status.NONE);
				GUI.removeStone(deadStones.getY(), deadStones.getX());
			}

		}
	}
	
	public void testRemoveIfDeadStone(Index i){
		Set<Index> checkedStones = new HashSet<>();
		if(isDead(i, checkedStones)){
			checkedStones.add(i);
			if (!checkedStones.isEmpty()){
				deadStone = true;
			}
			for(Index deadStones : checkedStones){
				stones.put(deadStones, Status.NONE);
			}

		}
	}

	/**
	 * advises on placement (is it a good place?)
	 * @param i
	 * @return
	 */
	public boolean isPlacementNoGood(Index i, String status){
		HashMap <Index, Status> stonesCopy = deepCopy();
		Status stoneStatus = status.equals("black") ? Status.BLACK : Status.WHITE;
		//true is bad placement
		boolean noGood = false;
		stones.put(i, stoneStatus);

		for( Index neighbor : getNeighbors(i)){										//look at all the neighbors
			boolean sameColorAsPlacedStone = false;	
			if(getStatus(neighbor) == getStatus(i)){								//if status is the same
				sameColorAsPlacedStone = true;										//change boolean to true
			}
			Set<Index> checkedNeighborStones = new HashSet<>();						//checks neighbors of all the same colored stones
			if(sameColorAsPlacedStone && isDead(neighbor, checkedNeighborStones)){	//adds those stones to a set to check if their 
				checkedNeighborStones.add(neighbor);								//neighbors are dead
				noGood = true;														// if thats true this stone shouldn't be placed										//empties point again
			}	
		}
		stones = stonesCopy;	
		return noGood;

	}

}