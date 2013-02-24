import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Client{
  public static void main (String [] args ) throws IOException {
    //localhost for testing
	  Socket sock = new Socket("localhost",8000);
	  System.out.println("Connecting...");
    
    //sending receiving class initiated
	  SendReceiveSocket sendrecsock = new SendReceiveSocket(sock);
    
	  String pathwaystart = "C:\\Users/Tom/TestDoc/";
    
    //create filepath to save file from server
	  String fromserver = "fromserver ";
  		Date fsdate = new Date();
  		String fssdate = fsdate.toString();
  		String fsrsdate = fssdate.replaceAll(":"," ");
  		String fromserverfilename = fromserver.concat(fsrsdate);
  		String fromserverfilepath = fromserverfilename.concat(".xml");
  		String fromserverpath = pathwaystart.concat(fromserverfilepath);
  	//final pathway holder is fromserverpath
  		System.out.println("File will be saved at: " + fromserverpath);
    
	//receive file
  		sendrecsock.ReceiveViaSocket(fromserverpath);
    
    //get the message and sender from the received file and display
  		XmlManip xmlmanip = new XmlManip();
  		String returnedresult = xmlmanip.returnRequired(fromserverpath,"text");
  		System.out.println("message is: " + returnedresult);
  		String returnedsender = xmlmanip.returnRequired(fromserverpath,"sender");
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
    
    //create message xml file writer
  		XmlWriter xmlwriter = new XmlWriter();
    
    //save file to send in format: toserver date and time
  		String toserver = "toserver ";
  		Date date = new Date();
  		String sdate = date.toString();
  		String rsdate = sdate.replaceAll(":"," ");
  		String toserverfilename = toserver.concat(rsdate);
  		String toserverfilepath = toserverfilename.concat(".xml");
  		String toserverpath = pathwaystart.concat(toserverfilepath);
  	//final file path holder is toserverfilepath
  		System.out.println("File to be sent saved locally at: " + toserverpath);
    
	//create xml file
  		xmlwriter.WriteToFile(s,"client", r, toserverfilename);
    
    //send file
  		sendrecsock.SendViaSocket(toserverpath);
    
  		sock.close();
  }
}