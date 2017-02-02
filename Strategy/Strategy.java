package Strategy;

import goGame.Board;
import goGame.Index;
import goGame.Board.Status;

public interface Strategy {
	
	public String getName();
	
	public Index determineMove(Board b, Status s);
	
	public boolean passOrPlay(Board b);

}
