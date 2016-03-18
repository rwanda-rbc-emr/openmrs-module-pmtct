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

import java.text.SimpleDateFormat;
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
import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracConfigurationUtil;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.module.pmtct.util.PMTCTUtil;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Yves GAKUBA
 */
public class FollowupMotherController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	//	private PMTCTConfiguration config = null;
	//	
	//	private PMTCTConstants constants = null;
	
	//	/**
	//	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
	//	 *      org.springframework.web.bind.ServletRequestDataBinder)
	//	 */
	//	@Override
	//	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
	//		binder.registerCustomEditor(Patient.class, new PatientEditor());
	//		binder.registerCustomEditor(Location.class, new LocationEditor());
	//		binder.registerCustomEditor(User.class, new UserEditor());
	//		binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(OpenmrsUtil.getDateFormat(), true));
	//	}
	//	
	//	/**
	//	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	//	 */
	//	@Override
	//	protected Object formBackingObject(HttpServletRequest request) throws Exception {
	//		PmtctFollowupMother pmtctFollowupMother = new PmtctFollowupMother();
	//		pmtctFollowupMother.setPatient(Context.getPatientService().getPatient(
	//		    Integer.parseInt(request.getParameter("patientId"))));
	//		
	//		return pmtctFollowupMother;
	//	}
	//	
	//	/**
	//	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	//	 *      java.lang.Object, org.springframework.validation.Errors)
	//	 */
	//	@SuppressWarnings( { "static-access", "deprecation" })
	//	@Override
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav;
		
		if (request.getParameter("save") != null && request.getParameter("save").trim().compareToIgnoreCase("true") == 0) {
			int action = Integer.valueOf(request.getParameter("btClicked"));
//			mav.addObject("btClicked", (action==null)?"":action);
			
			mav = new ModelAndView(new RedirectView(request.getContextPath() + "/patientDashboard.form?patientId="
		        + request.getParameter("patientId")));
			
			if (action == 1) {
				saveBilanPreARV(request, mav);
			} else if (action == 2) {
				saveCD4CountEncounter(request, mav);
			} else if (action == 3) {
				saveHIVTestEncounterForDiscordantCouples(request, mav);
			}
		}else
			mav=new ModelAndView(getViewName());
		
		loadUtils(mav, request);
		
		return mav;
	}
	
	private void loadUtils(ModelAndView mav, HttpServletRequest request) throws Exception {
		Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
		List<Encounter> encList = new ArrayList<Encounter>();
		List<Encounter> encList_bPreARV = new ArrayList<Encounter>();
		List<Encounter> encList_cd4Test = new ArrayList<Encounter>();
		List<Encounter> encList_cplDisc = new ArrayList<Encounter>();
		
		mav.addObject("encounterDate", (request.getParameter("encounterDate") == null) ? Context.getDateFormat().format(
		    new Date()) : request.getParameter("encounterDate"));
		
		//		fm.setEncounterDate((fm.getEncounterDate() == null) ? new Date() : fm.getEncounterDate());
		//			fm.setEncounterDate_cd4((fm.getEncounterDate_cd4() == null) ? new Date() : fm.getEncounterDate_cd4());
		//			fm
		//			        .setEncounterDate_cplDisc((fm.getEncounterDate_cplDisc() == null) ? new Date() : fm
		//			                .getEncounterDate_cplDisc());
		
		mav.addObject("location", (request.getParameter("location") == null) ? MohTracConfigurationUtil
		        .getDefaultLocationId() : request.getParameter("location"));
		//			fm.setLocation(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
		//			fm.setLocation_cd4(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
		//			fm.setLocation_cplDisc(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
		
		if (Context.getAuthenticatedUser().hasRole("Provider")) {
			mav.addObject("provider", (request.getParameter("provider") == null) ? Context.getAuthenticatedUser()
			        .getUserId() : request.getParameter("provider"));
			//				fm.setProvider(Context.getAuthenticatedUser());
			//				fm.setProvider_cd4(Context.getAuthenticatedUser());
			//				fm.setProvider_cplDisc(Context.getAuthenticatedUser());
		}
		
		mav.addObject("patient", patient);
		
		for (Encounter e : Context.getEncounterService().getEncountersByPatient(patient)) {
			if (e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId())
				encList.add(e);
		}
		
		int list = 0;
		
		for (Encounter e : encList) {
			list = 0;
			for (Obs ob : e.getAllObs()) {
				if (ob.getConcept().getConceptId() == PMTCTConstants.HEMOGLOBIN) {
					list = 1;
					break;
				} else if (ob.getConcept().getConceptId() == PMTCTConstants.RESULT_OF_HIV_TEST) {
					list = 2;
					break;
				}
			}
			if (list == 0)
				encList_cd4Test.add(e);
			else if (list == 1)
				encList_bPreARV.add(e);
			else if (list == 2)
				encList_cplDisc.add(e);
		}
		
		mav.addObject("encList_bPreARV", encList_bPreARV);
		mav.addObject("encList_cd4Test", encList_cd4Test);
		mav.addObject("encList_cplDisc", encList_cplDisc);
		
		//-------------------------------------------------------------------------------------------
		
		ConceptService cs = Context.getConceptService();
		
		//			map.put("cpnNumberIdentifierID", constants.getCpnIdentifierTypeId());
		mav.addObject("cpnNumberIdentifierID", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
		mav.addObject("hivStatusId", PMTCTConstants.HIV_STATUS);
		mav.addObject("positiveId", PMTCTConstants.POSITIVE);
		mav.addObject("partner_hivStatusId", PMTCTConstants.TESTING_STATUS_OF_PARTNER);
		mav.addObject("numberOfPregnanciesId", PMTCTConstants.NUMBER_OF_PREGNANCIES);
		mav.addObject("dpaId", PMTCTConstants.PREGNANT_DUE_DATE);
		mav.addObject("dateCD4CountResultReceivedId", PMTCTConfigurationUtils.getReceptionDateCD4CountConceptId());
		mav.addObject("hemoglobinId", PMTCTConstants.HEMOGLOBIN);
		mav.addObject("dateOfCD4CountId", PMTCTConstants.DATE_OF_CD4_COUNT);
		mav.addObject("CD4CountId", PMTCTConstants.CD4_COUNT);
		mav.addObject("tbScreeningId", PMTCTConstants.TB_SCREENING);
		mav.addObject("whoStageId", PMTCTConstants.WHO_STAGE);
		//			map.put("criticalLevelOfCD4Count", constants.getCriticalLevelOfCD4Count());
		mav.addObject("criticalLevelOfCD4Count", MohTracConfigurationUtil.getCriticalLevelOfCD4Count());
		mav.addObject("dateHIVTestId", PMTCTConstants.HIV_TEST_DATE);
		mav.addObject("resultOfHIVTestId", PMTCTConstants.RESULT_OF_HIV_TEST);
		mav.addObject("dateHIVTestReceivedId", PMTCTConfigurationUtils.getResultHivTestReceptionDateConceptId());
		mav.addObject("dateNextRdzId", PMTCTConstants.RETURN_VISIT_DATE);
		mav.addObject("providers", MohTracUtil.createProviderOptions());
		mav.addObject("locations", MohTracUtil.createLocationOptions());
		
		//Result of TB Screening
		mav.addObject("tbScreeningAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.TB_SCREENING));
		
		//Result of Who Stage
		mav.addObject("whoStageAnswers", PMTCTUtil.createWHOStageAdultOptions());
		
		//CPN Encounter
		mav.addObject("encounter", getCPNEncounterInfo(patient));
		
		//Last obs values
		mav.addObject("last_CD4Count", checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.CD4_COUNT)));
		mav.addObject("last_returnVisitDate", checkObservationLastValue(patient, cs
		        .getConcept(PMTCTConstants.RETURN_VISIT_DATE)));
		
		Obs last_hivStatus = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST));
		mav.addObject("last_hivStatus", last_hivStatus);
		if (last_hivStatus != null)
			//fm.setResultOfHivTest_cplDisc(last_hivStatus.getValueCoded().getConceptId());
			mav.addObject("resultOfHivTest", last_hivStatus.getValueCoded().getConceptId());
		
		Obs last_whoStage = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.WHO_STAGE));
		mav.addObject("last_whoStage", last_whoStage);
		if (last_whoStage != null)
			//				fm.setWhoStage(last_whoStage.getValueCoded().getConceptId());
			mav.addObject("whoStage", last_whoStage.getValueCoded().getConceptId());
		
		Obs last_tbScreening = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.TB_SCREENING));
		mav.addObject("last_tbScreening", last_tbScreening);
		if (last_tbScreening != null)
			//fm.setTbScreening(last_tbScreening.getValueCoded().getConceptId());
			mav.addObject("tbScreening", last_tbScreening.getValueCoded().getConceptId());
		
		mav.addObject("resultHIVTestAnswers", PMTCTUtil.createResultOfHivTestOptions());
	}
	
	/*protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		
		Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
		PmtctFollowupMother fm = (PmtctFollowupMother) command;
		List<Encounter> encList = new ArrayList<Encounter>();
		List<Encounter> encList_bPreARV = new ArrayList<Encounter>();
		List<Encounter> encList_cd4Test = new ArrayList<Encounter>();
		List<Encounter> encList_cplDisc = new ArrayList<Encounter>();
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
		//		constants = config.getListOfConstants().get(0);
		//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			fm.setEncounterDate((fm.getEncounterDate() == null) ? new Date() : fm.getEncounterDate());
			fm.setEncounterDate_cd4((fm.getEncounterDate_cd4() == null) ? new Date() : fm.getEncounterDate_cd4());
			fm
			        .setEncounterDate_cplDisc((fm.getEncounterDate_cplDisc() == null) ? new Date() : fm
			                .getEncounterDate_cplDisc());
			
			fm.setLocation(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
			fm.setLocation_cd4(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
			fm.setLocation_cplDisc(Context.getLocationService().getLocation(PMTCTConfigurationUtils.getDefaultLocationId()));
			
			if (Context.getAuthenticatedUser().hasRole("Provider")) {
				fm.setProvider(Context.getAuthenticatedUser());
				fm.setProvider_cd4(Context.getAuthenticatedUser());
				fm.setProvider_cplDisc(Context.getAuthenticatedUser());
			}
			
			map.put("patient", patient);
			
			for (Encounter e : Context.getEncounterService().getEncountersByPatient(patient)) {
				if (e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId())
					encList.add(e);
			}
			
			int list = 0;
			
			for (Encounter e : encList) {
				list = 0;
				for (Obs ob : e.getAllObs()) {
					if (ob.getConcept().getConceptId() == PMTCTConstants.HEMOGLOBIN) {
						list = 1;
						break;
					} else if (ob.getConcept().getConceptId() == PMTCTConstants.RESULT_OF_HIV_TEST) {
						list = 2;
						break;
					}
				}
				if (list == 0)
					encList_cd4Test.add(e);
				else if (list == 1)
					encList_bPreARV.add(e);
				else if (list == 2)
					encList_cplDisc.add(e);
			}
			
			map.put("encList_bPreARV", encList_bPreARV);
			map.put("encList_cd4Test", encList_cd4Test);
			map.put("encList_cplDisc", encList_cplDisc);
			
			//-------------------------------------------------------------------------------------------
			
			ConceptService cs = Context.getConceptService();
			
			//			map.put("cpnNumberIdentifierID", constants.getCpnIdentifierTypeId());
			map.put("cpnNumberIdentifierID", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
			map.put("hivStatusId", PMTCTConstants.HIV_STATUS);
			map.put("positiveId", PMTCTConstants.POSITIVE);
			map.put("partner_hivStatusId", PMTCTConstants.TESTING_STATUS_OF_PARTNER);
			map.put("numberOfPregnanciesId", PMTCTConstants.NUMBER_OF_PREGNANCIES);
			map.put("dpaId", PMTCTConstants.PREGNANT_DUE_DATE);
			map.put("dateCD4CountResultReceivedId", PMTCTConfigurationUtils.getReceptionDateCD4CountConceptId());
			map.put("hemoglobinId", PMTCTConstants.HEMOGLOBIN);
			map.put("dateOfCD4CountId", PMTCTConstants.DATE_OF_CD4_COUNT);
			map.put("CD4CountId", PMTCTConstants.CD4_COUNT);
			map.put("tbScreeningId", PMTCTConstants.TB_SCREENING);
			map.put("whoStageId", PMTCTConstants.WHO_STAGE);
			//			map.put("criticalLevelOfCD4Count", constants.getCriticalLevelOfCD4Count());
			map.put("criticalLevelOfCD4Count", MohTracConfigurationUtil.getCriticalLevelOfCD4Count());
			map.put("dateHIVTestId", PMTCTConstants.HIV_TEST_DATE);
			map.put("resultOfHIVTestId", PMTCTConstants.RESULT_OF_HIV_TEST);
			map.put("dateHIVTestReceivedId", PMTCTConfigurationUtils.getResultHivTestReceptionDateConceptId());
			map.put("dateNextRdzId", PMTCTConstants.RETURN_VISIT_DATE);
			
			//Result of TB Screening
			map.put("tbScreeningAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.TB_SCREENING));
			
			//Result of Who Stage
			map.put("whoStageAnswers", PMTCTUtil.createWHOStageAdultOptions());
			
			//CPN Encounter
			map.put("encounter", getCPNEncounterInfo(patient));
			
			//Last obs values
			map.put("last_CD4Count", checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.CD4_COUNT)));
			map.put("last_returnVisitDate", checkObservationLastValue(patient, cs
			        .getConcept(PMTCTConstants.RETURN_VISIT_DATE)));
			
			Obs last_hivStatus = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST));
			map.put("last_hivStatus", last_hivStatus);
			if (last_hivStatus != null)
				fm.setResultOfHivTest_cplDisc(last_hivStatus.getValueCoded().getConceptId());
			
			Obs last_whoStage = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.WHO_STAGE));
			map.put("last_whoStage", last_whoStage);
			if (last_whoStage != null)
				fm.setWhoStage(last_whoStage.getValueCoded().getConceptId());
			
			Obs last_tbScreening = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.TB_SCREENING));
			map.put("last_tbScreening", last_tbScreening);
			if (last_tbScreening != null)
				fm.setTbScreening(last_tbScreening.getValueCoded().getConceptId());
			
			map.put("resultHIVTestAnswers", PMTCTUtil.createResultOfHivTestOptions());
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			log.error("An error occured when trying to load values:");
			ex.printStackTrace();
		}
		return map;
	}*/

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
	 * @param patient
	 * @return
	 * @throws Exception
	 */
	private Encounter getCPNEncounterInfo(Patient patient) throws Exception {
		EncounterService es = Context.getEncounterService();
		Encounter encounter = null;
		
		List<Encounter> pmtctCPNEnc = new ArrayList<Encounter>();
		for (Encounter enc : es.getEncountersByPatient(patient)) {
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
	
	//	/**
	//	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	//	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	//	 *      org.springframework.validation.BindException)
	//	 */
	//	@SuppressWarnings("finally")
	//	@Override
	//	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
	//	                                BindException errors) throws Exception {
	//		PmtctFollowupMother fm = (PmtctFollowupMother) command;
	//		
	//		ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + getSuccessView()
	//		        + fm.getPatient().getPatientId()));
	//		
	//		//		EncounterService es = Context.getEncounterService();
	//		//		Encounter enc = new Encounter();
	//		//		ConceptService cs = Context.getConceptService();
	//		//		
	//		//		List<Obs> obsList = new ArrayList<Obs>();
	//		
	//		try {
	//			if (fm.getBtClicked().equalsIgnoreCase("1")) {
	//				//				//bilan pre-ARV
	//				//				//creating a list of obs
	//				//				//Hemoglobine
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.HEMOGLOBIN), fm.getHemoglobine(), 1));
	//				//				//Date of CD4 Count
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.DATE_OF_CD4_COUNT), fm.getDateOfCD4Count(), 2));
	//				//				//CD4 Count
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.CD4_COUNT), fm.getCd4Count(), 1));
	//				//				//Date result of CD4 Count received
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConfigurationUtils.getReceptionDateCD4CountConceptId()), fm
	//				//				        .getDateResultOfCD4CountReceived(), 2));
	//				//				//TB screening
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.TB_SCREENING), cs.getConcept(fm.getTbScreening()), 4));
	//				//				//WHO Stage
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.WHO_STAGE), cs.getConcept(fm.getWhoStage()), 4));
	//				//				//Next visit date
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.RETURN_VISIT_DATE), fm.getNextVisitDate(), 2));
	//				//				
	//				//				//creating the encounter and add obs
	//				//				enc = createEncounter(fm.getEncounterDate(), fm.getProvider(), fm.getLocation(), fm.getPatient(), es
	//				//				        .getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()), obsList);
	//				//				enc.setDateCreated(new Date());
	//				//				
	//				//				//save the encounter
	//				//				es.saveEncounter(enc);
	//				//				
	//				//				log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
	//				//				        + "(Bilan Pre-ARV) saved succesiffully");
	//				
	//			} else if (fm.getBtClicked().equalsIgnoreCase("2")) {
	//				//				//CD4 Test
	//				//				//creating a list of obs
	//				//				//Date of CD4 Count
	//				//				obsList.add(createObservation(fm.getEncounterDate_cd4(), fm.getLocation_cd4(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.DATE_OF_CD4_COUNT), fm.getDateOfCD4Count_cd4(), 2));
	//				//				//CD4 Count
	//				//				obsList.add(createObservation(fm.getEncounterDate_cd4(), fm.getLocation_cd4(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.CD4_COUNT), fm.getCD4Count_cd4(), 1));
	//				//				//Date result of CD4 Count received
	//				//				obsList.add(createObservation(fm.getEncounterDate_cd4(), fm.getLocation_cd4(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConfigurationUtils.getReceptionDateCD4CountConceptId()), fm
	//				//				        .getDateCD4CountResultReceived_cd4(), 2));
	//				//				//Next visit date
	//				//				obsList.add(createObservation(fm.getEncounterDate(), fm.getLocation(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.RETURN_VISIT_DATE), fm.getNextVisitDate_cd4(), 2));
	//				//				
	//				//				//creating the encounter and add obs
	//				//				enc = createEncounter(fm.getEncounterDate_cd4(), fm.getProvider_cd4(), fm.getLocation_cd4(),
	//				//				    fm.getPatient(), es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()),
	//				//				    obsList);
	//				//				enc.setDateCreated(new Date());
	//				//				
	//				//				//save the encounter
	//				//				es.saveEncounter(enc);
	//				//				
	//				//				log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
	//				//				        + "(CD4 Test) saved succesiffully");
	//				
	//			} else if (fm.getBtClicked().equalsIgnoreCase("3")) {
	//				//				//Cpl Discordant
	//				//				//creating a list of obs
	//				//				//Date of Hiv test
	//				//				obsList.add(createObservation(fm.getEncounterDate_cplDisc(), fm.getLocation_cplDisc(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.HIV_TEST_DATE), fm.getHivTestDate_cplDisc(), 2));
	//				//				//Result of Hiv test
	//				//				obsList.add(createObservation(fm.getEncounterDate_cplDisc(), fm.getLocation_cplDisc(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.RESULT_OF_HIV_TEST), cs.getConcept(fm.getResultOfHivTest_cplDisc()), 4));
	//				//				//Date of Hiv test received
	//				//				obsList.add(createObservation(fm.getEncounterDate_cplDisc(), fm.getLocation_cplDisc(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConfigurationUtils.getResultHivTestReceptionDateConceptId()), fm
	//				//				        .getDateResultOfHivTestReceived_cplDisc(), 2));
	//				//				//Return visit date
	//				//				obsList.add(createObservation(fm.getEncounterDate_cplDisc(), fm.getLocation_cplDisc(), fm.getPatient(), cs
	//				//				        .getConcept(PMTCTConstants.RETURN_VISIT_DATE), fm.getReturnVisitDate_cplDisc(), 2));
	//				//				
	//				//				//creating the encounter and add obs
	//				//				enc = createEncounter(fm.getEncounterDate_cplDisc(), fm.getProvider_cplDisc(), fm.getLocation_cplDisc(), fm
	//				//				        .getPatient(), es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()),
	//				//				    obsList);
	//				//				enc.setDateCreated(new Date());
	//				//				
	//				//				//save the encounter
	//				//				es.saveEncounter(enc);
	//				//				
	//				//				log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
	//				//				        + "(Cpl Discordant) saved succesiffully");
	//			}
	//			
	//			//messages
	//			String msg = getMessageSourceAccessor().getMessage("pmtct.general.saveSuccess");
	//			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
	//		}
	//		catch (Exception e) {
	//			//error
	//			String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
	//			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
	//			
	//			log.error("An error occured when trying to save CPN information for patient with id "
	//			        + fm.getPatient().getPatientId() + " - " + fm.getPatient().getPersonName());
	//			e.printStackTrace();
	//		}
	//		finally {
	//			return mav;
	//		}
	//	}
	
	private boolean saveBilanPreARV(HttpServletRequest request, ModelAndView mav) {
		boolean saved = true;
		EncounterService es = Context.getEncounterService();
		Encounter enc = new Encounter();
		ConceptService cs = Context.getConceptService();
		
		List<Obs> obsList = new ArrayList<Obs>();
		try {
			SimpleDateFormat df = Context.getDateFormat();
			Date encounterDate = df.parse(request.getParameter("encounterDate"));
			Location location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("location")));
			Patient p = Context.getPatientService().getPatient(Integer.valueOf(request.getParameter("patientId")));
			User provider = Context.getUserService().getUser(Integer.valueOf(request.getParameter("provider")));
			
			//bilan pre-ARV
			//creating a list of obs
			//Hemoglobine
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.HEMOGLOBIN), Double
			        .valueOf(request.getParameter("hemoglobine")), 1));
			//Date of CD4 Count
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.DATE_OF_CD4_COUNT), df
			        .parse(request.getParameter("dateOfCD4Count")), 2));
			//CD4 Count
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.CD4_COUNT), Double
			        .valueOf(request.getParameter("cd4Count")), 1));
			//Date result of CD4 Count received
			obsList
			        .add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConfigurationUtils
			                .getReceptionDateCD4CountConceptId()), df.parse(request
			                .getParameter("dateResultOfCD4CountReceived")), 2));
			//TB screening
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.TB_SCREENING), cs
			        .getConcept(Integer.valueOf(request.getParameter("tbScreening"))), 4));
			//WHO Stage
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.WHO_STAGE), cs
			        .getConcept(Integer.valueOf(request.getParameter("whoStage"))), 4));
			//Next visit date
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.RETURN_VISIT_DATE), df
			        .parse(request.getParameter("nextVisitDate")), 2));
			
			//creating the encounter and add obs
			enc = createEncounter(encounterDate, provider, location, p, es.getEncounterType(PMTCTConfigurationUtils
			        .getMotherFollowUpEncounterTypeId()), obsList);
			enc.setDateCreated(new Date());
			
			//save the encounter
			es.saveEncounter(enc);
			
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "pmtct.general.saveSuccess");
			log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
			        + "(Bilan Pre-ARV) saved succesiffully");
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "pmtct.general.notSaved");
			
			log.error(">>>>SAVE>>BILAN>>PRE>>ARV>>ENCOUNTER>> An error occured when trying to save encounter for Patient#"
			        + request.getParameter("patientId"));
			e.printStackTrace();
		}
		return saved;
	}
	
	private boolean saveCD4CountEncounter(HttpServletRequest request, ModelAndView mav) {
		boolean saved = true;
		EncounterService es = Context.getEncounterService();
		Encounter enc = new Encounter();
		ConceptService cs = Context.getConceptService();
		
		List<Obs> obsList = new ArrayList<Obs>();
		try {
			SimpleDateFormat df = Context.getDateFormat();
			Date encounterDate = df.parse(request.getParameter("encounterDate"));
			Location location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("location")));
			Patient p = Context.getPatientService().getPatient(Integer.valueOf(request.getParameter("patientId")));
			User provider = Context.getUserService().getUser(Integer.valueOf(request.getParameter("provider")));
			
			//CD4 Test
			//creating a list of obs
			//Date of CD4 Count
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.DATE_OF_CD4_COUNT), df
			        .parse(request.getParameter("dateOfCD4Count")), 2));
			//CD4 Count
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.CD4_COUNT), Double
			        .valueOf(request.getParameter("cd4Count")), 1));
			//Date result of CD4 Count received
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConfigurationUtils
			        .getReceptionDateCD4CountConceptId()), df.parse(request.getParameter("dateCD4CountResultReceived")), 2));
			//Next visit date
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.RETURN_VISIT_DATE), df
			        .parse(request.getParameter("nextVisitDate")), 2));
			
			//creating the encounter and add obs
			enc = createEncounter(encounterDate, provider, location, p, es.getEncounterType(PMTCTConfigurationUtils
			        .getMotherFollowUpEncounterTypeId()), obsList);
			enc.setDateCreated(new Date());
			
			//save the encounter
			es.saveEncounter(enc);
			
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "pmtct.general.saveSuccess");
			log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
			        + "(CD4 Test) saved succesiffully");
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "pmtct.general.notSaved");
			
			log.error(">>>>SAVE>>CD4>>COUNT>>ENCOUNTER>> An error occured when trying to save encounter for Patient#"
			        + request.getParameter("patientId"));
			e.printStackTrace();
		}
		return saved;
	}
	
	private boolean saveHIVTestEncounterForDiscordantCouples(HttpServletRequest request, ModelAndView mav) {
		boolean saved = true;
		EncounterService es = Context.getEncounterService();
		Encounter enc = new Encounter();
		ConceptService cs = Context.getConceptService();
		
		List<Obs> obsList = new ArrayList<Obs>();
		try {
			SimpleDateFormat df = Context.getDateFormat();
			Date encounterDate = df.parse(request.getParameter("encounterDate"));
			Location location = Context.getLocationService().getLocation(Integer.valueOf(request.getParameter("location")));
			Patient p = Context.getPatientService().getPatient(Integer.valueOf(request.getParameter("patientId")));
			User provider = Context.getUserService().getUser(Integer.valueOf(request.getParameter("provider")));
			
			//Cpl Discordant
			//creating a list of obs
			//Date of Hiv test
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.HIV_TEST_DATE), df
			        .parse(request.getParameter("hivTestDate")), 2));
			//Result of Hiv test
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST), cs
			        .getConcept(Integer.valueOf(request.getParameter("resultOfHivTest"))), 4));
			//Date of Hiv test received
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConfigurationUtils
			        .getResultHivTestReceptionDateConceptId()), df
			        .parse(request.getParameter("dateResultOfHivTestReceived")), 2));
			//Return visit date
			obsList.add(createObservation(encounterDate, location, p, cs.getConcept(PMTCTConstants.RETURN_VISIT_DATE), df
			        .parse(request.getParameter("nextVisitDate")), 2));
			
			//creating the encounter and add obs
			enc = createEncounter(encounterDate, provider, location, p, es.getEncounterType(PMTCTConfigurationUtils
			        .getMotherFollowUpEncounterTypeId()), obsList);
			enc.setDateCreated(new Date());
			
			//save the encounter
			es.saveEncounter(enc);
			
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "pmtct.general.saveSuccess");
			log.info(es.getEncounterType(PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()).getName()
			        + "(Cpl Discordant) saved succesiffully");
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "pmtct.general.notSaved");
			
			log
			        .error(">>>>SAVE>>HIV>>TEST>>DISCORDANT>>COUPLES>>ENCOUNTER>> An error occured when trying to save encounter for Patient#"
			                + request.getParameter("patientId"));
			e.printStackTrace();
		}
		return saved;
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
					log.fatal("PMTCT : An observation has not been saved because it was null.");
			}
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			enc = null;
		}
		return enc;
	}
	
}
