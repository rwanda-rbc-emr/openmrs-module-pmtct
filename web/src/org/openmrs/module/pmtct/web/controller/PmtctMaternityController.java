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
package org.openmrs.module.pmtct.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonName;
import org.openmrs.Program;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.DuplicateIdentifierException;
import org.openmrs.api.EncounterService;
import org.openmrs.api.IdentifierNotUniqueException;
import org.openmrs.api.InsufficientIdentifiersException;
import org.openmrs.api.InvalidIdentifierFormatException;
import org.openmrs.api.PatientIdentifierException;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.ContextProvider;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.module.pmtct.util.PMTCTUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
 */
public class PmtctMaternityController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private ModelAndView saveMaternityEncounter(HttpServletRequest request) {
		ConceptService cs = Context.getConceptService();
		List<Obs> obsList = new ArrayList<Obs>();
		Encounter enc = new Encounter();
		EncounterService es = Context.getEncounterService();
		
		try {
			
			//			PmtctMaternity mt=new PmtctMaternity();
			
			Date encDate = Context.getDateFormat().parse(request.getParameter("encounterDate"));
			Location location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("location")));
			Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
			User provider = Context.getUserService().getUser(Integer.parseInt(request.getParameter("provider")));
			
			//----- HIV TEST IN WORKING ROOM ---
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			        .getHivTestInDeliveryRoomConceptId()), Double.valueOf(request.getParameter("testHivWorkroom")), 1));
			
			if (request.getParameter("testHivWorkroom").compareTo("1") == 0) {
				//-----RESULT HIV TEST IN WORKING ROOM---
				obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST),
				    cs.getConcept(Integer.parseInt(request.getParameter("resultHivTest"))), 4));
			}
			
			//-----DPA---
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.PREGNANT_DUE_DATE),
			    Context.getDateFormat().parse(request.getParameter("dpaDate")), 2));
			
			//-----DATE CONFINEMENT---
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.DATE_OF_CONFINEMENT),
			    Context.getDateFormat().parse(request.getParameter("dateConfinement")), 2));
			
			//-----ARV RECEIVED--->>>>>>>>>>>>
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.ARV_FOR_PMTCT), Double
			        .valueOf(request.getParameter("ARVReceived")), 1));
			
			//-----PROPHYLAXIS--->>>>>>>>>>>>>>
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.PROPHYLAXIS_STOPPED),
			    Double.valueOf(request.getParameter("prophylaxis")), 1));
			
			//-----TRANSFER OUT DATE---
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConstants.TRANSFER_OUT_DATE),
			    Context.getDateFormat().parse(request.getParameter("transferOutDate")), 2));
			
			//-----CHILD BORN STATUS---
			obsList.add(createObservation(encDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			        .getBornStatusConceptId()), cs.getConcept(Integer.valueOf(request.getParameter("childBornStatus"))), 4));
			
			//-----COMMENT ---
			obsList.add(createObservation(encDate, location, patient, cs
			        .getConcept(PMTCTConstants.CLINICAL_COMMENT_IMPRESSION), request.getParameter("comment"), 3));
			
			//creating the encounter and add obs
			enc = createEncounter(encDate, provider, location, patient, es.getEncounterType(PMTCTConfigurationUtils
			        .getMaternityEncounterTypeId()), obsList);
			enc.setDateCreated(new Date());
			
			//save the encounter
			log.info(es.getEncounterType(PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
			        + " : Saving the Mother's encounter...");
			es.saveEncounter(enc);
			log.info(es.getEncounterType(PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
			        + " : Mother's encounter saved successfully.");
			
			//			Program program = Context.getProgramWorkflowService().getProgram(PMTCTConfigurationUtils.getPmtctProgramId());
			//exit patient from PMTCT program
			//			if (mt.getInfantFeedingMethod() == PMTCTConstants.INFANT_FORMULA)
			//				completePMTCTProgram(request, encDate, program);
			
			//------------ CHILD INFO ----------------------------------------------------------------------------------------------------------
			
			if (Integer.valueOf(request.getParameter("childBornStatus")).intValue() != PMTCTConfigurationUtils
			        .getBornDeadConceptId()) {
				int numberOfChildrenCreated = createChildren(request, encDate, location, provider, patient);
				String msg = ContextProvider.getMessage("pmtct.general.saveSuccess");
				msg += "<br/>" + numberOfChildrenCreated + " child/children created !";
				request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
			}
			
			//---------------------------------------------------------------------------------------------------------------------------------
			
		}
		catch (InvalidIdentifierFormatException iife) {
			log.error(iife);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.formatInvalid");
		}
		catch (IdentifierNotUniqueException inue) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + inue);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.notUnique");
		}
		catch (DuplicateIdentifierException die) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + die);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.duplicate");
		}
		catch (InsufficientIdentifiersException iie) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + iie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "PatientIdentifier.error.insufficientIdentifiers");
		}
		catch (PatientIdentifierException pie) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + pie);
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "PatientIdentifier.error.general");
		}
		catch (Exception e) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> An error occured when trying to save encounter for a patient with id "
			        + request.getParameter("patientId"));
			e.printStackTrace();
			
			String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
		}
		
		return new ModelAndView(new RedirectView(request.getContextPath() + "/patientDashboard.form?patientId="
		        + request.getParameter("patientId")));
	}
	
	private int createChildren(HttpServletRequest request, Date encDate, Location loc, User provider, Patient mother)
	                                                                                                                 throws Exception {
		
		int numberOfChildrenCreated = 0;
		try {
			
			int numberOfChildren = Integer.valueOf(request.getParameter("numberOfChildren"));
			Date birthdate = Context.getDateFormat().parse(request.getParameter("dateConfinement"));
			
			int count = 0;
			while (count == 0 || count < numberOfChildren) {
				
				if (request.getParameter("pId_" + count) != null) {
					
					Patient patientChild = new Patient();
					PersonName pn = new PersonName(request.getParameter("fName_" + count), "", request.getParameter("gName_"
					        + count));
					patientChild.getNames().add(pn);//need a set of names
					patientChild.setBirthdate(birthdate);
					patientChild.setGender(request.getParameter("gender_" + count));//string
					patientChild.setCreator(Context.getAuthenticatedUser());
					
					//========= IDENTIFIER ========
					PatientIdentifier pi = new PatientIdentifier();
					pi.setDateCreated(new Date());
					pi.setCreator(Context.getAuthenticatedUser());
					pi.setIdentifier(request.getParameter("pId_" + count));
					pi.setLocation(loc);
					pi.setIdentifierType(Context.getPatientService().getPatientIdentifierType(
					    PMTCTConfigurationUtils.getLocalHealthCenterIdentifierTypeId()));
					
					if (!Context.getPatientService().isIdentifierInUseByAnotherPatient(pi)) {
						patientChild.addIdentifier(pi);
						
						//========= ATTRIBUTES ========
						PersonAttribute pa = new PersonAttribute();
						pa.setAttributeType(Context.getPersonService().getPersonAttributeType(
						    PMTCTConstants.MOTHER_NAME_ATTRIBUTE_TYPE_ID));
						pa.setCreator(Context.getAuthenticatedUser());
						pa.setDateCreated(new Date());
						pa.setValue(mother.getPersonName().toString());
						
						patientChild.addAttribute(pa);
						
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Saving the children...");
						Context.getPatientService().savePatient(patientChild);
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Children saved successfully with ID " + patientChild.getPatientId());
						
						//======== SAVE RELATIONSHIP=======
						Relationship relation = new Relationship();
						relation.setPersonA(Context.getPersonService().getPerson(mother.getPersonId()));
						relation.setPersonB(Context.getPersonService().getPerson(patientChild.getPersonId()));
						
						RelationshipType relationType = Context.getPersonService().getRelationshipType(
						    PMTCTConfigurationUtils.getRelationshipTypeId());
						relation.setCreator(Context.getAuthenticatedUser());
						relation.setRelationshipType(relationType);
						relation.setDateCreated(new Date());
						
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Saving the relationship...");
						Context.getPersonService().saveRelationship(relation);
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Relationship of type " + relationType.getDescription() + " saved successfully.");
						
						//========ADD CHILD IN PMTCT-PROGRAM=======
						PatientProgram patientProgram = new PatientProgram();
						Program program = Context.getProgramWorkflowService().getProgram(
						    PMTCTConfigurationUtils.getPmtctProgramId());
						patientProgram.setPatient(patientChild);
						patientProgram.setProgram(program);
						patientProgram.setDateEnrolled(birthdate); // the child is enrolled in pmtct the same day as he/she born
						patientProgram.setCreator(Context.getAuthenticatedUser());
						patientProgram.setDateCreated(new Date());
						
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Trying to enroll the Child in " + program.getName() + "...");
						Context.getProgramWorkflowService().savePatientProgram(patientProgram);
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Enrollement finish successfully.");
						
						//PmtctMaternity mt = new PmtctMaternity();
						//------ child observations --------
						ConceptService cs = Context.getConceptService();
						List<Obs> childObs = new ArrayList<Obs>();
						
						//-----INFANT FEEDING METHOD ---
						childObs.add(createObservation(encDate, loc, patientChild, cs
						        .getConcept(PMTCTConstants.INFANT_FEEDING_METHOD), cs.getConcept(Integer.valueOf(request
						        .getParameter("infantFeedingMethod_" + count))), 4));
						
						//-----CHILD WEIGHT ---
						childObs.add(createObservation(encDate, loc, patientChild, cs.getConcept(PMTCTConstants.WEIGHT),
						    Double.valueOf(request.getParameter("weight_" + count)), 1));
						
						//-----CHILD HEIGHT ---
						childObs.add(createObservation(encDate, loc, patientChild, cs.getConcept(PMTCTConstants.HEIGHT),
						    Double.valueOf(request.getParameter("height_" + count)), 1));
						
						//-----RETURN VISIT DATE ---
						childObs.add(createObservation(encDate, loc, patientChild, cs
						        .getConcept(PMTCTConstants.RETURN_VISIT_DATE), Context.getDateFormat().parse(
						    request.getParameter("returnVisitDate_" + count)), 2));
						
						//----- CHILD COMMENT ---				
						childObs.add(createObservation(encDate, loc, patientChild, cs
						        .getConcept(PMTCTConstants.CLINICAL_COMMENT_IMPRESSION), request.getParameter("childComment_"
						        + count), 3));
						
						//save the encounter
						Encounter childEnc = new Encounter();
						childEnc = createEncounter(encDate, provider, loc, patientChild, Context.getEncounterService()
						        .getEncounterType(PMTCTConfigurationUtils.getMaternityEncounterTypeId()), childObs);
						childEnc.setDateCreated(new Date());
						
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Saving the Child encounter...");
						Context.getEncounterService().saveEncounter(childEnc);
						log.info(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
						        + " : Child encounter saved successfully.");
						
						//EncounterService encsev = Context.getEncounterService();
						//PatientService patientService = Context.getPatientService();
						
						//					PersonService personService = Context.getPersonService();
						//					ProgramWorkflowService programWorkFlowService = Context.getProgramWorkflowService();
						
						numberOfChildrenCreated += 1;
					} else
						log.error(">>>>>>>>"
						        + Context.getEncounterService().getEncounterType(
						            PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName() + " : The child ["
						        + patientChild + "] cannot be created because someone else is using this identifier ["
						        + request.getParameter("pId_" + count) + "] !");
				}
				
				count += 1;
			}
			
			//=========== CHILD INFO =============
			//if (mt.getChirdBornStutus() != PMTCTConfigurationUtils.getBornDeadConceptId()) {				
			
			//}
			
			//if (mt.getChirdBornStutus() != PMTCTConfigurationUtils.getBornDeadConceptId()) {
			
			//}
			
		}
		catch (Exception e) {
			log.info(">>>>>>>>PMTCT>>MATERNITY>>ENC " + e.getMessage());
			e.printStackTrace();
		}
		
		return numberOfChildrenCreated;
		
	}
	
	private boolean completePMTCTProgram(HttpServletRequest request, Date dateCompleted, Program program) {
		EncounterService es = Context.getEncounterService();
		PatientProgram pp = null;
		try {
			ArrayList<PatientProgram> ppList = (ArrayList) request.getSession().getAttribute("pmtctprogram");
			for (PatientProgram p : ppList) {
				if (p.getDateCompleted() == null) {
					pp = p;
					break;
				}
			}
			if (pp != null) {
				pp.setDateCompleted(dateCompleted);
				log.info(es.getEncounterType(PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
				        + " : Trying to save the date of Completion of PatientID_" + pp.getPatient().getPatientId() + " in "
				        + program.getName());
				Context.getProgramWorkflowService().updatePatientProgram(pp);
				log.info(es.getEncounterType(PMTCTConfigurationUtils.getMaternityEncounterTypeId()).getName()
				        + " : Patient with ID " + pp.getPatient().getPatientId() + ", complete the " + program.getName()
				        + " successfully.");
			}
			return true;
		}
		catch (Exception e) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> An error occured when trying to complete the program... " + pp + "\n"
			        + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	//	@Override
	//	protected PmtctMaternity formBackingObject(HttpServletRequest request) throws Exception {
	//		PmtctMaternity pmtctMaternity = new PmtctMaternity();
	//		if (request.getParameter("patientId") != null || (!request.getParameter("patientId").equals(""))) {
	//			pmtctMaternity.setPatient(((PatientService) ServiceContext.getInstance().getService(PatientService.class))
	//			        .getPatient(Integer.parseInt(request.getParameter("patientId"))));
	//		}
	//		return pmtctMaternity;
	//	}
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		if (request.getParameter("save") != null) {
			mav = saveMaternityEncounter(request);
			return mav;
		}
		
		initializeData(mav, request);
		
		return mav;
	}
	
	private void initializeData(ModelAndView mav, HttpServletRequest request) throws Exception {
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		try {
			Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
			mav.addObject("patient", patient);
			mav.addObject("todayDate", Context.getDateFormat().format(new Date()));
			mav.addObject("defaultLocation", Context.getLocationService().getLocation(
			    PMTCTConfigurationUtils.getDefaultLocationId()));
			mav.addObject("defaultProvider", Context.getAuthenticatedUser());
			
			mav.addObject("childBornAliveId", PMTCTConfigurationUtils.getBornAliveConceptId());
			mav.addObject("testHivWorkroomId", PMTCTConfigurationUtils.getHivTestInDeliveryRoomConceptId());
			mav.addObject("resultHivTestId", PMTCTConstants.RESULT_HIV_TEST_IN_DELIVERY_ROOM);
			mav.addObject("dpaId", PMTCTConstants.PREGNANT_DUE_DATE);
			mav.addObject("dateConfinementId", PMTCTConstants.DATE_OF_CONFINEMENT);
			mav.addObject("prophylaxisId", PMTCTConstants.PROPHYLAXIS_STOPPED);
			mav.addObject("transferOutDateId", PMTCTConstants.TRANSFER_OUT_DATE);
			mav.addObject("chirdBornStatusId", PMTCTConfigurationUtils.getBornStatusConceptId());
			mav.addObject("commentId", PMTCTConstants.CLINICAL_COMMENT_IMPRESSION);
			mav.addObject("infantFeedingMethodId", PMTCTConstants.INFANT_FEEDING_METHOD);
			//			mav.addObject("childAgeBreastedId", PMTCTConstants.BREASTFED_UNTIL_WHAT_AGE);
			mav.addObject("childReturnVisitDateId", PMTCTConstants.RETURN_VISIT_DATE);
			mav.addObject("takingAntiRetroviralForPmtctId", PMTCTConstants.ARV_FOR_PMTCT);
			mav.addObject("weightId", PMTCTConstants.WEIGHT);
			mav.addObject("heightId", PMTCTConstants.HEIGHT);
			mav.addObject("positiveId", PMTCTConstants.POSITIVE);
			
			mav.addObject("tracnetIdentifierTypeId", PMTCTConfigurationUtils.getTRACNETIdentifierTypeId());
			mav.addObject("localIdentifierTypeId", PMTCTConfigurationUtils.getLocalHealthCenterIdentifierTypeId());
			
			mav.addObject("resultHivTestAnswers", PMTCTUtil.createResultOfHivTestOptions());
			
			mav.addObject("childBornStatusAnswers", PMTCTUtil.createCodedOptions(PMTCTConfigurationUtils
			        .getBornStatusConceptId()));
			mav.addObject("childWeight", Context.getConceptService().getConcept(PMTCTConstants.WEIGHT).getName().getName());
			mav.addObject("infantFeedingMethodAnswers", PMTCTUtil.createInfantFeedingMethodOptions());
			
			//Last obs values
			mav.addObject("lastHivResultStatus", checkObservationLastValue(patient, Context.getConceptService().getConcept(
			    PMTCTConstants.RESULT_OF_HIV_TEST)));
			
			//--------- CD4 INFORMATION ---------
			EncounterService es = Context.getEncounterService();//encounter service
			
			//get all encounters for that patient
			List<Encounter> encList = es.getEncountersByPatient(patient);
			
			Encounter pmtctCPNInfoEnc = null;//last encounter of type CPNInfo for that patient
			
			Double cd4Count = 0.0;//cd4 cont 4r that patient
			
			//pmtctCPNInfoEnc
			for (Encounter enc : encList) {
				if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getCPNEncounterTypeId()) {
					if (pmtctCPNInfoEnc != null) {
						if (pmtctCPNInfoEnc.getEncounterDatetime().compareTo(enc.getEncounterDatetime()) > 0)
							pmtctCPNInfoEnc = enc;
					} else
						pmtctCPNInfoEnc = enc;
				}
			}
			
			if (null != pmtctCPNInfoEnc) {
				for (Obs obs : pmtctCPNInfoEnc.getAllObs()) {
					if (obs.getConcept().getConceptId() == PMTCTConstants.CD4_COUNT) {
						cd4Count = Double.parseDouble(obs.getValueAsString(Context.getLocale()));
						break;
					}
				}
			}
			//--------- END CD4 ----------
			mav.addObject("cd4Value", cd4Count);
			
			Obs last_dpa = checkObservationLastValue(patient, Context.getConceptService().getConcept(
			    PMTCTConstants.PREGNANT_DUE_DATE));
			mav.addObject("last_dpa", last_dpa);
			if (last_dpa != null)
				mav.addObject("dpa", Context.getDateFormat().format(last_dpa.getValueDatetime()));
			
		}
		catch (Exception e) {
			log.error(">>>>>>>>>PMTCT>>MATERNITY>>>>ENC>> " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//	@Override
	//	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
	//		HashMap<String, Object> map = new HashMap<String, Object>();
	//		PmtctMaternity mt = (PmtctMaternity) command;
	//		
	//		//request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
	//		//mt.setBornDeadConceptId(PMTCTConfigurationUtils.getBornDeadConceptId()); //used in validation
	//		
	//		try {
	//			//mt.setEncounterDate(new Date());
	//			//mt.setLocation(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
	//			//if (Context.getAuthenticatedUser().hasRole("Provider"))
	//			//	mt.setProvider(Context.getAuthenticatedUser());
	//			
	//			//Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
	//			//map.put("patient", patient);
	//			
	//			
	//			
	//			
	//			// ============ REGUARDING TO THE CHILD============
	//			
	//			
	////			map.put("path", request.getContextPath());
	//			
	//			
	//		}
	//		catch (Exception ex) {
	//			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
	//			    "An error occured when trying to load data. Contact your administrator");
	//			log.error(">>>>PMTCT>>MATERNITY>>ENC>> An error occured when loading values:");
	//			ex.printStackTrace();
	//		}
	//		return map;
	//	}
	
	private Double changeBoolean(Boolean b) {
		if (b.equals(true))
			return 1.0;
		else
			return 0.0;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param patient
	 * @param concept
	 * @return
	 */
	private Obs checkObservationLastValue(Patient patient, Concept concept) {
		List<Obs> obs = Context.getObsService().getObservationsByPersonAndConcept(patient, concept);
		Obs ob = null;
		
		if (obs != null) {
			for (Obs o : obs) {
				if (null != ob) {
					if (ob.getObsDatetime().compareTo(o.getObsDatetime()) <= 0)
						ob = o;
				} else
					ob = o;
			}
		}
		return ob;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param obsDatetime The obs datetime
	 * @param loc The location
	 * @param p The Patient
	 * @param c The concept concerned
	 * @param obsValue The value of the obs
	 * @param obsValueType The type of obs value - 1 Numeric; 2 Datetime; 3 Text; 4 Coded
	 * @return
	 */
	private Obs createObservation(Date obsDatetime, Location loc, Person p, Concept c, Object obsValue, int obsValueType) {
		Obs o = new Obs();
		
		try {
			o.setDateCreated(new Date());
			o.setObsDatetime(obsDatetime);
			o.setLocation(loc);
			o.setPerson(p);
			o.setConcept(c);
			
			if (obsValueType == 1) {
				o.setValueNumeric((Double) obsValue);
			} else if (obsValueType == 2) {
				o.setValueDatetime((Date) obsValue);
			} else if (obsValueType == 3) {
				o.setValueText((String) obsValue);
			} else if (obsValueType == 4) {
				o.setValueCoded((Concept) obsValue);
			}
		}
		catch (Exception e) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + e.getMessage());
			e.printStackTrace();
			o = null;
		}
		return o;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param encounterDate Date of the encounter
	 * @param provider Provider
	 * @param location Location
	 * @param patient Patient
	 * @param encounterType The type of that encounter
	 * @param obsList The list of obs related to that encounter
	 * @return
	 */
	private Encounter createEncounter(Date encounterDate, User provider, Location location, Patient patient,
	                                  EncounterType encounterType, List<Obs> obsList) {
		Encounter enc = new Encounter();
		
		try {
			enc.setDateCreated(new Date());
			enc.setEncounterDatetime(encounterDate);
			enc.setProvider(provider);
			enc.setLocation(location);
			enc.setPatient(patient);
			enc.setEncounterType(encounterType);
			
			for (Obs o : obsList) {
				if (null != o)
					enc.addObs(o);
				else
					log.error(">>>>PMTCT>>MATERNITY>>ENC>> An observation has not been saved because it was null.");
			}
		}
		catch (Exception e) {
			log.error(">>>>PMTCT>>MATERNITY>>ENC>> " + e.getMessage());
			e.printStackTrace();
			enc = null;
		}
		return enc;
	}
}
