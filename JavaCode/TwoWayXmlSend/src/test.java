import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;



public class test {

	public static void main(String[] args) throws Exception {
		
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		Cipher cipher = Cipher.getInstance("RSA");
 
		kpg.initialize(2048);
		KeyPair keyPair = kpg.generateKeyPair();
		PrivateKey privKey = keyPair.getPrivate();
		PublicKey pubKey = keyPair.getPublic();
		//String pk = pubKey.getEncoded().toString();
		
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		
		String cleartextFile = "cleartext.txt";
		String ciphertextFile = "ciphertextRSA.txt";
		 
		FileInputStream fis = new FileInputStream(cleartextFile);
		FileOutputStream fos = new FileOutputStream(ciphertextFile);
		CipherOutputStream cos = new CipherOutputStream(fos, cipher);
		
		String test;
		
		byte[] block = new byte[32];
		int i;
		while ((i = fis.read(block)) != -1) {
			System.out.println("i is: " + i);
			test = new String(block);
			System.out.println("block: " + test);
			cos.write(block, 0, i);
		}
	
		cos.close();
		
		String cleartextAgainFile = "cleartextAgainRSA.txt";
		
		cipher.init(Cipher.DECRYPT_MODE, privKey);
		
		fis = new FileInputStream(ciphertextFile);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		fos = new FileOutputStream(cleartextAgainFile);
		
		while ((i = cis.read(block)) != -1) {
			test = new String(block);
			System.out.println("block: " + test);
			fos.write(block, 0, i);
		}
		fos.close();	
		
		System.out.println("finished");
	}
}
