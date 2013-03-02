package com.thirdyearproject.clientserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
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
			String decrypted = pathwaystart + "decrypted.txt";
			System.out.println("Unencrypted file put: " + decrypted);
			encryptdecrypt.decrypt(recfromserv[0],decrypted);
			//output - user string2 wants to register with you, accept?
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(decrypted));
			fromfile = in.readLine();
			in.close();
			String[] splitted = fromfile.split(" ");
			int otheruser = Integer.parseInt(splitted[1]);
			//System.out.println("User " + otheruser + " wants to register with you, accept? y/n");
			//String s;
			//Scanner input = new Scanner(System.in);
			//s = input.nextLine();
			//if input n
			//if (s.equals("n")) {
				//send n to server
				//show message and exit
				//toServer.writeChar('n');
			//} else { //if input y
				
				//save string1 in client/string2/string2nonce
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "nonce.txt"));
				out.write(splitted[0]);
				out.close();
				
				// save currentuserID, requesteduserID, Timestamp in a file
				BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "response1.txt"));
				Long timestamp = System.currentTimeMillis();
				String thetime = timestamp.toString();
				String thefinal = currentUserID + " " + otheruser + " " + thetime;
				out2.write(thefinal);
				out2.close();
				
				// send file to server
				sendrecsock.SendViaSocket(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "response1.txt");
				
				// receive file1
				sendrecsock.ReceiveViaSocket(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file1.txt");
				// send ack
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				// receive file2
				sendrecsock.ReceiveViaSocket(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file2.txt");
				// send ack
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				// decrypt file2
				String filedecrypted = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "decryptedfile2.txt";
				encryptdecrypt.decrypt(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file2.txt", filedecrypted);
				// take out first string
				String fromfiledec;
				BufferedReader in2 = new BufferedReader(new FileReader(filedecrypted));
				fromfiledec = in2.readLine();
				in.close();
				String[] splitteddec = fromfiledec.split(" ");
				boolean ok = false;
				String savekey = null;
				
				if (Integer.parseInt(splitteddec[0]) == otheruser) {
					// if it matches requested user name
					// take second string
					Long difference = System.currentTimeMillis() - Long.parseLong(splitteddec[1]);
					if (difference < 1000) {
					// if it is within a boundary of new timestamp
					// send y to server
						toServer.writeChar('y');
					// decrypt file1 and save as 'reqesteduserID'+pub.key in key folder
						savekey = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "pub.key";
						encryptdecrypt.decrypt(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file1.txt", savekey);
						ok = true;
					} else {
						// otherwise send n and exit
						toServer.writeChar('n');
					}
					
				} else {
					toServer.writeChar('n');
				}
				
				String encryptedfiletosend = null;
				// if it was y
				if (ok == true) {
					// send requesteduserID to server
					toServer.writeInt(otheruser);
					// create file with string2nonce, NonceB, currentuserID
					String filetosend = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "tosend.txt";
					BufferedWriter out3 = new BufferedWriter(new FileWriter(filetosend));
					try {
					SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
					String mynonce = new Integer( prng.nextInt() ).toString();
					//int mynonce = prng.next(64);
					
					BufferedWriter out4 = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + "mynonce.txt"));
					out4.write(mynonce);
					out4.close();
					
					String tosend = splitted[0] + " " + mynonce + " " + currentUserID;
					out3.write(tosend);
					out3.close();
					// encrypt and send to server
					encryptedfiletosend = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "encryptedtosend.txt";
					encryptdecrypt.encrypt(savekey,filetosend,encryptedfiletosend);
					} catch ( NoSuchAlgorithmException ex ) {
					      System.err.println(ex);
					    }
					
					sendrecsock.SendViaSocket(encryptedfiletosend);
					sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
				}
				
			}
			
			
			
			
		//}
		if (recfromserv[1] != null) {
			System.out.println("DO PART 2");
			
			//decrypt recfromserv[1] file
			encryptdecrypt.decrypt(recfromserv[1],pathwaystart+"decrpyted.txt");
			//check string1 against own nonce for string3
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"decrpyted.txt"));
			fromfile = in.readLine();
			String[] input = fromfile.split(" ");
			
			BufferedReader in2 = new BufferedReader(new FileReader(pathwaystart + input[2] + "/" + "mynonce.txt"));
			String mynonce = in2.readLine();
			in2.close();
			
			if (mynonce.equals(input[0])) {
				//add string3 to trusted
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + "trusted/" + input[2] + ".txt"));
				out.write("trusted");
				out.close();
				//create file string2 (their nonce), currentuserID
				BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + input[2] + "/" + "tosend.txt"));
				out2.write(input[1] + " " + Integer.toString(currentUserID));
				out2.close();
				
				//encrypt with their key
				encryptdecrypt.encrypt(pathwaystart + input[2] + "/" + input[2] + "pubkey.key",pathwaystart + input[2] + "/" + "tosend.txt",pathwaystart + input[2] + "/" + "encryptedtosend.txt");
				
				//send string3 to server
				toServer.writeInt(Integer.parseInt(input[2]));
				//send encrypted file to server
				sendrecsock.SendViaSocket(pathwaystart + input[2] + "/" + "encryptedtosend.txt");
				//receive ack
				sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
			} else {
				System.out.print("nonces are different - ERROR");
				toServer.writeInt(0);
			}
			
			
		}
		if (recfromserv[2] != null) {
			System.out.println("DO PART 3");
			
			//decrypt recfromserv[2] file
			encryptdecrypt.decrypt(recfromserv[2],pathwaystart+"decrpyted.txt");
			//check string1 with own nonce for string2
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"decrpyted.txt"));
			fromfile = in.readLine();
			String[] input = fromfile.split(" ");
			
			BufferedReader in2 = new BufferedReader(new FileReader(pathwaystart + input[1] + "/" + "mynonce.txt"));
			String mynonce = in2.readLine();
			in2.close();
			
			if (mynonce.equals(input[0])) {
				//add string2 to trusted
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + "trusted/" + input[1] + ".txt"));
				out.write("trusted");
				out.close();	
			}
		
		}
		/*
		 * char sendingfile; sendingfile = fromServer.readChar();
		 * System.out.println("From server: " + sendingfile);
		 */

		Boolean sendmessage = true;

		while (sendmessage) {
			Scanner mainin = new Scanner(System.in);
			String sm;
			System.out.println("Do you wish to send a message? y/n");
			sm = mainin.nextLine();
			char c;

			if (sm.equals("y")) {
				System.out.println("YES");
				c = sm.charAt(0);
				toServer.writeChar(c);

				// user inputs receiver user id
				String r;
				System.out.println("Enter receiver user ID");
				r = mainin.nextLine();
				// System.out.println("receiving char from server");
				// char fs = fromServer.readChar();
				String keypath = pathwaystart + "keys/" + r + "pubkey.key";
				System.out.println("keypath is: " + keypath);
				File f = new File(keypath);
				if (!(f.exists())) {

					System.out.println("You do not have user " + r + "registered. Registration protocol will now be initiated");
					toServer.writeChar('n');

					// register
					String requesteduser = r;
					// save currentuserID, requesteduserID, Timestamp in a file
					BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + requesteduser + "/" + requesteduser + "initial.txt"));
					Long timestamp = System.currentTimeMillis();
					String thetime = timestamp.toString();
					String thefinal = currentUserID + " " + requesteduser + " " + thetime;
					out2.write(thefinal);
					out2.close();
					
					// send file to server
					sendrecsock.SendViaSocket(pathwaystart + requesteduser + "/" + requesteduser + "initial.txt");
					
					// receive file1
					String savekey = pathwaystart + requesteduser + "/" + requesteduser + "pub.key";
					sendrecsock.ReceiveViaSocket(savekey);
					// send ack
					sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
					// receive file2
					sendrecsock.ReceiveViaSocket(pathwaystart + requesteduser + "/" + requesteduser + "file2.txt");
					// send ack
					sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
					// decrypt file2
					String filedecrypted = pathwaystart + requesteduser + "/" + requesteduser + "decryptedfile2.txt";
					encryptdecrypt.decrypt(pathwaystart + requesteduser + "/" + requesteduser + "file2.txt", filedecrypted);
					// take out first string
					String fromfiledec;
					BufferedReader in2 = new BufferedReader(new FileReader(filedecrypted));
					fromfiledec = in2.readLine();
					in2.close();
					String[] splitteddec = fromfiledec.split(" ");
					boolean ok = false;
					System.out.println("splitteddec[0]: " + splitteddec[0]);
					System.out.println("requesteduser: " + requesteduser );
					if (splitteddec[0].equals(requesteduser)) {
						// if it matches requested user name
						// take second string
						Long difference = System.currentTimeMillis() - Long.parseLong(splitteddec[1]);
						System.out.println("difference = " + difference);
						if (difference < 1000) {
							System.out.println("timestamp OK");
						// if it is within a boundary of new timestamp
						// send y to server
							toServer.writeChar('y');
						// decrypt file1 and save as 'reqesteduserID'+pub.key in key folder
							//savekey = pathwaystart + requesteduser + "/" + requesteduser + "pub.key";
							//encryptdecrypt.decrypt(pathwaystart + requesteduser + "/" + requesteduser + "file1.key", savekey);
							ok = true;
						} else {
							// otherwise send n and exit
							toServer.writeChar('n');
						}
					}
					String encryptedfiletosend = null;
					// if it was y
					if (splitteddec[0].equals(requesteduser)) {
						// send requesteduserID to server
						toServer.writeInt(Integer.parseInt(requesteduser));
						// create file with NonceA currentuserID (save nonce in requserID/mynonce
						try {
							SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
							String mynonce = new Integer( prng.nextInt() ).toString();
							
							BufferedWriter out4 = new BufferedWriter(new FileWriter(pathwaystart + requesteduser + "/" + "mynonce.txt"));
							out4.write(mynonce);
							out4.close();
							
							String filetosend = pathwaystart + requesteduser + "/" + "tosend.txt";
							BufferedWriter out3 = new BufferedWriter(new FileWriter(filetosend));
							String tosend = mynonce + " " + currentUserID;
							out3.write(tosend);
							out3.close();
							// encrypt and send to server
							encryptedfiletosend = pathwaystart + requesteduser + "/" + requesteduser + "encryptedtosend.txt";
							encryptdecrypt.encrypt(savekey,filetosend,encryptedfiletosend);
							} catch ( NoSuchAlgorithmException ex ) {
							      System.err.println(ex);
							    }
						
							sendrecsock.SendViaSocket(encryptedfiletosend);
							sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
					}
					
					System.out.println("Registration step 1 complete");
				} else {
					toServer.writeChar('y');

					toServer.writeInt(Integer.parseInt(r));
					// user inputs message
					// Scanner in = new Scanner(System.in);
					String s;
					System.out.println("Enter a message");
					s = mainin.nextLine();

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
						//pathwaystart = pathwaystart + "client" + Integer.toString(currentUserID) + "/";
						// create encryptoin/decryption object
						PUB_KEY_LOC = pathwaystart + "client" + Integer.toString(currentUserID) + "/" + "keys/mypublickey.key";
						PRIV_KEY_LOC = pathwaystart + "client" + Integer.toString(currentUserID) + "/" + "keys/myprivatekey.key";
						encryptdecrypt = new EncryptDecrypt(PUB_KEY_LOC, PRIV_KEY_LOC);
						// send public key to server
						SERV_PUB_KEY_LOC = pathwaystart + "client" + Integer.toString(currentUserID) + "/" + "keys/serverpublickey.key";
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