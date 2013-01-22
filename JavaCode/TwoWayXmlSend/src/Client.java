import java.net.*;
import java.io.*;

public class Client{
  public static void main (String [] args ) throws IOException {
   int filesize=6022386; // filesize temporary hardcoded

    long start = System.currentTimeMillis();
    int bytesRead;
    int current = 0;
    //localhost for testing
    Socket sock = new Socket("localhost",8003);
    System.out.println("Connecting...");
    
    String receivedfilepath = "C:\\Users/Tom/TestDoc/receivedfromserver.xml";
    
    // receive file
   byte [] mybytearray  = new byte [filesize];
    InputStream is = sock.getInputStream();
    FileOutputStream fos = new FileOutputStream(receivedfilepath);
    BufferedOutputStream bos = new BufferedOutputStream(fos);
    bytesRead = is.read(mybytearray,0,mybytearray.length);
    current = bytesRead;

  //this made it not work so has been commented out
    /*do {
       bytesRead = is.read(mybytearray, current, (mybytearray.length-current));
       if(bytesRead >= 0) current += bytesRead;
    } while(bytesRead > -1);*/

    bos.write(mybytearray, 0 , current);
    bos.flush();
    long end = System.currentTimeMillis();
    System.out.println(end-start);
    bos.close();
    
    XmlManip xmlmanip = new XmlManip();
    String returnedresult = xmlmanip.returnRequired(receivedfilepath,"text");
    System.out.println("message is:" + returnedresult);
    //add to try and fix
    //is.flush();
    //is.close();
    //fos.close();
    //sock.close();
    
    //send file
    File myFile = new File ("C:\\Users/Tom/TestDoc/yyy.xml");
    byte [] mybytearray2  = new byte [(int)myFile.length()];
    FileInputStream fis = new FileInputStream(myFile);
    BufferedInputStream bis = new BufferedInputStream(fis);
    bis.read(mybytearray2,0,mybytearray2.length);
    OutputStream os = sock.getOutputStream();
    System.out.println("Sending...");
    os.write(mybytearray2,0,mybytearray2.length);
    os.flush();
    sock.close();
  }
}