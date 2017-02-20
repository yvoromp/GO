package Players;

import Strategy.FillBoardStrategy;
import Strategy.Strategy;
import goGame.Board;
import goGame.Board.Status;

public class AI extends Player{
	
	private Strategy strategy;
	
	public AI (Status s, Strategy strategy, int boardSize) {
		super(strategy.getName() + " - " + s.toString(), s, boardSize);
		this.strategy = strategy;
	}
	
	public boolean passOrPlay(Board b){
		return strategy.passOrPlay(b);	
	}
	public String webMove(Board board){
		return"not needed";
	}
	
	public AI (Status s, int boardSize){
		super("fill" + " - " + s.toString(), s, boardSize);
		this.strategy = new FillBoardStrategy();

	}
	
	public String determineMove(Board b){
		return strategy.determineMove(b, this.getStone());
	}

}
