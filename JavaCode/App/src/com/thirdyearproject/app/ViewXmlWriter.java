package com.thirdyearproject.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewXmlWriter extends Activity {

	@SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_xml_writer);

        // Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
     // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(AppActivity.EXTRA_MESSAGE);
        String mreceiver = intent.getStringExtra(AppActivity.EXTRA_RECEIVER);
        
        //xml writer and display
        TextView xmlResult = (TextView) findViewById(R.id.xmlresult);
        
        String messagetext = message;
    	String sendertext = "sender";
    	String receivertext = mreceiver;

        try {
        	DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            
            //same as server xmlwriter.java
            Element rootElement = document.createElement("xmlmessage");
    		document.appendChild(rootElement);
            Element mainmessage = document.createElement("mainmessage");
    		rootElement.appendChild(mainmessage);
     
    		// set attribute to message element
    		Attr attr = document.createAttribute("id");
    		attr.setValue("1");
    		mainmessage.setAttributeNode(attr);
     
    		// shorten way
    		// staff.setAttribute("id", "1");
     
    		// sender elements
    		Element sender = document.createElement("sender");
    		sender.appendChild(document.createTextNode(sendertext));
    		mainmessage.appendChild(sender);
     
    		// receiver elements
    		Element receiver = document.createElement("receiver");
    		receiver.appendChild(document.createTextNode(receivertext));
    		mainmessage.appendChild(receiver);

    				
    		// text elements
    		Element text = document.createElement("text");
    		text.appendChild(document.createTextNode(messagetext));
    		mainmessage.appendChild(text);
     
    		// other elements
    		Element other = document.createElement("other");
    		other.appendChild(document.createTextNode("19191919191"));
    		mainmessage.appendChild(other);
    		//end similarity

    		//here
    		/*TransformerFactory transformerFactory = TransformerFactory.newInstance();
       		Transformer stransformer = transformerFactory.newTransformer();
       		DOMSource ssource = new DOMSource(document);
       		
       		String path = "typoutput.xml";
       		
       		StreamResult sresult = new StreamResult(new File(path));
       		stransformer.transform(ssource, sresult);
       		//to here*/
    		
    		//method straight from developer android
    		
    		String state = Environment.getExternalStorageState();
    	    if (Environment.MEDIA_MOUNTED.equals(state))
    	    {
    	    	
    	    	TransformerFactory transformerFactory = TransformerFactory.newInstance();
           		Transformer stransformer = transformerFactory.newTransformer();
           		DOMSource ssource = new DOMSource(document);
           		
           		String path = "typoutputs.xml";
           		
           		
           		StreamResult sresult = new StreamResult(new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), path));
           		stransformer.transform(ssource, sresult);
           	
           		Log.d("here",this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
    	    }
    	    else
    	    {
    	    	Log.i("EXTERNAL STORAGE ERROR","ERROR");
    	    }
    		//until here
    		    		
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            Properties outFormat = new Properties();
            outFormat.setProperty(OutputKeys.INDENT, "yes");
            outFormat.setProperty(OutputKeys.METHOD, "xml");
            outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            outFormat.setProperty(OutputKeys.VERSION, "1.0");
            outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(outFormat);
            DOMSource domSource = 
            new DOMSource(document.getDocumentElement());
            OutputStream output = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(output);
            transformer.transform(domSource, result);
            String xmlString = output.toString();
            xmlResult.setText(xmlString);
            
            //FileOutputStream fOut = openFileOutput("samplefile3.txt",MODE_PRIVATE);
           // OutputStreamWriter osw = new OutputStreamWriter(fOut); 

			// Write the string to the file
			//osw.write(xmlString);
			
			/* ensure that everything is
			* really written out and close */
			//osw.flush();
			//osw.close();
         
			//FileInputStream fIn = openFileInput("samplefile3.txt");
	       // InputStreamReader isr = new InputStreamReader(fIn);

	        /* Prepare a char-Array that will
	         * hold the chars we read back in. */
	        //char[] inputBuffer = new char[xmlString.length()];

	        // Fill the Buffer with data from the file
	        //isr.read(inputBuffer);

	        // Transform the chars to a String
	        //String readString = new String(inputBuffer);

	        // Check if we read back the same chars that we had written out
	        //boolean isTheSame = xmlString.equals(readString);

	       // Log.i("File Reading stuff", "success = " + isTheSame);
	        
	       // xmlResult.setText(readString);
			
			
        } catch (ParserConfigurationException e) {
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        } //catch (IOException ioe) 
       // {ioe.printStackTrace();}

        
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

}
