package com.thirdyearproject.clientserver;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import de.flexiprovider.common.ies.IESParameterSpec;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.ec.parameters.CurveParams;
import de.flexiprovider.ec.parameters.CurveRegistry.BrainpoolP160r1;

public class EncryptDecryptECC {

	public static final String ALGORITHM = "RSA";

	public static String PRIVATE_KEY_FILE = "myprivatekey.key";

	public static String PUBLIC_KEY_FILE = "mypublickey.key";

	public static Cipher cipher;
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static CipherInputStream cis;
	public static CipherOutputStream cos;
	public static IESParameterSpec iesParams;

	public static ObjectInputStream inputStream;

	public EncryptDecryptECC(String pub, String priv) {

		//Provider p1 = Security.getProvider("SunEC");
		
		Security.addProvider(new FlexiCoreProvider());
		Security.addProvider(new FlexiECProvider());
		
		PRIVATE_KEY_FILE = priv;
		PUBLIC_KEY_FILE = pub;

		// Check if the pair of keys are present else generate those.
		if (!areKeysPresent()) {
			// Method generates a pair of keys using the RSA algorithm and
			// stores it
			// in their respective files
			generateKey();
		}
		try {
			//cipher = Cipher.getInstance("ECIES");
			cipher = Cipher.getInstance("ECIES", "FlexiEC");
			
			//iesParams = new IESParameterSpec("AES128-CBC","HmacSHA1", null, null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateKey() {

		try {
			//final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECIES");
			//keyGen.initialize(256);
			//final KeyPair key = keyGen.generateKeyPair();
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");
			
			CurveParams ecParams = new BrainpoolP160r1();
			
			kpg.initialize(ecParams, new SecureRandom());
			KeyPair keyPair = kpg.generateKeyPair();
			

			File privateKeyFile = new File(PRIVATE_KEY_FILE);
			File publicKeyFile = new File(PUBLIC_KEY_FILE);

			// Create files to store public and private key
			if (privateKeyFile.getParentFile() != null) {
				privateKeyFile.getParentFile().mkdirs();
			}
			privateKeyFile.createNewFile();

			if (publicKeyFile.getParentFile() != null) {
				publicKeyFile.getParentFile().mkdirs();
			}
			publicKeyFile.createNewFile();

			// Saving the Public key in a file
			ObjectOutputStream publicKeyOS = new ObjectOutputStream(new FileOutputStream(publicKeyFile));
			publicKeyOS.writeObject(keyPair.getPublic());
			
			//publicKeyOS.writeObject(pubKey);
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(keyPair.getPrivate());
			//privateKeyOS.writeObject(privKey);
			privateKeyOS.close();
			
			//savePrivKey(privKey,privateKeyFile);
			//savePubKey(pubKey,publicKeyFile);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*public static void savePrivKey(PrivateKey key, File file) throws IOException  {
	    byte[] encoded = key.getEncoded();
	    //String data = new BigInteger(1, encoded).toString(16);
	    //writeStringToFile(file, data);
	    
	    BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(data);
		out.close();	    
	    
	}
	
	public static void savePubKey(PublicKey key, File file) throws IOException  {
	    byte[] encoded = key.getEncoded();
	    String data = new BigInteger(1, encoded).toString(16);
	    //writeStringToFile(file, data);
	    
	    BufferedWriter out = new BufferedWriter(new FileWriter(file));
		out.write(data);
		out.close();	    
	    
	}
	public static PrivateKey loadPrivateKey(File file) throws IOException {
	    //String hex = new String(readFileToByteArray(file));
	    
	    BufferedReader in = new BufferedReader(new FileReader(file));
		String hex = in.readLine();
		in.close();
	    
	    byte[] encoded = new BigInteger(hex, 16).toByteArray();
	    PrivateKey key = new PrivateKeySpec(encoded, "ECIES");
	    return key;
	}
	
	public static PublicKey loadPublicKey(File file) throws IOException {
	    //String hex = new String(readFileToByteArray(file));
	    
	    BufferedReader in = new BufferedReader(new FileReader(file));
		String hex = in.readLine();
		in.close();
	    
	    byte[] encoded = new BigInteger(hex, 16).toByteArray();
	    SecretKey key = new SecretKeySpec(encoded, "AES");
	    return key;
	}
	*/
	
	/**
	 * The method checks if the pair of public and private key has been
	 * generated.
	 * 
	 * @return flag indicating if the pair of keys were generated.
	 */
	public static boolean areKeysPresent() {

		File privateKey = new File(PRIVATE_KEY_FILE);
		File publicKey = new File(PUBLIC_KEY_FILE);

		if (privateKey.exists() && publicKey.exists()) {
			return true;
		}
		return false;
	}

	public void encrypt(String pubkeyloc, String input, String output) {

		try {

			inputStream = new ObjectInputStream(new FileInputStream(pubkeyloc));
			final PublicKey pubkey = (PublicKey) inputStream.readObject();

			cipher.init(Cipher.ENCRYPT_MODE, pubkey);
			//cipher.init(Cipher.ENCRYPT_MODE, pubkey, iesParams);

			fis = new FileInputStream(input);
			fos = new FileOutputStream(output);
			cos = new CipherOutputStream(fos, cipher);

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void decrypt(String input, String output) {

		try {

			inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
			final PrivateKey privkey = (PrivateKey) inputStream.readObject();

			cipher.init(Cipher.DECRYPT_MODE, privkey);
			//cipher.init(Cipher.DECRYPT_MODE, privkey, iesParams);

			fis = new FileInputStream(input);
			cis = new CipherInputStream(fis, cipher);
			fos = new FileOutputStream(output);

			String test;
			byte[] block = new byte[32];
			int i;
			while ((i = cis.read(block)) != -1) {
				test = new String(block);
				System.out.println("block: " + test);
				fos.write(block, 0, i);
			}
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}