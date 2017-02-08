package communication;

import java.net.Socket;
import Players.Player;
import goGame.Board.Status;
import java.io.BufferedReader;		//reads from inputstream
import java.io.BufferedWriter; 		// writes to outputstream
import java.io.InputStreamReader; 	//reads bytes and decodes them into characters
import java.io.OutputStreamWriter; 	//Characters written to it are encoded into bytes
import java.io.IOException;
import java.util.HashMap;


public class Peer extends Thread{

	protected String name;
	protected int boardSize;
	protected Server server;
	protected Socket socket;
	protected BufferedReader in;
	protected BufferedWriter out;
	private int passCounter;

	public enum Key{PLAYER, END ,CHAT,INVALID,WARNING,GO,CANCEL,WAITING,READY,MOVE,VALID,PASS,PASSED,TABLEFLIP,TABLEFLIPPED, EXIT}

	/**
	 * try to create a new peer object and init both streams
	 * @param server
	 * @param socket
	 * @throws IOException
	 */
	public Peer(Server server, Socket socket) throws IOException{

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



	/**
	 * sends commandtexts over a socket to the peer
	 * @param text
	 */
	public void sendCommandText(String text){
		try{
			out.write(text);
			out.newLine();  //writes a /n at the end of all the text
			out.flush();
		}catch (IOException e){
			e.printStackTrace();
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
		server.removePeer(this);

	}

	/**
	 * Reads strings of the stream of the socket-connection and writes the
	 * characters to the default output.
	 */
	public void run(){
		String text = "";
		try{
			Status currentStoneStatus = null;
			try{
				if(server.getGame(this) != null){
					HashMap <String, Player> playersInGame = server.getGame(this).getPlayers();
					currentStoneStatus = playersInGame.get(name).getStone();
				}	
			}catch(NullPointerException e){
				server.print("no game found!");
			}
			String[] splited = text.split(" ");
			Key key = Key.valueOf(splited[0]);
			switch (key) {
			case GO:					
				try{
					int boardSize = Integer.parseInt(splited [1]);
					if((boardSize > 131 || boardSize < 5 || boardSize % 2 == 0)){
						server.sendAll(Key.WARNING + " " + "boardsize should be in range 5-131 and odd!");
					}else{
						server.addToWaitingList(this, boardSize);
					}
					break;
				}catch(NumberFormatException e){
					sendCommandText(Key.WARNING + " " + "there's no integer there (wo)man!");
				}break;	
			case EXIT:
				server.sendAll(Key.WARNING + " " + name + "is not longer connected to the server");
				shutDown();
				break;
			case WAITING:
				sendCommandText(text);
				break;
			case CHAT:
				server.sendAll(text);
				break;
			case CANCEL:
				server.removeFromWaitingList(name);
				sendCommandText(Key.CHAT + " " + "you are no longer in the waitinglist, to start again type GO statement");
				break;
			case MOVE:
				passCounter = 0;
				int playerIndex = server.getGame(this).getPlayerIndex();
				Status stoneStatus = (playerIndex == 0) ? Status.BLACK : Status.WHITE;
				int x = -1;
				int y = -1;
				if(stoneStatus.equals(currentStoneStatus)){
					try{
						int xInput = Integer.parseInt(splited[1]);
						x = xInput;
						int yInput = Integer.parseInt(splited[2]);
						y = yInput;
					}catch(NumberFormatException e){
						sendCommandText(Key.WARNING + " " + "there's no integer there (wo)man!");
						server.sendToPairedClients(Key.INVALID + " " + currentStoneStatus + " invalid move!", this);
						server.kickClient(this);
						server.sendToPairedClients(Key.END + " " + this.getClientName() + " " + "lost!", this);
						break;
					}
					if(server.getGame(this).board.isValidMove(x, y)){
						server.sendToPairedClients(Key.VALID + " " + currentStoneStatus + " " + x + " " + y, this);
						server.getGame(this).board.setStone(server.getGame(this).board.getPointAt(x, y));
						if (currentStoneStatus == Status.BLACK){
							server.getGame(this).board.blackPassed = false;
						}
					}else{
						server.sendToPairedClients(Key.INVALID + " " + currentStoneStatus + " invalid move!", this);
						server.kickClient(this);
						server.sendToPairedClients(Key.END + " " + this.getClientName() + " lost!", this);
						break;
					}
				}else{
					server.sendToPairedClients(Key.INVALID + " stone status doesn't meet the requirements", this);
					server.kickClient(this);
					server.sendToPairedClients(Key.END + " " + this.getClientName() + " lost!", this);
					break;
				}
				break;		
			case TABLEFLIP:
				String stonestate = (currentStoneStatus == Status.BLACK) ? "black" : "white";
				server.sendToPairedClients(Key.TABLEFLIPPED + " " + stonestate, this);
				server.sendToPairedClients(Key.END + " " + this.getClientName() + " lost!", this);
				break;

			case PASS:
				passCounter = (currentStoneStatus == Status.BLACK) ? 1 : 0;
				if(currentStoneStatus == Status.WHITE && passCounter > 0){
					server.sendToPairedClients(Key.END + " " + server.getGame(this).board.blackStoneCounter + " " + server.getGame(this).board.whiteStoneCounter, this);
					break;
				}
				String stonestate2 = (currentStoneStatus == Status.BLACK) ? "black" : "white";
				server.sendToPairedClients(Key.PASSED + " " + stonestate2, this);
				if(server.getGame(this).board.blackPassed){
					server.sendToPairedClients(Key.END + " " + server.getGame(this).board.blackStoneCounter + " " + server.getGame(this).board.whiteStoneCounter, this);
				}break;
			case PLAYER:
				name = splited [1];
				server.sendAll(Key.PLAYER + " " + name);
			default:
				sendCommandText(text);
				break;
			}

		}catch(IllegalArgumentException e){
			sendCommandText(Key.WARNING + " " + "use the right keywords and arguments!");
		}
		shutDown();
	}



}
