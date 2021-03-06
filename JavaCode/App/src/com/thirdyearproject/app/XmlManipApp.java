package com.thirdyearproject.app;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;


public class XmlManipApp {

	String mainmessage = null;
	String returnrequired = null;
	
	public XmlManipApp(){
		
	}
	
	
public String returnRequired(String filepath, String required) {
		
		try {
			 
			File fXmlFile = new File(filepath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			NodeList nList = doc.getElementsByTagName("message");	
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					returnrequired = eElement.getElementsByTagName(required).item(0).getTextContent();
					
				}
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		Log.i("about to return:",returnrequired);
	return returnrequired;	
	}
	
	
}