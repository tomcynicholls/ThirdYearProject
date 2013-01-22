import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class TCPClient {

    public static void main(String args[]) {
        

        Socket clientSocket = null;
        InputStream is = null;
        BufferedOutputStream outToServer = null;

        try {
            clientSocket = new Socket("127.0.0.1" ,8000);
            is = clientSocket.getInputStream();
            outToServer = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            // Do exception handling
        }
        System.out.println("receiving from server");
        String file = "C:\\Users/Tom/TestDoc/yyyfromserver.xml";
        SendFile sendthis = new SendFile();
        sendthis.SendThisFile(file,is);
        
        System.out.println("sending to server");
        String file2 = "C:\\Users/Tom/TestDoc/xxx.xml";
        ReceiveFile receivefile = new ReceiveFile();
        receivefile.ReceiveAFile(outToServer, file2);
        
        /*try {
			clientSocket.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}*/
       
    }
}