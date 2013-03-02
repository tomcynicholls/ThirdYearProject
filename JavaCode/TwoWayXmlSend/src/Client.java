import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client {

	public static DataInputStream fromServer;
	public static DataOutputStream toServer;
	public static int currentUserID;
	public static EncryptDecrypt encryptdecrypt;
	public static String PUB_KEY_LOC;
	public static String PRIV_KEY_LOC;
	public static String SERV_PUB_KEY_LOC;
	public static SendReceiveSocket sendrecsock;
	public static String pathwaystart = "C:\\Users/Tom/TestDoc/";

	public static void main(String[] args) throws IOException {

		// localhost for testing
		Socket sock = new Socket("localhost", 8000);
		System.out.println("Connecting...");

		// sending receiving class initiated
		sendrecsock = new SendReceiveSocket(sock);

		clientLoginProtocol(sock);

		

		// create filepath to save file from server
		String fromserver = "fromserver ";
		Date fsdate = new Date();
		String fssdate = fsdate.toString();
		String fsrsdate = fssdate.replaceAll(":", " ");
		String fromserverfilename = fromserver.concat(fsrsdate);
		String fromserverfilepath = fromserverfilename.concat(".xml");
		String fromserverpath = pathwaystart.concat(fromserverfilepath);
		// final pathway holder is fromserverpath
		System.out.println("File will be saved at: " + fromserverpath);

		// receive file
		sendrecsock.ReceiveViaSocket(fromserverpath);
		System.out.println(fromserverpath + "received");
		sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
		System.out.println("ack sent");

		// get the message and sender from the received file and display
		XmlManip xmlmanip = new XmlManip();
		String returnedresult = xmlmanip.returnRequired(fromserverpath, "text");
		System.out.println("message is: " + returnedresult);
		String returnedsender = xmlmanip.returnRequired(fromserverpath, "sender");
		System.out.println("from user: " + returnedsender);

		String otherfilepath;
		char sendingfile;
		String otherfp;
		String[] recfromserv = new String[3];
		for (int i = 2; i < 5; i++) {
			sendingfile = fromServer.readChar();
			if (sendingfile == 'y') {
				otherfilepath = Integer.toString(i);
				otherfp = pathwaystart + otherfilepath + ".xml";
				sendrecsock.ReceiveViaSocket(otherfp);
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				recfromserv[i - 2] = otherfp;
			}
		}

		sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");

		/*
		 * for (int j = 0; j < 3; j++) { System.out.println("array pos is" +
		 * recfromserv[j] + "!!"); if (recfromserv[j] != null) {
		 * System.out.println("DO PART " + j); } }
		 */

		if (recfromserv[0] != null) {
			System.out.println("DO PART 1");
		
			//decrypt recfromserv[0] file
			//output - user string2 wants to register with you, accept?
			//if input n
			//send n to server
			//show message and exit
			//if input y
			//save string1 in client/string2/string2nonce
			
			// save currentuserID, requesteduserID, Timestamp in a file
			// send file to server

			// receive file1
			// send ack
			// receive file2
			// send ack
			// decrypt file2
			// take out first string
			// if it matches requested user name
			// take second string
			// if it is within a boundary of new timestamp
			// send y to server
			// decrypt file1 and save as 'reqesteduserID'+pub.key in key
			// folder
			// otherwise send n and exit

			// if it was y
			// send requesteduserID to server
			// create file with string2none, NonceB, currentuserID
			// encrypt and send to server
			
			
		}
		if (recfromserv[1] != null) {
			System.out.println("DO PART 2");
			
			//decrypt recfromserv[1] file
			//check string1 against own nonce for string3
			//add string3 to trusted
			//create file string2 (their nonce), currentuserID
			//encrypt with their key
			//send string3 to server
			//send encrypted file to server
			//receive ack
			
		}
		if (recfromserv[2] != null) {
			System.out.println("DO PART 3");
			
			//decrypt recfromserv[2] file
			//check string1 with own nonce for string2
			//add string2 to trusted
		}
		/*
		 * char sendingfile; sendingfile = fromServer.readChar();
		 * System.out.println("From server: " + sendingfile);
		 */

		Boolean sendmessage = true;

		while (sendmessage) {
			Scanner in = new Scanner(System.in);
			String sm;
			System.out.println("Do you wish to send a message? y/n");
			sm = in.nextLine();
			char c;

			if (sm.equals("y")) {
				System.out.println("YES");
				c = sm.charAt(0);
				toServer.writeChar(c);

				// user inputs receiver user id
				String r;
				System.out.println("Enter receiver user ID");
				r = in.nextLine();
				// System.out.println("receiving char from server");
				// char fs = fromServer.readChar();
				String keypath = pathwaystart + "keys/" + r + "pubkey.key";
				System.out.println("keypath is: " + keypath);
				File f = new File(keypath);
				if (!(f.exists())) {

					System.out.println("You do not have user " + r + "registered. Registration protocol will now be initiated");
					toServer.writeChar('n');

					// register

					// save currentuserID, requesteduserID, Timestamp in a file
					// send file to server

					// receive file1
					// send ack
					// receive file2
					// send ack
					// decrypt file2
					// take out first string
					// if it matches requested user name
					// take second string
					// if it is within a boundary of new timestamp
					// send y to server
					// decrypt file1 and save as 'reqesteduserID'+pub.key in key
					// folder
					// otherwise send n and exit

					// if it was y
					// send requesteduserID to server
					// create file with NonceA currentuserID (save nonce in requserID/mynonce
					// encrypt and send to server

				} else {
					toServer.writeChar('y');

					toServer.writeInt(Integer.parseInt(r));
					// user inputs message
					// Scanner in = new Scanner(System.in);
					String s;
					System.out.println("Enter a message");
					s = in.nextLine();

					// create message xml file writer
					XmlWriter xmlwriter = new XmlWriter();

					// save file to send in format: toserver date and time
					String toserver = "toserver ";
					Date date = new Date();
					String sdate = date.toString();
					String rsdate = sdate.replaceAll(":", " ");
					String toserverfilename = toserver.concat(rsdate);
					String toserverfilepath = toserverfilename.concat(".xml");
					String toserverpath = pathwaystart.concat(toserverfilepath);
					// final file path holder is toserverfilepath
					System.out.println("File to be sent saved locally at: " + toserverpath);

					// create xml file
					xmlwriter.WriteToFile(s, Integer.toString(currentUserID), r, "client" + Integer.toString(currentUserID) + "/" + toserverfilename);

					// TO DO - encrypt
					encryptdecrypt.encrypt(keypath, toserverpath, pathwaystart + "tosend.xml");

					// send file
					sendrecsock.SendViaSocket(pathwaystart + "tosend.xml");

					DataInputStream fromServer = new DataInputStream(sock.getInputStream());
					char success;
					success = fromServer.readChar();
					if (success == 'y') {
						System.out.println("Message sending successful");
					} else {
						System.out.println("Message sending unsuccessful. Please try again");
					}

				}

				// sock.close();

			} else {
				if (sm.equals("n")) {
					System.out.println("NO");
					c = sm.charAt(0);
					toServer.writeChar(c);
					sendmessage = false;
					break;
				} else {
					System.out.println("Please enter y or n");
				}
			}
		}

		sock.close();
	}

	public static void clientLoginProtocol(Socket sock) throws IOException {

		fromServer = new DataInputStream(sock.getInputStream());
		toServer = new DataOutputStream(sock.getOutputStream());

		Boolean finished = false;

		while (!finished) {

			String s;
			Scanner in = new Scanner(System.in);
			while (true) {
				System.out.println("Do you have a user ID? [y/n]");
				s = in.nextLine();
				char c;
				int userID;
				char qisUser;

				if (s.equals("y")) {
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
						System.out.println("LOG IN PROTOCOL FINISHED");
						currentUserID = userID;
						pathwaystart = pathwaystart + "client" + Integer.toString(currentUserID) + "/";
						// create encryption/decryption object

						if (encryptdecrypt == null) {
							System.out.println("IT'S NULL");
							PUB_KEY_LOC = pathwaystart + "keys/mypublickey.key";
							PRIV_KEY_LOC = pathwaystart + "keys/myprivatekey.key";
							SERV_PUB_KEY_LOC = pathwaystart + "keys/serverpublickey.key";
							encryptdecrypt = new EncryptDecrypt(PUB_KEY_LOC, PRIV_KEY_LOC);
						}

						finished = true;
						break;
					} else {
						System.out.println("Unsuccessful log-in - Please restart");
						// sock.close();
						finished = true;
						break;
					}
					// break;
				} else {
					if (s.equals("n")) {
						System.out.println("NO");
						c = s.charAt(0);
						toServer.writeChar(c);
						userID = fromServer.readInt();
						System.out.println("userID is: " + userID);
						currentUserID = userID;
						pathwaystart = pathwaystart + "client" + Integer.toString(currentUserID) + "/";
						// create encryptoin/decryption object
						PUB_KEY_LOC = pathwaystart + "keys/mypublickey.key";
						PRIV_KEY_LOC = pathwaystart + "keys/myprivatekey.key";
						encryptdecrypt = new EncryptDecrypt(PUB_KEY_LOC, PRIV_KEY_LOC);
						// send public key to server
						SERV_PUB_KEY_LOC = pathwaystart + "keys/serverpublickey.key";
						sendrecsock.SendViaSocket(PUB_KEY_LOC);
						sendrecsock.ReceiveViaSocket(SERV_PUB_KEY_LOC);

						break;
					} else {
						System.out.println("Please enter y or n");
					}
				}

			}
		}

		// fromServer.close();
		// toServer.close();
	}
}