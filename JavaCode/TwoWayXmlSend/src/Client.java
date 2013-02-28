import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	public static void main(String[] args) throws IOException {

		// localhost for testing
		Socket sock = new Socket("localhost", 8000);
		System.out.println("Connecting...");

		// sending receiving class initiated
		sendrecsock = new SendReceiveSocket(sock);
		
		clientLoginProtocol(sock);

		String pathwaystart = "C:\\Users/Tom/TestDoc/";

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

		// get the message and sender from the received file and display
		XmlManip xmlmanip = new XmlManip();
		String returnedresult = xmlmanip.returnRequired(fromserverpath, "text");
		System.out.println("message is: " + returnedresult);
		String returnedsender = xmlmanip.returnRequired(fromserverpath, "sender");
		System.out.println("from user: " + returnedsender);

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

				// user inputs message
				// Scanner in = new Scanner(System.in);
				String s;
				System.out.println("Enter a message");
				s = in.nextLine();

				// user inputs receiver user id
				String r;
				System.out.println("Enter receiver user ID");
				r = in.nextLine();

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
				xmlwriter.WriteToFile(s, Integer.toString(currentUserID), r, toserverfilename);

				// send file
				sendrecsock.SendViaSocket(toserverpath);

				DataInputStream fromServer = new DataInputStream(sock.getInputStream());
				char success;
				success = fromServer.readChar();
				if (success == 'y') {
					System.out.println("Message sending successful");
				} else {
					System.out.println("Message sending unsuccessful. Please try again");
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
						//create encryption/decryption object
						
						if (encryptdecrypt == null ) {
							System.out.println("IT'S NULL");
							PUB_KEY_LOC = "client"+currentUserID+"/mypublickey.key";
							PRIV_KEY_LOC = "client"+currentUserID+"/myprivatekey.key";
							SERV_PUB_KEY_LOC = "client"+currentUserID+"/serverpublickey.key";
							encryptdecrypt = new EncryptDecrypt(PUB_KEY_LOC,PRIV_KEY_LOC);
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
						//create encryptoin/decryption object
						PUB_KEY_LOC = "client"+currentUserID+"/mypublickey.key";
						PRIV_KEY_LOC = "client"+currentUserID+"/myprivatekey.key";
						encryptdecrypt = new EncryptDecrypt(PUB_KEY_LOC,PRIV_KEY_LOC);
						//send public key to server
						SERV_PUB_KEY_LOC = "client"+currentUserID+"/serverpublickey.key";
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