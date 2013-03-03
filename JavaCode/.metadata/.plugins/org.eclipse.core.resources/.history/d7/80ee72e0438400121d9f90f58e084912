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

		String privkeyloc = "client/myprivatekey.key"; 
		String pubkeyloc = "client/mypublickey.key"; 
		String servkeyloc = "client/serverkey.key"; 
		
		EncryptDecryptECC ende = new EncryptDecryptECC(pubkeyloc,privkeyloc);

		SendReceiveSocket sendrec = new SendReceiveSocket(sock);

		sendrec.SendViaSocket(pubkeyloc);

		sendrec.ReceiveViaSocket("client/fromserverencrypted.xml");

		ende.decrypt("client/fromserverencrypted.xml", "client/fromserverdecrypted.xml");

		sendrec.ReceiveViaSocket(servkeyloc);

		ende.encrypt(servkeyloc, "client/sendtoserver.xml", "client/sendtoserverencrypted.xml");

		sendrec.SendViaSocket("client/sendtoserverencrypted.xml");

	}

}
