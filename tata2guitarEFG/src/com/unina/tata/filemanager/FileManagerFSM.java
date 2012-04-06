package com.unina.tata.filemanager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import com.unina.tata.translator.translator;

public class FileManagerFSM {
	private static String outputname;
	private static File dir;
	/**
	 * @param args
	 * @throws TransformerException 
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws XPathExpressionException 
	 */
	public static void main(String[] args) throws IOException, TransformerException, XPathExpressionException, ParserConfigurationException {
		// TODO Auto-generated method stub
		//input 1 il file xml del guitree ottenuto dal crawler
		translator.tataGuiTreeDocument=ExportFileIntoDocument(args[0]);
		//output il nome del file dot in cui verrà generata l'FSM
		outputname=args[1];
		translator.createFSM();
		writeDotDocumentInFile();
	}
	
	private static Document ExportFileIntoDocument(String filePath) {
		try {
			File f = new File (System.getProperty ("java.class.path"));
			dir = f.getAbsoluteFile ().getParentFile ();
			File file = new File(dir.toString()+"\\"+filePath);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			return doc;		

		}
		catch (Exception e) {			
		    e.printStackTrace();
		    return null;
		  }		
	}
	
	private static void writeDotDocumentInFile() throws IOException {
		Writer output = null;
		File file = new File(outputname +".dot");
		output = new BufferedWriter(new FileWriter(file));
		output.write(translator.dotFile);
		output.close();
		System.out.println("File .dot created"); 
	}

}
