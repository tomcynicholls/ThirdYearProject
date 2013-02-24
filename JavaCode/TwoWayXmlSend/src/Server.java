import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
  public static void main (String [] args ) throws IOException {
    
	// create socket
    ServerSocket servsock = new ServerSocket(8000);
     
    //create array to temporarily store sending message filenames
    String[] filenamestore = new String[30];
    
    //create xml writer
    XmlWriter xmlwriter = new XmlWriter();
    
    String pathwaystart = "C:\\Users/Tom/TestDoc/";
    String nomessagefile = pathwaystart.concat("nomessagefile.xml");
    File f = new File(nomessagefile);
    
    if (filenamestore[0] == null) { 
    	System.out.println("store empty"); 
    	//store no message file
    		xmlwriter.WriteToFile("no message","server","client","nomessagefile");
    		filenamestore[0] = "nomessagefile.xml";
    }
        
    //start server loop
    while (true) {
      System.out.println("Waiting...");

      //accept socket connection
      	Socket sock = servsock.accept();
      	System.out.println("Accepted connection : " + sock);
      
      //sending receiving class initiated
      	SendReceiveSocket sendrecsock = new SendReceiveSocket(sock);
      
      //server gets clients ip address
      	InetAddress inetAddress = sock.getInetAddress();
      	String stringip = inetAddress.getHostAddress();
      	int arraypos = returnArrayPos(stringip);
      
      //display filename storage array position assignment
      	System.out.println("IP Address is: " + stringip + " Array assignment " + arraypos);
      
      //if the user has no message (first time connecting) associate no message file with client
      	if (filenamestore[arraypos] == null) {
      		filenamestore[arraypos] = "nomessagefile.xml";
      	}
      
      //get filepath from array store
      	String filepath = pathwaystart.concat(filenamestore[arraypos]);
      //sendfile
      	sendrecsock.SendViaSocket(filepath);

      //message sent so replace with no message file association
      	filenamestore[arraypos] = "nomessagefile.xml";
      
      //receive file start
      //create file name in format: from ip address date and time
      	String from = "from ";
      	Date date = new Date();
      	String sdate = date.toString();
      	String rsdate = sdate.replaceAll(":"," ");
      	String fromip = from.concat(stringip);
      	String filename = fromip.concat(rsdate);
      	String fromfilepath = filename.concat(".xml");
      	String path = pathwaystart.concat(fromfilepath);
      //final filepath holder is path
		System.out.println("Will save data from client/app at: " + path);
      
      //actually receive file
		sendrecsock.ReceiveViaSocket(path);
     
		sock.close();
      
      //server reads ip address from file
		XmlManip xmlmanip = new XmlManip();
		String returnedresult = xmlmanip.returnRequired(path,"receiver");
		System.out.println("receiver is:" + returnedresult); 
     
      //and saves filename in appropriate array position
		int rpos = returnArrayPos(returnedresult);
		filenamestore[rpos] = fromfilepath;
      
       
      }
   }
  
  //strip the last digits of the ip address and return
  public static int returnArrayPos(String ip){
	 //probably have to change this value for across the network
	  //localhost - 8 home - 10 uni - 11
	  	String sub = ip.substring(8);
	    int arraypos = Integer.parseInt(sub);
	    
	    return arraypos;
	  
  }
  
  //return string ip as long (with . removed)
  public static Long returnLongIP(String ip){
	  
		System.out.println(ip);
		char[] charip = ip.toCharArray();
		char[] fcharip = new char[charip.length];
		int count = 0;
		
		for (int i = 0; i<charip.length; i++) {
			if (charip[i] != '.') { 
				fcharip[count] = charip[i];
				count++;
			}
		}
		
		String finalip = String.valueOf(fcharip);
				
		String trimmed = finalip.trim();
		
		Long result = Long.valueOf(trimmed);
		System.out.println(result);
		
		return result;
		
		
  }
  
}
