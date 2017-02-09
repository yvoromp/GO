package communication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import communication.Client;
import communication.ClientHandler.Key;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class serverTest {

	private Client client1;
	private Client client2;
	InetAddress gameServerAddress = null;
	int port = 8989;

    @Before
    public void setUp() {
		try {
    	gameServerAddress = InetAddress.getByName("192.168.2.14");
    	}catch (UnknownHostException e){
			System.out.println(" IP address is invalid!");
    	}
    	try{
    		client1 = new Client("yvo", gameServerAddress , 8989);
    		client1.sendText(Key.PLAYER + " yvo");
    		client1.start();
    		client2 = new Client("Inger", gameServerAddress , 8989);
    		client2.sendText(Key.PLAYER + " inger");
    		client2.start();
    	}catch(IOException e){
    		System.out.println(" problem with init of client");
    	}
        
    }
	
	
	@Test
	public void testReadingWritingFiles() {
		//TODO: assertionerror 49
			client1.sendText("GO 5");
			client2.sendText("GO 5");
			try{
			Thread.sleep(1000);
			}catch (InterruptedException e){
				System.out.println("sleep problem");
			}
			assertEquals("black", client1.stoneStatus);
			assertFalse(client2.myTurn);
			

	}
}
