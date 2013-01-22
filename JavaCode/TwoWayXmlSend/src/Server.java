import java.net.*;
import java.io.*;

public class Server {
  public static void main (String [] args ) throws IOException {
    // create socket
    ServerSocket servsock = new ServerSocket(8003);
    int bytesRead;
    int current = 0;
    int filesize=6022386; // filesize temporary hardcoded

    //create array to temporarily store sending message filenames
    String[] filenamestore = new String[5];
    //create xml writer
    XmlWriter xmlwriter = new XmlWriter();
    
    String pathwaystart = "C:\\Users/Tom/TestDoc/";
    
    if (filenamestore[1] == null) { 
    	System.out.println("store empty"); 
    	//store no message file
    	xmlwriter.WriteToFile("no message","server","client","nomessagefile");
    	filenamestore[1] = "nomessagefile.xml";
    }
    
    long start = System.currentTimeMillis();
    
    //start server loop
    while (true) {
      System.out.println("Waiting...");

      Socket sock = servsock.accept();
      System.out.println("Accepted connection : " + sock);

      
   // sendfile
	String filepath = pathwaystart.concat(filenamestore[1]);
    File myFile = new File (filepath);
      byte [] mybytearray  = new byte [(int)myFile.length()];
      FileInputStream fis = new FileInputStream(myFile);
      BufferedInputStream bis = new BufferedInputStream(fis);
      bis.read(mybytearray,0,mybytearray.length);
      OutputStream os = sock.getOutputStream();
      System.out.println("Sending...");
      os.write(mybytearray,0,mybytearray.length);
      os.flush();
      //add in to try and fix
      //os.close();
      //fis.close();
      //bis.close();
      //os.flush();
      //sock.close();
      
      //receive file
      byte [] mybytearray2  = new byte [filesize];
      InputStream is = sock.getInputStream();
      FileOutputStream fos = new FileOutputStream("C:\\Users/Tom/TestDoc/fromclient.xml");
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      bytesRead = is.read(mybytearray2,0,mybytearray2.length);
      current = bytesRead;

      //this made it not work so has been commented out
     /* do {
         bytesRead = is.read(mybytearray2, current, (mybytearray2.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead > -1);*/

      bos.write(mybytearray2, 0 , current);
      bos.flush();
      long end = System.currentTimeMillis();
      System.out.println(end-start);
      bos.close();
      sock.close();
      
      //save received filename
      filenamestore[1] = "fromclient.xml";
      
       
      }
   }
}
