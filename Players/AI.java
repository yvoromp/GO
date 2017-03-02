package Players;

import Strategy.FillBoardStrategy;
import Strategy.Strategy;
import goGame.Board;
import goGame.Board.Status;

public class AI extends Player{
	
	private Strategy strategy;
	private String status;
	
	public AI (String name, Status s, int boardSize) {
		super(name,s,boardSize);
		strategy = new FillBoardStrategy();
		this.status = (s.equals(Status.BLACK) ? "black" : "white");
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
