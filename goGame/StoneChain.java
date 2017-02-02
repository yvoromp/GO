package goGame;

import java.util.ArrayList;
import goGame.Board.Status;

public class StoneChain {
	
	public ArrayList<Stone> certainStonesArr;
	public Status status;
	
	public StoneChain(Status status) {
		certainStonesArr = new ArrayList<>();
	}
	

	public int getLiberties() {
		int totalLiberties = 0;
		for (Stone s : certainStonesArr) {
			totalLiberties += s.liberties;
		}
		return totalLiberties;
	}
	
	public void addStoneToChain(Stone s){
		s.chain = this;
		certainStonesArr.add(s);
	}
	
	public StoneChain chainsStones(StoneChain stoneChain){
		StoneChain result = new StoneChain(stoneChain.status);
		for (Stone s : stoneChain.certainStonesArr){
			result.addStoneToChain(s);
		}
		return result;
	}

}
