package com.thirdyearproject.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

public class AppEncryptDecrypt {

	public static final String ALGORITHM = "RSA";

	public static String PRIVATE_KEY_FILE = "myprivatekey.key";

	public static String PUBLIC_KEY_FILE = "mypublickey.key";

	public static Cipher cipher;
	public static FileInputStream fis;
	public static FileOutputStream fos;
	public static CipherInputStream cis;
	public static CipherOutputStream cos;

	public static ObjectInputStream inputStream;

	public AppEncryptDecrypt(String pub, String priv) {

		PRIVATE_KEY_FILE = priv;
		PUBLIC_KEY_FILE = pub;

		// Check if the pair of keys are present else generate those.
		//if (!areKeysPresent()) {
			// Method generates a pair of keys using the RSA algorithm and
			// stores it
			// in their respective files
			generateKey();
		//}
		try {
			cipher = Cipher.getInstance(ALGORITHM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generateKey() {

		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(2048);
			final KeyPair key = keyGen.generateKeyPair();

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
			publicKeyOS.writeObject(key.getPublic());
			publicKeyOS.close();

			// Saving the Private key in a file
			ObjectOutputStream privateKeyOS = new ObjectOutputStream(new FileOutputStream(privateKeyFile));
			privateKeyOS.writeObject(key.getPrivate());
			privateKeyOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
