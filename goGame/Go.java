package goGame;

import java.util.Scanner;
import Players.HumanPlayer;
import Players.Player;
import Players.WebPlayer;
import goGame.Board.Status;



public class Go {
	
	public static void main(String[] args) {
		
		System.out.println("Enter your name, player1: ");
    	Scanner input = new Scanner(System.in);
    	String playerName1 = input.nextLine();
    	
    	System.out.println("Enter your name, player2: ");
    	String playerName2 = input.nextLine();
    	
    	System.out.println("Enter your boardsize [int]");
    	int boardSize = input.nextInt();


    	Player player1 = currentPlayer(playerName1, Status.BLACK, boardSize);
    	Player player2 = currentPlayer(playerName2, Status.WHITE, boardSize);
        Game game = new Game(player1, player2, boardSize);
        
        game.start();
        input.close();
        
    }
	
	/**
	 * creates new humanplayer player
	 * @param name
	 * @param color
	 * @param boardSize
	 * @return
	 */
	public static Player currentPlayer(String name, Status color, int boardSize) {
		return new HumanPlayer(name, color, boardSize);
	}
	
	/**
	 * creates new webPlayer
	 * @param name
	 * @param color
	 * @param boardSize
	 * @return
	 */
	public static Player newWebPlayer(String name, Status color, int boardSize) {
		return new WebPlayer(name, color, boardSize);
	}
}
