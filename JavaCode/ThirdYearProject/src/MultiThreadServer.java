import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

public class MultiThreadServer extends JFrame {
	//Text area for displaying contents
	private JTextArea jta = new JTextArea();
	
	public static void main (String[] args) {
		new MultiThreadServer();
	}
	
	public MultiThreadServer() {
		//Place text area on the frame
		setLayout(new BorderLayout());
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		setTitle("MultiThreadServer");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try{
			//create server socket
			ServerSocket serverSocket = new ServerSocket(8000);
			jta.append("Server started at " + new Date() + '\n');
			
			//number a client
			int clientNo = 1;
			
			while (true) {
				//listen for a new connection request
				Socket socket = serverSocket.accept();
				
				//display the client number
				jta.append("Starting thread for client " + clientNo + " at " + new Date() + '\n');
				
				//find the client's host name and ip address
				InetAddress inetAddress = socket.getInetAddress();
				jta.append("Client " + clientNo + "'s host name is " + inetAddress.getHostName() + '\n');
				jta.append("Client " + clientNo + "'s IP Address is " + inetAddress.getHostAddress() + '\n');
				
				//create a new task for the connection
				HandleAClient task = new HandleAClient(socket);
				
				//start the new thread
				task.run();
				
				//increment clientNo
				
				clientNo++;
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}
	
//inner class
//define the thread class for handling new connection
class HandleAClient implements Runnable {
	private Socket socket; //a connected socket
	
	//construct a thread
	public HandleAClient(Socket socket) {
		this.socket = socket;
	}
	
	//run a thread
	public void run() {
		try {
			//create data input and output streams
			DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
			DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			
			//continuosly serve the client
			while(true) {
				//recieve radius from client
				double radius = inputFromClient.readDouble();
				
				//compute area
				double area = radius * radius * Math.PI;
				
				//send area back to the client
				outputToClient.writeDouble(area);
				
				jta.append("radius received from client: " + radius + '\n');
				
				XmlWriter writer = new XmlWriter();
				String strradius = Double.toString(radius);
				writer.WriteToFile(strradius,"client","server","clientserver");
				
				jta.append("Area found: " + area + '\n');
				
				String strarea = Double.toString(area);
				writer.WriteToFile(strarea,"server","client","serverclient");
				}
			}
		catch(IOException e) {
			System.err.println(e);
		}
	 }
  }
}