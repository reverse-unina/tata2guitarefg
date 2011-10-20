package com.unina.tata.efgtranslatornew;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class translatorEFGnew {
	public static Document tataEFGDocument;
	public static Document tataGuiTreeDocument;
	public static Document GuitarEFGDocument;
	private static ArrayList<String> eventsList;
	
	
	public static void translateefg() throws XPathExpressionException, ParserConfigurationException {
		createGuitarDocument();
		EventListInGuitar();
		MatrixInGuitar();
		System.out.println("DONE");
	}
	
	private static void EventListInGuitar() throws XPathExpressionException {
		eventsList = new ArrayList<String>();		 
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("//EVENT");
		Object result = expr.evaluate(tataEFGDocument, XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;	
		for (int i=0; i<nodes.getLength();i++) {
			
			Element elem = (Element) nodes.item(i);
			//cerchiamo gli input relativi all'evento corrente
			XPath eventXpath = XPathFactory.newInstance().newXPath();
			XPathExpression eventExpr = eventXpath.compile("//EVENT[@id='"+elem.getAttribute("id")+"']");
			Object eventResult = eventExpr.evaluate(tataGuiTreeDocument, XPathConstants.NODESET);
			NodeList eventNodes = (NodeList) eventResult;	
			NodeList inputs = ((Element) eventNodes.item(0).getParentNode()).getElementsByTagName("INPUT");
			
				
			for(int j=0; j<inputs.getLength();j++) {
				Element ielem = (Element) inputs.item(j);
				
				Element eventid = GuitarEFGDocument.createElement("EventId");
				eventid.setTextContent(ielem.getAttribute("id"));
				
				Element widgetid = GuitarEFGDocument.createElement("WidgetId");
				widgetid.setTextContent(ielem.getAttribute("input_id"));
				
				Element eventType = GuitarEFGDocument.createElement("Type");
				eventType.setTextContent("-");
				
				
				Element initialEvent = GuitarEFGDocument.createElement("Initial");
				
				if(elem.getParentNode().getNodeName()=="EFG"){
					initialEvent.setTextContent("true");
				} else {
					initialEvent.setTextContent("false");
				}
				
				Element actionEvent = GuitarEFGDocument.createElement("Action");
				actionEvent.setTextContent("input");
				
				Element event = GuitarEFGDocument.createElement("Event");
				event.appendChild(eventid);
				event.appendChild(widgetid);
				event.appendChild(eventType);
				event.appendChild(initialEvent);
				event.appendChild(actionEvent);
				GuitarEFGDocument.getElementsByTagName("Events").item(0).appendChild(event);
				
				eventsList.add(ielem.getAttribute("id"));
			}
			
			Element eventid = GuitarEFGDocument.createElement("EventId");
			eventid.setTextContent(elem.getAttribute("id"));
			
			Element widgetid = GuitarEFGDocument.createElement("WidgetId");
			widgetid.setTextContent(elem.getAttribute("widget_id"));
			
			Element eventType = GuitarEFGDocument.createElement("Type");
			eventType.setTextContent("-");
			
			
			Element initialEvent = GuitarEFGDocument.createElement("Initial");
			
			if(elem.getParentNode().getNodeName()=="EFG"){
				initialEvent.setTextContent("true");
			} else {
				initialEvent.setTextContent("false");
			}
			
			Element actionEvent = GuitarEFGDocument.createElement("Action");
			actionEvent.setTextContent(elem.getAttribute("type"));
			
			Element event = GuitarEFGDocument.createElement("Event");
			event.appendChild(eventid);
			event.appendChild(widgetid);
			event.appendChild(eventType);
			event.appendChild(initialEvent);
			event.appendChild(actionEvent);
			GuitarEFGDocument.getElementsByTagName("Events").item(0).appendChild(event);
			
			eventsList.add(elem.getAttribute("id"));
		}
	}
	
	private static void MatrixInGuitar() throws XPathExpressionException {
		Element eventgraph = GuitarEFGDocument.createElement("EventGraph");
		for (int i=0; i<eventsList.size(); i++){
			Element rowgraph = GuitarEFGDocument.createElement("Row");
			
			if(eventsList.get(i).contains("e")) {
				XPath eventXpath = XPathFactory.newInstance().newXPath();
				XPathExpression eventExpr = eventXpath.compile("//EVENT[@id='"+eventsList.get(i)+"']");
				Object eventResult = eventExpr.evaluate(tataGuiTreeDocument, XPathConstants.NODESET);
				NodeList eventNodes = (NodeList) eventResult;			
				String startActivityId=((Element) eventNodes.item(0).getPreviousSibling().getPreviousSibling()
						.getPreviousSibling().getPreviousSibling()).getAttribute("id");
				String finalActivityId= ((Element) eventNodes.item(0).getNextSibling().getNextSibling())
						.getAttribute("id");
				
				boolean newwindow;
				if(startActivityId.compareTo(finalActivityId)==0) {
					newwindow=false;
				} else
					newwindow=true;
				
				XPath startActivityXpath = XPathFactory.newInstance().newXPath();
				XPathExpression startActivityExpr = startActivityXpath.compile("//START_ACTIVITY[@id='"+finalActivityId+"']");
				Object startActivityResult = startActivityExpr.evaluate(tataGuiTreeDocument, XPathConstants.NODESET);
				NodeList startActivityNodes = (NodeList) startActivityResult;
				
				ArrayList<String> subsequentEventsList = new ArrayList<String>();
				for (int j=0; j<startActivityNodes.getLength(); j++) {
//					System.out.println(((Element) startActivityNodes.item(j).getNextSibling().getNextSibling().
//							getNextSibling().getNextSibling()).getAttribute("id"));
					subsequentEventsList.add(((Element) startActivityNodes.item(j).getNextSibling().getNextSibling().
						getNextSibling().getNextSibling()).getAttribute("id"));
				}
				
				ArrayList<String> subsequentInputsList = new ArrayList<String>();
				for(int j=0; j<startActivityNodes.getLength();j++) {
					NodeList inputs = ((Element) startActivityNodes.item(j).getParentNode()).getElementsByTagName("INPUT");
					for(int k=0; k<inputs.getLength();k++) {
						subsequentInputsList.add(((Element)inputs.item(k)).getAttribute("id"));
					}			
				}
				
				for (int k=0; k<eventsList.size(); k++) {
					Element eventofgraph = GuitarEFGDocument.createElement("E");
					if(subsequentEventsList.contains(eventsList.get(k))||subsequentInputsList.contains(eventsList.get(k))) {
						if(newwindow)
							eventofgraph.setTextContent("2");
						else
							eventofgraph.setTextContent("1");
					} else {
						eventofgraph.setTextContent("0");
					}
					rowgraph.appendChild(eventofgraph);
				}
				
				eventgraph.appendChild(rowgraph);
			}
			else {
				XPath inputXpath = XPathFactory.newInstance().newXPath();
				XPathExpression inputExpr = inputXpath.compile("//INPUT[@id='"+eventsList.get(i)+"']");
				Object inputResult = inputExpr.evaluate(tataGuiTreeDocument, XPathConstants.NODESET);
				NodeList inputNodes = (NodeList) inputResult;
				Element input=(Element) inputNodes.item(0);
				ArrayList<String> subsequentEventsList = new ArrayList<String>();
				
				NodeList inputs = ((Element) input.getParentNode()).getElementsByTagName("INPUT");
				for (int j=0; j<inputs.getLength(); j++) {
					subsequentEventsList.add(((Element) inputs.item(j)).getAttribute("id"));
				}
				
				NodeList ieventnode = ((Element) input.getParentNode().getParentNode()).getElementsByTagName("EVENT");
				subsequentEventsList.add(((Element) ieventnode.item(0)).getAttribute("id"));		
				System.out.println();
				for(int j=0; j<subsequentEventsList.size();j++)
					System.out.println(subsequentEventsList.get(j));
				
				for (int k=0; k<eventsList.size(); k++) {
					Element eventofgraph = GuitarEFGDocument.createElement("E");
					if(subsequentEventsList.contains(eventsList.get(k))) {
						eventofgraph.setTextContent("1");
					} else {
						eventofgraph.setTextContent("0");
					}
					rowgraph.appendChild(eventofgraph);
				}
			eventgraph.appendChild(rowgraph);
			}
		}
					
		GuitarEFGDocument.getElementsByTagName("EFG").item(0).appendChild(eventgraph);
	}
	
	private static void createGuitarDocument() throws ParserConfigurationException {
		DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder =  documentBuilderFactory.newDocumentBuilder();
		GuitarEFGDocument = documentBuilder.newDocument();
		Element guitarRoot = GuitarEFGDocument.createElement("EFG");
		GuitarEFGDocument.appendChild(guitarRoot);
		Element guitarEvents = GuitarEFGDocument.createElement("Events");
		guitarRoot.appendChild(guitarEvents);	
		
	}
}
