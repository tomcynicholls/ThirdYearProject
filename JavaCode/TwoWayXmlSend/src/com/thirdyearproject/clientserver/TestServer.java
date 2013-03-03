package com.thirdyearproject.clientserver;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {

	public static void main(String[] args) throws IOException {

		ServerSocket servsock = new ServerSocket(8000);

		System.out.println("Waiting...");

		// accept socket connection
		Socket sock = servsock.accept();
		System.out.println("Accepted connection : " + sock);
		InetAddress inetAddress = sock.getInetAddress();
		System.out.println(inetAddress);
		int userID = 1;
		/*
		String privkeyloc = "server/myprivatekey.key"; 
		String pubkeyloc = "server/mypublickey.key"; 
		//String clientkeyloc = "server/serverkey.key"; 

		EncryptDecryptECC ende = new EncryptDecryptECC(pubkeyloc,privkeyloc);
		SendReceiveSocket sendrec = new SendReceiveSocket(sock);

		String keyloc = "server/key" + Integer.toString(userID) + ".key";
		
		sendrec.ReceiveViaSocket(keyloc);

		ende.encrypt(keyloc,"server/toencrypt.xml", "server/encryptedtosend.xml");

		sendrec.SendViaSocket("server/encryptedtosend.xml");

		sendrec.SendViaSocket(pubkeyloc);

		sendrec.ReceiveViaSocket("server/encryptedfromclient.xml");

		ende.decrypt("server/encryptedfromclient.xml", "server/decryptedfromclient.xml");
*/
		EncryptDecryptAES ende = new EncryptDecryptAES("server/myprivatekey.txt");
		SendReceiveSocket sendrec = new SendReceiveSocket(sock);

		String keyloc = "server/key" + Integer.toString(userID) + ".txt";
		sendrec.ReceiveViaSocket(keyloc);

		ende.encrypt(keyloc,"server/toencrypt.xml", "server/encryptedtosend.xml");

		sendrec.SendViaSocket("server/encryptedtosend.xml");

		//sendrec.SendViaSocket("server/mypublickey.key");

		sendrec.ReceiveViaSocket("server/encryptedfromclient.xml");

		ende.decrypt(keyloc,"server/encryptedfromclient.xml", "server/decryptedfromclient.xml");
		
		
	}

}
