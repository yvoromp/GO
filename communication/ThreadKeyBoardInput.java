package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ThreadKeyBoardInput extends Thread{

	private String validKeyboardInput;
	protected Client client;

	public void run(){
		inputByKeyboard(client);
	}
	
	/**
	 * checks if there is input
	 * @param client
	 * @return
	 */
	public boolean isKeyboardInput(Client client){
		BufferedReader k = new BufferedReader(new InputStreamReader(System.in));
		boolean valid = false;
		try{
			valid = ((validKeyboardInput = k.readLine()) != null) ? true : false;
		}catch (IOException e){
			client.print("can't read the input");
		}
		return valid;

	}

	/**
	 * sends keyboardinput to client output
	 * @param client
	 */
	public void inputByKeyboard(Client client){
		try{
			while(isKeyboardInput(client)){
				client.sendText(validKeyboardInput);
			}

		}catch (IllegalArgumentException e){
			client.print("you entered nonsense");
		}catch(NullPointerException e){
			client.sendText(validKeyboardInput);
		}
	}
}
