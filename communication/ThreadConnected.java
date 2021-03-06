package communication;

import java.net.Socket;

public class ThreadConnected extends Thread{

	protected Client client;
	protected Socket socket;
	protected boolean connectionMade;

	@Override
	/**
	 * loops until socket is connected
	 */
	public void run(){
		synchronized(this){
			while(!socket.isConnected()){
				connectionMade = false;
			}
			connectionMade = true;
			notify();
		}
	}
}

