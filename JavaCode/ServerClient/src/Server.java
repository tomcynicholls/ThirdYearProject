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
			
			String test = "test";
			String testa = "tom";
			String testb = "liz";
			String testc = "yyy";
			
			XmlWriter writer = new XmlWriter();
			writer.WriteToFile(test,testa,testb,testc);
			
			
			//listen for a connection request
			Socket socket = serverSocket.accept();
			
			//create data input and output streams
			DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
			DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());
			
			
			while (true) {
				//receive radius from the client
				double radius = inputFromClient.readDouble();
				
				//compute area
				double area = radius * radius * Math.PI;
				
				//send area back to the client
				outputToClient.writeDouble(area);
				
				jta.append("Radius received from client: " + radius + '\n');
				jta.append("Area found: " + area + '\n');
			}
		}
		catch(IOException ex) {
			System.err.println(ex);
		}
	}
}
