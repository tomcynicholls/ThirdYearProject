package com.thirdyearproject.clientserver;
import java.io.IOException;
import java.net.Socket;

public class TestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		Socket sock = new Socket("localhost", 8000);
		System.out.println("Connecting...");

		EncryptDecrypt ende = new EncryptDecrypt("client/mypublickey.key", "client/myprivatekey.key");

		SendReceiveSocket sendrec = new SendReceiveSocket(sock);

		sendrec.SendViaSocket("client/mypublickey.key");

		sendrec.ReceiveViaSocket("client/fromserverencrypted.xml");

		ende.decrypt("client/fromserverencrypted.xml", "client/fromserverdecrypted.xml");

		sendrec.ReceiveViaSocket("client/serverpubkey.key");

		ende.encrypt("client/serverpubkey.key", "client/sendtoserver.xml", "client/sendtoserverencrypted.xml");

		sendrec.SendViaSocket("client/sendtoserverencrypted.xml");

	}

}