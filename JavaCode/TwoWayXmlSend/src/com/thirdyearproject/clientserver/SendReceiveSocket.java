package com.thirdyearproject.clientserver;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SendReceiveSocket {

	Socket sock = null;
	int filesize = 6022386; // filesize temporary hardcoded]
	int bytesRead;
	int current = 0;

	public SendReceiveSocket(Socket s) {

		sock = s;

	}

	public void SendViaSocket(String filepath) {

		try {

			// String filepath = pathwaystart.concat(filenamestore[arraypos]);
			File myFile = new File(filepath);
			byte[] mybytearray = new byte[(int) myFile.length()];
			FileInputStream fis = new FileInputStream(myFile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(mybytearray, 0, mybytearray.length);
			OutputStream os = sock.getOutputStream();
			//System.out.println("Sending...");
			os.write(mybytearray, 0, mybytearray.length);
			//System.out.println("Sent");
			
			
			//bis.close();
			//fis.close();
			os.flush();
			//os.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void ReceiveViaSocket(String path) {
		
		
		try {
			//System.out.println("here1");
			byte[] mybytearray2 = new byte[filesize];
			//System.out.println("here2");
			InputStream is = sock.getInputStream();
			//System.out.println(is.available());
			//System.out.println("here3");
			FileOutputStream fos = new FileOutputStream(path);
			//System.out.println("here4");
			BufferedOutputStream bos = new BufferedOutputStream(fos);
			//System.out.println("here5");
			bytesRead = is.read(mybytearray2, 0, mybytearray2.length);
			//System.out.println("here6");
			current = bytesRead;
			//System.out.println("here7");
			bos.write(mybytearray2, 0, current);
			//System.out.println("here8");
			
			//is.close();
			bos.flush();
			fos.flush();
			//bos.close();
			//fos.close();
			//bos.close();
			//is.reset();
			
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
