package goGame;

import java.util.HashMap;
import java.util.HashSet;

import Players.Player;
import communication.Client;
import communication.ClientHandler.Key;
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
	 * creates a new game
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
	
	//starts the game and checks if you want a revanche
//	public void start() {
//		reset(boardSize);
//		play();
//		boolean rematch = rematch();
//		if(rematch){
//			board.gameEnded = false;
//			oldGamePositions = new HashSet<String>();
//			start();
//		}
//
//	}
	
	//checks the answer you type at the end of the game
//    private boolean rematch() {
//        boolean bAnswer = false;
//        System.out.print("\n do you want to play again? [y]");
//        Scanner in = new Scanner(System.in);
//            String answer = in.nextLine();
//            if(answer.equals("y")){
//            	bAnswer = true;
//            }
//            in.close();
//            return bAnswer;
//    }
        
        
        
	
	//resets the game
//	private void reset(int boardSize) {
//		playerIndex = 0;
//		board.reset(boardSize);
//		
//	}
	public void changePlayerIndex(){
		playerIndex++;
		playerIndex = playerIndex % NUMBER_PLAYERS;
	}
    
	//plays the game
//	public void play() {
//		update();
//		while(!this.board.gameOver()){
//			getCurrentPlayer().webMove(this.board);
//			//getCurrentPlayer().makeMove(this.board);
//			oldGamePositions.add(this.board.allPositions);
//			update();
//			playerIndex++;
//			playerIndex = playerIndex % NUMBER_PLAYERS;
//		}
//		printResult();
//		
//	}
	
	//prints the game situation
	public void update(){
		System.out.println("\n current game situation: \n\n" + board.toString() + "\n");
		board.savePositions();
		changePlayerIndex();
	}
	
	//prints the result of the played game
	public void printResult(){
		String score = board.totalScore();
		System.out.println(score);
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
//	public void moveMyStone(String Xpos, String yPos, Client client){
//	try{
//		int x = Integer.parseInt(Xpos);
//		int y = Integer.parseInt(yPos);
//		if(board.isValidMove(x, y)){
//			board.setStone(board.getPointAt(x, y), );
//			client.sendText(Key.MOVE + " " +x + " " +y);
//		}else{
//			client.print("invalid move man, please enter a valid move");
//		}
//	}catch (NumberFormatException e){
//		client.print("what are you doing? that shit ain't integers");
//	}
//}

}
