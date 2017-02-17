package communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class keyBoardInput {

	private String validKeyboardInput;
	BufferedReader k = new BufferedReader(new InputStreamReader(System.in));

	public boolean isKeyboardInput(Client client){
		boolean valid = false;
		try{
			valid = ((validKeyboardInput = k.readLine()) != null) ? true : false;
		}catch (IOException e){
			client.print("can't read the input");
		}
		return valid;

	}

	public void inputByKeyboard(Client client){
		try{
			while(isKeyboardInput(client)){
				client.sendText(validKeyboardInput);
			}

		}catch (IllegalArgumentException e){
			client.print("you entered nonsense");
		}	
	}
}
