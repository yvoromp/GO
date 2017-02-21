package communication;


import java.net.Socket;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.io.IOException;
import java.net.UnknownHostException;		// Thrown to indicate that the IP address of a host could not be determined.

import Players.Player;
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
				System.out.println("port: " + port + " is not an integer");
				System.exit(0); //abnormal termination of the JVM
			}
		}


		// tries to make a new client object;
		try{
			String clientName = readText("fill in your name");
			Client client = new Client(clientName, gameServerAddress, port);
			client.sendText(Key.PLAYER + " " + clientName);
			keyBoardInput clientKeyBoardInput = new keyBoardInput();
			client.start();

			if(clientName.equals("yvo") || clientName.equals("human")){
				clientKeyBoardInput.inputByKeyboard(client);
			}else{
				String send =readText("");
				client.sendText(send);
			}

			while(true){
				try{
					while(client.socket.isConnected()){

						if(client.gameStarted){
							boolean myTurn = client.game.getCurrentPlayer().equals(client.player) ? true : false;
							if(myTurn){
								client.print("the while loop is activated");
								Player myPlayer = client.game.getCurrentPlayer();
								String chosenCoordinates = myPlayer.determineMove(client.game.board);
								client.print("send: " + chosenCoordinates);
								client.sendText(chosenCoordinates);
								myTurn = !myTurn;
							}

						}
					}

				}catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e){
			System.out.println("creating client object failed!");
			System.exit(0);
		}
	}

	protected String name;
	public String stoneStatus;
	public Player player;
	private Socket socket;
	private BufferedReader in;
	private BufferedWriter out;
	protected Game game;
	protected boolean connected;
	public boolean myTurn;
	protected boolean gameStarted = false;

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
				e.printStackTrace();
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
	public void print(String text){
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

	/**
	 * takes input form terminal and prints it or sends it to clientHandlerclass
	 * @param client
	 * @return
	 */


	/**
	 * reads the input given from the clientHandler or server and acts upon recieved input
	 */
	public void readServerInput(){
		String serverInput = "";
		KeyConvertor convert = new KeyConvertor();
		try{
			while((serverInput = (in.readLine())) != null){
				String[] splited = serverInput.split(" ");
				Key key = Key.valueOf(splited[0]);
				switch (key){

				case WAITING:
					print("Waiting on opponent.......");
					break;	
				case READY:
					convert.keyReady(this, splited[1], splited[2], splited[3]);
					break;
				case VALID:
					convert.keyValid(this, splited[1], splited[2], splited[3]);
					break;
				case INVALID:
					convert.keyInvalid(this, splited[1]);
					break;
				case END:
					convert.keyEnd(this, splited[1], splited[2]);
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
					convert.keyPassed(this, splited[1]);
					break;
				case PLAYER:
					name = splited[1];
					break;
				default:
					print(serverInput);
					break;
				}
			}
		}catch (IOException e){
			print("can't read the input from server");
		}catch (IllegalArgumentException e){
			e.printStackTrace();
		}
	}

}
