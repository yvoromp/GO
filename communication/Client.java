package communication;


import java.net.Socket;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;		// Thrown to indicate that the IP address of a host could not be determined.

import Players.AI;
import Players.Player;
import Players.WebPlayer;
import Strategy.FillBoardStrategy;
import communication.ClientHandler.Key;
import goGame.Game;
import goGame.Board.Status;

public class Client extends Thread{


	public static void main (String[] args){


		InetAddress gameServerAddress = null;
		int port = 0;	


		while (gameServerAddress == null){
			//check the IP- address
			try {
				gameServerAddress = InetAddress.getByName(readText("fill in the IP address")); //Determines the IP address of a host, given the host's name
			}catch (UnknownHostException e){
				System.out.println(" IP address is invalid!");
				System.exit(0); //abnormal termination of the JVM
			}
		}

		while(port == 0){
			//check the port
			try{
				port = Integer.parseInt(readText("fill in the portnumber"));
			}catch(NumberFormatException e){   // port is not of the numeric type
				print("port: " + port + " is not an integer");
				System.exit(0); //abnormal termination of the JVM
			}
		}


		// tries to make a new client object;
		try{
			String clientName = readText("fill in your name");
			Client client = new Client(clientName, gameServerAddress, port);
			client.sendText(Key.PLAYER + " " + clientName);
			client.start();

			do{
				client.inputByKeyboard(client);
			}while(true);

		}catch(IOException e){
			System.out.println("creating client object failed!");
			System.exit(0);
		}
	}

	private String name;
	public String stoneStatus;
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	private Game game;
	private boolean connected;
	public boolean myTurn;
	private String validKeyboardInput;

	/**
	 * creates a new client object
	 * @param name
	 * @param gameServerAddress
	 * @param port
	 * @throws IOException
	 */
	public Client(String name, InetAddress gameServerAddress, int port) throws IOException{
		this.name = name; 
		this.socket = new Socket(gameServerAddress, port);
		this.in = new BufferedReader(new InputStreamReader (this.socket.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter (this.socket.getOutputStream()));
		game = null;
		connected = true;
		myTurn = false;
	}
	/**
	 * takes a string and returns a status
	 * @param status
	 * @return
	 */
	public Status stringToStatus(String status){
		Status thisStatus = status.equals("black") ? Status.BLACK : Status.WHITE;
		return thisStatus;
	}

	/**
	 * reads the serverInput and outputs it
	 */
	public void run(){
		while(connected){
			try{
				readServerInput();
			}catch(NullPointerException e){
				print("can't read the server input");
			}
		}
	}

	/**
	 * send text to the clientHandlerclass
	 * @param text
	 */
	public void sendText(String text){
		try{
			out.write(text);
			out.newLine();
			out.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}


	//returns the name of the client
	public String getClientName(){
		return name;
	}

	/**
	 * prints string to system.out
	 * @param text
	 */
	public static void print(String text){
		System.out.println(text);
	}

	/**
	 * reads input from terminal 
	 * @param text
	 * @return
	 */
	public static String readText(String text){
		System.out.print(text); // string converts into bytes
		String answer = null;
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			answer = in.readLine();
		}catch (IOException e){

		}
		return (answer == null) ? "" : answer;
	}

	public boolean isKeyboardInput(){
		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		validKeyboardInput = "";
		boolean valid = false;
		try{
			valid = ((validKeyboardInput = k.readLine()) != null) ? true : false;
		}catch (IOException e){
			print("can't read the input");
		}
		return valid;

	}

	
	public void moveMyStone(String Xpos, String yPos, Client client){
		try{
			int x = Integer.parseInt(Xpos);
			int y = Integer.parseInt(yPos);
			if(client.game.board.isValidMove(x, y)){
				client.game.board.setStone(client.game.board.getPointAt(x, y));
				client.sendText(validKeyboardInput);
			}else{
				print("invalid move man, please enter a valid move");
			}
		}catch (NumberFormatException e){
			print("what are you doing? that shit ain't integers");
		}
	}


	/**
	 * takes input form terminal and prints it or sends it to clientHandlerclass
	 * @param client
	 * @return
	 */
	public void inputByKeyboard(Client client){
		try{
			while(isKeyboardInput()){
				String[] splited = validKeyboardInput.split(" ");
				Key key = Key.valueOf(splited[0]);

				switch(key){
				case MOVE:
					if (client.myTurn) {
						moveMyStone(splited[1], splited[2], client);
					}else{
						print("It's not your turn to move");
					}
					break;
				default:
					client.sendText(validKeyboardInput);
					break;
				}
			}
		}catch (IllegalArgumentException e){
			print("you entered nonsense");
		}	
	}

	/**
	 * reads the input given from the clientHandler or server and acts upon recieved input
	 */
	public void readServerInput(){
		String serverInput = "";
		try{
			while(in.readLine() != null){
				serverInput = in.readLine();
				String[] splited = serverInput.split(" ");
				Key key = Key.valueOf(splited[0]);
				switch (key){

				case WAITING:
					print("Waiting on opponent.......");
					break;
				case READY:
					print("Opponent found");
					String status1 = splited[1];
					myTurn = status1.equals("black");
					stoneStatus = status1;
					Status p1 = stringToStatus(status1);
					Status p2 = (p1 == Status.BLACK) ? Status.WHITE : Status.BLACK;
					Player player1 = new WebPlayer(name, p1, Integer.parseInt(splited[3]));
					Player player2 = new WebPlayer(splited[2], p2, Integer.parseInt(splited[3]));
					game = new Game(player1,player2,Integer.parseInt(splited[3]));
					game.start();
					break;
				case CHAT:
					print(serverInput);
					break;
				case VALID:
					String status = splited[1];
					if(status.equals(stoneStatus)){
						myTurn = false;
					}else{
						print("opponent made a move, your turn!");
						int x = Integer.parseInt(splited[2]);
						int y = Integer.parseInt(splited[3]);
						game.board.setStone(game.board.getPointAt(x,y));
						myTurn = true;
						break;
					}
					break;
				case INVALID:
					String statusToBeKicked = splited[1];
					if(statusToBeKicked.equals(stoneStatus)){
						print("nice job turd, bye bye!");
					}else{
						print("your opponent is a retard, he's gone");
					}
					break;
				case WARNING:
					print(serverInput);
					break;
				case END:
					int score1 = Integer.parseInt(splited[1]);
					int score2 = Integer.parseInt(splited[2]);
					if(score1 < score2){
						print("white won: " + score1 + " : " + score2);
					}else if(score1 < score2){
						print("black won: " + score1 + " : " + score2);
					}else{
						print("there's a draw " + score1 + " : " + score2);
					}
					break;
				case TABLEFLIPPED:
					print("your opponent flipped the board!");
					break;
				case EXIT:
					print("bye bye, playing time is over!");
					in.close();
					out.close();	
					socket.close();
					connected =false;
					break;
				case PASSED:
					String passer= splited[1];
					if(!passer.equals(stoneStatus)){
						myTurn= true;
						print("your opponent has passed, your turn!");
					}else{
						print("you passed this turn!");
						myTurn = false;
					}
				case PLAYER:
					break;
				default:
					print(serverInput);
					break;
				}
			}
		}catch (IOException e){
			print("can't read the input from server");
		}catch (IllegalArgumentException e){
			print("wrong use of keywords by the server");
		}
	}

}
