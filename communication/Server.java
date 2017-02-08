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
import communication.Peer.Key;
import goGame.Board.Status;
import goGame.Game;
import goGame.Go;

import java.util.ArrayList;


public class Server {
	
	public ArrayList<Peer> threads;
	public HashMap <Integer, HashSet<String>> clientPref;
	public HashMap <String, Integer> waiting;
	public HashMap <Peer, Game> clientInGame;
	public HashMap <Game, HashSet<Peer>> allClientsInGame;
	
	private static int myPort = 8989;


	//tries to start a Server
	public static void main (String[] args){
		Server server = new Server(myPort);
		server.run();
	}
	

	//create a new server object
	public Server(int port){
		myPort = port;
		threads = new ArrayList<Peer>();
		clientPref = new HashMap<Integer, HashSet<String>>();
		waiting = new HashMap<String, Integer>();
		clientInGame = new HashMap <Peer, Game>();
		allClientsInGame = new HashMap <Game, HashSet<Peer>>();

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
				Peer peer = new Peer(this, socket);
				addPeer(peer);
				System.out.println("new peer-connection made");
				Thread peerThread = new Thread(peer);
				peerThread.start();
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
		for (Peer peer : threads){
			peer.sendCommandText(text);
		}
	}
	
	//send only to the two paired clients
	public synchronized void sendToPairedClients(String text, Peer peer){
		for(Peer peerInGame : allClientsInGame.get(clientInGame.get(peer))){
		System.out.println(text);
		peerInGame.sendCommandText(text);
		}
		
	}
	

	//adds a peerconnection to the threadList
	private void addPeer(Peer peer) {
		threads.add(peer);
		if(threads.size() > 20){
			System.out.println("maximum amount of clients reached");
			peer.sendCommandText(Key.CHAT + " maximum amount of clients reached");
			removePeer(peer);
		}
		System.out.println("peer connection added");
	}

	//removes the Thread of the selected client
	public void removePeer(Peer peer){
		threads.remove(peer);
		System.out.println("peer connection removed");
	}
	
	//adds a new game to the map	
	public void addNewGame(Game game, Peer peer1, Peer peer2){
		//players get coupled to a game 
		clientInGame.put(peer1, game);
		clientInGame.put(peer2, game);
		if(!allClientsInGame.containsKey(game)){
			allClientsInGame.put(game, new HashSet<>());
		}
		//add peers to the set, so the game has two players
		allClientsInGame.get(game).add(peer1);
		allClientsInGame.get(game).add(peer2);
	}
	
	//returns game of the peer object
	public Game getGame(Peer peer){
		return clientInGame.get(peer);
	}
	
	//removes the game by removing client from waitingList and the current game 
	public void removeGame(Peer peer){
		allClientsInGame.get(waiting.get(peer)).remove(peer);
		clientInGame.remove(peer);
	}
	
	//stops the current game
	public void kickClient(Peer peer){
		clientInGame.get(peer).board.gameOver();
		try{
			peer.getClient().close();
		}catch(IOException e){
			print("encountered kick - problem with closing of client's socket");
		}
	}
	
	//adds peer to the waiting list
	public void addToWaitingList(Peer peer, Integer boardSize){
		waiting.put(peer.getClientName(), boardSize);
		if(!clientPref.containsKey(boardSize)){
			clientPref.put(boardSize, new HashSet<String>());
			print("client game preferences saved");
		}
		print("check5");
		clientPref.get(boardSize).add(peer.getClientName());
		print(clientPref.get(boardSize).toString() +"    all values");
		if(clientPairBoardSize() != 0 ){
			int prefBoardSize = boardSize;
			startGoGame(prefBoardSize);
		}else{
			peer.sendCommandText(Key.WAITING + "");
		}
		print("client added to waitinglist");
		
		
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
		
		Peer peer1 = null;
		Peer peer2 = null;
		System.out.println(pairedClients[0] + " playing with black");
		System.out.println(pairedClients[1] + " playing with white");
		Player player1 = Go.newWebPlayer(pairedClients[0], Status.BLACK, prefBoardSize);
		Player player2 = Go.newWebPlayer(pairedClients[1], Status.WHITE, prefBoardSize);
		Game game = new Game(player1, player2, prefBoardSize);
		for(Peer peer : threads){
			if(peer.getClientName().equals(pairedClients[0])){
				peer1 = peer;
			}else if(peer.getClientName().equals(pairedClients[1])){
				peer2 = peer;
			}
		}
		
		
		peer1.sendCommandText(Key.READY + " " + "black" + " " + peer2.getClientName() + " " + prefBoardSize);
		peer2.sendCommandText(Key.READY + " " + "white" + " " + peer1.getClientName() + " " + prefBoardSize);
		addNewGame(game, peer1, peer2);
		sendAll(Key.CHAT + " " +"new game started between " + peer1.getClientName() + " and " + peer2.getClientName());
		game.start();
		removeFromWaitingList(pairedClients[0]);
		removeFromWaitingList(pairedClients[1]);


	}
}
