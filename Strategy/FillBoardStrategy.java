package Strategy;


import goGame.Board;
import goGame.Index;
import goGame.Board.Status;


public class FillBoardStrategy implements Strategy{
	
	public String name = "fill";
	
	@Override
	public String getName(){
		return name;
	}
	
	@Override
	public Index determineMove(Board b, Status s) {
		int x = (int) (Math.floor(Math.random()*(Board.DIM)));
		int y = (int) (Math.floor(Math.random()* (Board.DIM)));
		boolean valid = (b.isValidMove(x,y) && !b.isPlacementNoGood(b.getPointAt(x, y)));
		while(!valid) {
			x = (int) (Math.floor(Math.random()*(Board.DIM)));
			y = (int) (Math.floor(Math.random()* (Board.DIM)));
			valid = (b.isValidMove(x,y) && !b.isPlacementNoGood(b.getPointAt(x, y)));
		}
		System.out.println("move" + " " + x + " " + y);
		return b.getPointAt(x,y);
		
	}
	
	@Override
	public boolean passOrPlay(Board b){
		
		for(int i = 0; i < Board.DIM; i++){
			for(int j = 0; j < Board.DIM; j++){

				if(b.isValidMove(i, j)){
					if(b.isPlacementNoGood(b.getPointAt(i, j))){
						b.stones.put(b.getPointAt(i,j), Status.NONE);
						return true;
					}else{
						b.stones.put(b.getPointAt(i,j), Status.NONE);
						return false;
					}
				}
				
			}
		}return true;
	}
	 
}