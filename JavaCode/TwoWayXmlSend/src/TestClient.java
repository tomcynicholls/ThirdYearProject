import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		
		Socket sock = new Socket("localhost",8000);
		System.out.println("Connecting...");

		DataInputStream fromServer = new DataInputStream(sock.getInputStream());
      	DataOutputStream toServer = new DataOutputStream(sock.getOutputStream());
      	
      	Boolean finished = false;
      	
      	while(!finished) {
      	
      	String s;
		Scanner in = new Scanner(System.in);
		while(true) {
  		System.out.println("Do you have a user ID? [y/n]");
  		s = in.nextLine();
  		char c;
  		int userID;
  		char qisUser;
		
  		if (s.equals("y")){
  			System.out.println("YES");
  			c = s.charAt(0);
  			toServer.writeChar(c);
  			
  			System.out.println("What is you user ID? ");
  			s = in.nextLine();
  			userID = Integer.parseInt(s);
  			toServer.writeInt(userID);
  			qisUser = fromServer.readChar();
  			System.out.println("Is user? " + qisUser);
  			if (qisUser == 'y') {
  				System.out.println("Successful log-in");
  				System.out.print("LOG IN PROTOCOL FINISHED");
  				finished = true;
  				break;
  			} else {
  				System.out.println("Unsuccessful log-in - Please restart");
  				//sock.close();
  				finished = true;
  				break;
  			}
  			//break;
  		}
  		else {
  			if (s.equals("n")) {
  				System.out.println("NO");
  				c = s.charAt(0);
  	  			toServer.writeChar(c);
  	  			userID = fromServer.readInt();
  	  			System.out.println("userID is: " + userID);
  				break;
  			}
  			else {
  				System.out.println("Please enter y or n");
  			}
  		}
  		
		}
      	}
      	
      	
		
	}

}
