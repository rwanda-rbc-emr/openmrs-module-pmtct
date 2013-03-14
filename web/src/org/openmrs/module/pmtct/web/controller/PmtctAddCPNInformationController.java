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
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Location;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.User;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.PmtctCPNInformation;
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
public class PmtctAddCPNInformationController extends SimpleFormController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
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
		PmtctCPNInformation pmtctCPNInfo = new PmtctCPNInformation();
		pmtctCPNInfo.setPatient(Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId"))));
		
		return pmtctCPNInfo;
	}
	
	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest,
	 *      java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings( { "finally", "static-access", "deprecation" })
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		HashMap<String, Object> map = new HashMap<String, Object>();
		PmtctCPNInformation pmtctCPNInfo = (PmtctCPNInformation) command;
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		map.put("yesConceptId", PMTCTConstants.YES);
		
		try {
			if (pmtctCPNInfo.getLocation() == null)
				pmtctCPNInfo.setLocation(Context.getLocationService().getLocation(
				    PMTCTConfigurationUtils.getDefaultLocationId()));
			if (pmtctCPNInfo.getCpnDate() == null)
				pmtctCPNInfo.setCpnDate(new Date());
			if (pmtctCPNInfo.getProvider() == null && Context.getAuthenticatedUser().hasRole("Provider"))
				pmtctCPNInfo.setProvider(Context.getAuthenticatedUser());
			
			//Patient
			Patient patient = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
			map.put("patient", patient);
			PersonAddress pa = patient.getPersonAddress();
			map.put("loc", (pa == null) ? new PersonAddress() : pa);
			
			ConceptService cs = Context.getConceptService();
			
			//Result of Civil status, Education level and Main Activities
			map.put("civilStatusAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.CIVIL_STATUS));
			map.put("educationLevelAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.EDUCATION_LEVEL));
			map.put("mainActivitiesAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.MAIN_ACTIVITY));
			
			//Result of HIV Test, HIV Test of a partner, Syphilis Test, HIV Status Disclosed, Partenaire attendu, Dépistage du partenaire séparement
			map.put("resultHIVTestAnswers", PMTCTUtil.createResultOfHivTestOptions());
			map.put("resultHIVTestAnswersForPartner", PMTCTUtil.createResultOfHivTestOptions());
			map.put("resultSyphilisTestAnswers", PMTCTUtil.createCodedOptions(PMTCTConstants.SYPHILIS_TEST));
			map.put("resultHIVStatusDisclosedAnswers", PMTCTUtil
			        .createCodedOptions(PMTCTConstants.HAVE_YOU_DISCLOSED_YOUR_HIV_STATUS_TO_YOUR_PARTNER));
			map.put("resultPartenaireAttenduAnswers", PMTCTUtil.createCodedOptions(PMTCTConfigurationUtils
			        .getPatientArrivedWithPartnerConceptId()));
			map.put("depistagePartenaireSeparementAnswers", PMTCTUtil.createCodedOptions(PMTCTConfigurationUtils
			        .getPartnerTestedSeparatelyConceptId()));
			
			//Getting names of concepts - Labels
			map.put("dateReceptionMoustiquaireId", PMTCTConfigurationUtils.getReceptionDateBedNetConceptId());
			map.put("dateTestVIHPartenaireId", PMTCTConfigurationUtils.getHivTestDateForPartnerConceptId());
			map.put("dateRemiseResultatSyphilisId", PMTCTConfigurationUtils.getResultSyphilisTestReceptionDateConceptId());
			map.put("dateRemiseResultatVIHId", PMTCTConfigurationUtils.getResultHivTestReceptionDateConceptId());
			map.put("dateTestSyphilisId", PMTCTConfigurationUtils.getSyphilisTestDateConceptId());
			map.put("depistagePartenaireSeparementId", PMTCTConfigurationUtils.getPartnerTestedSeparatelyConceptId());
			map.put("partenaireAttanduId", PMTCTConfigurationUtils.getPatientArrivedWithPartnerConceptId());
			
			map.put("resultSyphilisTestId", PMTCTConstants.SYPHILIS_TEST);
			map.put("HIVStatusDisclosedId", PMTCTConstants.HAVE_YOU_DISCLOSED_YOUR_HIV_STATUS_TO_YOUR_PARTNER);
			map.put("HIVStatusTestNameId", PMTCTConstants.HIV_STATUS);
			map.put("resultHIVTestId", PMTCTConstants.RESULT_OF_HIV_TEST);
			map.put("resultHIVTestForPartnerId", PMTCTConstants.TESTING_STATUS_OF_PARTNER);
			map.put("numberOfWeeksPregnantId", PMTCTConstants.NUMBER_OF_WEEKS_PREGNANT);
			map.put("pregnancyDueDateId", PMTCTConstants.PREGNANT_DUE_DATE);
			map.put("dateHIVTestId", PMTCTConstants.HIV_TEST_DATE);
			map.put("commentDescriptionId", PMTCTConstants.CLINICAL_COMMENT_IMPRESSION);
			map.put("numberOfPregnanciesId", PMTCTConstants.NUMBER_OF_PREGNANCIES);
			map.put("returnVisitDateId", PMTCTConstants.RETURN_VISIT_DATE);
			map.put("dateOfLastMenstrualPeriodId", PMTCTConstants.DATE_OF_LAST_MENSTRUAL_PERIOD);
			
			map.put("cpnNumberIdentifierID", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
			map.put("civilStatusAttributeId", PMTCTConfigurationUtils.getCivilStatusAttributeTypeId());
			map.put("educationLevelAttributeId", PMTCTConfigurationUtils.getEducationLevelAttributeTypeId());
			map.put("mainActivityAttributeId", PMTCTConfigurationUtils.getMainActivityAttributeTypeId());
			
			//			map.put("path", request.getContextPath());
			
			//Last obs values
			Obs last_hivStatus = checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST));
			map.put("last_hivStatus", last_hivStatus);
			map.put("last_syphilisStatus", checkObservationLastValue(patient, cs.getConcept(PMTCTConstants.SYPHILIS_TEST)));
			map.put("last_hivPartnerStatus", checkObservationLastValue(patient, cs
			        .getConcept(PMTCTConstants.TESTING_STATUS_OF_PARTNER)));
			map
			        .put("last_resultOfHiv", checkObservationLastValue(patient, cs
			                .getConcept(PMTCTConstants.RESULT_OF_HIV_TEST)));
			
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Please contact your administrator...");
			log.error(">>>>>PMTCT>>CPN>>INFO>> An error occured when trying to load CPN information for patient with id "
			        + request.getParameter("patientId"));
			ex.printStackTrace();
		}
		finally {
			return map;
		}
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
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	@SuppressWarnings( { "deprecation", "finally" })
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
	                                BindException errors) throws Exception {
		
		PmtctCPNInformation pmtctCPNInfo = (PmtctCPNInformation) command;
		
		ConceptService cs = Context.getConceptService();
		ModelAndView mav = new ModelAndView(new RedirectView(request.getContextPath() + getSuccessView()
		        + pmtctCPNInfo.getPatient().getPatientId()));
		
		List<Obs> obsList = new ArrayList<Obs>();
		
		Patient patient = pmtctCPNInfo.getPatient();
		Location location = pmtctCPNInfo.getLocation();
		
		Date obsDate = pmtctCPNInfo.getCpnDate();
		//creating the encounter and add obs
		
		Encounter encounter = new Encounter();
		
		EncounterService es = Context.getEncounterService();
		
		//creating new identifier cpn_number if patient doesn't have one
		
		PatientIdentifier pi = new PatientIdentifier();
		
		try {
			//Identifier
			if (pmtctCPNInfo.getCpnNumber() != null) {
				pi.setDateCreated(new Date());
				pi.setIdentifier(pmtctCPNInfo.getCpnNumber());
				pi.setLocation(location);
				pi.setIdentifierType(Context.getPatientService().getPatientIdentifierType(
				    PMTCTConfigurationUtils.getCPNIdentifierTypeId()));
				patient.addIdentifier(pi);//add the new identifier to the patient
				
				//saving the changes	
				log.info(">>>PMTCT>>CPN>>INFO>> "
				        + es.getEncounterType(PMTCTConfigurationUtils.getCPNEncounterTypeId()).getName()
				        + " : Trying to save the new Identifier of type "
				        + Context.getPatientService().getPatientIdentifierType(
				            PMTCTConfigurationUtils.getCPNIdentifierTypeId()).getName() + "...");
				
				Context.getPatientService().updatePatient(patient);
				log.info(">>>PMTCT>>CPN>>INFO>> "
				        + es.getEncounterType(PMTCTConfigurationUtils.getCPNEncounterTypeId()).getName()
				        + " : New Identifier saved successfully for Patient " + patient.getPatientId());
			}
			
			obsList.add(createObservation(obsDate, location, patient, cs
			        .getConcept(PMTCTConstants.DATE_OF_LAST_MENSTRUAL_PERIOD), pmtctCPNInfo.getDateOfLastMenstrualPeriod(),
			    2));
			obsList.add(createObservation(obsDate, location, patient,
			    cs.getConcept(PMTCTConstants.NUMBER_OF_WEEKS_PREGNANT), pmtctCPNInfo.getNumberOfWeeksPregnant(), 1));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.PREGNANT_DUE_DATE),
			    pmtctCPNInfo.getPregnantDueDate(), 2));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.NUMBER_OF_PREGNANCIES),
			    pmtctCPNInfo.getNumberOfPregnancies(), 1));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.HIV_TEST_DATE),
			    pmtctCPNInfo.getHivTestDate(), 2));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.RESULT_OF_HIV_TEST), cs
			        .getConcept(pmtctCPNInfo.getResultOfHivTest()), 4));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			        .getResultHivTestReceptionDateConceptId()), pmtctCPNInfo.getDateResultOfHivTestReceived(), 2));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			        .getSyphilisTestDateConceptId()), pmtctCPNInfo.getSyphilis_TestDate(), 2));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.SYPHILIS_TEST), cs
			        .getConcept(pmtctCPNInfo.getResultSyphilisTest()), 4));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			        .getResultSyphilisTestReceptionDateConceptId()), pmtctCPNInfo.getDateResultOfSyphilisTestReceived(), 2));
			obsList.add(createObservation(obsDate, location, patient, cs
			        .getConcept(PMTCTConstants.CLINICAL_COMMENT_IMPRESSION), pmtctCPNInfo.getComment(), 3));
			obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConstants.RETURN_VISIT_DATE),
			    pmtctCPNInfo.getNextVisitDate(), 2));
			if (null != pmtctCPNInfo.getDateMosquitoNetReceived())
				obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
				        .getReceptionDateBedNetConceptId()), pmtctCPNInfo.getDateMosquitoNetReceived(), 2));
			obsList
			        .add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
			                .getPatientArrivedWithPartnerConceptId()), cs.getConcept(pmtctCPNInfo
			                .getPatientArrivedWithPartner()), 4));
			
			//patient arrived with partner - observations
			if (pmtctCPNInfo.getPatientArrivedWithPartner() == PMTCTConstants.YES) {
				obsList.add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
				        .getHivTestDateForPartnerConceptId()), pmtctCPNInfo.getHivTestDateOfPartner(), 2));
				obsList.add(createObservation(obsDate, location, patient, cs
				        .getConcept(PMTCTConstants.TESTING_STATUS_OF_PARTNER), cs.getConcept(pmtctCPNInfo
				        .getResultHivTestOfPartner()), 4));
				obsList.add(createObservation(obsDate, location, patient, cs
				        .getConcept(PMTCTConstants.HAVE_YOU_DISCLOSED_YOUR_HIV_STATUS_TO_YOUR_PARTNER), cs
				        .getConcept(pmtctCPNInfo.getDisclosedStatusToPartner()), 4));
				obsList
				        .add(createObservation(obsDate, location, patient, cs.getConcept(PMTCTConfigurationUtils
				                .getPartnerTestedSeparatelyConceptId()), cs.getConcept(pmtctCPNInfo
				                .getPartnerTestedSeparately()), 4));
			}
			
			encounter = createEncounter(obsDate, pmtctCPNInfo.getProvider(), location, patient, es
			        .getEncounterType(PMTCTConfigurationUtils.getCPNEncounterTypeId()), obsList);
			encounter.setDateCreated(new Date());
			
			//save the encounter
			log.info(">>>PMTCT>>CPN>>INFO>> "
			        + es.getEncounterType(PMTCTConfigurationUtils.getCPNEncounterTypeId()).getName()
			        + " : Saving the CPN encounter...");
			es.saveEncounter(encounter);
			log.info(">>>PMTCT>>CPN>>INFO>> "
			        + es.getEncounterType(PMTCTConfigurationUtils.getCPNEncounterTypeId()).getName()
			        + " : CPN encounter saved successfully.");
			
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "pmtct.general.saveSuccess");
		}
		catch (Exception e) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "pmtct.general.notSaved");
			
			log.error(">>>PMTCT>>CPN>>INFO>> An error occured when trying to save CPN information for patient with id "
			        + request.getParameter("patientId") + " - " + pmtctCPNInfo.getPatient().getPersonName());
			e.printStackTrace();
		}
		finally {
			return mav;
		}
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
				o.setValueText("" + obsValue);
			} else if (obsValueType == 4) {
				o.setValueCoded((Concept) obsValue);
			}
		}
		catch (Exception e) {
			log.error(">>>PMTCT>>CPN>>INFO>> An Error occured when trying to create an observation :\n");
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
					log.error(">>>PMTCT>>CPN>>INFO>> An observation has not been saved because it was null.");
			}
		}
		catch (Exception e) {
			log.error(">>>PMTCT>>CPN>>INFO>> An Error occured when trying to create an encounter :\n");
			e.printStackTrace();
			enc = null;
		}
		return enc;
	}
}
