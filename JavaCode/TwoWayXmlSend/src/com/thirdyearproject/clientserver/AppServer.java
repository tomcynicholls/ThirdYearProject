package com.thirdyearproject.clientserver;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class AppServer {
  public static void main (String [] args ) throws IOException {
    // create socket
    ServerSocket servsock = new ServerSocket(8000);
    int bytesRead;
    int current = 0;
    int filesize=6022386; // filesize temporary hardcoded
    //String pathwaystart = "C:\\Users/Tom/TestDoc/";
    String pathwaystart = "server/";
    
    //String privkeyloc = "server/myprivatekey.key"; 
	//String pubkeyloc = "server/mypublickey.key"; 
    
    //EncryptDecryptAES ende = new EncryptDecryptAES(privkeyloc);
    
    //create array to temporarily store sending message filenames
    String[] filenamestore = new String[30];
    //create xml writer
    XmlWriter xmlwriter = new XmlWriter();
    
    
    
    if (filenamestore[1] == null) { 
    	//System.out.println("store empty"); 
    	//store no message file
    	xmlwriter.WriteToFile("no message","server","client","nomessagefile");
    	filenamestore[1] = "nomessagefile.xml";
    }
    
   // DBManager dbman = new DBManager();
    //System.out.println("Connection : " +dbman.doConnection());
    //long start = System.currentTimeMillis();
    
    //start server loop
    while (true) {
      //System.out.println("Waiting...");

      Socket sock = servsock.accept();
      //System.out.println("Accepted connection : " + sock);
      
      //server gets clients ip address
      InetAddress inetAddress = sock.getInetAddress();
      String stringip = inetAddress.getHostAddress();
     int arraypos = returnArrayPos(stringip);
     
    // int currentuserID = dbman.addNewUser(stringip);
    // DataOutputStream dos = new DataOutputStream(sock.getOutputStream());
   //  dos.writeInt(currentuserID);
     
    // System.out.println("Current user ID: " + currentuserID);
     SendReceiveSocket sendrecsock = new SendReceiveSocket(sock);
     //sendrecsock.SendViaSocket(pubkeyloc);
     //String userkey = "server/userkey.key";
     //System.out.println("receiving user key");
     //sendrecsock.ReceiveViaSocket(userkey);
    // System.out.println("userkey received");
     
     
   //if the user has no message (first time connecting) associate no message file with client
   	//if (dbman.returnFirstMessagefromID(currentuserID,4) == null) {
   		//dbman.updateUser(currentuserID,"messloc1","nomessagefile.xml");
   //	}
     
      //System.out.println("IP Address is: " + stringip + " Array assignment " + arraypos);
      
      //if the user has no message (first time connecting) associate no message file with client
      if (filenamestore[arraypos] == null) {
    	  filenamestore[arraypos] = "nomessagefile.xml";
      }
      
   // sendfile
	//String filepath = pathwaystart.concat(filenamestore[arraypos]);
     // String filepathpre = pathwaystart.concat(dbman.returnFirstMessagefromID(currentuserID,4));
    //  String filepath = pathwaystart.concat("tosend.xml");
    //  ende.encrypt(userkey,filepathpre,filepath);
      
    File myFile = new File (pathwaystart + filenamestore[arraypos]);
      byte [] mybytearray  = new byte [(int)myFile.length()];
      FileInputStream fis = new FileInputStream(myFile);
      BufferedInputStream bis = new BufferedInputStream(fis);
      bis.read(mybytearray,0,mybytearray.length);
      OutputStream os = sock.getOutputStream();
      //System.out.println("Sending...");
      os.write(mybytearray,0,mybytearray.length);
     // System.out.println("Sent");
      os.flush();
      
      //message sent so replace with no message file association
      filenamestore[arraypos] = "nomessagefile.xml";
      
      //dbman.updateUser(currentuserID,"messloc1","nomessagefile.xml");
      
      //receive file
      //create file name in format: from ip address date and time
      	String from = "from ";
      	Date date = new Date();
      	String sdate = date.toString();
      	String rsdate = sdate.replaceAll(":"," ");
      	String fromip = from.concat(stringip);
      	String filename = fromip.concat(rsdate);
      	
      	String fromfilepath = filename.concat(".xml");
      	String path = pathwaystart.concat(fromfilepath);
		//System.out.println("Will save data from client/app at: " + path);
      
      
      byte [] mybytearray2  = new byte [filesize];
      InputStream is = sock.getInputStream();
      FileOutputStream fos = new FileOutputStream(path);
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
      //long end = System.currentTimeMillis();
      //System.out.println(end-start);
      bos.close();
      sock.close();
      
      //server reads ip address from file
      XmlManip xmlmanip = new XmlManip();
      String returnedresult = xmlmanip.returnRequired(path,"receiver");
      //System.out.println("receiver is:" + returnedresult); 
      
      //dbman.updateUser(currentuserID,"messloc1",fromfilepath);
     
      //and saves filename in appropriate array position
      int rpos = returnArrayPos(returnedresult);
      filenamestore[rpos] = fromfilepath;
      
       
      }
   }
  //strip the last digits of the ip address and return
  public static int returnArrayPos(String ip){
	 //probably have to change this value for across the network
	  //localhost - 8 home - 10 uni - 11
	  String sub = ip.substring(10);
	    int arraypos = Integer.parseInt(sub);
	    
	    return arraypos;
	  
  }
  
}
       