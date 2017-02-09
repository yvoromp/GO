package Players;

import goGame.Board.Status;

import java.util.Scanner;

import communication.ClientHandler.Key;
import goGame.Board;
import goGame.Index;
import Strategy.Strategy;
import Strategy.FillBoardStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WebPlayer extends Player{
	private int x;
	private int y;
	private Status status;
	private Strategy strategy;
		
		/**
		 * creates a human player instance
		 * @param name
		 * @param status
		 * @param boardSize
		 */
		public WebPlayer(String name, Status status, int boardSize){
			super(name, status, boardSize);
			this.strategy = new FillBoardStrategy();
			
		}
		
		public boolean passOrPlay(Board b){
			return false;
		}
		public Index determineMove(Board board){
			return board.getPointAt(x,y);
		}
		
		/**
		 * asks the human player where he/she likes to place his/her stone 
		 */
		public String webMove(Board board){
			String s = "nothing send from game";
			System.out.println( "*" + getName() +" playing with " + getStone().toString() + " it's your turn");
			String input = readInput();
			String[] splited = input.split(" "); 
			System.out.println("splitted" + splited[0]);
			if(splited[0].equals(Key.PASS)){
				System.out.println("you skipped your turn by passing");
				s = (Key.PASS + " " + this.status);
				return s;
			}else if(splited[0].equals(Key.TABLEFLIP)){
				System.out.println("you gave up!");
				s = (Key.TABLEFLIP + " " + this.status);
				return s;
			}else if(splited[0].equals(Key.MOVE)){
				try{
					x = Integer.parseInt(splited[1]);
					y = Integer.parseInt(splited[2]);
				}catch (NumberFormatException e){
					System.out.println("2nd and 3rd inputs should be integers");
				}
				if(board.isValidMove(Integer.parseInt(splited[1]), Integer.parseInt(splited[2]))){
					s = (Key.MOVE + " " + this.status);
					System.out.println("you made a move");
					this.x =Integer.parseInt(splited[1]);
					this.y = Integer.parseInt(splited[2]);
					return s;
				}else{
					s = ("your move is invalid, try again");
					return s;
				}

			}
			return s;
		}
		
		/**
		 * reads of int values until one that's not suitable is entered
		 * @return
		 */
		public String readInput(){
			String s = "";
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
				try{
					s = input.readLine();
				}catch (IOException e){
					System.out.println("No valid input has been entered");
				}
				return s;
		}

}
