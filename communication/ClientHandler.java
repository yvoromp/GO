package communication;

import java.net.Socket;
import java.net.SocketException;

import communication.KeyConvertor;
import Players.Player;
import goGame.Board.Status;
import java.io.BufferedReader;		//reads from inputstream
import java.io.BufferedWriter; 		// writes to outputstream
import java.io.InputStreamReader; 	//reads bytes and decodes them into characters
import java.io.OutputStreamWriter; 	//Characters written to it are encoded into bytes
import java.io.IOException;
import java.util.HashMap;


public class ClientHandler extends Thread{

	protected String name;
	protected int boardSize;
	protected Server server;
	protected Socket socket;
	protected BufferedReader in;
	protected BufferedWriter out;
	protected int passCounter;
	private Status currentStone;
	private String validInput;

	public enum Key{PLAYER, END ,CHAT,INVALID,WARNING,GO,CANCEL,WAITING,READY,MOVE,VALID,PASS,PASSED,TABLEFLIP,TABLEFLIPPED, EXIT}

	/**
	 * try to create a new clientHandler object and init both streams
	 * @param server
	 * @param socket
	 * @throws IOException
	 */
	public ClientHandler(Server server, Socket socket) throws IOException{

		if ((server == null || socket == null)){
			System.out.println("Server and socket not connectable!");
			System.exit(0);
		}
		this.server = server;
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	/**
	 * reads name from terminal and notifies all clients
	 * @throws IOException
	 */
	public void announce() throws IOException{
		name = in.readLine().toLowerCase();
		server.sendAll(Key.PLAYER + " " + name);
	}


	public Socket getClient(){
		return socket;
	}

	public String getClientName(){
		return name;
	}
	
	public String statusToString(Status stone){
		String s = stone.equals(Status.BLACK) ? "black" : "white";
		return s;
	}



	/**
	 * sends commandtexts over a socket to the client
	 * @param text
	 */
	public void sendCommandText(String text){
		try{
			out.write(text);
			out.newLine();  //writes a /n at the end of all the text
			out.flush();
		}catch (IOException e){
			e.printStackTrace();
			server.print("check sendcommandtext problem");
			shutDown();
		}
	}

	public int getBoardSize(){
		return this.boardSize;
	}



	/**
	 * gives stonestate when a certain string is entered
	 * @param state
	 * @return
	 */
	public Status setStoneState(String state){
		if(state.equals("black")){
			return Status.BLACK;
		}else{
			return Status.WHITE;
		}
	}

	// terminates the connection
	public void shutDown(){
		System.out.println("connection terminated!");
		server.removeClientHandler(this);

	}

	public boolean isInput(){
		boolean valid = false;
		try{
			valid = ((validInput = in.readLine()) != null) ? true : false;
		}catch (IOException e){
			sendCommandText(Key.WARNING + " " + "there's nothing to read here!");
			shutDown();
		}
		return valid;

	}

	public void getCurrentStone(){
		currentStone = null;
		try{
			if(server.getGame(this) != null){
				HashMap <String, Player> playersInGame = server.getGame(this).getPlayers();
				currentStone = playersInGame.get(name).getStone();
			}	
		}catch(NullPointerException e){
			server.print(Key.END+ " " + "no game found!");
			shutDown();
		}
	}


	/**
	 * Reads strings of the stream of the socket-connection and writes the
	 * characters to the default output.
	 */
	public void run(){
		KeyConvertor check = new KeyConvertor();
		try{
			while(isInput()){
				getCurrentStone();

				String[] splited = validInput.split(" ");
				Key key = Key.valueOf(splited[0]);

				switch (key) {
				case GO:
					check.keyGo(splited[1],server, this);
					break;	
				case EXIT:
					check.keyExit(server, name, this);
					break;
				case WAITING:
					check.keyWaiting(validInput, this);
					break;
				case CHAT:
					check.keyChat(validInput, server);
					break;
				case CANCEL:
					check.keyCancel(name, server, this);
					break;
				case MOVE:
					server.sendToPairedClients(Key.CHAT + " Currentstone = " + currentStone, this,server);
					check.keyMove(splited[1], splited[2], currentStone, server, this);
					break;
				case TABLEFLIP:
					check.keyTableFlip(currentStone, server, this);
					break;
				case PASS:
					check.keyPass(currentStone, passCounter, server, this);
					break;
				case PLAYER:
					check.keyPlayer(splited[1], server, this);
					break;
				default:
					sendCommandText(Key.CHAT + " " + validInput);
					break;
				}
			}
		}catch(IllegalArgumentException e){
			sendCommandText(Key.WARNING + " " + "use the right keywords and arguments!");
			server.print("WARNING!! clientHandler.run shutdown");	
		}
		shutDown();
	}

}
