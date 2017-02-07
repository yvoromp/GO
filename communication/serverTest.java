package communication;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import communication.Server;
import communication.Client;

import static org.junit.Assert.assertTrue;


public class serverTest {

	private Client client;
	private Server server;
	InetAddress gameServerAddress = null;
	int port = 8989;
	
	private BufferedReader in;
	private BufferedWriter out;
	private Socket socket;

    @Before
    public void setUp() {
//TODO: mulittreading of server and client or else serversoket.accept wont work  	
    	Server.main(new String[] {" "});
    	
    	try{
    	this.in = new BufferedReader(new InputStreamReader (this.socket.getInputStream()));
		this.out = new BufferedWriter(new OutputStreamWriter (this.socket.getOutputStream()));
    	}catch( IOException e){
    		System.out.println(" problem with init of in/out");
    	}
		try {
    	gameServerAddress = InetAddress.getByName("10.30.18.62");
    	}catch (UnknownHostException e){
			System.out.println(" IP address is invalid!");
    	}
    	try{
    		client = new Client("yvo", gameServerAddress , 8989);
    		client.start();
    	}catch(IOException e){
    		System.out.println(" problem with init of client");
    	}
        
    }
	
	
	@Test
	public void testReadingWritingFiles() {
			client.sendText("GO 5");
			try{
				Thread.sleep(5000);
			}catch (InterruptedException e){
				e.printStackTrace();
			}
			
			assertTrue(server.clientPref.containsKey(5));

	}
}
