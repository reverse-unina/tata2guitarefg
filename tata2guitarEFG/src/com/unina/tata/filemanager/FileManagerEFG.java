package com.unina.tata.filemanager;

import java.io.File;
import java.io.IOException;


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

import com.unina.tata.efgtranslator.*;

public class FileManagerEFG {
	private static File dir;
	private static String outputname;
	/**
	 * @param args
	 * @throws XPathExpressionException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, IOException, TransformerException {
		// TODO Auto-generated method stub
		//input 1 il file xml del guitree ottenuto dal crawler
		translator.tataGuiTreeDocument=ExportFileIntoDocument(args[0]);
		//input 2 il file xml dell'EFG ottenuto dal crawler
		translator.tataEFGDocument=ExportFileIntoDocument(args[1]);
		//output il nome del file xml in cui verrà generata l'efg in formato guitar
		outputname=args[2];
		translator.translateefg();
		writeGuitarDocumentInFile();
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
	
	private static void writeGuitarDocumentInFile() throws IOException, TransformerException {
		TransformerFactory transformerFactory =  TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.METHOD,"xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				
		DOMSource source = new DOMSource(translator.GuitarEFGDocument);
		File fos = new File(dir.toString()+"\\"+outputname);
		//fos.createNewFile();
	    Result result = new StreamResult(fos);
	
		transformer.transform(source, result);
		
	}
}
