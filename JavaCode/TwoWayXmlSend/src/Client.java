import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client{
  public static void main (String [] args ) throws IOException {
   int filesize=6022386; // filesize temporary hardcoded

    long start = System.currentTimeMillis();
    int bytesRead;
    int current = 0;
    //localhost for testing
    Socket sock = new Socket("localhost",8003);
    System.out.println("Connecting...");
    
    String pathwaystart = "C:\\Users/Tom/TestDoc/";
    //String receivedfilepath = "C:\\Users/Tom/TestDoc/receivedfromserver.xml";
    
    String fromserver = "fromserver ";
  	Date fsdate = new Date();
  	String fssdate = fsdate.toString();
  	String fsrsdate = fssdate.replaceAll(":"," ");
  	String fromserverfilename = fromserver.concat(fsrsdate);
  	
  	String fromserverfilepath = fromserverfilename.concat(".xml");
  	String fromserverpath = pathwaystart.concat(fromserverfilepath);
	System.out.println(fromserverpath);
    
    
    // receive file
   byte [] mybytearray  = new byte [filesize];
    InputStream is = sock.getInputStream();
    FileOutputStream fos = new FileOutputStream(fromserverpath);
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
    String returnedresult = xmlmanip.returnRequired(fromserverpath,"text");
    System.out.println("message is:" + returnedresult);
    String returnedsender = xmlmanip.returnRequired(fromserverpath,"receiver");
    System.out.println("from: " + returnedsender);
    
    //user inputs message
    Scanner in = new Scanner(System.in);
    String s;
    System.out.println("Enter a message");
    s = in.nextLine();
    
    //user inputs receiver ip address
    String r;
    System.out.println("Enter receiver ip address");
    r = in.nextLine();
    
    //create message xml file
    XmlWriter xmlwriter = new XmlWriter();
    
    //save file to send in format: toserver date and time
    String toserver = "toserver ";
  	Date date = new Date();
  	String sdate = date.toString();
  	String rsdate = sdate.replaceAll(":"," ");
  	String toserverfilename = toserver.concat(rsdate);
  	
  	String toserverfilepath = toserverfilename.concat(".xml");
  	String toserverpath = pathwaystart.concat(toserverfilepath);
	System.out.println(toserverpath);
    
    xmlwriter.WriteToFile(s,"client", r, toserverfilename);
    
    //send file
    File myFile = new File (toserverpath);
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