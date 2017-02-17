package Strategy;

import goGame.Board;
import goGame.Board.Status;

public interface Strategy {
	
	public String getName();
	
	public String determineMove(Board b, Status s);
	
	public boolean passOrPlay(Board b);

}
