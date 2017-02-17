package goGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

	
	

	public Board(int boardSize){
		DIM = boardSize;
		reset(DIM);
	}
	
	// method for changing  betweenplayers
	public void changePlayer(){
		isBlack = !isBlack;
	}
	
	//pass, inlcuding passrule
	public void pass(){
		if( !isBlack && blackPassed){
			System.out.println(" according to the pass-rule the game ended!");
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
	
//	public Status getStatus(int x, int y) {
//		return (stones.get(getPointAt(x, y)));
//	}

	/**
	 * checks if the index is empty
	 * @param i
	 * @return
	 */
	public boolean isEmpty(Index i){
		return (stones.get(i) == Status.NONE);
	}
	
	
	public Index getLastMove(){
		return lastMove;
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
	 * checks for suicide
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
	 * chekcs if a move is valid
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isValidMove(int x, int y){
		if ( x < 0 || y < 0 || x > DIM -1 || y > DIM - 1){
			return false;
		}
		
		if (stones.get(getPointAt(x,y)) != Status.NONE){
			System.out.println(" position1");
			return false;
		}
		Index stone = getPointAt(x,y);
		setStone(stone);
		if(getStatus(stone) == Status.NONE){
			stones.put(stone, Status.NONE);
			System.out.println(" position2");
			return false;
		}
		
		oldPositions = new HashSet <String>();
		String s = "";
		for (int i = 0; i < DIM; i++) {
			String visual = "";
			for (int j = 0; j < DIM; j++) {	
				visual = visual + stones.get(stone);	
			}
			s = s + visual ;
		}
		allPositions = s;

		for(String string : Game.oldGamePositions){
			if(string.contains(allPositions)){
				System.out.println("position has occured before, voilation of KO rule");
				allPositions = "";
				stones.put(stone, Status.NONE);
				return false;
			}
		}
		savePositions();
		stones.put(stone, Status.NONE);
		lastMove = stone;
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
	 * checks for liberties
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
		return "EINDSCORE: Black scores " + blackStoneCounter + " and White scores " + whiteStoneCounter;
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
	public void reset(int boardSize){
		stones = new HashMap<Index, Status>();
		DIM = boardSize;
		System.out.println(DIM + "   :  is the DIM value ");
		for (int i = 0; i < DIM; i++){
			for(int j = 0; j < DIM; j++){
				Index index = new Index(i , j);
				stones.put(index, Status.NONE);
				//System.out.println("The first index position has status:  " + stones.get(index));
				
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
	 * places the stone on the board
	 * @param i
	 */
	public void setStone(Index i){
		Status s;
		
		if(isBlack){
			s = Status.BLACK;
		}else{
			s = Status.WHITE;
		}
		stones.put(i, s);
		
		for( Index neighbor : getNeighbors(i)){
			boolean sameColorAsPlacedStone = false;
			if(getStatus(neighbor) == getStatus(i)){
				sameColorAsPlacedStone = true;
			}
//			if(rightStage){
//				removeIfDeadStone(neighbor);
//			}
			
			Set<Index> checkedNeighborStones = new HashSet<>();
			checkedNeighborStones.add(i);
			if(isDead(i, checkedNeighborStones)){
				stones.put(i, Status.NONE);
			}
			if(getStatus(neighbor) == Status.NONE && sameColorAsPlacedStone){
				//System.out.println("SS2");
				stones.put(i, Status.NONE);
			}
			if(isHarakiri(i,s)){
				//System.out.println("SS3");
				stones.put(i, Status.NONE);
			}
			

		}
		
	}
	
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
				}
				
			}
	}
	
	/**
	 * advises on placement (is it a good place?)
	 * @param i
	 * @return
	 */
	public boolean isPlacementNoGood(Index i){
		boolean noGood = false;
		Status s;
		if(isBlack){
			s = Status.BLACK;
		}else{
			s = Status.WHITE;
		}
		
		for( Index neighbor : getNeighbors(i)){										//look at all the neighbors
			boolean sameColorAsPlacedStone = false;	
			stones.put(i, s);
			if(getStatus(neighbor) == getStatus(i)){								//if status is the same
				sameColorAsPlacedStone = true;										//change boolean to true
			}
			Set<Index> checkedNeighborStones = new HashSet<>();						//checks neighbors of all the same colored stones
			if(sameColorAsPlacedStone && isDead(neighbor, checkedNeighborStones)){	//adds those stones to a set to check if their 
				checkedNeighborStones.add(neighbor);								//neighbors are dead
				noGood = true;														// if thats true this stone shouldn't be placed										//empties point again
			}	
		}
		stones.put(i, Status.NONE);	
		return noGood;
		
	}
	
}