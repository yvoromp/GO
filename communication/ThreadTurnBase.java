package communication;

import Players.Player;

public class ThreadTurnBase  extends Thread{

	protected Client client;

	@Override
	public void run(){
		synchronized(this){
			client.print("ThreadTurnBase myTurn :" + client.myTurn);
			if(client.myTurn){
				client.print("ThreadTurnBase activated!");
				Player myPlayer = client.game.getCurrentPlayer();
				String chosenCoordinates = myPlayer.determineMove(client.game.board);
				client.sendText(chosenCoordinates);
				client.myTurn = !client.myTurn;
				
			}
			notify();
		}
	}

}
