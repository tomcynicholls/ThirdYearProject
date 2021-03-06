package com.thirdyearproject.clientserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Server {

	public static EncryptDecrypt encryptdecrypt;
	//public static final String SERV_PUB_KEY_LOC = "C:\\Users/Tom/TestDoc/server/keys/servpubkey.key";
	//public static final String SERV_PRIV_KEY_LOC = "C:\\Users/Tom/TestDoc/server/keys/servprivkey.key";
	public static SendReceiveSocket sendrecsock;
	//public static String pathwaystart = "C:\\Users/Tom/TestDoc/server/";
	public static final String SERV_PUB_KEY_LOC = "server/keys/servpubkey.key";
	public static final String SERV_PRIV_KEY_LOC = "server/keys/servprivkey.key";
	public static String pathwaystart = "server/";

	public static void main(String[] args) throws IOException {

		// create socket
		ServerSocket servsock = new ServerSocket(8500);

		// create xml writer
		XmlWriter xmlwriter = new XmlWriter();

		// create encryption/decryption module
		encryptdecrypt = new EncryptDecrypt(SERV_PUB_KEY_LOC, SERV_PRIV_KEY_LOC);

		
		String nomessagefile = pathwaystart.concat("nomessagefile.xml");
		System.out.println(nomessagefile);
		File f = new File(nomessagefile);
		if (!(f.exists())) {
			xmlwriter.WriteToFile("no message", "server", "client", "server/nomessagefile");
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
			if (con.returnFirstMessagefromID(currentID, 4) == null) {
				con.updateUser(currentID, "messloc1", "nomessagefile.xml");
			}

			DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());
			DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());
			
			// get filepath from database
			String prefilepath = con.returnFirstMessagefromID(currentID, 4);
			String filepath = pathwaystart.concat(prefilepath);
			
			if (prefilepath.equals("nomessagefile.xml")){
				outputToClient.writeChar('n');
			} else {
				outputToClient.writeChar('y');
			// sendfile
			sendrecsock.SendViaSocket(filepath);
			System.out.println(filepath + "sent");
			sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));
			System.out.println("ack received");

			// System.out.println(filepath);
			// filepath =
			// pathwaystart.concat(con.returnFirstMessagefromID(currentID,3));
			// System.out.println(filepath);
			// sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/testserv.xml");
			// sendrecsock.SendViaSocket(filepath);
			}
			// send all possible files
			
			String[] fparray = new String[3];
			String holder;
			String test = "xxx.xml";

			for (int i = 0; i < 3; i++) {
				fparray[i] = con.returnFirstMessagefromID(currentID, i + 5);
				holder = con.returnFirstMessagefromID(currentID, i + 5);
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

			System.out.println("Sending ack");
			//sendrecsock.SendViaSocket(pathwaystart.concat("servack.txt"));

			/*
			 * for (int j = 0; j<3; j++) { if (fparray[j] != "xxx.xml") {
			 * System.out.println("DO " + j); } }
			 */

			if (!(fparray[0].equals("xxx.xml"))) {
				System.out.println("DO PART 1");
				
				
				// receive file from client
				sendrecsock.ReceiveViaSocket(pathwaystart+"received.txt");
				
				// take second string
				String fromfile;
				BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"received.txt"));
				fromfile = in.readLine();
				in.close();

				String[] splitted = fromfile.split(" ");
				
				// get key filename from db using second string
				String filename = con.returnFirstMessagefromID(Integer.parseInt(splitted[1]), 3);
				String pubkeytouse = con.returnFirstMessagefromID(Integer.parseInt(splitted[0]), 3);
				
				// encrypt and send
				//encryptdecrypt.encrypt(pubkeytouse,filename,pathwaystart+"tosend.key");
				sendrecsock.SendViaSocket(pathwaystart+filename);
				sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));
				
				// create file using string2 and string3
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart+"tosend.txt"));
				out.write(splitted[1] + " " + splitted[2]);
				out.close();
				
				// encrypt and send
				encryptdecrypt.encrypt(pathwaystart+pubkeytouse,pathwaystart+"tosend.txt", pathwaystart+"encryptedtosend.txt");
				sendrecsock.SendViaSocket(pathwaystart+"encryptedtosend.txt");
				sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));

				// receive y or n
				char rec = inputFromClient.readChar();
				
				// if y
				if (rec == 'y') {
					// receive int receiveruserID
					int receiveruserID = inputFromClient.readInt();
					// receive file from client
					sendrecsock.ReceiveViaSocket(pathwaystart.concat("messloc3for"+Integer.toString(receiveruserID)+".txt"));
					// save file in messloc3 user id receiveruserID
					con.updateUser(currentID, "messloc3","messloc3for"+Integer.toString(receiveruserID)+".txt");
					sendrecsock.SendViaSocket(pathwaystart+"servack.txt");
					
				} else {
					// if no show error and exit
					System.out.println("ERROR");
				}
				
				con.updateUser(currentID, "messloc2", "xxx.xml");
			}
			if (!(fparray[1].equals("xxx.xml"))) {
				System.out.println("DO PART 2");
				
				//receive int user
				int user = inputFromClient.readInt();
				//receive file
				sendrecsock.ReceiveViaSocket(pathwaystart.concat("messloc4for"+Integer.toString(user)+".txt"));
				//save file in messloc4 user db
				con.updateUser(currentID, "messloc4","messloc4for"+Integer.toString(user)+".txt");
				//send ack
				//sendrecsock.SendViaSocket(pathwaystart+"servack.txt");
				con.updateUser(currentID, "messloc3", "xxx.xml");
				
			}
			if (!(fparray[2].equals("xxx.xml"))) {
				System.out.println("DO PART 3");
				con.updateUser(currentID, "messloc4", "xxx.xml");
			}

			// message sent so replace with no message file association
			System.out.println("Doing it here");
			con.updateUser(currentID, "messloc1", "nomessagefile.xml");
			//con.updateUser(currentID, "messloc2", "xxx.xml");
			//con.updateUser(currentID, "messloc3", "xxx.xml");
			//con.updateUser(currentID, "messloc4", "xxx.xml");

			Boolean recmessage = true;
			// DataInputStream inputFromClient = new
			// DataInputStream(sock.getInputStream());

			while (recmessage) {
				char cont = inputFromClient.readChar();
				// System.out.println("sending to client");
				// outputToClient.write('y');
				if (cont == 'y') {
					System.out.println("YES");

					char rec = inputFromClient.readChar();

					if (rec == 'n') {
						// user doesn't have pub key so register
						System.out.println("Register user key initiated");

						// receive file from client
						sendrecsock.ReceiveViaSocket(pathwaystart+"received.txt");
						
						// take second string
						String fromfile;
						BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"received.txt"));
						fromfile = in.readLine();
						in.close();

						String[] splitted = fromfile.split(" ");
						
						// get key filename from db using second string
						String filename = con.returnFirstMessagefromID(Integer.parseInt(splitted[1]), 3);
						String pubkeytouse = con.returnFirstMessagefromID(Integer.parseInt(splitted[0]), 3);
						//System.out.println("pubkeytouse: " + pubkeytouse);
						String publoc = pathwaystart.concat(pubkeytouse);
						//System.out.println("publoc: " + publoc);
						System.out.println("filename :" + filename);
						// encrypt and send
						//System.out.println("publoc: " + publoc + "filename: " + (pathwaystart+filename) );
						//encryptdecrypt.encrypt(publoc,pathwaystart+filename,pathwaystart+"tosend.key");
						sendrecsock.SendViaSocket(pathwaystart+filename);
						sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));
						
						// create file using string2 and string3
						BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart+"tosend.txt"));
						out.write(splitted[1] + " " + splitted[2]);
						out.close();
						
						// encrypt and send
						encryptdecrypt.encrypt(publoc,pathwaystart+"tosend.txt", pathwaystart+"encryptedtosend.txt");
						sendrecsock.SendViaSocket(pathwaystart+"encryptedtosend.txt");
						sendrecsock.ReceiveViaSocket(pathwaystart.concat("servack.txt"));

						// receive y or n
						char rec2 = inputFromClient.readChar();
						
						// if y
						if (rec2 == 'y') {
							// receive int receiveruserID
							int receiveruserID = inputFromClient.readInt();
							// receive file from client
							sendrecsock.ReceiveViaSocket(pathwaystart.concat("messloc2for"+Integer.toString(receiveruserID)+".txt"));
							// save file in messloc2 user id receiveruserID
							con.updateUser(currentID, "messloc2","messloc2for"+Integer.toString(receiveruserID)+".txt");
							sendrecsock.SendViaSocket(pathwaystart+"encryptedtosend.txt");
							
						} else {
							// if no show error and exit
							System.out.println("ERROR");
						}

					} else {
						// user will send file
						// receive file
						// receive file start
						// create file name in format: from ip address date and
						// time

						int receiver = inputFromClient.readInt();

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

						

						if (con.userIDExists(receiver)) {
							con.updateUser(receiver, "messloc1", fromfilepath);
							outputToClient.writeChar('y');
						} else {
							System.out.println("Not registered!");

							outputToClient.writeChar('n');
						}
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
//HERE CHANGED
					String currentuserpubkeyloc = "keys/user" + Integer.toString(currentID) + "pubkey.key";
					sendrecsock.ReceiveViaSocket(pathwaystart + "server/" + currentuserpubkeyloc);
					sendrecsock.SendViaSocket(SERV_PUB_KEY_LOC);

					con.updateUser(currentID, "pubkeyloc", currentuserpubkeyloc);
					con.updateUser(currentID, "messloc1", "nomessagefile.xml");
					con.updateUser(currentID, "messloc2", "xxx.xml");
					con.updateUser(currentID, "messloc3", "xxx.xml");
					con.updateUser(currentID, "messloc4", "xxx.xml");

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
