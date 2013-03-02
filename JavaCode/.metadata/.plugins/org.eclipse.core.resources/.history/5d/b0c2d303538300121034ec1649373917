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

		EncryptDecrypt ende = new EncryptDecrypt("server/mypublickey.key", "server/myprivatekey.key");
		SendReceiveSocket sendrec = new SendReceiveSocket(sock);

		String pubkeyloc = "server/pubkey" + Integer.toString(userID) + ".key";
		sendrec.ReceiveViaSocket(pubkeyloc);

		ende.encrypt(pubkeyloc, "server/toencrypt.xml", "server/encryptedtosend.xml");

		sendrec.SendViaSocket("server/encryptedtosend.xml");

		sendrec.SendViaSocket("server/mypublickey.key");

		sendrec.ReceiveViaSocket("server/encryptedfromclient.xml");

		ende.decrypt("server/encryptedfromclient.xml", "server/decryptedfromclient.xml");

	}

}
