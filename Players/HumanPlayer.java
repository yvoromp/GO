package Players;

import goGame.Board.Status;

import java.util.Scanner;

import communication.ClientHandler.Key;
import goGame.Board;

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

		String prompt = "*" + getName() + getStone().toString() + " : give your move";
		String answer = readLine(prompt);
		String[] splited = answer.split(" ");
		x = Integer.parseInt(splited[0]);
		y = Integer.parseInt(splited[1]);

		boolean valid =  (board.isValidMove(x,y,status));
		while(!valid) {
			System.out.println("ERROR: position " + x + " , " + y + " is not a valid move");
			String prompt3 = "*" + getName() + getStone().toString() + " : make your move";
			String answer2 = readLine(prompt3);
			String[] splited2 = answer2.split(" ");
			x = Integer.parseInt(splited2[1]);
			y = Integer.parseInt(splited2[2]);
			valid = (board.isValidMove(x,y,status));

		}
		return (Key.MOVE + " "+ x + " " + y);
	}

	/**
	 * reads of int values until one that's not suitable is entered
	 * @param prompt
	 * @return
	 */
	public String readLine(String prompt){
		String value = "";
		boolean correctRead = false;
		Scanner line= new Scanner(System.in);
		do {
			System.out.print(prompt);
			try (Scanner scanLine = new Scanner(line.nextLine())) {
				if(scanLine.hasNextLine()) {
					correctRead = true;
					value = scanLine.nextLine();
				}

			}
		} while (!correctRead);
		return value;



	}


}
