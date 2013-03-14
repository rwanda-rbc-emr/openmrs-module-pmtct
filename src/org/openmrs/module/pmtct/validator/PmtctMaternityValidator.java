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
import org.openmrs.module.pmtct.PmtctMaternity;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 */
public class PmtctMaternityValidator implements Validator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class c) {
		return PmtctMaternity.class.isAssignableFrom(c);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object object, Errors error) {
		// Control here
		PmtctMaternity pmtctMaternity = (PmtctMaternity) object;
		
		try {
			if (pmtctMaternity.getLocation() == null || pmtctMaternity.getLocation().equals("")) {
				error.rejectValue("location", "pmtct.general.error.location");
			}
			
			if (pmtctMaternity.getProvider() == null || pmtctMaternity.getProvider().equals("")) {
				error.rejectValue("provider", "pmtct.general.error.provider");
			}
			
			if (pmtctMaternity.getEncounterDate() == null || pmtctMaternity.getEncounterDate().equals("")) {
				error.rejectValue("encounterDate", "pmtct.general.error.encounterDate");
			}
			
			if (pmtctMaternity.getDpaDate() == null || pmtctMaternity.getDpaDate().equals(""))
				error.rejectValue("dpaDate", "pmtct.cpn.error.pregnantDueDate");
			
			if (pmtctMaternity.getEncounterDate() != null && pmtctMaternity.getDateConfinement() != null
			        && !pmtctMaternity.getEncounterDate().equals("") && !pmtctMaternity.getDateConfinement().equals("")
			        && pmtctMaternity.getEncounterDate().compareTo(pmtctMaternity.getDateConfinement()) > 0) {
				error.rejectValue("dateConfinement", "pmtct.maternity.error.dpaDateIncompatibleWithEncDate");
				
				pmtctMaternity.setDateConfinement(null);
			}
			
			if (pmtctMaternity.getDateConfinement() == null || pmtctMaternity.getDateConfinement().equals(""))
				error.rejectValue("dateConfinement", "pmtct.maternity.error.dateConfinement");
			
			if (pmtctMaternity.getEncounterDate() != null && pmtctMaternity.getDateConfinement() != null
			        && !pmtctMaternity.getEncounterDate().equals("") && !pmtctMaternity.getDateConfinement().equals("")
			        && pmtctMaternity.getEncounterDate().compareTo(pmtctMaternity.getDateConfinement()) < 0) {
				error.rejectValue("dateConfinement", "pmtct.maternity.error.dateConfinementIncompatibleWithEncDate");
				
				pmtctMaternity.setDateConfinement(null);
			}
			
			if (pmtctMaternity.getTransferOutDate() == null || pmtctMaternity.getTransferOutDate().equals(""))
				error.rejectValue("transferOutDate", "pmtct.maternity.error.transferOutDate");
			
			if (pmtctMaternity.getTestHivWorkroom()) {
				if (pmtctMaternity.getResultHivTest() == 0) {
					error.rejectValue("resultHivTest", "pmtct.general.error.requiredField");
				}
			}
			
			if (pmtctMaternity.getChirdBornStutus() != pmtctMaternity.getBornDeadConceptId()) {
				if (pmtctMaternity.getChildName() == null || pmtctMaternity.getChildName().equals("")) {
					error.rejectValue("childName", "pmtct.maternity.error.childName");
				}
				
				if (pmtctMaternity.getChildSurname() == null || pmtctMaternity.getChildSurname().equals("")) {
					error.rejectValue("childSurname", "pmtct.maternity.error.childSurname");
				}
				
				if (pmtctMaternity.getChildIdentifier() == null || pmtctMaternity.getChildIdentifier().equals("")) {
					error.rejectValue("childIdentifier", "pmtct.maternity.error.childIdentifier");					
				}
				
				if (pmtctMaternity.getChildGender() == null || pmtctMaternity.getChildGender().equals("")) {
					error.rejectValue("childGender", "pmtct.maternity.error.childGender");
				}
				
//				if (pmtctMaternity.getChildTracNetId() == null || pmtctMaternity.getChildTracNetId().equals("")) {
//					error.rejectValue("childTracNetId", "pmtct.maternity.error.childTracNetId");
//				}
				
				if (pmtctMaternity.getChildHeight() <= 0) {
					error.rejectValue("childHeight", "pmtct.maternity.error.childHeight");
				}
				
				if (pmtctMaternity.getChildWeight() <= 0) {
					error.rejectValue("childWeight", "pmtct.maternity.error.childWeight");
				}
				
//				if (pmtctMaternity.getChildAgeBreasted() <= 0) {
//					error.rejectValue("childAgeBreasted", "pmtct.maternity.error.childAgeBreasted");
//				}
				
				if (pmtctMaternity.getInfantFeedingMethod() == 0) {
					error.rejectValue("infantFeedingMethod", "pmtct.general.error.requiredField");
				}
			}
		}
		catch (Exception ex) {
			log.error("An error occured when trying to validate the Form:\n");
			ex.printStackTrace();
		}
	}
	
}
