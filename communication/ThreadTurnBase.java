package communication;

import Players.Player;

public class ThreadTurnBase  extends Thread{

	protected Client client;

	@Override
	/**
	 * checks for turn of client and changes turn
	 */
	public void run(){
		synchronized(this){
			if(client.myTurn){
				Player myPlayer = client.game.getCurrentPlayer();
				String chosenCoordinates = myPlayer.determineMove(client.game.board);
				client.sendText(chosenCoordinates);
				client.myTurn = !client.myTurn;
				
			}
			notify();
		}
	}

}
