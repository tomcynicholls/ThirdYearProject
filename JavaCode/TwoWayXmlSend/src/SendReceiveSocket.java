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
	int filesize=6022386; // filesize temporary hardcoded]
	int bytesRead;
	int current = 0;
	
	public SendReceiveSocket(Socket s) {
		
		sock = s;
		
	}
	
	public void SendViaSocket(String filepath){
		
		try {
		
		//String filepath = pathwaystart.concat(filenamestore[arraypos]);
	    File myFile = new File (filepath);
	      byte [] mybytearray  = new byte [(int)myFile.length()];
	      FileInputStream fis = new FileInputStream(myFile);
	      BufferedInputStream bis = new BufferedInputStream(fis);
	      bis.read(mybytearray,0,mybytearray.length);
	      OutputStream os = sock.getOutputStream();
	      System.out.println("Sending...");
	      os.write(mybytearray,0,mybytearray.length);
	      System.out.println("Sent");
	      os.flush();
	      
	      
		 } catch (Exception e) {
				e.printStackTrace();
			    }
	      
	}
	
	public void ReceiveViaSocket(String path){
		
		try {
			
			byte [] mybytearray2  = new byte [filesize];
		      InputStream is = sock.getInputStream();
		      FileOutputStream fos = new FileOutputStream(path);
		      BufferedOutputStream bos = new BufferedOutputStream(fos);
		      bytesRead = is.read(mybytearray2,0,mybytearray2.length);
		      current = bytesRead;
		      bos.write(mybytearray2, 0 , current);
		      bos.flush();
		      bos.close();
		      
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}