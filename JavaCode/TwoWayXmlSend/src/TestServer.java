import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class TestServer {

	public static void main(String[] args) throws IOException {
		
		ServerSocket servsock = new ServerSocket(8000);
	
		System.out.println("Waiting...");

	      //accept socket connection
	      	Socket sock = servsock.accept();
	      	System.out.println("Accepted connection : " + sock);
	      	InetAddress inetAddress = sock.getInetAddress();
	      	
	      	DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());
	      	DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());
	      	
	      	DBManager con = new DBManager(); 
	    	System.out.println("Connection : " +con.doConnection());
	      	
	      	int currentID;
	      	
	      	Boolean finished = false;
		
		while (!finished) {
		     
			char isUser = inputFromClient.readChar();
			System.out.println("Is a user? " + isUser);
			String stringip;
			int userID = 0;
			char qisUser = 'n';
			boolean userCorrect;
		    //if not user - create  	
			//if user - receive user id from client
			
			if (isUser == 'y'){
	  			System.out.println("YES");
	  			userID = inputFromClient.readInt();
	  			System.out.println("User ID is: " + userID);
	  			//server queries - respond to client
	  			userCorrect = con.userIDExists(userID);
	  			System.out.println("con.userIDExists result =" + userCorrect);
	  			if (userCorrect) {
	  			qisUser = 'y';
	  			currentID = userID;
	  			outputToClient.writeChar(qisUser);
	  			finished = true;
	  			} else {
	  			outputToClient.writeChar(qisUser);
	  			sock.close();
	  			finished = true;
	  			}
	  		
	  		}
	  		else {
	  			if (isUser == 'n') {
	  				System.out.println("NO");
	  				stringip = inetAddress.getHostAddress();
		  			System.out.println("ip address is: ");
		  			//create new user in db and return user id
		  			userID = con.addNewUser(stringip);
		  			currentID = userID;
		  			outputToClient.writeInt(userID);
		  			
	  			}
	  			else {
	  				System.out.println("ERROR - NOT Y OR N");
	  			}
	  		}
		      	
		}
		
		
	}

}
