package goGame;

import goGame.Board.Status;


public class Stone {

	public Status status;
	public int liberties;
	public int x;
	public int y;
	public int index;
	

	/**
	 * creates a stone
	 * @param x
	 * @param y
	 * @param s
	 */
	public Stone(int x, int y, Status s){
		this.x = x;
		this.y = y;
		liberties = 4;
		if(x == 0){					//is the stone on the first column?
			liberties--;
		}
		if(y == 0){					//is the stone on the first row?
			liberties--;
		}
		if(x == Board.DIM -1){		//is the stone on the last column?
			liberties--;
		}
		if(y == Board.DIM -1){					//is the stone on the last row?
			liberties--;
		}
		index = ((y * Board.DIM)+x);
		this.status = s;
	}


}
