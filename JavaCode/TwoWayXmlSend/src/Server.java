import java.io.DataInputStream;
import java.io.DataOutputStream;
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
    
    //create xml writer
    XmlWriter xmlwriter = new XmlWriter();
    
    String pathwaystart = "C:\\Users/Tom/TestDoc/";
    String nomessagefile = pathwaystart.concat("nomessagefile.xml");
    File f = new File(nomessagefile);
    if(!(f.exists())){
    	xmlwriter.WriteToFile("no message","server","client","nomessagefile");
    }
    
    DBManager con = new DBManager(); 
  	System.out.println("Connection : " +con.doConnection());
    
    int currentID;
        
    //start server loop
    while (true) {
      System.out.println("Waiting...");

      //accept socket connection
      	Socket sock = servsock.accept();
      	System.out.println("Accepted connection : " + sock);
      
      	currentID = serverLoginProtocol(sock,con);
      	
      	
      //sending receiving class initiated
      	SendReceiveSocket sendrecsock = new SendReceiveSocket(sock);
      
      //server gets clients ip address
      	InetAddress inetAddress = sock.getInetAddress();
      	String stringip = inetAddress.getHostAddress();
      	int arraypos = returnArrayPos(stringip);
      
      //display filename storage array position assignment
      	System.out.println("IP Address is: " + stringip + " Array assignment " + arraypos);
      
      //if the user has no message (first time connecting) associate no message file with client
      	if (con.returnFirstMessagefromID(currentID) == null) {
      		con.updateUser(currentID,"messloc1","nomessagefile.xml");
      	}
      
      //get filepath from array store
      	String filepath = pathwaystart.concat(con.returnFirstMessagefromID(currentID));
      //sendfile
      	sendrecsock.SendViaSocket(filepath);

      //message sent so replace with no message file association
      	con.updateUser(currentID,"messloc1","nomessagefile.xml");
      	
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
     
		//sock.close();
      
      //server reads ip address from file
		XmlManip xmlmanip = new XmlManip();
		String returnedresult = xmlmanip.returnRequired(path,"receiver");
		System.out.println("receiver is:" + returnedresult); 
     
      //and saves filename in appropriate array position		
		int recuser = Integer.parseInt(returnedresult);
		
		DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());
		
		if (con.userIDExists(recuser)) {
			con.updateUser(recuser,"messloc1",fromfilepath);
			outputToClient.writeChar('y');
		} else {
			System.out.println("Not registered!");
		
			outputToClient.writeChar('n');
		}
		
		sock.close();
      
       
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
  
  public static int serverLoginProtocol(Socket sock, DBManager con) throws IOException {
	  InetAddress inetAddress = sock.getInetAddress();
    	
    	DataInputStream inputFromClient = new DataInputStream(sock.getInputStream());
    	DataOutputStream outputToClient = new DataOutputStream(sock.getOutputStream());
    	
    	
    	
    	int currentID = -1;
    	
    	Boolean finished = false;
	
	while (!finished) {
	     
		char isUser = inputFromClient.readChar();
		System.out.println("Is a user? " + isUser);
		String stringip;
		int userID = 0;
		char qisUser = 'n';
		boolean userCorrect;
	    //if not user - create  	
		//if user - receive user id from client
		
		if (isUser == 'y'){
			System.out.println("YES");
			userID = inputFromClient.readInt();
			System.out.println("User ID is: " + userID);
			//server queries - respond to client
			userCorrect = con.userIDExists(userID);
			System.out.println("con.userIDExists result =" + userCorrect);
			if (userCorrect) {
			qisUser = 'y';
			currentID = userID;
			outputToClient.writeChar(qisUser);
			finished = true;
			} else {
			outputToClient.writeChar(qisUser);
			//sock.close();
			finished = true;
			}
		
		}
		else {
			if (isUser == 'n') {
				System.out.println("NO");
				stringip = inetAddress.getHostAddress();
	  			System.out.println("ip address is: ");
	  			//create new user in db and return user id
	  			userID = con.addNewUser(stringip);
	  			currentID = userID;
	  			outputToClient.writeInt(userID);
	  			
			}
			else {
				System.out.println("ERROR - NOT Y OR N");
			}
		}
	      	
	}
	//inputFromClient.close();
	//outputToClient.close();
	return currentID;
  }
  
}
