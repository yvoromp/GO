package goGame;

public class Index {

	private int x;
	private int y;
	private int pos;

	public Index(int x, int y){
		this.x = x;
		this.y =y;
		this.pos = x +(y * Board.DIM);


	}
	public int getPos(){
		return pos;
	}

	public int getX(){
		return x;
	}

	public int getY(){
		return y;
	}

	public int hashCode() {
		return (x << 16) + y;
	}


}
