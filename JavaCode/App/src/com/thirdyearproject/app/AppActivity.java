package com.thirdyearproject.app;

import java.util.Date;

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
	
	public static String recsender;
	public static String recmessage;
	
	public ConnectionHandler conhandler = new ConnectionHandler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        gogogo = false;
        
        String pathwaystart = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fullpathwaystart = pathwaystart.concat("/");
        
        //create received file pathway
        String fromserver = "fromserver ";
      	Date fsdate = new Date();
      	String fssdate = fsdate.toString();
      	String fsrsdate = fssdate.replaceAll(":"," ");
      	String fromserverfilename = fromserver.concat(fsrsdate);
      	String fromserverfilepath = fromserverfilename.concat(".xml");
      	fromserverpath = fullpathwaystart.concat(fromserverfilepath);
    	Log.i("from server path is: ",fromserverpath);
        
    	
        conhandler.execute();
       
        /*while (gogogo == false)
        {
        	//Log.i("here","not yet");
        }*/
        //Log.i("info","about to read from stream");
        //conhandler.readFromStream(fromserverpath);
        
        
        //get and display relevant data from received file
       /* XmlManipApp xmlmanip = new XmlManipApp();
        String returnedresult = xmlmanip.returnRequired(fromserverpath,"text");
        Log.i("message is: " , returnedresult);
        String returnedsender = xmlmanip.returnRequired(fromserverpath,"receiver");
        Log.i("from: " , returnedsender);*/
        
    }
    
    
    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
    	
    	// Log.i("info","about to read from stream");
        // conhandler.readFromStream(fromserverpath);
    	
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
        
        xmlwriter.WriteToFile(message,"app", receiver, toserverpath);
        
        //send file
        Log.i("calling:","writeToStream");
        conhandler.writeToStream(toserverpath);
    	
    	intent.putExtra(EXTRA_MESSAGE, message);
    	intent.putExtra(EXTRA_RECEIVER, receiver);
    	startActivity(intent);
    }
    
    public void receiveMessage(View view) {
    	((TextView)findViewById(R.id.textview_from)).setText(recsender);
        ((TextView)findViewById(R.id.textview_message)).setText(recmessage);
    }
    
}