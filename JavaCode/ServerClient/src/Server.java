import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class Server extends JFrame {
	//Text area for displaying contents
	private JTextArea jta = new JTextArea();
	
	public static void main (String[] args) {
		new Server();
	}
	
	public Server() {
		//Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("Server");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try{
			//create server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			jta.append("Server started at " + new Date() + '\n');			
			
			//listen for a connection request
			Socket socket = serverSocket.accept();
			
			//create data input and output streams
			//DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
			//DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			
			BufferedOutputStream outToClient = new BufferedOutputStream(socket.getOutputStream());
			BufferedInputStream inFromClient = new BufferedInputStream(socket.getInputStream());
			//add buffered input here
			
			while (true) {
				//receive radius from the client
				/*double radius = inputFromClient.readDouble();
				
				//compute area
				double area = radius * radius * Math.PI;
				
				//send area back to the client
				outputToClient.writeDouble(area);
				
				jta.append("Radius received from client: " + radius + '\n');
				jta.append("Area found: " + area + '\n');*/
				
				//SEND FILE TO CLIENT
				File myFile = new File("C:\\Users/Tom/TestDoc/yyy.xml");
                byte[] mybytearray = new byte[(int) myFile.length()];
                byte[] aByte = new byte[1];
		        int bytesRead;

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
                    //outToClient.close();
                    

                    // File sent, exit the main method
                    return;
                } catch (IOException ex) {
                    // Do exception handling
                }
                
                //RECEIVE FROM CLIENT
               /* ByteArrayOutputStream baos = new ByteArrayOutputStream();

	            FileOutputStream fos = null;
	            BufferedOutputStream bos = null;
	           
	                fos = new FileOutputStream("C:\\Users/Tom/TestDoc/xxxFromClient.xml");
	                bos = new BufferedOutputStream(fos);
	                bytesRead = inFromClient.read(aByte, 0, aByte.length);

	                do {
	                        baos.write(aByte);
	                        bytesRead = inFromClient.read(aByte);
	                } while (bytesRead != -1);

	                bos.write(baos.toByteArray());
	                bos.flush();
	                bos.close();
                */
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}
}
