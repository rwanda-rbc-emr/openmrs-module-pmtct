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
package org.openmrs.module.pmtct.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.pmtct.PmtctFollowupMother;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 */
public class PmtctFollowupMotherValidator implements Validator {
	
	private static Log log = LogFactory.getLog(PmtctFollowupMotherValidator.class);
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	//@Override =>incompatible with jre5
	public boolean supports(Class c) {
		return PmtctFollowupMother.class.isAssignableFrom(c);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	//@Override =>incompatible with jre5
	public void validate(Object obj, Errors error) {
		PmtctFollowupMother fm = (PmtctFollowupMother) obj;
		boolean errorFound = false;
		try {
			if (fm.getBtClicked().equalsIgnoreCase("1")) {
				
				//Bilan pre-ARV
				
				if (fm.getProvider() == null) {
					error.rejectValue("provider", "pmtct.general.error.provider");
					errorFound = true;
				}
				if (fm.getLocation() == null) {
					error.rejectValue("location", "pmtct.general.error.location");
					errorFound = true;
				}
				if (fm.getEncounterDate() == null || fm.getEncounterDate().equals("")) {
					error.rejectValue("encounterDate", "pmtct.general.error.encounterDate");
					errorFound = true;
				}
				if (fm.getDateOfCD4Count() == null || fm.getDateOfCD4Count().equals("")) {
					error.rejectValue("dateOfCD4Count", "pmtct.fm.error.dateOfCD4Count");
					errorFound = true;
				}
				if (fm.getDateResultOfCD4CountReceived() == null || fm.getDateResultOfCD4CountReceived().equals("")) {
					error.rejectValue("dateResultOfCD4CountReceived", "pmtct.fm.error.dateResultOfCD4CountReceived");
					errorFound = true;
				}
				if (fm.getCd4Count() == null || fm.getCd4Count() < 0) {
					error.rejectValue("cd4Count", "pmtct.fm.error.cd4Count");
					errorFound = true;
				}
				if (fm.getHemoglobine() == null) {
					error.rejectValue("hemoglobine", "pmtct.fm.error.hemoglobine");
					errorFound = true;
				}
				if (fm.getHemoglobine() != null && fm.getHemoglobine() < 10 || fm.getHemoglobine() > 18) {
					error.rejectValue("hemoglobine", "pmtct.fm.error.hemoglobineRange");
					errorFound = true;
				}
				if (fm.getTbScreening()==0) {
					error.rejectValue("tbScreening", "pmtct.general.error.requiredField");
					errorFound = true;
				}
				if (fm.getWhoStage()==0) {
					error.rejectValue("whoStage", "pmtct.general.error.requiredField");
					errorFound = true;
				}
				if (fm.getNextVisitDate() == null || fm.getNextVisitDate().equals("")) {
					error.rejectValue("nextVisitDate", "pmtct.fm.error.nextVisitDate");
					errorFound = true;
				}
				
			} else if (fm.getBtClicked().equalsIgnoreCase("2")) {
				
				//CD4 Test
				
				if (fm.getProvider_cd4() == null) {
					error.rejectValue("provider_cd4", "pmtct.general.error.provider");
					errorFound = true;
				}
				if (fm.getLocation_cd4() == null) {
					error.rejectValue("location_cd4", "pmtct.general.error.location");
					errorFound = true;
				}
				if (fm.getEncounterDate_cd4() == null || fm.getEncounterDate_cd4().equals("")) {
					error.rejectValue("encounterDate_cd4", "pmtct.general.error.encounterDate");
					errorFound = true;
				}
				if (fm.getDateOfCD4Count_cd4() == null || fm.getDateOfCD4Count_cd4().equals("")) {
					error.rejectValue("dateOfCD4Count_cd4", "pmtct.fm.error.dateOfCD4Count");
					errorFound = true;
				}
				if (fm.getCD4Count_cd4() == null || fm.getCD4Count_cd4() < 0) {
					error.rejectValue("CD4Count_cd4", "pmtct.fm.error.cd4Count");
					errorFound = true;
				}
				if (fm.getDateCD4CountResultReceived_cd4() == null || fm.getDateCD4CountResultReceived_cd4().equals("")) {
					error.rejectValue("dateCD4CountResultReceived_cd4", "pmtct.fm.error.dateResultOfCD4CountReceived");
					errorFound = true;
				}
				if (fm.getNextVisitDate_cd4() == null || fm.getNextVisitDate_cd4().equals("")) {
					error.rejectValue("nextVisitDate_cd4", "pmtct.fm.error.nextVisitDate");
					errorFound = true;
				}
				
			} else if (fm.getBtClicked().equalsIgnoreCase("3")) {
				
				//cpl Discordant
				
				if (fm.getProvider_cplDisc() == null) {
					error.rejectValue("provider_cplDisc", "pmtct.general.error.provider");
					errorFound = true;
				}
				if (fm.getLocation_cplDisc() == null) {
					error.rejectValue("location_cplDisc", "pmtct.general.error.location");
					errorFound = true;
				}
				if (fm.getEncounterDate_cplDisc() == null || fm.getEncounterDate_cplDisc().equals("")) {
					error.rejectValue("encounterDate_cplDisc", "pmtct.general.error.encounterDate");
					errorFound = true;
				}
				if (fm.getHivTestDate_cplDisc() == null || fm.getHivTestDate_cplDisc().equals("")) {
					error.rejectValue("hivTestDate_cplDisc", "pmtct.cpn.error.hivTestDate");
					errorFound = true;
				}
				if (fm.getDateResultOfHivTestReceived_cplDisc() == null
				        || fm.getDateResultOfHivTestReceived_cplDisc().equals("")) {
					error.rejectValue("dateResultOfHivTestReceived_cplDisc", "pmtct.cpn.error.dateResultOfHivTestReceived");
					errorFound = true;
				}
				if (fm.getResultOfHivTest_cplDisc()==0) {
					error.rejectValue("resultOfHivTest_cplDisc", "pmtct.general.error.requiredField");
					errorFound = true;
				}
			}
			
			if (errorFound) {
				error.rejectValue("error", "pmtct.general.error.errorOnTheForm");
			}
		}
		catch (Exception ex) {
			log.error("An error occured when trying to validate the Form:\n");
			ex.printStackTrace();
		}
	}
	
}
