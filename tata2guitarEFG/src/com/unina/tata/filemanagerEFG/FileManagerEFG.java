package com.unina.tata.filemanagerEFG;

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

import com.unina.tata.efgtranslatornew.translatorEFGnew;

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
		translatorEFGnew.tataGuiTreeDocument=ExportFileIntoDocument(args[0]);
		translatorEFGnew.tataEFGDocument=ExportFileIntoDocument(args[1]);
		outputname=args[2];
		translatorEFGnew.translateefg();
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
				
		DOMSource source = new DOMSource(translatorEFGnew.GuitarEFGDocument);
		File fos = new File(dir.toString()+"\\"+outputname);
		//fos.createNewFile();
	    Result result = new StreamResult(fos);
	
		transformer.transform(source, result);
		
	}
}
