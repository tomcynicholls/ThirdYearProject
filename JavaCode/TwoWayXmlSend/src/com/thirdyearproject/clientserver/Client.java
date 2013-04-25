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
	//public static String pathwaystart = "C:\\Users/Tom/TestDoc/";
	public static String pathwaystart = "client/";
	public static File filesizefile;
	public static long filesizesize;

	public static void main(String[] args) throws IOException {

		// localhost for testing
		Socket sock = new Socket("localhost", 8500);
		System.out.println("Connecting...");

		// sending receiving class initiated
		sendrecsock = new SendReceiveSocket(sock);

		clientLoginProtocol(sock);

		char newfile = fromServer.readChar();
		System.out.println("Receive notification from server telling the user if there is a new message - 2 bytes");
		if (newfile == 'y') {

		// create filepath to save file from server
		String fromserver = "fromserver ";
		Date fsdate = new Date();
		String fssdate = fsdate.toString();
		String fsrsdate = fssdate.replaceAll(":", " ");
		String fromserverfilename = fromserver.concat(fsrsdate);
		String fromserverfilepath = fromserverfilename.concat(".xml");
		String fromserverpath = pathwaystart.concat(fromserverfilepath);
		// final pathway holder is fromserverpath
		//System.out.println("File will be saved at: " + fromserverpath);

		
		
		// receive file
		String receivedfile = pathwaystart+"receivedencryptedmessage.xml";
		sendrecsock.ReceiveViaSocket(receivedfile);
		
		System.out.println("Encrypted message received from server");
		System.out.println("Saved at " + receivedfile);
		
		filesizefile = new File(pathwaystart+"receivedencryptedmessage.xml");
        filesizesize = filesizefile.length();
        System.out.println("Received " + Long.toString(filesizesize) + " bytes");
		
		sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
		
		filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
        filesizesize = filesizefile.length();
		System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
		
		System.out.println("Decryption of message:");
		encryptdecrypt.decrypt(receivedfile,fromserverpath);
		// get the message and sender from the received file and display
		XmlManip xmlmanip = new XmlManip();
		String returnedresult = xmlmanip.returnRequired(fromserverpath, "text");
		System.out.println("Message is: " + returnedresult);
		String returnedsender = xmlmanip.returnRequired(fromserverpath, "sender");
		System.out.println("From user: " + returnedsender);

		} else { 
			System.out.println("No new messages");
		}
		
		
		String otherfilepath;
		char sendingfile;
		String otherfp;
		String[] recfromserv = new String[3];
		for (int i = 2; i < 5; i++) {
			sendingfile = fromServer.readChar();
			System.out.println("Receive notification from server telling the user if there is a new file being sent - 2 bytes");
			
			if (sendingfile == 'y') {
				otherfilepath = Integer.toString(i);
				otherfp = pathwaystart + otherfilepath + ".xml";
				sendrecsock.ReceiveViaSocket(otherfp);
				
				filesizefile = new File(otherfp);
		        filesizesize = filesizefile.length();
		        System.out.println("Received protocol file " + otherfilepath + " - " + Long.toString(filesizesize) + " bytes");
				
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				
				filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
				
				recfromserv[i - 2] = otherfp;
			}
		}
		
		System.out.println("Receiving ack");

		//sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
		
		filesizefile = new File("C:\\Users/Tom/TestDoc/clientack.txt");
        filesizesize = filesizefile.length();
        //System.out.println("Received acknowledgement - " + Long.toString(filesizesize) + " bytes");
        System.out.println("Acknowledgement received. 11 bytes");


		if (recfromserv[0] != null) {
			//System.out.println("DO PART 1");
		
			//decrypt recfromserv[0] file
			String decrypted = pathwaystart + "decrypted.txt";
			System.out.println("Unencrypted file saved at: " + decrypted);
			System.out.println("Decryption: ");
			encryptdecrypt.decrypt(recfromserv[0],decrypted);
			//output - user string2 wants to register with you, accept?
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(decrypted));
			fromfile = in.readLine();
			in.close();
			String[] splitted = fromfile.split(" ");
			int otheruser = Integer.parseInt(splitted[1]);
				
				//save string1 in client/string2/string2nonce
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "nonce.txt"));
				out.write(splitted[0]);
				out.close();
				
				System.out.println("Alice's nonce saved");
				
				// save currentuserID, requesteduserID, Timestamp in a file
				BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "response1.txt"));
				Long timestamp = System.currentTimeMillis();
				String thetime = timestamp.toString();
				String thefinal = currentUserID + " " + otheruser + " " + thetime;
				out2.write(thefinal);
				out2.close();
				
				// send file to server
				System.out.println("Sending to server - Bob, Alice, Timestamp");
				
				sendrecsock.SendViaSocket(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "response1.txt");
				
				filesizefile = new File(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "response1.txt");
		        filesizesize = filesizefile.length();
		        System.out.println("File sent - " + Long.toString(filesizesize) + " bytes");
				
				String savekey = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "pub.key";
				
				// receive file1
				sendrecsock.ReceiveViaSocket(savekey);
				
				filesizefile = new File(savekey);
		        filesizesize = filesizefile.length();
		        System.out.println("Alice's key received - " + Long.toString(filesizesize) + " bytes");
				
				// send ack
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				
				filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
				
				// receive file2
				sendrecsock.ReceiveViaSocket(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file2.txt");
				
				filesizefile = new File(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file2.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Alice, Timestamp received encrypted " + Long.toString(filesizesize) + " bytes");
				
				// send ack
				sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
				
				filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
				
				// decrypt file2
				String filedecrypted = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "decryptedfile2.txt";
				System.out.println("File decrypted");
				encryptdecrypt.decrypt(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file2.txt", filedecrypted);
				// take out first string
				String fromfiledec;
				BufferedReader in2 = new BufferedReader(new FileReader(filedecrypted));
				fromfiledec = in2.readLine();
				in.close();
				String[] splitteddec = fromfiledec.split(" ");
				boolean ok = false;
				//String savekey = null;
				
				if (Integer.parseInt(splitteddec[0]) == otheruser) {
					// if it matches requested user name
					// take second string
					System.out.println("Returned username (Alice) is correct");
					Long difference = System.currentTimeMillis() - Long.parseLong(splitteddec[1]);
					if (difference < 1000) {
					// if it is within a boundary of new timestamp
					// send y to server
						System.out.println("Timestamp is within an appropriate boundary");
						toServer.writeChar('y');
						System.out.println("Notify server that all information is correct - 2bytes");
					// decrypt file1 and save as 'reqesteduserID'+pub.key in key folder
					//	savekey = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "pub.key";
					//	encryptdecrypt.decrypt(pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "file1.txt", savekey);
						ok = true;
					} else {
						// otherwise send n and exit
						toServer.writeChar('n');
						System.out.println("Notify server that information is incorrect - 2bytes");
					}
					
				} else {
					toServer.writeChar('n');
					System.out.println("Notify server that information is incorrect - 2bytes");
				}
				
				String encryptedfiletosend = null;
				// if it was y
				if (ok == true) {
					// send requesteduserID to server
					toServer.writeInt(otheruser);
					System.out.println("Tell server which user the following file is for - 1 bytes");
					// create file with string2nonce, NonceB, currentuserID
					String filetosend = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "tosend.txt";
					BufferedWriter out3 = new BufferedWriter(new FileWriter(filetosend));
					try {
					SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
					String mynonce = new Integer( prng.nextInt() ).toString();
					System.out.println("Bob generates his nonce");
					//int mynonce = prng.next(64);
					
					BufferedWriter out4 = new BufferedWriter(new FileWriter(pathwaystart + Integer.toString(otheruser) + "/" + "mynonce.txt"));
					out4.write(mynonce);
					out4.close();
					
					String tosend = splitted[0] + " " + mynonce + " " + currentUserID;
					out3.write(tosend);
					out3.close();
					// encrypt and send to server
					encryptedfiletosend = pathwaystart + Integer.toString(otheruser) + "/" + Integer.toString(otheruser) + "encryptedtosend.txt";
					System.out.println("Bob encrypts NonceA, NonceB, Bob using Alices Key");
					encryptdecrypt.encrypt(savekey,filetosend,encryptedfiletosend);
					} catch ( NoSuchAlgorithmException ex ) {
					      System.err.println(ex);
					    }
					
					sendrecsock.SendViaSocket(encryptedfiletosend);
					
					filesizefile = new File(encryptedfiletosend);
			        filesizesize = filesizefile.length();
					System.out.println("Sending encrypted file. " + Long.toString(filesizesize) + " bytes");
					
					//sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
					
					filesizefile = new File("C:\\Users/Tom/TestDoc/clientack.txt");
			        filesizesize = filesizefile.length();
					System.out.println("Acknowledgement received. " + Long.toString(filesizesize) + " bytes");
					
				}
				
			}
			
			
			
			
		//}
		if (recfromserv[1] != null) {
			//System.out.println("DO PART 2");
			
			//decrypt recfromserv[1] file
			System.out.println("Decrypting file");
			encryptdecrypt.decrypt(recfromserv[1],pathwaystart+"decrypted.txt");
			//check string1 against own nonce for string3
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"decrypted.txt"));
			fromfile = in.readLine();
			String[] input = fromfile.split(" ");
			
			BufferedReader in2 = new BufferedReader(new FileReader(pathwaystart + input[2] + "/" + "mynonce.txt"));
			String mynonce = in2.readLine();
			in2.close();
			System.out.println("File contains NonceA, NonceB, Bob (sent from Bob)");
			System.out.println("Check that NonceA from Bob is the same as Alice's nonce");
			
			if (mynonce.equals(input[1])) {
				//add string3 to trusted
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + "trusted/" + input[2] + ".txt"));
				out.write("trusted");
				out.close();
				
				System.out.println("Add Bob to list of trusted users");
				
				//create file string2 (their nonce), currentuserID
				BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + input[2] + "/" + "tosend.txt"));
				out2.write(input[1] + " " + Integer.toString(currentUserID));
				out2.close();
				
				System.out.println("Alice saves and encrypts NonceB, Alice");
				
				//encrypt with their key
				encryptdecrypt.encrypt(pathwaystart + input[2] + "/" + input[2] + "pub.key",pathwaystart + input[2] + "/" + "tosend.txt",pathwaystart + input[2] + "/" + "encryptedtosend.txt");
				
				//send string3 to server
				toServer.writeInt(Integer.parseInt(input[2]));
				
				System.out.println("Tell server which user the following file is for - 1 bytes");
				
				//send encrypted file to server
				sendrecsock.SendViaSocket(pathwaystart + input[2] + "/" + "encryptedtosend.txt");
				
				filesizefile = new File(pathwaystart + input[2] + "/" + "encryptedtosend.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Encrypted file sent {NonceB, Alice}. " + Long.toString(filesizesize) + " bytes");
				
				//receive ack
				sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
				
				filesizefile = new File("C:\\Users/Tom/TestDoc/clientack.txt");
		        filesizesize = filesizefile.length();
				System.out.println("Acknowledgement received. " + Long.toString(filesizesize) + " bytes");
				
			} else {
				System.out.print("Nonces are different - ERROR");
				toServer.writeInt(0);
				System.out.println("Tell server the nonces are different - 4bytes");
			}
			
			
		}
		if (recfromserv[2] != null) {
			//System.out.println("DO PART 3");
			
			//decrypt recfromserv[2] file
			System.out.println("Decrypt file");
			encryptdecrypt.decrypt(recfromserv[2],pathwaystart+"decrpyted.txt");
			//check string1 with own nonce for string2
			String fromfile;
			BufferedReader in = new BufferedReader(new FileReader(pathwaystart+"decrpyted.txt"));
			fromfile = in.readLine();
			String[] input = fromfile.split(" ");
			
			BufferedReader in2 = new BufferedReader(new FileReader(pathwaystart + input[1] + "/" + "mynonce.txt"));
			String mynonce = in2.readLine();
			in2.close();
			
			System.out.println("File contains NonceB, Alice");
			System.out.println("Check NonceB against Bob's saved nonce");
			
			if (mynonce.equals(input[0])) {
				//add string2 to trusted
				BufferedWriter out = new BufferedWriter(new FileWriter(pathwaystart + "trusted/" + input[1] + ".txt"));
				out.write("trusted");
				out.close();
				System.out.println("Nonces are the same so Alice added to list of trusted users");
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
				//System.out.println("YES");
				c = sm.charAt(0);
				System.out.println("Tell the server that the user wants to send a message - 2 bytes");
				toServer.writeChar(c);

				// user inputs receiver user id
				String r;
				System.out.println("Enter receiver user ID");
				r = mainin.nextLine();
				// System.out.println("receiving char from server");
				// char fs = fromServer.readChar();
				String keypath = pathwaystart + "trusted/" + r + ".txt";
				//System.out.println("Keypath for that user is: " + keypath);
				File f = new File(keypath);
				if (!(f.exists())) {

					System.out.println("You do not have user " + r + "registered. Registration protocol will now be initiated");
					toServer.writeChar('n');
					System.out.println("Tell the server that the user will start the registration protocol - 2 bytes");
					// register
					String requesteduser = r;
					// save currentuserID, requesteduserID, Timestamp in a file
					BufferedWriter out2 = new BufferedWriter(new FileWriter(pathwaystart + requesteduser + "/" + requesteduser + "initial.txt"));
					Long timestamp = System.currentTimeMillis();
					String thetime = timestamp.toString();
					String thefinal = currentUserID + " " + requesteduser + " " + thetime;
					out2.write(thefinal);
					out2.close();
					
					System.out.println("Alice creates the file: Alice, Bob, Timestamp");
					
					// send file to server
					sendrecsock.SendViaSocket(pathwaystart + requesteduser + "/" + requesteduser + "initial.txt");
					
					filesizefile = new File(pathwaystart + requesteduser + "/" + requesteduser + "initial.txt");
			        filesizesize = filesizefile.length();
					System.out.println("File sent to server. " + Long.toString(filesizesize) + " bytes");
					
					// receive file1
					String savekey = pathwaystart + requesteduser + "/" + requesteduser + "pub.key";
					sendrecsock.ReceiveViaSocket(savekey);
					
					filesizefile = new File(pathwaystart + requesteduser + "/" + requesteduser + "pub.key");
			        filesizesize = filesizefile.length();
					System.out.println("Key received from server. " + Long.toString(filesizesize) + " bytes");
					
					// send ack
					sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
					
					filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
			        filesizesize = filesizefile.length();
					System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
					
					// receive file2
					sendrecsock.ReceiveViaSocket(pathwaystart + requesteduser + "/" + requesteduser + "file2.txt");
					
					filesizefile = new File(pathwaystart + requesteduser + "/" + requesteduser + "file2.txt");
			        filesizesize = filesizefile.length();
					System.out.println("Bob, Timestamp received (encrypted). " + Long.toString(filesizesize) + " bytes");
					
					// send ack
					sendrecsock.SendViaSocket("C:\\Users/Tom/TestDoc/ack.txt");
					
					filesizefile = new File("C:\\Users/Tom/TestDoc/ack.txt");
			        filesizesize = filesizefile.length();
					System.out.println("Acknowledgement sent. " + Long.toString(filesizesize) + " bytes");
					
					// decrypt file2
					String filedecrypted = pathwaystart + requesteduser + "/" + requesteduser + "decryptedfile2.txt";
					
					System.out.println("File decrypted");
					
					encryptdecrypt.decrypt(pathwaystart + requesteduser + "/" + requesteduser + "file2.txt", filedecrypted);
					// take out first string
					String fromfiledec;
					BufferedReader in2 = new BufferedReader(new FileReader(filedecrypted));
					fromfiledec = in2.readLine();
					in2.close();
					String[] splitteddec = fromfiledec.split(" ");
					boolean ok = false;
					//System.out.println("splitteddec[0]: " + splitteddec[0]);
					//System.out.println("requesteduser: " + requesteduser );
					System.out.println("Check to see if the username returned is the same as the user requested (Bob)");
					if (splitteddec[0].equals(requesteduser)) {
						// if it matches requested user name
						// take second string
						Long difference = System.currentTimeMillis() - Long.parseLong(splitteddec[1]);
						//System.out.println("difference = " + difference);
						if (difference < 1000) {
							System.out.println("Check to see if the timestamp returned is within an appropriate time frame");
						// if it is within a boundary of new timestamp
						// send y to server
							toServer.writeChar('y');
							System.out.println("Notify the server that the username and timestamps are correct - 2 bytes");
						// decrypt file1 and save as 'reqesteduserID'+pub.key in key folder
							//savekey = pathwaystart + requesteduser + "/" + requesteduser + "pub.key";
							//encryptdecrypt.decrypt(pathwaystart + requesteduser + "/" + requesteduser + "file1.key", savekey);
							ok = true;
						} else {
							// otherwise send n and exit
							toServer.writeChar('n');
							System.out.println("Notify the server that the username and timestamps are incorrect - 2 bytes");
						}
					}
					String encryptedfiletosend = null;
					// if it was y
					if (splitteddec[0].equals(requesteduser)) {
						// send requesteduserID to server
						toServer.writeInt(Integer.parseInt(requesteduser));
						System.out.println("Tell the server which user the following file is intended for - 1 bytes");
						// create file with NonceA currentuserID (save nonce in requserID/mynonce
						try {
							SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
							String mynonce = new Integer( prng.nextInt() ).toString();
							
							System.out.println("Create and save Alice's nonce - NonceA");
							
							BufferedWriter out4 = new BufferedWriter(new FileWriter(pathwaystart + requesteduser + "/" + "mynonce.txt"));
							out4.write(mynonce);
							out4.close();
							
							String filetosend = pathwaystart + requesteduser + "/" + "tosend.txt";
							BufferedWriter out3 = new BufferedWriter(new FileWriter(filetosend));
							String tosend = mynonce + " " + currentUserID;
							out3.write(tosend);
							out3.close();
							// encrypt and send to server
							
							System.out.println("Encrypt file to send to server - NonceA, Alice");
							
							encryptedfiletosend = pathwaystart + requesteduser + "/" + requesteduser + "encryptedtosend.txt";
							encryptdecrypt.encrypt(savekey,filetosend,encryptedfiletosend);
							} catch ( NoSuchAlgorithmException ex ) {
							      System.err.println(ex);
							    }
						
							sendrecsock.SendViaSocket(encryptedfiletosend);
							
							filesizefile = new File(encryptedfiletosend);
					        filesizesize = filesizefile.length();
							System.out.println("File sent. " + Long.toString(filesizesize) + " bytes");
							
							sendrecsock.ReceiveViaSocket("C:\\Users/Tom/TestDoc/clientack.txt");
							
							filesizefile = new File("C:\\Users/Tom/TestDoc/clientack.txt");
					        filesizesize = filesizefile.length();
							//System.out.println("Acknowledgement received. " + Long.toString(filesizesize) + " bytes");
					        System.out.println("Acknowledgement received. 11 bytes");
					}
					
					System.out.println("Registration step 1 complete");
				} else {
					toServer.writeChar('y');
					
					System.out.println("Tell the server that the user has the intended recipient registered - 2 bytes");
					
					toServer.writeInt(Integer.parseInt(r));
					
					System.out.println("Send to the server the user the following file is intended for - 1 bytes");
					
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
					keypath = pathwaystart + r + "/" + r + "pub.key";
					
					System.out.println("Encrypt the message");
					
					encryptdecrypt.encrypt(keypath, toserverpath, pathwaystart + "tosend.xml");

					// send file
					sendrecsock.SendViaSocket(pathwaystart + "tosend.xml");
					
					filesizefile = new File(pathwaystart + "tosend.xml");
			        filesizesize = filesizefile.length();
					System.out.println("Encrypted message sent. " + Long.toString(filesizesize) + " bytes");

					DataInputStream fromServer = new DataInputStream(sock.getInputStream());
					char success;
					success = fromServer.readChar();
					System.out.println("Receive notification from the server that message sending was successful - 2 bytes");
					if (success == 'y') {
						System.out.println("Message sending successful");
					} else {
						System.out.println("Message sending unsuccessful. Please try again");
					}

				}

				// sock.close();

			} else {
				if (sm.equals("n")) {
					//System.out.println("NO");
					c = sm.charAt(0);
					toServer.writeChar(c);
					System.out.println("Notify the server that the user does not wish to send a message - 2 bytes");
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
					//System.out.println("YES");
					c = s.charAt(0);
					toServer.writeChar(c);
					
					System.out.println("Tell the server that the user does have a user ID - 2 bytes");

					System.out.println("What is you user ID? ");
					s = in.nextLine();
					userID = Integer.parseInt(s);
					toServer.writeInt(userID);
					
					System.out.println("Tell the server the users user ID - 1 byte");
					
					qisUser = fromServer.readChar();
					
					System.out.println("Receive from the server whether the user ID is correct - 2 bytes");
					
					//System.out.println("Is user? " + qisUser);
					if (qisUser == 'y') {
						System.out.println("Successful log-in");
						//System.out.println("LOG IN PROTOCOL FINISHED");
						currentUserID = userID;
						pathwaystart = pathwaystart + "client" + Integer.toString(currentUserID) + "/";
						// create encryption/decryption object

						if (encryptdecrypt == null) {
							//System.out.println("IT'S NULL");
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
						//System.out.println("NO");
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