package com.thirdyearproject.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity {
	
	public static Long rsagenstart;
	public static Long rsagenfin;
	public static Long rsaenstart;
	public static Long rsaenfin;
	public static Long rsadestart;
	public static Long rsadefin;
	public static Long rsagen;
	public static Long rsaen;
	public static Long rsade;
	
	public static Long aesgenstart;
	public static Long aesgenfin;
	public static Long aesenstart;
	public static Long aesenfin;
	public static Long aesdestart;
	public static Long aesdefin;
	public static Long aesgen;
	public static Long aesen;
	public static Long aesde;
	
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        
     // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(AppActivity.EXTRA_MESSAGE);
        String mreceiver = intent.getStringExtra(AppActivity.EXTRA_RECEIVER);
        // Create the text view
        ((TextView)findViewById(R.id.show_message)).setText(message);
        ((TextView)findViewById(R.id.show_receiver)).setText(mreceiver);
        // Set the text view as the activity layout
       // setContentView(textView);
        
        //Security.addProvider(new FlexiCoreProvider());
        
        //try{
        RSA();
        //} catch (Exception ex){
        //	Log.i("ERROR","ERROR WITH RSA");
       // }
        
        //try{
            AES();
           // } catch (Exception ex){
            //	Log.i("ERROR WITH AES","ERROR WITH AES");
           // }
            
            ((TextView)findViewById(R.id.rsa)).setText("RSA");
            ((TextView)findViewById(R.id.aes)).setText("AES");
            ((TextView)findViewById(R.id.rsa_gen)).setText("Generate Keys: " + Long.toString(rsagenfin - rsagenstart));
            ((TextView)findViewById(R.id.rsa_en)).setText("Encryption: " + Long.toString(rsaenfin - rsaenstart));
            ((TextView)findViewById(R.id.rsa_de)).setText("Decryption: " + Long.toString(rsadefin - rsadestart));
            ((TextView)findViewById(R.id.aes_gen)).setText("Generate Keys: " + Long.toString(aesgenfin - aesgenstart));
            ((TextView)findViewById(R.id.aes_en)).setText("Encryption: " + Long.toString(aesenfin - aesenstart));
            ((TextView)findViewById(R.id.aes_de)).setText("Decryption: " + Long.toString(aesdefin - aesdestart));
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /** Called when the user clicks the Send button */
    public void returnButton(View view) {
    	//changed to return instead of show xml using below code
    	/*Intent intent = new Intent(this, ViewXmlWriter.class);
    	String message = ((TextView)findViewById(R.id.show_message)).getText().toString();
    	String mreceiver = ((TextView)findViewById(R.id.show_receiver)).getText().toString();
    	intent.putExtra(AppActivity.EXTRA_MESSAGE, message);
    	intent.putExtra(AppActivity.EXTRA_RECEIVER, mreceiver);
    	startActivity(intent);*/
    	
    	Intent intent = new Intent(this, AppActivity.class);
    	startActivity(intent);
    }
    
    public void RSA() {
    	
    	try {
    	//Security.addProvider(new FlexiCoreProvider());
    	Log.i("RSA Start - Current time: ", Long.toString(System.currentTimeMillis()));
    	rsagenstart = System.currentTimeMillis();
    	//KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "FlexiCore");
    	//Cipher cipher = Cipher.getInstance("RSA", "FlexiCore");
    	
    	KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
    	Cipher cipher = Cipher.getInstance("RSA");

    	kpg.initialize(2048);
    	KeyPair keyPair = kpg.generateKeyPair();
    	PrivateKey privKey = keyPair.getPrivate();
    	PublicKey pubKey = keyPair.getPublic();
    	
    	Log.i("Generated keys - Current time: ", Long.toString(System.currentTimeMillis()));
    	rsagenfin = System.currentTimeMillis();
    	//System.out.println(System.currentTimeMillis());
    	cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    	
    	Log.i("Cipher initialised for encryption - Current time: ", Long.toString(System.currentTimeMillis()));
    	rsaenstart = System.currentTimeMillis();
    	
    	String cleartextFile = AppActivity.fullpathwaystart + "typoutput.txt";
    	String ciphertextFile = AppActivity.fullpathwaystart + "ciphertextRSA.txt";
    	
    	//Log.i("cleartextFile loc" , cleartextFile);
    	
    	FileInputStream fis = new FileInputStream(cleartextFile);
    	FileOutputStream fos = new FileOutputStream( new File(ciphertextFile));
    	CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    	byte[] block = new byte[32];
    	int i;
    	while ((i = fis.read(block)) != -1) {
    	cos.write(block, 0, i);
    	}
    	cos.close();
    	
    	Log.i("Encrypted - Current time: ", Long.toString(System.currentTimeMillis()));
    	rsaenfin = System.currentTimeMillis();
    	//System.out.println(System.currentTimeMillis());
    	
    	
    	String cleartextAgainFile = AppActivity.fullpathwaystart +  "cleartextAgainRSA.txt";
    	rsadestart = System.currentTimeMillis();
    	cipher.init(Cipher.DECRYPT_MODE, privKey);
    	
    	Log.i("Cipher initilised for decryption - Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	

    	fis = new FileInputStream(ciphertextFile);
    	CipherInputStream cis = new CipherInputStream(fis, cipher);
    	fos = new FileOutputStream(cleartextAgainFile);

    	while ((i = cis.read(block)) != -1) {
    	fos.write(block, 0, i);
    	}
    	fos.close();
    	Log.i("Decrypted - Current time: ", Long.toString(System.currentTimeMillis()));
    	rsadefin = System.currentTimeMillis();
    	
    } catch (FileNotFoundException ex) {
    	Log.i("EXCEPTION", "FILE NOT FOUND EXCEPTION");
    }catch (IOException ex) {
    	Log.i("EXCEPTION", "IO EXCEPTION");
    }catch (InvalidKeyException ex) {
    	Log.i("EXCEPTION", "INVALID KEY EXCEPTION");
    }catch (NoSuchAlgorithmException ex) {
    	Log.i("EXCEPTION", "NO SUCH ALGORITHM EXCEPTION");
    }catch (NoSuchPaddingException ex) {
    	Log.i("EXCEPTION", "NO SUCH PADDING EXCEPTION");
    }
    }
    
    public void AES() {
    
    	
    try {
    	//Security.addProvider(new FlexiCoreProvider());

    	//Cipher cipher = Cipher.getInstance("AES128_CBC", "FlexiCore");

    	//KeyGenerator keyGen = KeyGenerator.getInstance("AES", "FlexiCore");
    	Log.i("AES Start - Current time: ", Long.toString(System.currentTimeMillis()));
    	aesgenstart = System.currentTimeMillis();
    		Cipher cipher = Cipher.getInstance("AES");	
    		KeyGenerator keyGen = KeyGenerator.getInstance("AES");

    	SecretKey secKey = keyGen.generateKey();
    	
    	Log.i("Generated key - Current time: ", Long.toString(System.currentTimeMillis()));
    	aesgenfin = System.currentTimeMillis();
    	
    	cipher.init(Cipher.ENCRYPT_MODE, secKey);

    	Log.i("Initialised Cipher for encryption - Current time: ", Long.toString(System.currentTimeMillis()));
    	aesenstart = System.currentTimeMillis();
    	
    	String cleartextFile = AppActivity.fullpathwaystart + "typoutput.txt";
    	String ciphertextFile = AppActivity.fullpathwaystart + "ciphertextSymm.txt";

    	//Log.i("Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	FileInputStream fis = new FileInputStream( new File(cleartextFile));
    	
    	//Log.i("Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	FileOutputStream fos = new FileOutputStream(ciphertextFile);
    	
    	//Log.i("Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	CipherOutputStream cos = new CipherOutputStream(fos, cipher);

    	//Log.i("Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	byte[] block = new byte[8];
    	int i;
    	while ((i = fis.read(block)) != -1) {
    	cos.write(block, 0, i);
    	}
    	cos.close();
    	String cleartextAgainFile = AppActivity.fullpathwaystart +"cleartextAgainSymm.txt";

    	Log.i("File encrypted - Current time: ", Long.toString(System.currentTimeMillis()));
    	aesenfin = System.currentTimeMillis();
    	
    	aesdestart = System.currentTimeMillis();
    	cipher.init(Cipher.DECRYPT_MODE, secKey);

    	Log.i("Initialised cipher for decryption - Current time: ", Long.toString(System.currentTimeMillis()));
    	
    	fis = new FileInputStream(ciphertextFile);
    	CipherInputStream cis = new CipherInputStream(fis, cipher);
    	fos = new FileOutputStream(cleartextAgainFile);

    	while ((i = cis.read(block)) != -1) {
    	fos.write(block, 0, i);
    	}
    	fos.close();
    	
    	Log.i("File decrypted - Current time: ", Long.toString(System.currentTimeMillis()));
    	aesdefin = System.currentTimeMillis();
 
    } catch (FileNotFoundException ex) {
    	Log.i("EXCEPTION", "FILE NOT FOUND EXCEPTION");
    }catch (IOException ex) {
    	Log.i("EXCEPTION", "IO EXCEPTION");
    }catch (InvalidKeyException ex) {
    	Log.i("EXCEPTION", "INVALID KEY EXCEPTION");
    }catch (NoSuchAlgorithmException ex) {
    	Log.i("EXCEPTION", "NO SUCH ALGORITHM EXCEPTION");
    }catch (NoSuchPaddingException ex) {
    	Log.i("EXCEPTION", "NO SUCH PADDING EXCEPTION");
    }
    
    }
}
