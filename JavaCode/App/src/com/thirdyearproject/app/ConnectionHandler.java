package com.thirdyearproject.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionHandler extends AsyncTask<String, Void, String>{

//public static String serverip = "192.168.1.11";
public static int serverport = 8500;
Socket s;
public int filesize=6022386; // filesize temporary hardcoded
public int bytesRead;
public int current = 0;
public InputStream is;
public OutputStream os;
public DataInputStream dis;

@Override
protected String doInBackground(String... params) {

    try {
        Log.i("AsyncTank", "doInBackground: Creating Socket");
        //InetAddress serverAddr = InetAddress.getByName(serverip);
        s = new Socket("192.168.1.8", 8000);
    } catch (Exception e) {
        Log.i("AsyncTank", "doInBackground: Cannot create Socket");
    }
    if (s.isConnected()) {
        try {
        	is = s.getInputStream();
        	os = s.getOutputStream();
        	dis = new DataInputStream(s.getInputStream());
            Log.i("AsyncTank", "doInBackground: Socket created, Streams assigned");
            //AppActivity.myuserID = dis.readInt();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.i("AsyncTank", "doInBackground: Cannot assign Streams, Socket not connected");
            e.printStackTrace();
        }
    } else {
        Log.i("AsyncTank", "doInBackground: Cannot assign Streams, Socket is closed");
    }
    
    Log.i("HERE",AppActivity.fromserverpath);
    //writeToStream(AppActivity.privkeyloc);
    readFromStream(AppActivity.fromserverpath);
    
    
    return null;
}

@Override
protected void onPostExecute(String result) {
    Log.i("HERE","BACKGROUND FINISHED");
    Log.i("HERE",AppActivity.fromserverpath);
    
    //readFromStream(AppActivity.fromserverpath);
}

public void writeToStream(String toserverpath) {
    try {
        if (s.isConnected()){
            Log.i("AsynkTask", "writeToStream : Writing");
            Log.i("Sending file start: ", Long.toString(System.currentTimeMillis()));
            String percenttoservpath = toserverpath.replaceAll(" ","%20");
            File myFile = new File (percenttoservpath);
            Log.i("testing","1");
            Log.i("testing",percenttoservpath);
            String myfilesize = Integer.toString((int)myFile.length());
            Log.i("testing",myfilesize);
            byte [] mybytearray2  = new byte [(int)myFile.length()];
            Log.i("testing","1");
            FileInputStream fis = new FileInputStream(new File (percenttoservpath));
            Log.i("testing","1");
            BufferedInputStream bis = new BufferedInputStream(fis);
            Log.i("testing","1");
            bis.read(mybytearray2,0,mybytearray2.length);
            //OutputStream os = s.getOutputStream();
            Log.i("testing","Sending...");
            os.write(mybytearray2,0,mybytearray2.length);
            Log.i("testing","1");
            os.flush();
            Log.i("Sending file start: ", Long.toString(System.currentTimeMillis()));
            //s.close();
            
            
            
        } else {
            Log.i("AsynkTask", "writeToStream : Cannot write to stream, Socket is closed");
        }
    } catch (Exception e) {
        Log.i("AsynkTask", "writeToStream : Writing failed");
    }
}

public void readFromStream(String fromserverpath) {
    try {
        if (s.isConnected()) {
            Log.i("AsynkTask", "readFromStream : Reading message");
            
            byte [] mybytearray  = new byte [filesize];
            Log.i("testing","1");
            InputStream is = s.getInputStream();
            FileOutputStream fos = new FileOutputStream(fromserverpath);
            Log.i("testing","2");
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.i("testing","3");
            //FAILS HERE
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            
            //added code to try and fix:
           /* ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Log.i("testing","4");
    		byte[] buffer = new byte[1024];
    		Log.i("testing","4");
    		int read = 0;
    		Log.i("testing","4");
    		while ((read = is.read(buffer, 0, buffer.length)) != -1) {
    			Log.i("testing","4");
    			baos.writeTo(fos);
    		}		
    		baos.flush();*/
            
            
            Log.i("testing","4");
            current = bytesRead;
            Log.i("testing","5");
            bos.write(mybytearray, 0 , current);
            Log.i("testing","6");
            bos.flush();
            bos.close();
            
            //String decrypted = AppActivity.fullpathwaystart + "decrypted.xml";
            //AppActivity.ende.decrypt(AppActivity.privkeyloc,fromserverpath,decrypted);
            
            XmlManipApp xmlmanip = new XmlManipApp();
            String returnedresult = xmlmanip.returnRequired(fromserverpath,"text");
            Log.i("message is: " , returnedresult);
            
            String returnedsender = xmlmanip.returnRequired(fromserverpath,"sender");
            Log.i("from: " , returnedsender);
            
            AppActivity.recsender = returnedsender;
        	AppActivity.recmessage = returnedresult;
            
        } else {
            Log.i("AsynkTask", "readFromStream : Cannot Read, Socket is closed");
        }
    } catch (Exception e) {
        Log.i("AsynkTask", "readFromStream : Writing failed");
    }
   /* } catch (IndexOutOfBoundsException e) {
    	Log.i("AsyncTask","Index out of bounds exception");
    } catch (IOException x) {
    	Log.i("AsyncTask","IO Exception");
    }*/

}

}