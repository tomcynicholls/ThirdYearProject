import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame {
	//Text field for receiving radius
	private JTextField jtf = new JTextField();
	
	//Text area to display contents
	private JTextArea jta = new JTextArea();
	
	//IOStreams
	private DataOutputStream toServer;
	private DataInputStream fromServer;
	
	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		//panel p to hold the label and text field
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new JLabel("Enter Radius"), BorderLayout.CENTER);
		p.add(jtf, BorderLayout.CENTER);
		jtf.setHorizontalAlignment(JTextField.RIGHT);
		
		setLayout(new BorderLayout());
		add(p, BorderLayout.NORTH);
		add(new JScrollPane(jta), BorderLayout.CENTER);
		
		jtf.addActionListener(new ButtonListener()); //register listener

		setTitle("Client");
		setSize(500, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		try {
			//create a socket to connect to the server
			Socket socket = new Socket("192.168.1.11", 8000);
			
			//create an input stream to receive data from the server
			fromServer = new DataInputStream(socket.getInputStream());
			
			//create an output stream to send data to the server
			toServer = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException ex) {
			jta.append(ex.toString() + '\n');
		}
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				//get the radius from the text field
				double radius = Double.parseDouble(jtf.getText().trim());
				
				//send the radius to the server
				toServer.writeDouble(radius);
				toServer.flush();
				
				//get area from the server
				double area = fromServer.readDouble();
				
				//display to the text area
				jta.append("Radius is " + radius + '\n');
				jta.append("Area recieved from the server is " + area + '\n');	
			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}	
}
