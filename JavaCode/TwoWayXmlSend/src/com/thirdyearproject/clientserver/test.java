package com.thirdyearproject.clientserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class test {

	public static void main(String[] args) throws Exception {

		/*
		 * EncryptDecrypt ende = new EncryptDecrypt(); String cleartextFile =
		 * "cleartext.xml"; String ciphertextFile = "ciphertextRSA.xml";
		 * 
		 * ende.encrypt("mypublickey.key",cleartextFile,ciphertextFile);
		 * 
		 * 
		 * 
		 * String cleartextAgainFile = "cleartextAgainRSA.xml";
		 * 
		 * ende.decrypt(ciphertextFile,cleartextAgainFile);
		 */

		/*String test1 = "this is a test";
		String[] test = test1.split(" ");
		System.out.println(test1);
		System.out.println(test[0] + test[1] + test[2] + test[3]);

		BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
		out.write(test1);
		out.close();

		String fromfile;
		BufferedReader in = new BufferedReader(new FileReader("test.txt"));
		fromfile = in.readLine();
		in.close();

		String[] test2 = fromfile.split(" ");
		System.out.println(fromfile);
		System.out.println(test2[1] + test2[1] + test2[0] + test2[3]);
		
		String[] testing = new String[4];
		testing[1] = "hello";
		testing[2] = null;
		String here = null;
		if (here == null) {
			System.out.println("IT WORKS");
		}*/
		
		for (int i = 0; i<10; i++) {
		String userID = "1";
		String requserID = "2";
		Long time = System.currentTimeMillis();
		String thetime = time.toString();
		String thefinal = userID + " " + requserID + " " + thetime;
		System.out.println(thefinal);

		BufferedWriter out = new BufferedWriter(new FileWriter("test.txt"));
		out.write(thefinal);
		out.close();

		String fromfile;
		BufferedReader in = new BufferedReader(new FileReader("test.txt"));
		fromfile = in.readLine();
		in.close();

		String[] test2 = fromfile.split(" ");
		//System.out.println(fromfile);
		//System.out.println(test2[0] + test2[1] + test2[2]);
		}
	}
}