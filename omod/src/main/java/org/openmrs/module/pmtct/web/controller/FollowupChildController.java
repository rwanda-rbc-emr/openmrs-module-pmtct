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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.Person;
import org.openmrs.Program;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.PMTCTConfiguration;
import org.openmrs.module.pmtct.PmtctChildInformation;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.module.pmtct.util.PMTCTUtil;
import org.openmrs.propertyeditor.LocationEditor;
import org.openmrs.propertyeditor.PatientEditor;
import org.openmrs.propertyeditor.UserEditor;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.WebConstants;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 *
 */
public class FollowupChildController extends SimpleFormController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
//	private PMTCTConfiguration config = null;
//	
//	private PMTCTConstants constants = null;
	
	/**
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	 *      org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		binder.registerCustomEditor(Patient.class, new PatientEditor());
		binder.registerCustomEditor(Location.class, new LocationEditor());
		binder.registerCustomEditor(User.class, new UserEditor());
		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(OpenmrsUtil.getDateFormat(), true));
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		PmtctChildInformation pmtctChildTest = new PmtctChildInformation();
		pmtctChildTest.setPatient(Context.getPatientService()
		        .getPatient(Integer.parseInt(request.getParameter("patientId"))));
		return pmtctChildTest;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings( { "deprecation", "static-access" })
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		PmtctChildInformation pmtctChildTest = (PmtctChildInformation) command;
		
		//Default encounter type for infant form
		List<DrugOrder> drugOrders = new ArrayList<DrugOrder>();
		EncounterService es = Context.getEncounterService();
		List<Encounter> encList = es.getEncounters(pmtctChildTest.getPatient());
		List<Encounter> pmtctMaternityEnc = new ArrayList<Encounter>();
		List<Encounter> pmtctPCREnc = new ArrayList<Encounter>();
		List<Encounter> pmtctSerology9Enc = new ArrayList<Encounter>();
		List<Encounter> pmtctSerology18Enc = new ArrayList<Encounter>();
		List<DrugOrder> drugOrdersTmp = null;
		
//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//		constants = config.getListOfConstants().get(0);
//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			pmtctChildTest.setPatient(Context.getPatientService().getPatient(
			    Integer.parseInt(request.getParameter("patientId"))));
			pmtctChildTest.setLocation(Context.getLocationService().getLocation(
			    PMTCTConfigurationUtils.getDefaultLocationId()));
			pmtctChildTest.setEncounterDate((null == pmtctChildTest.getEncounterDate()) ? new Date() : pmtctChildTest
			        .getEncounterDate());
			if (Context.getAuthenticatedUser().hasRole("Provider"))
				pmtctChildTest.setProvider(Context.getAuthenticatedUser());
			
			map.put("patient", pmtctChildTest.getPatient());
			
			drugOrdersTmp = Context.getOrderService().getDrugOrdersByPatient(
			    Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId"))));
			for (DrugOrder d : drugOrdersTmp) {
				if (d.getConcept().getConceptId() == PMTCTConstants.PMTCT_DRUG_ORDER_CONCEPT_RELATED_ID)
					drugOrders.add(d);
			}
			
			map.put("drugOrders", drugOrders);
			
			for (Encounter enc : encList) {
				if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMaternityEncounterTypeId()) {
					pmtctMaternityEnc.add(enc);
				} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils
				        .getPCRTestEncounterTypeId()) {
					pmtctPCREnc.add(enc);
				} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils
				        .getSerology9MonthEncounterTypeId()) {
					pmtctSerology9Enc.add(enc);
				} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils
				        .getSerology18MonthEncounterTypeId()) {
					pmtctSerology18Enc.add(enc);
				}
			}
			
			if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId())) {
				
				//reason for exiting care
				map.put("reasonForExitingCareAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.REASON_FOR_EXITING_CARE));
				map.put("reasonForExitingCareId", PMTCTConstants.REASON_FOR_EXITING_CARE);
				//				log.info(".....................----------------............"+PMTCTConstants.REASON_FOR_EXITING_CARE);
				
				if (pmtctSerology9Enc.size() != 0) {
					for (Obs o : pmtctSerology9Enc.get(0).getAllObs()) {
						if (o.getConcept().getConceptId() == PMTCTConstants.RETURN_VISIT_DATE) {
							if (pmtctSerology18Enc.size() != 0)
								request.setAttribute("encounter", pmtctSerology18Enc.get(0));
							request.setAttribute("returnDateVisit", o.getValueAsString(Context.getLocale()));
							break;
						}
					}
				}
			} else if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId())) {
				if (pmtctPCREnc.size() != 0) {
					for (Obs o : pmtctPCREnc.get(0).getAllObs()) {
						if (o.getConcept().getConceptId() == PMTCTConstants.RETURN_VISIT_DATE) {
							if (pmtctSerology9Enc.size() != 0)
								request.setAttribute("encounter", pmtctSerology9Enc.get(0));
							request.setAttribute("returnDateVisit", o.getValueAsString(Context.getLocale()));
							break;
						}
					}
				}
			} else if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getPCRTestEncounterTypeId())) {
				if (pmtctMaternityEnc.size() != 0) {
					for (Obs o : pmtctMaternityEnc.get(0).getAllObs()) {
						if (o.getConcept().getConceptId() == PMTCTConstants.RETURN_VISIT_DATE) {
							if (pmtctPCREnc.size() != 0)
								request.setAttribute("encounter", pmtctPCREnc.get(0));
							request.setAttribute("returnDateVisit", o.getValueAsString(Context.getLocale()));
							break;
						}
					}
				}
			}
			
			//Result of HIV Test
			map.put("resultHIVTestAnswers", PMTCTUtil.createResultOfHivTestOptions());
			
			map.put("resultHIVTestId", PMTCTConstants.RESULT_OF_HIV_TEST);
			map.put("dateOfReturnedResultId", PMTCTConstants.DATE_OF_RETURNED_RESULT);
			map.put("returnVisitDateId", PMTCTConstants.RETURN_VISIT_DATE);
			map.put("dateHIVTestId", PMTCTConstants.HIV_TEST_DATE);
			map.put("path", request.getContextPath());
			map.put("positiveId", PMTCTConstants.POSITIVE);
			
			map.put("pcrEncId", PMTCTConfigurationUtils.getPCRTestEncounterTypeId());
			map.put("ser9MonthEncId", PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId());
			map.put("ser18MonthEncId", PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId());
			
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			log.error("An error occured when trying to load Children HIV Test: ID= " + request.getParameter("patientId"));
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
	                                BindException errors) throws Exception {
		
		PmtctChildInformation ci = (PmtctChildInformation) command;
		
		ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + getSuccessView()
		        + ci.getPatient().getPatientId()));
		
		EncounterService es = Context.getEncounterService();
		Encounter enc = new Encounter();
		ConceptService cs = Context.getConceptService();
		
		List<Obs> obsList = new ArrayList<Obs>();
		
		try {
			//observation 1
			obsList.add(createObservation(ci.getEncounterDate(), ci.getLocation(), ci.getPatient(), cs
			        .getConcept(PMTCTConstants.HIV_TEST_DATE), ci.getHivTestDate(), 2));
			
			//observation 2
			obsList.add(createObservation(ci.getEncounterDate(), ci.getLocation(), ci.getPatient(), cs
			        .getConcept(PMTCTConstants.RESULT_OF_HIV_TEST), cs.getConcept(ci.getResultOfHivTest()), 4));
			
			//observation 3
			obsList.add(createObservation(ci.getEncounterDate(), ci.getLocation(), ci.getPatient(), cs
			        .getConcept(PMTCTConstants.DATE_OF_RETURNED_RESULT), ci.getDateOfReturnedResult(), 2));
			
			PatientProgram pp = new PatientProgram();
			
			//exit reason
			if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId())) {
				if (ci.getResultOfHivTest() != PMTCTConstants.POSITIVE) {
					//observation 5
					obsList.add(createObservation(ci.getEncounterDate(), ci.getLocation(), ci.getPatient(), cs
					        .getConcept(PMTCTConstants.REASON_FOR_EXITING_CARE), cs.getConcept(ci.getReasonOfExitingCare()),
					    4));
				}
				
				//exit patient from PMTCT program
				pp = (PatientProgram) ((ArrayList) request.getSession().getAttribute("pmtctprogram")).get(0);
				pp.setDateCompleted(ci.getEncounterDate());
			} else {
				//observation 4
				obsList.add(createObservation(ci.getEncounterDate(), ci.getLocation(), ci.getPatient(), cs
				        .getConcept(PMTCTConstants.RETURN_VISIT_DATE), ci.getReturnedVisitDate(), 2));
			}
			
			Program program = Context.getProgramWorkflowService().getProgram(PMTCTConfigurationUtils.getHivProgramId());
			PatientProgram patientProgram = new PatientProgram();
			
			if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId())) {
				if (ci.getResultOfHivTest() == PMTCTConstants.POSITIVE) {
					//enroll the child in PMTCT program					
					patientProgram.setPatient(ci.getPatient());
					patientProgram.setProgram(program);
					patientProgram.setDateEnrolled(ci.getDateOfReturnedResult());
					patientProgram.setCreator(Context.getAuthenticatedUser());
					patientProgram.setDateCreated(new Date());
				}
			}
			
			//creating the encounter and add obs
			enc = createEncounter(ci.getEncounterDate(), ci.getProvider(), ci.getLocation(), ci.getPatient(), es
			        .getEncounterType(Integer.parseInt(request.getParameter("encounterType"))), obsList);
			enc.setDateCreated(new Date());
			
			//save the encounter
			log.info(es.getEncounterType(Integer.parseInt(request.getParameter("encounterType"))).getName()
			        + " : Saving the encounter...");
			es.saveEncounter(enc);
			log.info(es.getEncounterType(Integer.parseInt(request.getParameter("encounterType"))).getName()
			        + " : Encounter saved successfully.");
			
			if (request.getParameter("encounterType").toString().equalsIgnoreCase(
			    "" + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId())) {
				Context.getProgramWorkflowService().updatePatientProgram(pp);
				if (ci.getResultOfHivTest() == PMTCTConstants.POSITIVE) {
					log.info(es.getEncounterType(Integer.parseInt(request.getParameter("encounterType"))).getName()
					        + " : Trying to enroll patient in HIV Program...");
					Context.getProgramWorkflowService().savePatientProgram(patientProgram);
					log.info(es.getEncounterType(Integer.parseInt(request.getParameter("encounterType"))).getName()
					        + " : Patient enrolled successfully in HIV Program.");
				}
			}
			
			String msg = getMessageSourceAccessor().getMessage("pmtct.general.saveSuccess");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
			
			log.info(es.getEncounterType(Integer.parseInt(request.getParameter("encounterType"))).getName()
			        + " : saved succesiffully.");
		}
		catch (Exception e) {
			log.error("An error occured when trying to save Children HIV Test: ID= " + request.getParameter("patientId"));
			e.printStackTrace();
			
			String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		
		return mav;
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
			System.err.println(e.getMessage());
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
					log.info("PMTCT : An observation has not been saved because it was null.");
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			enc = null;
		}
		return enc;
	}
}
