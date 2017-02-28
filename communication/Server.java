package communication;

import java.net.Socket;
import java.net.UnknownHostException;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import Players.Player;
import communication.ClientHandler.Key;
import goGame.Board.Status;
import goGame.Game;
import goGame.Go;

import java.util.ArrayList;


public class Server {
	
	public ArrayList<ClientHandler> threads;
	public HashMap <Integer, HashSet<String>> clientPref;
	public HashMap <String, Integer> waiting;
	public HashMap <ClientHandler, Game> clientInGame;
	public HashMap <Game, HashSet<ClientHandler>> allClientsInGame;
	
	private static int myPort = 8989;


	//tries to start a Server
	public static void main (String[] args){
		Server server = new Server(myPort);
		server.run();
	}
	

	//create a new server object
	public Server(int port){
		myPort = port;
		threads = new ArrayList<ClientHandler>();
		clientPref = new HashMap<Integer, HashSet<String>>();
		waiting = new HashMap<String, Integer>();
		clientInGame = new HashMap <ClientHandler, Game>();
		allClientsInGame = new HashMap <Game, HashSet<ClientHandler>>();

	}

	public void run(){
		try{
			InetAddress IPAddress = InetAddress.getLocalHost();
			System.out.println("server up and running and has IP adress: " + IPAddress.getHostAddress());
		}catch (UnknownHostException e){
			System.out.println("can't find the IP address!");
		}
		ServerSocket serverSocket = null;
		
		//tries to create a socket
		try{
			serverSocket = new ServerSocket(myPort);
		}catch(IOException e){
			System.out.println("could not create a socket on port " + myPort);
		}
		
		while(true){
			try{
				Socket socket = serverSocket.accept();
				System.out.println("new client connected!");
				ClientHandler clientHandler = new ClientHandler(this, socket);
				addClientHandler(clientHandler);
				System.out.println("new clientHandler-connection made");
				Thread clientHandlerThread = new Thread(clientHandler);
				clientHandlerThread.start();
				System.out.println("new thread started");
				
			}catch (IOException e){
				System.out.println("socket problem encountered!");
			}
		}

	}


	public void print(String text){
		System.out.println(text);
	}

	//sends message to all clients in the client array
	public synchronized void sendAll(String text){
		for (ClientHandler clientHandler : threads){
			clientHandler.sendCommandText(text);
		}
	}
	
	//send only to the two paired clients
	public synchronized void sendToPairedClients(String text, ClientHandler clientHandler, Server server){
		for(ClientHandler clientHandlerInGame : allClientsInGame.get(clientInGame.get(clientHandler))){
		System.out.println(text);
		clientHandlerInGame.sendCommandText(text);
		}
		
	}
	

	//adds a clientHandlerconnection to the threadList
	private void addClientHandler(ClientHandler clientHandler) {
		threads.add(clientHandler);
		if(threads.size() > 20){
			System.out.println("maximum amount of clients reached");
			clientHandler.sendCommandText(Key.CHAT + " maximum amount of clients reached");
			removeClientHandler(clientHandler);
		}
		System.out.println("clientHandler connection added");
	}

	//removes the Thread of the selected client
	public void removeClientHandler(ClientHandler clientHandler){
		threads.remove(clientHandler);
		System.out.println("clientHandler connection removed");
	}
	
	//adds a new game to the map	
	public void addNewGame(Game game, ClientHandler clientHandler1, ClientHandler clientHandler2){
		//players get coupled to a game 
		clientInGame.put(clientHandler1, game);
		clientInGame.put(clientHandler2, game);
		if(!allClientsInGame.containsKey(game)){
			allClientsInGame.put(game, new HashSet<>());
		}
		//add clientHandlers to the set, so the game has two players
		allClientsInGame.get(game).add(clientHandler1);
		allClientsInGame.get(game).add(clientHandler2);
	}
	
	//returns game of the clientHandler object
	public Game getGame(ClientHandler clientHandler){
		return clientInGame.get(clientHandler);
	}
	
	//removes the game by removing client from waitingList and the current game 
	public void removeGame(ClientHandler clientHandler){
		allClientsInGame.get(waiting.get(clientHandler)).remove(clientHandler);
		clientInGame.remove(clientHandler);
	}
	
	//stops the current game
	public void kickClient(ClientHandler clientHandler){
		clientInGame.get(clientHandler).board.gameOver();
		try{
			clientHandler.getClient().close();
		}catch(IOException e){
			print("encountered kick - problem with closing of client's socket");
		}
	} 
	
	//adds clientHandler to the waiting list
	public void addToWaitingList(ClientHandler clientHandler, Integer boardSize){
		print(clientHandler.getClientName());
		waiting.put(clientHandler.getClientName(), boardSize);
		if(!clientPref.containsKey(boardSize)){
			clientPref.put(boardSize, new HashSet<String>());
			print("client game preferences saved");
		}
		print("check5");
		clientPref.get(boardSize).add(clientHandler.getClientName());
		print(clientPref.get(boardSize).toString() +"    all values");
		if(clientPairBoardSize() != 0 ){
			int prefBoardSize = boardSize;
			startGoGame(prefBoardSize);
		}else{
			clientHandler.sendCommandText(Key.WAITING + " ");
		}
		print(Key.CHAT + " client added to waitinglist");
		
		
	}
	
	//removes client from set of the waitingList
	public void removeFromWaitingList(String clientName){
		clientPref.get(waiting.get(clientName)).remove(clientName);
		waiting.remove(clientName);
	}
	
	public boolean isClientPair(int boardSize){
		
		boolean isPair = false;
			if(clientPref.get(boardSize).size() > 1){ 
					isPair = true;
					}
			print(isPair +"   isclientpair");
			return isPair;
	}
	
	public int clientPairBoardSize(){
		int boardSize = 0;
		for(Integer i : clientPref.keySet()){
			if (clientPref.get(i).size() > 1){
				boardSize = i;
				print(boardSize + "   is larger than 1");
			}
			print(boardSize +"   isclientpair");
		}return boardSize;
	}
			
			
	public void startGoGame(int prefBoardSize){	
		print("clients paired a game is going to start!");
		HashSet<String> set = clientPref.get(prefBoardSize);
		String [] pairedClients = set.toArray(new String[set.size()]);
		
		ClientHandler clientHandler1 = null;
		ClientHandler clientHandler2 = null;
		System.out.println(pairedClients[0] + " playing with black");
		System.out.println(pairedClients[1] + " playing with white");
		Player player1 = Go.currentPlayer(pairedClients[0], Status.BLACK, prefBoardSize);
		Player player2 = Go.currentPlayer(pairedClients[1], Status.WHITE, prefBoardSize);
		Game game = new Game(player1, player2, prefBoardSize);
		for(ClientHandler clientHandler : threads){
			if(clientHandler.getClientName().equals(pairedClients[0])){
				clientHandler1 = clientHandler;
			}else if(clientHandler.getClientName().equals(pairedClients[1])){
				clientHandler2 = clientHandler;
			}
		}
		
		clientHandler1.sendCommandText(Key.CHAT + " hello");
		clientHandler1.sendCommandText(Key.READY + " " + "black" + " " + clientHandler2.getClientName() + " " + prefBoardSize);
		clientHandler2.sendCommandText(Key.CHAT + " hello2");
		clientHandler2.sendCommandText(Key.READY + " " + "white" + " " + clientHandler1.getClientName() + " " + prefBoardSize);
		addNewGame(game, clientHandler1, clientHandler2);
		sendToPairedClients(Key.CHAT + " u bent gepaired, uw spel begint!", clientHandler1, this);
		game.start();
		removeFromWaitingList(pairedClients[0]);
		removeFromWaitingList(pairedClients[1]);
		//sendAll(Key.CHAT + " " +"new game started between " + clientHandler1.getClientName() + " and " + clientHandler2.getClientName());

	}
}
