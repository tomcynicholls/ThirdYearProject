import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client extends JFrame {
	//Text field for receiving radius
	private JTextField jtf = new JTextField();
	
	//Text area to display contents
	private JTextArea jta = new JTextArea();
	
	//IOStreams
	private OutputStream toServer;
	private InputStream fromServer;
	
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
			Socket socket = new Socket("localhost", 8000);
			
			//create an input stream to receive data from the server
			fromServer = socket.getInputStream();
			
			//create an output stream to send data to the server
			toServer = socket.getOutputStream();
		}
		catch (IOException ex) {
			jta.append(ex.toString() + '\n');
		}
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			while (true) {
			try {
				//get the radius from the text field
				double radius = Double.parseDouble(jtf.getText().trim());
				byte[] aByte = new byte[1];
		        int bytesRead;
				
				//RECEIVE FILE FROM SERVER
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

		            FileOutputStream fos = null;
		            BufferedOutputStream bos = null;
		           
		                fos = new FileOutputStream("C:\\Users/Tom/TestDoc/yyyFromServer.xml");
		                bos = new BufferedOutputStream(fos);
		                bytesRead = fromServer.read(aByte, 0, aByte.length);

		                do {
		                        baos.write(aByte);
		                        bytesRead = fromServer.read(aByte);
		                } while (bytesRead != -1);

		                bos.write(baos.toByteArray());
		                bos.flush();
		                bos.close();
		                
		         //SEND FILE TO SERVER
		               /* File myFile = new File("C:\\Users/Tom/TestDoc/xxx.xml");
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
		                    toServer.write(mybytearray, 0, mybytearray.length);
		                    toServer.flush();
		                    //toServer.close();
		                    

		                    // File sent, exit the main method
		                    return;
		                } catch (IOException ex) {
		                    // Do exception handling
		                }      
		           
				*/
				//get area from the server
				//double area = fromServer.readDouble();
				
				//display to the text area
				//jta.append("Radius is " + radius + '\n');
				//jta.append("Area recieved from the server is " + area + '\n');	
			}
			catch (IOException ex) {
				System.err.println(ex);
			}
		}
		}
	}	
}