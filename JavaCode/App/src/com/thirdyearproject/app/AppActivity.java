package com.thirdyearproject.app;

//import java.security.InvalidAlgorithmParameterException;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.Security;
//import java.security.spec.ECGenParameterSpec;
import java.util.Date;

//import org.spongycastle.jce.provider.BouncyCastleProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



public class AppActivity extends Activity {
    /** Called when the activity is first created. */
	public final static String EXTRA_MESSAGE = "com.thirdyearproject.app.MESSAGE";
	public final static String EXTRA_RECEIVER = "com.thirdyearproject.app.RECEIVER";
	public static String fromserverpath;
	public static Boolean gogogo;
	public static String privkeyloc;
	public static String pubkeyloc;
	public static String servkeyloc;
	
	public static String recsender;
	public static String recmessage;
	public static int myuserID = 4;
	public static String fullpathwaystart;
	public static AppEncryptDecryptAES ende;
	
	//public ConnectionHandler conhandler = new ConnectionHandler();
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.main);
        
      
        gogogo = false;
        
        String pathwaystart = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        fullpathwaystart = pathwaystart.concat("/");
        
        privkeyloc = fullpathwaystart + "myprivatekey.key"; 
    	pubkeyloc = fullpathwaystart + "mypublickey.key"; 
    	servkeyloc = fullpathwaystart + "servkey.key";
        
        //ende = new AppEncryptDecryptAES(privkeyloc);
        
        //ende.encrypt(privkeyloc,fullpathwaystart+"typoutput.xml",fullpathwaystart+"typ.xml");
        
        //create received file pathway
        String fromserver = "fromserver ";
      	Date fsdate = new Date();
      	String fssdate = fsdate.toString();
      	String fsrsdate = fssdate.replaceAll(":"," ");
      	String fromserverfilename = fromserver.concat(fsrsdate);
      	String fromserverfilepath = fromserverfilename.concat(".xml");
      	fromserverpath = fullpathwaystart.concat(fromserverfilepath);
    	Log.i("from server path is: ",fromserverpath);
        
    	//NOTE: UNCOMMENT ALL CONHANDLER RELATED ELEMENTS INORDER TO COMMUNICATE WITH THE SERVER
    	//has been commented out to allow for analysis
      //conhandler.execute();
       
        /*while (gogogo == false)
        {
        	//Log.i("here","not yet");
        }*/
        //Log.i("info","about to read from stream");
        //conhandler.readFromStream(fromserverpath);
        
    	Log.d("Error","Testing error message");
        
        //get and display relevant data from received file
       /*XmlManipApp xmlmanip = new XmlManipApp();
        String returnedresult = xmlmanip.returnRequired(fromserverpath,"text");
        Log.i("message is: " , returnedresult);
        String returnedsender = xmlmanip.returnRequired(fromserverpath,"receiver");
        Log.i("from: " , returnedsender);
        */
       
    }
    
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	
    	// Log.i("info","about to read from stream");
        //conhandler.readFromStream(fromserverpath);
    	
    	Intent intent = new Intent(this, DisplayMessageActivity.class);
    	EditText editText = (EditText) findViewById(R.id.edit_message);
    	EditText editTextReceiver = (EditText) findViewById(R.id.edit_receiver);
    	String message = editText.getText().toString();
    	String receiver = editTextReceiver.getText().toString();
    	
    	//create message xml file
        XmlWriterApp xmlwriter = new XmlWriterApp();
        
        //save file to send in format: toserver date and time
        String spathwaystart = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String sfullpathwaystart = spathwaystart.concat("/");
        
        String toserver = "toserver ";
      	Date date = new Date();
      	String sdate = date.toString();
      	String rsdate = sdate.replaceAll(":"," ");
      	String rstdate = rsdate.replaceAll("%"," ");
      	String toserverfilename = toserver.concat(rstdate);
      	
      	String toserverfilepath = toserverfilename.concat(".xml");
      	String toserverpath = sfullpathwaystart.concat(toserverfilepath);
    	Log.i("to server path is: " , toserverpath);
        
    	String sender = "User: " + Integer.toString(myuserID); 
    	
        xmlwriter.WriteToFile(message,sender, receiver, toserverpath);
        
        //send file
        Log.i("calling:","writeToStream");
        
        //conhandler.writeToStream(toserverpath);
    	
    	intent.putExtra(EXTRA_MESSAGE, message);
    	intent.putExtra(EXTRA_RECEIVER, receiver);
    	startActivity(intent);
    }
    
    public void receiveMessage(View view) {
    	Log.i("User ID is: ",Integer.toString(myuserID));
    	((TextView)findViewById(R.id.textview_from)).setText("User 8");
        ((TextView)findViewById(R.id.textview_message)).setText("How are you?");
    }
    
}