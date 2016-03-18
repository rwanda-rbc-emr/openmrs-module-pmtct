/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.pmtct;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class PMTCTConfiguration {
//	
//	private static PMTCTConfiguration instance = null;
//	
//	private static List<PMTCTConstants> constants = null;
//	
//	private static String configFilePath;
//	
//	private static Document doc = null;
//	
////	private static List<PMTCTConfigTemplate> programs = null;
//	
//	private static List<PMTCTConfigTemplate> drugOrderTypes = null;
//	
//	private static List<PMTCTConfigTemplate> relationshipTypes = null;
//	
////	private static List<PMTCTConfigTemplate> attributeTypes = null;
//	
////	private static List<PMTCTConfigTemplate> encounterTypes = null;
//	
//	private static List<PMTCTConfigTemplate> identifierTypes = null;
//	
////	private static List<PMTCTConfigTemplate> concepts = null;
//	
//	//>>> moving defaultlocation and defaultpagesize to portal configurations
////	private static int defaultLocation = 0;
////	
////	private static int pageSize = 15;
//	
//	private static double criticalLevelOfCD4Count = 350.0;
//	
//	private static boolean modificationEnabled;
//	
//	private static boolean displayHelpMessage;
//	
//	private static Log log = LogFactory.getLog(PMTCTConfiguration.class);
//	
//	private PMTCTConfiguration() {
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param path Location of the configuration file
//	 * @return
//	 */
//	public static PMTCTConfiguration getInstance(String path) {
//		instance = (instance != null) ? instance : new PMTCTConfiguration();
//		loadConfiguration(path);
//		return instance;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param d
//	 * @param parent
//	 * @param text
//	 * @return
//	 */
//	private Element createGroup(Document d, Element parent, String text) {
//		Element element = d.createElement(text);
//		if (parent != null)
//			parent.appendChild(element);
//		return element;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param d
//	 * @param parent
//	 * @param text
//	 * @param ob
//	 * @param reset
//	 */
//	private void createAttributes(Document d, Element parent, String text, PMTCTConfigTemplate ob, boolean reset) {
//		Element el = d.createElement(text);
//		parent.appendChild(el);
//		
//		Element child1 = d.createElement("id");
//		child1.appendChild(d.createTextNode(ob.getId()));
//		el.appendChild(child1);
//		
//		Element child2 = d.createElement("defaultvalue");
//		child2.appendChild(d.createTextNode(ob.getDefaultValue()));
//		el.appendChild(child2);
//		
//		Element child3 = d.createElement("value");
//		child3.appendChild(d.createTextNode((!reset) ? ob.getValue() : ob.getDefaultValue()));
//		el.appendChild(child3);
//		
//		Element child4 = d.createElement("description");
//		child4.appendChild(d.createTextNode(ob.getDescription()));
//		el.appendChild(child4);
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param request
//	 * @param reset
//	 * @return
//	 */
//	private boolean update(HttpServletRequest request, boolean reset) {
//		
//		Document d = null;
//		int index = 1, count = 0;
//		
//		try {
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			d = db.newDocument();
//			
//			//creating the root
//			Element root = createGroup(d, null, "pmtctconfigurations");
//			d.appendChild(root);
//			
//			//creating the modification section
//			Element modify = createGroup(d, root, "modifications");
//			Element child1 = d.createElement("enable");
//			child1.appendChild(d.createTextNode((request.getParameter("enableModification") == null) ? "0" : "1"));
//			modify.appendChild(child1);
//			
//			//>>> moving defaultlocation and defaultpagesize to portal configurations
////			Element child2 = d.createElement("defaultLocation");
////			String loc = request.getParameter("defaultLocation");
////			if (loc.compareTo("") == 0)
////				loc = "0";
////			child2.appendChild(d.createTextNode((!reset) ? loc : "0"));
////			modify.appendChild(child2);
////			
////			Element child3 = d.createElement("pagesize");
////			String size = request.getParameter("defaultPageSize");
////			if (size.trim() == "" || Integer.parseInt(size.trim()) < 10)
////				size = "10";
////			child3.appendChild(d.createTextNode((!reset) ? size : "15"));
////			modify.appendChild(child3);
//			
//			Element child4 = d.createElement("criticalLevelOfCD4Count");
//			String level = request.getParameter("criticalLevelOfCD4Count");
//			if (level.trim() == "" || Double.parseDouble(level.trim()) < 40.0)
//				level = "350.0";
//			child4.appendChild(d.createTextNode((!reset) ? level : "350.0"));
//			modify.appendChild(child4);
//			
//			Element child5 = d.createElement("displayHelpMessage");
//			child5.appendChild(d.createTextNode((!reset) ?request.getParameter("displayHelpMessage"):"0"));
//			modify.appendChild(child5);
//			
//			//creating the programs section
//			/*Element programs = createGroup(d, root, "programs");
//			while (count < 2) {
//				PMTCTConfigTemplate template = getProgramById(index);
//				template.setValue(request.getParameter("program_" + index));
//				createAttributes(d, programs, "prgm", template, reset);
//				count++;
//				index++;
//			}*/
//			
//			//creating the attribute section
//			/*Element attributeTypes = createGroup(d, root, "attributeTypes");
//			count = 0;
//			while (count < 3) {
//				PMTCTConfigTemplate template = getAttributeTypeById(index);
//				template.setValue(request.getParameter("attributeType_" + index));
//				createAttributes(d, attributeTypes, "attributeType", template, reset);
//				count++;
//				index++;
//			}*/
//			
//			//creating the attribute section
//			Element identifierTypes = createGroup(d, root, "identifierTypes");
//			count = 0;
//			while (count < 1) {
//				PMTCTConfigTemplate template = getIdentifierById(index);
//				template.setValue(request.getParameter("identifierType_" + index));
//				createAttributes(d, identifierTypes, "identifierType", template, reset);
//				count++;
//				index++;
//			}
//			
//			//creating the drugOrderTypes section
//			Element drugOrderTypes = createGroup(d, root, "drugOrderTypes");
//			count = 0;
//			while (count < 1) {
//				PMTCTConfigTemplate template = getDrugOrderTypeById(index);
//				template.setValue(request.getParameter("drugOrderType_" + index));
//				createAttributes(d, drugOrderTypes, "drugOrderType", template, reset);
//				count++;
//				index++;
//			}
//			
//			//creating the relationshipTypes section
//			Element relationshipTypes = createGroup(d, root, "relationshipTypes");
//			count = 0;
//			while (count < 1) {
//				PMTCTConfigTemplate template = getRelationshipTypeById(index);
//				template.setValue(request.getParameter("relationshipType_" + index));
//				createAttributes(d, relationshipTypes, "relationshipType", template, reset);
//				count++;
//				index++;
//			}
//			
//			//creating the encounterType section
////			Element encounterTypes = createGroup(d, root, "encounterTypes");
////			count = 0;
////			while (count < 6) {
////				PMTCTConfigTemplate template = getEncounterById(index);
////				template.setValue(request.getParameter("encounterType_" + index));
////				createAttributes(d, encounterTypes, "encounterType", template, reset);
////				count++;
////				index++;
////			}
//			
//			//creating the concept section
////			Element concepts = createGroup(d, root, "concepts");
////			count = 0;
////			while (count < 12) {
////				PMTCTConfigTemplate template = getConceptById(index);
////				template.setValue(request.getParameter("concept_" + index));
////				createAttributes(d, concepts, "concept", template, reset);
////				count++;
////				index++;
////			}
//			
//			saveXMLDocument(getConfigFilePath(), d);
//		}
//		catch (Exception e) {
//			log.error("An error occured when updating the config file: ");
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//		
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
//	/*private static void setPmtctPrograms(Document doc) {
//		programs = new ArrayList<PMTCTConfigTemplate>();
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("prgm");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					getPrograms().add(getConfigTemplateFromNode(fstNode));
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}*/
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	*/
//	 /*private static void setAttributeTypes(Document doc) {
//		attributeTypes = new ArrayList<PMTCTConfigTemplate>();
//		
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("attributeType");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					
//					getAttributeTypes().add(getConfigTemplateFromNode(fstNode));
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}*/
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
////	private static void setConcepts(Document doc) {
////		concepts = new ArrayList<PMTCTConfigTemplate>();
////		
////		try {
////			NodeList nodeLst = doc.getElementsByTagName("concept");
////			
////			for (int s = 0; s < nodeLst.getLength(); s++) {
////				
////				Node fstNode = nodeLst.item(s);
////				
////				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
////					
////					getConcepts().add(getConfigTemplateFromNode(fstNode));
////				}
////				
////			}
////		}
////		catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
//	private static void setIdentifierTypes(Document doc) {
//		identifierTypes = new ArrayList<PMTCTConfigTemplate>();
//		
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("identifierType");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					
//					getIdentifierTypes().add(getConfigTemplateFromNode(fstNode));
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
//	private static void setPmtctDrugOrderTypes(Document doc) {
//		drugOrderTypes = new ArrayList<PMTCTConfigTemplate>();
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("drugOrderTypes");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					getDrugOrderTypes().add(getConfigTemplateFromNode(fstNode));
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
//	private static void setPmtctRelationshipTypes(Document doc) {
//		relationshipTypes = new ArrayList<PMTCTConfigTemplate>();
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("relationshipTypes");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					getRelationshipTypes().add(getConfigTemplateFromNode(fstNode));
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
//	private static void setModificationStatus(Document doc) {
//		try {
//			NodeList nodeLst = doc.getElementsByTagName("modifications");
//			
//			for (int s = 0; s < nodeLst.getLength(); s++) {
//				
//				Node fstNode = nodeLst.item(s);
//				
//				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
//					Element fstElmnt = (Element) fstNode;
//					NodeList idElmntLst = fstElmnt.getElementsByTagName("enable");
//					Element idElmnt = (Element) idElmntLst.item(0);
//					NodeList id = idElmnt.getChildNodes();
//					
//					enableDisableModification(((Node) id.item(0)).getNodeValue().toString().compareTo("1") == 0);
//					
//					//>>> moving defaultlocation and defaultpagesize to portal configurations
////					Element scdElmnt = (Element) fstNode;
////					NodeList scdElmntLst = scdElmnt.getElementsByTagName("defaultLocation");
////					Element defaultLocElmnt = (Element) scdElmntLst.item(0);
////					NodeList defaultLoc = defaultLocElmnt.getChildNodes();
////					
////					defaultLocation = (((Node) defaultLoc.item(0)).getNodeValue() != null) ? Integer
////					        .parseInt(((Node) defaultLoc.item(0)).getNodeValue()) : 1;
////					
////					Element thrElmnt = (Element) fstNode;
////					NodeList thrElmntLst = thrElmnt.getElementsByTagName("pagesize");
////					Element sizeElmnt = (Element) thrElmntLst.item(0);
////					NodeList size = sizeElmnt.getChildNodes();
////					
////					pageSize = (((Node) size.item(0)).getNodeValue() != null) ? Integer.parseInt(((Node) size.item(0))
////					        .getNodeValue()) : 15;
//					
//					Element qElmnt = (Element) fstNode;
//					NodeList qElmntLst = qElmnt.getElementsByTagName("criticalLevelOfCD4Count");
//					Element cd4CriticalLevelElmnt = (Element) qElmntLst.item(0);
//					NodeList cd4CriticalLevel = cd4CriticalLevelElmnt.getChildNodes();
//					
//					criticalLevelOfCD4Count = (((Node) cd4CriticalLevel.item(0)).getNodeValue() != null) ? Double
//					        .parseDouble(((Node) cd4CriticalLevel.item(0)).getNodeValue()) : 1;
//					
//					Element helpElmnt = (Element) fstNode;
//					NodeList idHelpElmntLst = helpElmnt.getElementsByTagName("displayHelpMessage");
//					Element idHelpElmnt = (Element) idHelpElmntLst.item(0);
//					NodeList help = idHelpElmnt.getChildNodes();
//					
//					enableDisableDisplayHelpMessage(((Node) help.item(0)).getNodeValue().toString().compareTo("1") == 0);
//					
//				}
//				
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 */
//	private static void setMyClassProperties() {
//		constants = new ArrayList<PMTCTConstants>();
//		try {
//			PMTCTConstants constant = new PMTCTConstants();
//			
//			//>>> moving defaultlocation and defaultpagesize to portal configurations
////			constant.setDefaultLocation(defaultLocation);
////			constant.setPageSize(pageSize);
//			constant.setCriticalLevelOfCD4Count(criticalLevelOfCD4Count);
//			//programs
//			/*constant.setPmtctProgramId(Integer.parseInt((getProgramById(1).getValue() == "") ? "0" : getProgramById(1)
//			        .getValue()));
//			constant.setHivProgramId(Integer.parseInt((getProgramById(2).getValue() == "") ? "0" : getProgramById(2)
//			        .getValue()));*/
//			
//			//Attributes, Identifiers and Drug orders
////			constant.setCivilStatusAttributeTypeId(Integer.parseInt((getAttributeTypeById(1).getValue() == "") ? "0"
////			        : getAttributeTypeById(1).getValue()));
////			
////			constant.setEducationLevelAttributeTypeId(Integer.parseInt((getAttributeTypeById(2).getValue() == "") ? "0"
////			        : getAttributeTypeById(2).getValue()));
////			
////			constant.setMainActivityAttributeTypeId(Integer.parseInt((getAttributeTypeById(3).getValue() == "") ? "0"
////			        : getAttributeTypeById(3).getValue()));
//			
////			constant.setOldIdentificationNumber(Integer.parseInt((getIdentifierById(1).getValue() == "") ? "0"
////			        : getIdentifierById(1).getValue()));
////			
////			constant.setTracNetIdentifierId(Integer.parseInt((getIdentifierById(2).getValue() == "") ? "0"
////			        : getIdentifierById(2).getValue()));
//			
//			constant.setCpnIdentifierTypeId(Integer.parseInt((getIdentifierById(1).getValue() == "") ? "0"
//			        : getIdentifierById(1).getValue()));
//			
//			constant.setDrugOrderTypeId(Integer.parseInt((getDrugOrderTypeById(2).getValue() == "") ? "0"
//			        : getDrugOrderTypeById(2).getValue()));
//			
//			constant.setChildParentRelationshipTypeId(Integer.parseInt((getRelationshipTypeById(3).getValue() == "") ? "0"
//			        : getRelationshipTypeById(3).getValue()));
//			
//			
//			//encounterTypeIds
////			constant.setCpnEncounterTypeId(Integer.parseInt((getEncounterById(4).getValue() == "") ? "0"
////			        : getEncounterById(4).getValue()));
////			
////			constant.setMotherFollowUpEncounterTypeId(Integer.parseInt((getEncounterById(5).getValue() == "") ? "0"
////			        : getEncounterById(5).getValue()));
////			
////			constant.setMaternityEncounterTypeId(Integer.parseInt((getEncounterById(6).getValue() == "") ? "0"
////			        : getEncounterById(6).getValue()));
////			
////			constant.setPcrTestEncounterTypeId(Integer.parseInt((getEncounterById(7).getValue() == "") ? "0"
////			        : getEncounterById(7).getValue()));
////			
////			constant.setSerologyTestAt9MonthsEncounterTypeId(Integer.parseInt((getEncounterById(8).getValue() == "") ? "0"
////			        : getEncounterById(8).getValue()));
////			
////			constant.setSerologyTestAt18MonthsEncounterTypeId(Integer.parseInt((getEncounterById(9).getValue() == "") ? "0"
////			        : getEncounterById(9).getValue()));
//			
//			//concepts
//			/*constant.setBornAlive(Integer.parseInt((getConceptById(4).getValue() == "") ? "0" : getConceptById(4)
//			        .getValue()));
//			
//			constant.setBornDead(Integer.parseInt((getConceptById(5).getValue() == "") ? "0" : getConceptById(5)
//			        .getValue()));
//			
//			constant.setChildBornStatus(Integer.parseInt((getConceptById(6).getValue() == "") ? "0" : getConceptById(6)
//			        .getValue()));
//			
//			constant.setHivTestInDeliveryRoom(Integer.parseInt((getConceptById(7).getValue() == "") ? "0" : getConceptById(
//			    7).getValue()));
//			
//			constant.setPatientArriveWithPartner(Integer.parseInt((getConceptById(8).getValue() == "") ? "0"
//			        : getConceptById(8).getValue()));
//			
//			constant.setPartnerTestedSeparately(Integer.parseInt((getConceptById(9).getValue() == "") ? "0"
//			        : getConceptById(9).getValue()));
//			
//			constant.setSyphilisTestDate(Integer.parseInt((getConceptById(10).getValue() == "") ? "0" : getConceptById(10)
//			        .getValue()));
//			
//			constant.setDateResultOfHivTestReceived(Integer.parseInt((getConceptById(11).getValue() == "") ? "0"
//			        : getConceptById(11).getValue()));
//			        
//			constant.setDateResultOfSyphilisTestReceived(Integer.parseInt((getConceptById(12).getValue() == "") ? "0"
//			        : getConceptById(12).getValue()));
//			
//			constant.setPartnerHivTestingDate(Integer.parseInt((getConceptById(13).getValue() == "") ? "0" : getConceptById(
//			    13).getValue()));
//			
//			constant.setDateMosquitoNetReceived(Integer.parseInt((getConceptById(14).getValue() == "") ? "0"
//			        : getConceptById(14).getValue()));
//			
//			constant.setDateResultOfCD4CountReceived(Integer.parseInt((getConceptById(15).getValue() == "") ? "0"
//			        : getConceptById(15).getValue()));*/
//			
//			getListOfConstants().add(constant);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param doc
//	 */
////	private static void setPmtctEncounterTypes(Document doc) {
////		encounterTypes = new ArrayList<PMTCTConfigTemplate>();
////		try {
////			NodeList nodeLst = doc.getElementsByTagName("encounterType");
////			
////			for (int s = 0; s < nodeLst.getLength(); s++) {
////				
////				Node fstNode = nodeLst.item(s);
////				
////				if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
////					getEncounterTypes().add(getConfigTemplateFromNode(fstNode));
////				}
////				
////			}
////		}
////		catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param fstNode
//	 * @return
//	 */
//	private static PMTCTConfigTemplate getConfigTemplateFromNode(Node fstNode) {
//		PMTCTConfigTemplate pp = new PMTCTConfigTemplate();
//		
//		Element fstElmnt = (Element) fstNode;
//		NodeList idElmntLst = fstElmnt.getElementsByTagName("id");
//		Element idElmnt = (Element) idElmntLst.item(0);
//		NodeList id = idElmnt.getChildNodes();
//		pp.setId(((Node) id.item(0)).getNodeValue());
//		
//		Element element1 = (Element) fstNode;
//		NodeList defaultValueElmntLst = element1.getElementsByTagName("defaultvalue");
//		Element defaultValueElmnt = (Element) defaultValueElmntLst.item(0);
//		NodeList defaultValue = defaultValueElmnt.getChildNodes();
//		pp.setDefaultValue(((Node) defaultValue.item(0)).getNodeValue());
//		
//		Element element2 = (Element) fstNode;
//		NodeList valueElmntLst = element2.getElementsByTagName("value");
//		Element valueElmnt = (Element) valueElmntLst.item(0);
//		NodeList value = valueElmnt.getChildNodes();
//		if (null != (Node) value.item(0))
//			pp.setValue(((Node) value.item(0)).getNodeValue());
//		
//		Element element3 = (Element) fstNode;
//		NodeList descriptionElmntLst = element3.getElementsByTagName("description");
//		Element descriptionElmnt = (Element) descriptionElmntLst.item(0);
//		NodeList descriptionValue = descriptionElmnt.getChildNodes();
//		pp.setDescription(((Node) descriptionValue.item(0)).getNodeValue());
//		
//		return pp;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param path
//	 */
//	public static void loadConfiguration(String path) {
//		setConfigFilePath(path);
//		try {
//			File file = new File(getConfigFilePath());
//			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			doc = db.parse(file);
//			doc.getDocumentElement().normalize();
//			
//			//setPmtctPrograms(doc);
////			setAttributeTypes(doc);
//			setIdentifierTypes(doc);
////			setPmtctEncounterTypes(doc);
//			setPmtctDrugOrderTypes(doc);
//			setPmtctRelationshipTypes(doc);
//			setModificationStatus(doc);
////			setConcepts(doc);
//			setMyClassProperties();
//			
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param request
//	 * @param reset
//	 * @return
//	 */
//	public boolean save(HttpServletRequest request, boolean reset) {
//		return update(request, reset);
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param fileName
//	 * @param doc
//	 * @return
//	 */
//	private boolean saveXMLDocument(String fileName, Document doc) {
//		File xmlOutputFile = new File(fileName);
//		FileOutputStream fos;
//		Transformer transformer;
//		
//		try {
//			fos = new FileOutputStream(xmlOutputFile);
//		}
//		catch (FileNotFoundException e) {
//			log.error("Error occured: " + e.getMessage());
//			return false;
//		}
//		
//		TransformerFactory transformerFactory = TransformerFactory.newInstance();
//		try {
//			transformer = transformerFactory.newTransformer();
//		}
//		catch (TransformerConfigurationException e) {
//			log.error("Transformer configuration error: " + e.getMessage());
//			return false;
//		}
//		
//		DOMSource source = new DOMSource(doc);
//		StreamResult result = new StreamResult(fos);
//		try {
//			transformer.transform(source, result);
//		}
//		catch (TransformerException e) {
//			log.error("Error transform: " + e.getMessage());
//		}
//		System.out.println("XML file saved.");
//		return true;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @return
//	 */
//	private static String getConfigFilePath() {
//		return configFilePath;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param path
//	 */
//	private static void setConfigFilePath(String path) {
//		configFilePath = path;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @return
//	 */
//	public boolean isModificationEnabled() {
//		return modificationEnabled;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param bool
//	 */
//	private static void enableDisableModification(boolean bool) {
//		modificationEnabled = bool;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @return
//	 */
//	public boolean isDisplayHelpMessageEnabled() {
//		return displayHelpMessage;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param bool
//	 */
//	private static void enableDisableDisplayHelpMessage(boolean bool) {
//		displayHelpMessage = bool;
//	}
//	
//	/**
//	 * @return the programs
//	 */
//	/*public static List<PMTCTConfigTemplate> getPrograms() {
//		return programs;
//	}*/
//	
//	/**
//	 * @return the attributeTypes
//	 */
//	/*public static List<PMTCTConfigTemplate> getAttributeTypes() {
//		return attributeTypes;
//	}*/
//	
//	/**
//	 * @return the attributeTypes
//	 */
////	public static List<PMTCTConfigTemplate> getConcepts() {
////		return concepts;
////	}
//	
//	/**
//	 * @return the identifierTypes
//	 */
//	public static List<PMTCTConfigTemplate> getIdentifierTypes() {
//		return identifierTypes;
//	}
//	
//	/**
//	 * @return the encounterTypes
//	 */
////	public static List<PMTCTConfigTemplate> getEncounterTypes() {
////		return encounterTypes;
////	}
//	
//	/**
//	 * @return the drugOrderTypes
//	 */
//	public static List<PMTCTConfigTemplate> getDrugOrderTypes() {
//		return drugOrderTypes;
//	}
//	
//	/**
//	 * @return the RelationshipTypes
//	 */
//	public static List<PMTCTConfigTemplate> getRelationshipTypes() {
//		return relationshipTypes;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @return
//	 */
//	public static List<PMTCTConstants> getListOfConstants() {
//		return constants;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @return
//	 */
//	public static PMTCTConstants getConstants() {
//		return constants.get(0);
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param encounterId
//	 * @return
//	 */
////	private static PMTCTConfigTemplate getEncounterById(int encounterId) {
////		for (PMTCTConfigTemplate p : getEncounterTypes())
////			if (Integer.parseInt(p.getId()) == encounterId)
////				return p;
////		
////		return null;
////	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param programId
//	 * @return
//	 */
//	/*private static PMTCTConfigTemplate getProgramById(int programId) {
//		for (PMTCTConfigTemplate p : getPrograms())
//			if (Integer.parseInt(p.getId()) == programId)
//				return p;
//		
//		return null;
//	}*/
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param attributeId
//	 * @return
//	 */
//	/*private static PMTCTConfigTemplate getAttributeTypeById(int attributeId) {
//		for (PMTCTConfigTemplate p : getAttributeTypes())
//			if (Integer.parseInt(p.getId()) == attributeId)
//				return p;
//		
//		return null;
//	}*/
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param conceptId
//	 * @return
//	 */
////	private static PMTCTConfigTemplate getConceptById(int conceptId) {
////		for (PMTCTConfigTemplate p : getConcepts())
////			if (Integer.parseInt(p.getId()) == conceptId)
////				return p;
////		
////		return null;
////	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param identifierId
//	 * @return
//	 */
//	private static PMTCTConfigTemplate getIdentifierById(int identifierId) {
//		for (PMTCTConfigTemplate p : getIdentifierTypes())
//			if (Integer.parseInt(p.getId()) == identifierId)
//				return p;
//		
//		return null;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param typeId
//	 * @return
//	 */
//	private static PMTCTConfigTemplate getDrugOrderTypeById(int typeId) {
//		for (PMTCTConfigTemplate p : getDrugOrderTypes())
//			if (Integer.parseInt(p.getId()) == typeId)
//				return p;
//		
//		return null;
//	}
//	
//	/**
//	 * Auto generated method comment
//	 * 
//	 * @param typeId
//	 * @return
//	 */
//	private static PMTCTConfigTemplate getRelationshipTypeById(int typeId) {
//		for (PMTCTConfigTemplate p : getRelationshipTypes())
//			if (Integer.parseInt(p.getId()) == typeId)
//				return p;
//		
//		return null;
//	}
//	
}
