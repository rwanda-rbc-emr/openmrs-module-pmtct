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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptNumeric;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Obs;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.PersonName;
import org.openmrs.Program;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.EncounterService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;

/**
 *
 */
public class PMTCTModuleTag {
	
	private static Log log = LogFactory.getLog(PMTCTModuleTag.class);
	
	//	public static PMTCTConstants constants = PMTCTConfiguration.getConstants();
	
	/**
	 * From a patientId, get the patient's names [given_name middle_name family_name]
	 * 
	 * @param patientId
	 * @return
	 */
	public static String getPersonNames(Integer patientId) {
		if(null==patientId)
			return "-";
		Person person=Context.getPersonService().getPerson(patientId);
		if(null==person)
			return "-";
		PersonName personName = person.getPersonName();
		if(null==personName)
			return "-";
		String names = (personName.getGivenName().trim() + " " + personName.getMiddleName()).trim() + " "
		        + personName.getFamilyName().trim();
		
		return names;
	}
	
	public static String checkIfConceptExistByIdAsString(String conceptId) {
		String res = "";
		try {
			if (conceptId.trim().compareTo("") == 0)
				return res;
			Concept concept = Context.getConceptService().getConcept(Integer.parseInt(conceptId));
			res = (concept != null) ? "" + concept.getConceptId() : "";
		}
		catch (Exception e) {
			log.info(e.getMessage());
		}
		return res;
	}
	
	public static Patient getChildMother(Integer childPatientId) {
		PersonService ps = Context.getPersonService();
		Person child = ps.getPerson(childPatientId);
		
		Patient mother = null;
		
		try {
			List<Relationship> rel = ps.getRelationshipsByPerson(child);
			
			for (Relationship r : rel) {
				//			if (r.getRelationshipType().getRelationshipTypeId() == constants.getChildParentRelationshipTypeId()) {
				if (r.getRelationshipType().getRelationshipTypeId().intValue() == PMTCTConfigurationUtils.getRelationshipTypeId().intValue()) {
					mother = Context.getPatientService().getPatient(r.getPersonA().getPersonId());
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mother;
	}
	
	public static String isPatientDied(Integer patientId) {
		List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(
		    Context.getPatientService().getPatient(patientId),
		    Context.getConceptService().getConcept(PMTCTConstants.REASON_FOR_EXITING_CARE));
		
		boolean isDead = false;
		for (Obs o : obs) {
			if (o.getValueCoded() != null) {
				
				if (o.getValueCoded().getConceptId().intValue() == PMTCTConstants.PATIENT_DIED) {
					isDead = true;
					break;
				}
			}
		}
		
		return (isDead == true) ? "red" : "";
	}
	
	public static String getConceptNumericUnit(Integer conceptId) {
		Concept c = Context.getConceptService().getConcept(conceptId);
		log.info("-------------- > Concept#" + conceptId);
		if (c == null)
			return "";
		else if (!c.isNumeric()) {
			return "";
		} else {
			return ((ConceptNumeric) c).getUnits();
		}
	}
	
	public static PatientIdentifier personIdentifier(Patient p, Integer identifierTypeId) {
		PatientIdentifier pi = null;
		if (p != null)
			pi = p.getPatientIdentifier(identifierTypeId);
		return pi;
	}
	
	public static String personIdentifierByPatientIdAndIdentifierTypeId(Integer patientId, Integer identifierTypeId) {
		PatientIdentifier pi = null;
		Patient p = null;
		p = Context.getPatientService().getPatient(patientId);
		if (p != null)
			pi = p.getPatientIdentifier(identifierTypeId);
		return (pi != null) ? pi.toString() : "-";
	}
	
	public static String personAttribute(Patient p, Integer attributeTypeId) {
		try {
			PersonAttribute pa = null;
			if (p != null)
				pa = p.getAttribute(attributeTypeId);
			if (pa != null) {
				return Context.getConceptService().getConcept(Integer.parseInt(pa.getValue())).getName().getName();
			} else
				return "-";
		}
		catch (Exception e) {
			e.printStackTrace();
			return "-";
		}
	}
	
	public static String encounterId(List<Encounter> encList, Date d1, Date d2) {
		String res = "";
		for (Encounter enc : encList) {
			if (d1.compareTo(enc.getEncounterDatetime()) <= 0 && d2.compareTo(enc.getEncounterDatetime()) >= 0) {
				res = enc.getEncounterId().toString();
			}
		}
		
		return res;
	}
	
	public static String currentEncounterId(List<Encounter> encList, Date d1) {
		String res = "";
		for (Encounter enc : encList) {
			if (d1.compareTo(enc.getEncounterDatetime()) <= 0) {
				res = enc.getEncounterId().toString();
			}
		}
		
		return res;
	}
	
	public static String observationValue(Obs obs) {
		String value = "";
		if (null != obs)
			value = obs.getValueAsString(Context.getLocale());
		if ("false".equalsIgnoreCase(value))
			value = "NO";
		else if ("true".equalsIgnoreCase(value))
			value = "YES";
		else if ("".equalsIgnoreCase(value))
			value = "-";
		return value;
	}
	
	public static String observationValueByConcept(Encounter encounter, Integer conceptId) {
		String res = "-";
		if (null != encounter) {
			for (Obs o : encounter.getAllObs()) {
				if (o.getConcept().getConceptId() == conceptId.intValue()) {
					res = o.getValueAsString(Context.getLocale());
				}
			}
		}
		return res;
	}
	
	public static String lastObsValueByConceptId(Integer personId, Integer conceptId) {
		String res = "-";
		Obs obs = null;
		List<Obs> obsList = Context.getObsService().getObservationsByPersonAndConcept(
		    Context.getPersonService().getPerson(personId), Context.getConceptService().getConcept(conceptId));
		if (null != obsList && obsList.size() > 0) {
			for (Obs o : obsList) {
				if (obs == null)
					obs = o;
				if (obs.getObsDatetime().compareTo(o.getObsDatetime()) < 0) {
					obs = o;
				}
			}
			res = obs.getValueAsString(Context.getLocale());
		}
		return res;
	}
	
	public static String observationConceptName(Obs obs) {
		return (obs != null) ? obs.getConcept().getName().getName() : "-";
	}
	
	public static Encounter getCPNEncounterInfo(Integer patientId) throws Exception {
		EncounterService es = Context.getEncounterService();
		Encounter encounter = null;
		
		List<Encounter> pmtctCPNEnc = new ArrayList<Encounter>();
		for (Encounter enc : es.getEncountersByPatient(Context.getPatientService().getPatient(patientId))) {
			if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getCPNEncounterTypeId()) {
				pmtctCPNEnc.add(enc);
				if (null == encounter)
					encounter = enc;
				else if (encounter.getEncounterDatetime().compareTo(enc.getEncounterDatetime()) < 0)
					encounter = enc;
			}
		}
		
		return encounter;
	}
	
	public static Encounter getMaternityEncounterInfo(Integer patientId) throws Exception {
		EncounterService es = Context.getEncounterService();
		Encounter encounter = null;
		
		List<Encounter> pmtctMaternityEnc = new ArrayList<Encounter>();
		for (Encounter enc : es.getEncountersByPatient(Context.getPatientService().getPatient(patientId))) {
			if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMaternityEncounterTypeId()) {
				pmtctMaternityEnc.add(enc);
				if (null == encounter)
					encounter = enc;
				else if (encounter.getEncounterDatetime().compareTo(enc.getEncounterDatetime()) < 0)
					encounter = enc;
			}
		}
		
		return encounter;
	}
	
	public static String getProgramNameById(String programId) {
		if (programId.trim().compareToIgnoreCase("") == 0)
			return "";
		Program program = Context.getProgramWorkflowService().getProgram(Integer.parseInt(programId));
		return (program != null) ? program.getName() : "";
	}
	
	public static String getEncounterTypeNameById(String encounterTypeId) {
		if (encounterTypeId.trim().compareToIgnoreCase("") == 0)
			return "";
		EncounterType enctype = null;
		try {
			enctype = Context.getEncounterService().getEncounterType(Integer.parseInt(encounterTypeId));
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return (enctype != null) ? enctype.getName() : "";
	}
	
	public static String getAttributeTypeNameById(String attributeTypeId) {
		if (attributeTypeId.trim().compareToIgnoreCase("") == 0)
			return "";
		PersonAttributeType attributeType = null;
		try {
			attributeType = Context.getPersonService().getPersonAttributeType(Integer.parseInt(attributeTypeId));
		}
		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return (attributeType != null) ? attributeType.getName() : "";
	}
	
	public static String getIdentifierTypeNameById(String identifierTypeId) {
		if (identifierTypeId.trim().compareToIgnoreCase("") == 0)
			return "";
		PatientIdentifierType identifierType = Context.getPatientService().getPatientIdentifierType(
		    Integer.parseInt(identifierTypeId));
		return (identifierType != null) ? identifierType.getName() : "";
	}
	
	public static String getDrugOrderTypeNameById(String drugOrderTypeId) {
		if (drugOrderTypeId.trim().compareToIgnoreCase("") == 0)
			return "";
		OrderType orderType = Context.getOrderService().getOrderType(Integer.parseInt(drugOrderTypeId));
		return (orderType != null) ? orderType.getName() : "";
	}
	
	public static String getRelationshipTypeNameById(String relationshipTypeId) {
		if (relationshipTypeId.trim().compareToIgnoreCase("") == 0)
			return "";
		RelationshipType relationshipType = Context.getPersonService().getRelationshipType(
		    Integer.parseInt(relationshipTypeId));
		return (relationshipType != null) ? relationshipType.getDescription() : "";
	}
	
	public static String getConceptNameById(String conceptId) {
		if (conceptId.trim().compareToIgnoreCase("") == 0)
			return "-";
		Concept concept = Context.getConceptService().getConcept(Integer.parseInt(conceptId));
		return (concept != null) ? concept.getName().getName() : "-";
	}
	
	public static String getIdentifierByPatientIdAndIdentifierTypeId(Integer patientId) {
		Patient pt=Context.getPatientService().getPatient(patientId);
		if(null==pt)
			return "-";
		List<PatientIdentifier> pis = pt.getActiveIdentifiers();
		return (pis != null && pis.size() > 0) ? pis.get(0).getIdentifier() : "-";
	}
	
	/**
	 * @param gp
	 * @return
	 */
	public static String globalPropertyParser(String gp) {
		String res = "-";
		try {
			res = gp.substring(gp.lastIndexOf(".") + 1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
}
