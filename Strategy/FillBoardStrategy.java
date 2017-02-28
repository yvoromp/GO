package Strategy;


import communication.ClientHandler.Key;
import goGame.Board;
import goGame.Board.Status;


public class FillBoardStrategy implements Strategy{
	
	public String name = "fill";
	private String status;
	private int fastX;
	private int fastY;
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public String determineMove(Board b, Status s) {
		String statusToString = Status.BLACK.equals(s) ? "black" : "white";
		status = statusToString;
		String random = "";
		if(passOrPlay(b)){
			return Key.PASS + " " + " no good or valid moves";
		}
		int x = (int) (Math.floor(Math.random()*(Board.DIM)));
		int y = (int) (Math.floor(Math.random()* (Board.DIM)));
		boolean valid = (b.isValidMove(x,y,statusToString)); 
		//boolean valid = (b.isValidMove(x,y,statusToString) && !b.isPlacementNoGood(b.getPointAt(x, y),statusToString));
		random = "MOVE" + " " + x + " " + y;
		int fastCount=0;
		while(!valid) {
			x = (int) (Math.floor(Math.random()*(Board.DIM)));
			y = (int) (Math.floor(Math.random()* (Board.DIM)));
			valid = (b.isValidMove(x,y,statusToString));
			//valid = (b.isValidMove(x,y,statusToString) && !b.isPlacementNoGood(b.getPointAt(x, y),statusToString));
			random = "MOVE" + " " + x + " " + y;
			fastCount++;
			if(fastCount>250){
				return Key.PASS + " " + " no good or valid moves";
			}
		}
		return random;
		
	}
	
	@Override
	public boolean passOrPlay(Board b){
		//true is pass, false is play
		for(int i = 0; i < Board.DIM; i++){
			for(int j = 0; j < Board.DIM; j++){

				if(b.isValidMove(i, j,status)){
					return false;
//					if(b.isPlacementNoGood(b.getPointAt(i, j),status)){
//						b.stones.put(b.getPointAt(i,j), Status.NONE);
//					}else{
//						b.stones.put(b.getPointAt(i,j), Status.NONE);
//						fastX = i;
//						fastY = j;
//						return false;
//					}
				}
				
			}
		}
	return true;
	}
	 
}
