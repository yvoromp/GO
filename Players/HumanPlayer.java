package Players;

import goGame.Board.Status;

import java.util.Scanner;

import communication.ClientHandler.Key;
import goGame.Board;
import goGame.Index;

public class HumanPlayer extends Player{
	
	private String status;
	
	/**
	 * creates a human player instance
	 * @param name
	 * @param status
	 * @param boardSize
	 */
	public HumanPlayer(String name, Status status, int boardSize){
		super(name, status, boardSize);
		this.status = (status.equals(Status.BLACK) ? "black" : "white");
	}
	
	/**
	 * checks for pass
	 */
	public boolean passOrPlay(Board b){
		boolean pass = false;
		System.out.println("*" + getName() +" playing with " + getStone().toString() + " : do you want to pass press [p]?");
		Scanner passScanner= new Scanner(System.in);
		String input = passScanner.nextLine();
		if(input.equals("p")){
			System.out.println("pass");
			pass = true;
		}
		return pass;
	}
	
	
	public String webMove(Board board){
		return "not used here";
	}
	
	/**
	 * asks the human player where he/she likes to place his/her stone 
	 */
	public String determineMove(Board board){
		int x;
		int y;
		
		String prompt = "*" + getName() +" playing with " + getStone().toString() + " : select x position";
		int positionX = readInt(prompt);
		x = positionX;
		String prompt2 = "*" + getName() +" playing with " + getStone().toString() + " : select y position";
		int positionY = readInt(prompt2);
		y = positionY;
		
		boolean valid =  (board.isValidMove(x,y,status));
		while(!valid) {
			System.out.println("ERROR: position " + positionX + " , " + positionY + " is not a valid move");
			String prompt3 = "*" + getName() +" playing with " + getStone().toString() + " : select x position";
			int positionX2 = readInt(prompt3);
			x = positionX2;
			String prompt4 = "*" + getName() +" playing with " + getStone().toString() + " : select y position";
			int positionY2 = readInt(prompt4);
			y = positionY2;
			valid = (board.isValidMove(x,y,status));
			
			}
		return (Key.MOVE + " "+ x + " " + y);
	}
	
	/**
	 * reads of int values until one that's not suitable is entered
	 * @param prompt
	 * @return
	 */
	public int readInt(String prompt){
		int value = 0;
		boolean correctRead = false;
		Scanner line= new Scanner(System.in);
		do {
			System.out.print(prompt);
			try (Scanner scanLine = new Scanner(line.nextLine())) {
				if(scanLine.hasNextInt()) {
					correctRead = true;
					value = scanLine.nextInt();
				}
					
			}
		} while (!correctRead);
		return value;
		
		
		
	}


}
