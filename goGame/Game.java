package goGame;

import java.util.HashMap;
import java.util.HashSet;

import Players.Player;
import Players.WebPlayer;

import goGame.Board.Status;
import Gui.GoGUIIntegrator;

public class Game extends Thread{
	
	public static final int NUMBER_PLAYERS =2;
	public int boardSize;
	public Board board;
	private HashMap	<String, Player> players;
	public Player player1;
	public Player player2;
	public int playerIndex;
	public static HashSet <String> oldGamePositions;
	public static int passedBefore = 0;
	public int passcounter;
	private GoGUIIntegrator GUI;
	
	/**
	 * creates a new game with a gui
	 * @param s0
	 * @param s1
	 * @param boardSize
	 */
	public Game(Player s0, Player s1, int boardSize, GoGUIIntegrator gui){ 
		this.boardSize = boardSize;
		GUI = gui;
		board = new Board(boardSize, gui);
		board.reset(boardSize, gui);
		oldGamePositions = new HashSet<String>();
		player1 = s0;
		player2 = s1;
		players = new HashMap <String, Player>();
		players.put(s0.getName(), s0);
		players.put(s1.getName(), s1);
		playerIndex = 0;
		passcounter = 0;
		GUI.startGUI();
		
	}
	/**
	 * creates a game without a gui
	 * also used in tests
	 * @param s0
	 * @param s1
	 * @param boardSize
	 */
	public Game(Player s0, Player s1, int boardSize){ 
		this.boardSize = boardSize;
		board = new Board(boardSize);
		board.testReset(boardSize);
		oldGamePositions = new HashSet<String>();
		player1 = s0;
		player2 = s1;
		players = new HashMap <String, Player>();
		players.put(s0.getName(), s0);
		players.put(s1.getName(), s1);
		playerIndex = 0;
		passcounter = 0;
		
	}
	
	public Game(){
		
	}
	public void changePlayerIndex(){
		playerIndex++;
		playerIndex = playerIndex % NUMBER_PLAYERS;
	}
	
	//prints the game situation
	public void update(){
		System.out.println("\n current game situation: \n\n" + board.toString() + "\n");
		board.savePositions();
		changePlayerIndex();
	}

	public int getPlayerIndex(){
		return this.playerIndex;
	}
	
	public Player getCurrentPlayer(){
		return (playerIndex == 0) ? player1 : player2;
	}
	
	public HashMap<String, Player> getPlayers(){
		return this.players;
	}	


}
