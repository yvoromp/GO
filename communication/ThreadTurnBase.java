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
				client.print("send: " + chosenCoordinates);
				client.print(client.game.board.toString() + "/n");
				client.sendText(chosenCoordinates);
				client.myTurn = !client.myTurn;
				try{
					Thread.sleep(3000);
				}catch( InterruptedException e){
					
				}
				
			}
			notify();
		}
	}

}
