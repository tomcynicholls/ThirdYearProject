import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {
	
	public static EncryptDecrypt encryptdecrypt;
	public static final String SERV_PUB_KEY_LOC = "server/servpubkey.key";
	public static final String SERV_PRIV_KEY_LOC = "server/servprivkey.key";
	public static SendReceiveSocket sendrecsock;

	public static void main(String[] args) throws IOException {

		// create socket
		ServerSocket servsock = new ServerSocket(8000);

		// create xml writer
		XmlWriter xmlwriter = new XmlWriter();
		
		//create encryption/decryption module
		encryptdecrypt = new EncryptDecrypt(SERV_PUB_KEY_LOC,SERV_PRIV_KEY_LOC);

		String pathwaystart = "C:\\Users/Tom/TestDoc/";
		String nomessagefile = pathwaystart.concat("nomessagefile.xml");
		File f = new File(nomessagefile);
		if (!(f.exists())) {
			xmlwriter.WriteToFile("no message", "server", "client", "nomessagefile");
		}

		DBManager con = new DBManager();
		System.out.println("Connection : " + con.doConnection());

		int currentID;

		// start server loop
		while (true) {
			System.out.println("Waiting...");

			// accept socket connection
			Socket sock = servsock.accept();
			System.out.println("Accepted connection : " + sock);

			// sending receiving class initiated
			sendrecsock = new SendReceiveSocket(sock);
			
			currentID = serverLoginProtocol(sock, con);

			// server gets clients ip address
			InetAddress inetAddress = sock.getInetAddress();
			String stringip = inetAddress.getHostAddress();

			// display ipaddress
			System.out.println("IP Address is: " + stringip);

			// if the user has no message (first time connecting) associate
			// nomessage file with client
			if (con.returnFirstMessagefromID(currentID,1) == null) {
				con.updateUser(currentID, "messloc1", "nomessagefile.xml");
			}

			// get filepath from database
			String filepath = pathwaystart.concat(con.returnFirstMessagefromID(currentID,4));
			// sendfile
			sendrecsock.SendViaSocket(filepath);
			System.out.println(filepath + "sent");
			sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));
			System.out.println("ack received");
			
			//System.out.println(filepath);
			//filepath = pathwaystart.concat(con.returnFirstMessagefromID(currentID,3));
			//System.out.println(filepath);
			//sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/testserv.xml");
			//sendrecsock.SendViaSocket(filepath);
			
			//send all possible files
			DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());
			DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());
			String[] fparray = new String[3];
			String holder;
			String test = "xxx.xml";
			
			for (int i = 0; i<3; i++) {
				//fparray[i] = con.returnFirstMessagefromID(currentID,i+5);
				holder = con.returnFirstMessagefromID(currentID,i+5);
				System.out.println("here " + holder + " here");
				if (holder.equals(test)) {
					outputToClient.writeChar('n');
					System.out.println("outputing n");
					
				} else {
					System.out.println("outputing y");
					outputToClient.writeChar('y');
					System.out.println(holder);
					sendrecsock.SendViaSocket(pathwaystart.concat(holder));
					sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));
				}
			}	
			
			sendrecsock.SendViaSocket(pathwaystart.concat("servack.txt"));
			
			/*if (con.returnFirstMessagefromID(currentID,2) == null) {
				System.out.println("SENDING N");
				outputToClient.writeChar('n');
				outputToClient.flush();
			} else {
				outputToClient.writeChar('y');
				outputToClient.flush();
				System.out.println("SENDING Y");
			}*/
			
			// message sent so replace with no message file association
			con.updateUser(currentID, "messloc1", "nomessagefile.xml");

			Boolean recmessage = true;
			//DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());

			while (recmessage) {
				char cont = inputFromClient.readChar();

				if (cont == 'y') {
					System.out.println("YES");
					// receive file
					// receive file start
					// create file name in format: from ip address date and time
					String from = "from ";
					Date date = new Date();
					String sdate = date.toString();
					String rsdate = sdate.replaceAll(":", " ");
					String fromip = from.concat(stringip);
					String filename = fromip.concat(rsdate);
					String fromfilepath = filename.concat(".xml");
					String path = pathwaystart.concat(fromfilepath);
					// final filepath holder is path
					System.out.println("Will save data from client/app at: " + path);

					// actually receive file
					sendrecsock.ReceiveViaSocket(path);

					// sock.close();

					// server reads ip address from file
					XmlManip xmlmanip = new XmlManip();
					String returnedresult = xmlmanip.returnRequired(path, "receiver");
					System.out.println("receiver is:" + returnedresult);

					// and saves filename in appropriate array position
					int recuser = Integer.parseInt(returnedresult);

					//DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());

					if (con.userIDExists(recuser)) {
						con.updateUser(recuser, "messloc1", fromfilepath);
						outputToClient.writeChar('y');
					} else {
						System.out.println("Not registered!");

						outputToClient.writeChar('n');
					}
				}

				else {
					if (cont == 'n') {
						System.out.println("NO");
						recmessage = false;
						break;

					} else {
						System.out.println("ERROR - NOT Y OR N");
					}
				}
			}

			sock.close();

		}
	}

	public static int serverLoginProtocol(Socket sock, DBManager con)
			throws IOException {

		InetAddress inetAddress = sock.getInetAddress();
		DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());
		DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());

		int currentID = -1;

		Boolean finished = false;

		while (!finished) {

			char isUser = inputFromClient.readChar();
			System.out.println("Is a user? " + isUser);
			String stringip;
			int userID = 0;
			char qisUser = 'n';
			boolean userCorrect;
			// if not user - create
			// if user - receive user id from client

			if (isUser == 'y') {
				System.out.println("YES");
				userID = inputFromClient.readInt();
				System.out.println("User ID is: " + userID);
				// server queries - respond to client
				userCorrect = con.userIDExists(userID);
				System.out.println("con.userIDExists result =" + userCorrect);
				if (userCorrect) {
					qisUser = 'y';
					currentID = userID;
					outputToClient.writeChar(qisUser);
					finished = true;
				} else {
					outputToClient.writeChar(qisUser);
					// sock.close();
					finished = true;
				}

			} else {
				if (isUser == 'n') {
					System.out.println("NO");
					stringip = inetAddress.getHostAddress();
					System.out.println("ip address is: " + stringip);
					// create new user in db and return user id
					userID = con.addNewUser(stringip);
					currentID = userID;
					outputToClient.writeInt(userID);
					
					String currentuserpubkeyloc = "server/user"+Integer.toString(currentID)+"pubkey.key";
					sendrecsock.ReceiveViaSocket(currentuserpubkeyloc);
					sendrecsock.SendViaSocket(SERV_PUB_KEY_LOC);
					
					con.updateUser(currentID, "pubkeyloc", currentuserpubkeyloc);
					
				} else {
					System.out.println("ERROR - NOT Y OR N");
				}
			}

		}
		// inputFromClient.close();
		// outputToClient.close();
		return currentID;
	}

}
