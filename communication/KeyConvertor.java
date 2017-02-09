package communication;

import communication.ClientHandler.Key;
import goGame.Board.Status;

public class KeyConvertor {


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
	
	public void keyPlayer(String name, Server server){
		server.print(name + " had joined the server");
		server.sendAll(Key.PLAYER + " " + name);
		
	}
	
	public void keyMove(String xPos, String yPos, Status playedStone, Server server, ClientHandler clientHandler){
		clientHandler.passCounter = 0;
		int playerIndex = server.getGame(clientHandler).getPlayerIndex();
		Status currentStone = (playerIndex == 0) ? Status.BLACK : Status.WHITE;
		int x = -1;
		int y = -1;
		if(currentStone.equals(playedStone)){
			try{
				int xInput = Integer.parseInt(xPos);
				x = xInput;
				int yInput = Integer.parseInt(yPos);
				y = yInput;
			}catch(NumberFormatException e){
				clientHandler.sendCommandText(Key.WARNING + " " + "there's no integer there (wo)man!");
				server.sendToPairedClients(Key.INVALID + " " + playedStone + " invalid move!", clientHandler);
				server.kickClient(clientHandler);
				server.sendToPairedClients(Key.END + " " + clientHandler.getClientName() + " " + "lost!", clientHandler);
			}
			if(server.getGame(clientHandler).board.isValidMove(x, y)){
				server.sendToPairedClients(Key.VALID + " " + playedStone + " " + x + " " + y, clientHandler);
				server.getGame(clientHandler).board.setStone(server.getGame(clientHandler).board.getPointAt(x, y));
				if (playedStone == Status.BLACK){
					server.getGame(clientHandler).board.blackPassed = false;
				}
				else{
					server.sendToPairedClients(Key.INVALID + " " + playedStone + " invalid move!", clientHandler);
					server.kickClient(clientHandler);
					server.sendToPairedClients(Key.END + " " + clientHandler.getClientName() + " lost!", clientHandler);
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
