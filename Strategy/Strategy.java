package Strategy;

import goGame.Board;
import goGame.Board.Status;

public interface Strategy {
	
	public String getName();
	
	
	public boolean passOrPlay(Board b);

}
