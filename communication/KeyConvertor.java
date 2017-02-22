package communication;

import communication.ClientHandler.Key;
import goGame.Game;
import goGame.Board.Status;
import Players.Player;
//import Players.WebPlayer;
import Players.HumanPlayer;

public class KeyConvertor {
	
	
	public void keyReady(Client client, String status, String otherClientName, String boardSize){
		int validSize = 0;
		try{
			int size = Integer.parseInt(boardSize);
			validSize = size;
		}catch(NumberFormatException e){
			client.print("wrong dimension given by the server");
			client.connected = false;
		}
		client.print("Opponent found!");
		client.stoneStatus = status;
		client.print("You are playing with " + status);
		if(client.stoneStatus.equals("black")){
			Player player1 = new HumanPlayer(client.name, Status.BLACK, validSize);
			Player player2 = new HumanPlayer(otherClientName, Status.WHITE, validSize);
			client.player = player1;
			client.myTurn = true;
			client.print("init myTurn : " + client.myTurn);
			client.game = new Game(player1,player2,validSize);
		}else{
			Player player1 = new HumanPlayer(otherClientName, Status.BLACK, validSize);
			Player player2 = new HumanPlayer(client.name, Status.WHITE, validSize);
			client.player = player2;
			client.myTurn = false;
			client.print("init myTurn : " + client.myTurn);
			client.game = new Game(player1,player2,validSize);
		}
		client.game.start();
		client.gameStarted=true;
		//TODO: make a new method for applying thread
		ThreadTurnBase turnBase = new ThreadTurnBase();
		turnBase.client = client;
		turnBase.start();
		synchronized(turnBase){
			try{
				turnBase.wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		client.print("gamestarted: " + client.gameStarted);
		
	}


	public void keyGo(String boardSize, Server server, ClientHandler clientHandler){
		try{
			int size = Integer.parseInt(boardSize);
			if((size > 131 || size < 5 || size % 2 == 0)){
				server.sendAll(Key.WARNING + " " + "boardsize should be in range 5-131 and odd!");
			}else{
				server.addToWaitingList(clientHandler, size);
			}
		}catch(NumberFormatException e){
			clientHandler.sendCommandText(Key.WARNING + " " + "there's no integer there (wo)man!");
		}
	}
	
	public void keyValid(Client client, String status, String xPos, String yPos){
		client.print("client . stonestatus = " + client.stoneStatus);
		client.print("status = " + status);
		client.print("valid currentplayer : " + client.game.getCurrentPlayer().equals(client.player));
		client.print("valid client myturn :" + client.myTurn);
		int x = 0;
		int y = 0;
		try{
			x = Integer.parseInt(xPos);
			y = Integer.parseInt(yPos);
		}catch ( NumberFormatException e){
			client.print("wrong dimension given by the server");
			client.connected = false;
		}
		if(status.equals(client.stoneStatus)){
			client.game.board.setStone(client.game.board.getPointAt(x,y), client.stoneStatus);
			client.game.update();
			client.print("It's your opponents turn!");
			boolean myTurn = client.game.getCurrentPlayer().equals(client.player) ? true : false;
			client.print("valid client myturn :" + myTurn);
			//TODO make new method
			ThreadTurnBase turnBase = new ThreadTurnBase();
			turnBase.client = client;
			turnBase.start();
			synchronized(turnBase){
				try{
					turnBase.wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			client.print("myturn after valid is: " + myTurn );
			client.print("gamestarted after valid is: " + client.gameStarted);
		}else{
			client.game.board.setStone(client.game.board.getPointAt(x,y), status);
			client.game.update();
			client.print("opponent made a move, your turn!");
			client.myTurn = client.game.getCurrentPlayer().equals(client.player) ? true : false;
			client.print("valid client myturn :" + client.myTurn);
			//TODO: make new method
			ThreadTurnBase turnBase = new ThreadTurnBase();
			turnBase.client = client;
			turnBase.start();
			synchronized(turnBase){
				try{
					turnBase.wait();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
			client.print("gamestarted after valid is: " + client.gameStarted);
		}
		client.print("myturn after valid is: " + client.myTurn );
		
	}
	
	public void keyInvalid(Client client, String status){
		if(status.equals(client.stoneStatus)){
			client.print("nice job turd, bye bye!");
		}else{
			client.print("your opponent is a retard, he's gone");
		}
	}

	public void keyEnd(Client client, String scorePlayer1, String scorePlayer2){
		int score1 = 0;
		int score2 = 0;
		try{
			score1 = Integer.parseInt(scorePlayer1);
			score2 = Integer.parseInt(scorePlayer2);
		}catch(NumberFormatException e){
			client.print("score doesn't translate to numbers");
		}
		if(score1 < score2){
			client.print("white won: " + score1 + " : " + score2);
		}else if(score1 > score2){
			client.print("black won: " + score1 + " : " + score2);
		}else{
			client.print("there's a draw " + score1 + " : " + score2);
		}
	}
	
	
	public void keyExit(Server server, String name, ClientHandler clientHandler){
		server.sendAll(Key.WARNING + " " + name + "is not longer connected to the server");
		server.print(" caseEXIT shutdown");
		clientHandler.shutDown();
	}

	public void keyWaiting(String text, ClientHandler clientHandler){
		clientHandler.sendCommandText(text);
	}

	public void keyChat(String text, Server server){
		server.sendAll(Key.CHAT + " " + text);
	}

	public void keyCancel(String name, Server server, ClientHandler clientHandler){
		server.removeFromWaitingList(name);
		clientHandler.sendCommandText(Key.CHAT + " " + "you are no longer in the waitinglist, to start again type GO statement");
	}

	public void keyTableFlip(Status status, Server server, ClientHandler clientHandler){
		String stonestate = (status == Status.BLACK) ? "black" : "white";
		server.sendToPairedClients(Key.TABLEFLIPPED + " " + stonestate, clientHandler);
		server.sendToPairedClients(Key.END + " " + clientHandler.getClientName() + " lost!", clientHandler);
	}

	public void keyPass(Status status, int passCounter, Server server, ClientHandler clientHandler){
		if(status == Status.WHITE && passCounter > 0){
			server.sendToPairedClients(Key.END + " " + server.getGame(clientHandler).board.blackStoneCounter + " " + server.getGame(clientHandler).board.whiteStoneCounter, clientHandler);
		}else{
			clientHandler.passCounter++;
		}
		String stonestate2 = (status == Status.BLACK) ? "black" : "white";
		server.sendToPairedClients(Key.PASSED + " " + stonestate2, clientHandler);
	}
	
	public void keyPlayer(String name, Server server, ClientHandler clientHandler){
		clientHandler.name = name;
		server.print(name + " had joined the server");
		server.sendAll(Key.PLAYER + " " + name);
		
	}
	
	public void keyPassed(Client client, String statusOfPasser){
		if(!statusOfPasser.equals(client.stoneStatus)){
			client.myTurn= true;
			client.print("your opponent has passed, your turn!");
		}else{
			client.print("you passed this turn!");
			client.myTurn = false;
		}
	}
	
	
	public void keyMove(String xPos, String yPos, Status myStone, Server server, ClientHandler clientHandler){
		server.sendToPairedClients(Key.CHAT + " move detected !", clientHandler);
		clientHandler.passCounter = 0;
		String myStoneToString = (myStone.equals(Status.BLACK) ? "black" : "white");
		int playerIndex = server.getGame(clientHandler).getPlayerIndex();
		Status currentStone = (playerIndex == 0) ? Status.BLACK : Status.WHITE;
		int x = -1;
		int y = -1;
		server.print("playerindex:  " + playerIndex);
		server.print("currentStone:  " + currentStone.toString());
		server.print("myStone:  " + myStone);
		if(currentStone.equals(myStone)){
			String stoneToString = clientHandler.statusToString(myStone);
			try{
				int xInput = Integer.parseInt(xPos);
				x = xInput;
				int yInput = Integer.parseInt(yPos);
				y = yInput;
			}catch(NumberFormatException e){
				clientHandler.sendCommandText(Key.WARNING + " " + "there's no integer there (wo)man!");
				server.sendToPairedClients(Key.INVALID + " " + stoneToString + " invalid move!", clientHandler);
				server.kickClient(clientHandler);
				server.sendToPairedClients(Key.END + " " + clientHandler.getClientName() + " " + "lost!", clientHandler);
			}
			if(server.getGame(clientHandler).board.isValidMove(x, y, myStoneToString)){
				server.getGame(clientHandler).board.setStone(server.getGame(clientHandler).board.getPointAt(x, y), myStoneToString);
				server.getGame(clientHandler).update();
				server.sendToPairedClients(Key.VALID + " " + stoneToString + " " + x + " " + y, clientHandler);
				
				if (myStone == Status.BLACK){
					server.getGame(clientHandler).board.blackPassed = false;
				}
			}
			else{
				server.sendToPairedClients(Key.INVALID + " stone status doesn't meet the requirements", clientHandler);
				server.kickClient(clientHandler);
				server.sendToPairedClients(Key.END + " " + clientHandler.getClientName() + " lost!", clientHandler);
			}
		}
	}

}
