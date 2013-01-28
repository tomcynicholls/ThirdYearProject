import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {

    //private final String fileToSend = "C:\\Users/Tom/TestDoc/yyy.xml";

    public static void main(String args[]) {

        while (true) {
            ServerSocket welcomeSocket = null;
            Socket connectionSocket = null;
            InputStream is = null;
            BufferedOutputStream outToClient = null;

            try {
                welcomeSocket = new ServerSocket(8000);
                connectionSocket = welcomeSocket.accept();
                is = connectionSocket.getInputStream();
                outToClient = new BufferedOutputStream(connectionSocket.getOutputStream());
            } catch (IOException ex) {
                // Do exception handling
            }
           
            System.out.println("sending to client");
            String file = "C:\\Users/Tom/TestDoc/yyy.xml";
            ReceiveFile receivefile = new ReceiveFile();
            receivefile.ReceiveAFile(outToClient, file);
            
            System.out.println("receiving from client");
            String file2 = "C:\\Users/Tom/TestDoc/xxxfromclient.xml";
            SendFile sendthis = new SendFile();
            sendthis.SendThisFile(file2,is);
           /* try {
				connectionSocket.close();
			} catch (IOException e) {
				
				e.printStackTrace();
			}*/

            
           /* if (outToClient != null) {
                File myFile = new File("C:\\Users/Tom/TestDoc/yyy.xml");
                byte[] mybytearray = new byte[(int) myFile.length()];

                FileInputStream fis = null;

                try {
                    fis = new FileInputStream(myFile);
                } catch (FileNotFoundException ex) {
                    // Do exception handling
                }
                BufferedInputStream bis = new BufferedInputStream(fis);

                try {
                    bis.read(mybytearray, 0, mybytearray.length);
                    outToClient.write(mybytearray, 0, mybytearray.length);
                    outToClient.flush();
                    outToClient.close();
                    connectionSocket.close();

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                    // Do exception handling
                }
            }*/
        }
    }
}