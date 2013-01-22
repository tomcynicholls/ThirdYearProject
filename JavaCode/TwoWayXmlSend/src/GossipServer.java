import java.io.*;
import java.net.*;
public class GossipServer
{
  public static void main(String[] args) throws Exception
  {
      ServerSocket sersock = new ServerSocket(8000);
      System.out.println("Server  ready for chatting");
      Socket sock = sersock.accept( );                          
                               // reading from keyboard (keyRead object)
      BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
	                      // sending to client (pwrite object)
      OutputStream ostream = sock.getOutputStream(); 
      PrintWriter pwrite = new PrintWriter(ostream, true);
 
                             // receiving from server ( receiveRead  object)
      InputStream istream = sock.getInputStream();
      BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));
      
      InputStream is = sock.getInputStream();
      byte[] aByte = new byte[1];
      int bytesRead;
 
      String receiveMessage, sendMessage;               
      while(true)
      {
        if((receiveMessage = receiveRead.readLine( )) != null)  
        {
           System.out.println(receiveMessage); 
           
           ByteArrayOutputStream baos = new ByteArrayOutputStream();

           

               FileOutputStream fos = null;
               BufferedOutputStream bos = null;
               try {
                   fos = new FileOutputStream("C:\\Users/Tom/TestDoc/xxxfromclient.xml");
                   bos = new BufferedOutputStream(fos);
                   bytesRead = is.read(aByte, 0, aByte.length);

                   do {
                           baos.write(aByte);
                           bytesRead = is.read(aByte);
                   } while (bytesRead != -1);

                   bos.write(baos.toByteArray());
                   bos.flush();
                   bos.close();
                   //clientSocket.close();
               } catch (IOException ex) {
                   // Do exception handling
               }
        }         
        sendMessage = keyRead.readLine(); 
        pwrite.println(sendMessage);             
        System.out.flush(); 
        
      }               
    }                    
}