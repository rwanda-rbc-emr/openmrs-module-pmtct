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
import org.openmrs.module.pmtct.PmtctCPNInformation;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 */
public class PmtctCPNInformationValidator implements Validator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	//@Override =>incompatible with jre5
	public boolean supports(Class c) {
		return PmtctCPNInformation.class.isAssignableFrom(c);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	//@Override =>incompatible with jre5
	public void validate(Object object, Errors error) {
		PmtctCPNInformation pmtctCPNInfo = (PmtctCPNInformation) object;
		
		try {
			if (pmtctCPNInfo.getCpnDate() == null || pmtctCPNInfo.getCpnDate().equals(""))
				error.rejectValue("cpnDate", "pmtct.cpn.error.cpnDate");
			if (pmtctCPNInfo.getCpnNumber() == null || pmtctCPNInfo.getCpnNumber().equals(""))
				error.rejectValue("cpnNumber", "pmtct.cpn.error.cpnNumber");
			if (pmtctCPNInfo.getLocation() == null)
				error.rejectValue("location", "pmtct.general.error.location");
			if (pmtctCPNInfo.getProvider() == null)
				error.rejectValue("provider", "pmtct.general.error.provider");
			
			if (pmtctCPNInfo.getPregnantDueDate() == null || pmtctCPNInfo.getPregnantDueDate().equals(""))
				error.rejectValue("pregnantDueDate", "pmtct.cpn.error.pregnantDueDate");
			
			//HIV
			if (pmtctCPNInfo.getHivTestDate() == null || pmtctCPNInfo.getHivTestDate().equals(""))
				error.rejectValue("hivTestDate", "pmtct.cpn.error.hivTestDate");
			
			if (pmtctCPNInfo.getDateResultOfHivTestReceived() == null
			        || pmtctCPNInfo.getDateResultOfHivTestReceived().equals(""))
				error.rejectValue("dateResultOfHivTestReceived", "pmtct.cpn.error.dateResultOfHivTestReceived");
			
			if (pmtctCPNInfo.getDateResultOfHivTestReceived() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getDateResultOfHivTestReceived().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getDateResultOfHivTestReceived().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
				error.rejectValue("dateResultOfHivTestReceived", "pmtct.cpn.error.dateRHIVTRIncompatibleWithEncDate");
			
			if (pmtctCPNInfo.getHivTestDate() != null && pmtctCPNInfo.getDateResultOfHivTestReceived() != null
			        && !pmtctCPNInfo.getHivTestDate().equals("")
			        && !pmtctCPNInfo.getDateResultOfHivTestReceived().equals("")
			        && pmtctCPNInfo.getDateResultOfHivTestReceived().compareTo(pmtctCPNInfo.getHivTestDate()) < 0)
				error.rejectValue("dateResultOfHivTestReceived", "pmtct.cpn.error.dateROfHIVTRIncompatibleWithHIVTD");
			
			if (pmtctCPNInfo.getHivTestDate() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getHivTestDate().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getHivTestDate().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
				error.rejectValue("hivTestDate", "pmtct.cpn.error.dateHIVTIncompatibleWithEncDate");
			
			//SYPHILLIS
			/*if (pmtctCPNInfo.getSyphilis_TestDate() == null || pmtctCPNInfo.getSyphilis_TestDate().equals(""))
				error.rejectValue("syphilis_TestDate", "pmtct.cpn.error.syphilis_TestDate");
			
			if (pmtctCPNInfo.getSyphilis_TestDate() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getSyphilis_TestDate().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getSyphilis_TestDate().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
				error.rejectValue("syphilis_TestDate", "pmtct.cpn.error.dateSTIncompatibleWithEncDate");
			
			if (pmtctCPNInfo.getDateResultOfSyphilisTestReceived() == null
			        || pmtctCPNInfo.getDateResultOfSyphilisTestReceived().equals(""))
				error.rejectValue("dateResultOfSyphilisTestReceived", "pmtct.cpn.error.dateResultOfSyphilisTestReceived");
			
			if (pmtctCPNInfo.getDateResultOfSyphilisTestReceived() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getDateResultOfSyphilisTestReceived().equals("")
			        && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getDateResultOfSyphilisTestReceived().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
				error.rejectValue("dateResultOfSyphilisTestReceived", "pmtct.cpn.error.dateRSTRIncompatibleWithEncDate");
			
			if (pmtctCPNInfo.getSyphilis_TestDate() != null && pmtctCPNInfo.getDateResultOfSyphilisTestReceived() != null
			        && !pmtctCPNInfo.getSyphilis_TestDate().equals("")
			        && !pmtctCPNInfo.getDateResultOfSyphilisTestReceived().equals("")
			        && pmtctCPNInfo.getDateResultOfSyphilisTestReceived().compareTo(pmtctCPNInfo.getSyphilis_TestDate()) < 0)
				error.rejectValue("dateResultOfSyphilisTestReceived", "pmtct.cpn.error.dateROfSTRIncompatibleWithSTD");*/
			
			//			if (pmtctCPNInfo.getDateMosquitoNetReceived() == null || pmtctCPNInfo.getDateMosquitoNetReceived().equals(""))
			//				error.rejectValue("dateMosquitoNetReceived", "pmtct.cpn.error.dateMosquitoNetReceived");
			
			if (pmtctCPNInfo.getDateMosquitoNetReceived() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getDateMosquitoNetReceived().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getDateMosquitoNetReceived().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
				error.rejectValue("dateMosquitoNetReceived", "pmtct.cpn.error.dateMNRIncompatibleWithEncDate");
			
			if (pmtctCPNInfo.getNextVisitDate() == null || pmtctCPNInfo.getNextVisitDate().equals(""))
				error.rejectValue("nextVisitDate", "pmtct.cpn.error.dateNextVisitDate");
			
			if (pmtctCPNInfo.getNextVisitDate() != null && pmtctCPNInfo.getCpnDate() != null
			        && !pmtctCPNInfo.getNextVisitDate().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
			        && pmtctCPNInfo.getNextVisitDate().compareTo(pmtctCPNInfo.getCpnDate()) < 0)
				error.rejectValue("nextVisitDate", "pmtct.cpn.error.dateNextVisitDateIncompatibleWithEncDate");
			
			if (pmtctCPNInfo.getDateOfLastMenstrualPeriod() == null
			        || pmtctCPNInfo.getDateOfLastMenstrualPeriod().equals(""))
				error.rejectValue("dateOfLastMenstrualPeriod", "pmtct.cpn.error.dateOfLastMenstrualPeriod");
			
			if (pmtctCPNInfo.getNumberOfWeeksPregnant() == null || pmtctCPNInfo.getNumberOfWeeksPregnant() < 0)
				error.rejectValue("numberOfWeeksPregnant", "pmtct.cpn.error.numberOfWeeksPregnant");
			
			if (pmtctCPNInfo.getCpnDate() != null && pmtctCPNInfo.getDateOfLastMenstrualPeriod() != null
			        && !pmtctCPNInfo.getCpnDate().equals("") && !pmtctCPNInfo.getDateOfLastMenstrualPeriod().equals("")
			        && pmtctCPNInfo.getCpnDate().compareTo(pmtctCPNInfo.getDateOfLastMenstrualPeriod()) < 0) {
				error.rejectValue("dateOfLastMenstrualPeriod", "pmtct.cpn.error.dateLMPIncompatibleWithEncDate");
				
				pmtctCPNInfo.setPregnantDueDate(null);
			}
			
			if (pmtctCPNInfo.getPatientArrivedWithPartner() == PMTCTConstants.YES) {
				if (pmtctCPNInfo.getHivTestDateOfPartner() == null || pmtctCPNInfo.getHivTestDateOfPartner().equals(""))
					error.rejectValue("hivTestDateOfPartner", "pmtct.cpn.error.hivTestDateOfPartner");
				
				if (pmtctCPNInfo.getHivTestDateOfPartner() != null && pmtctCPNInfo.getCpnDate() != null
				        && !pmtctCPNInfo.getHivTestDateOfPartner().equals("") && !pmtctCPNInfo.getCpnDate().equals("")
				        && pmtctCPNInfo.getHivTestDateOfPartner().compareTo(pmtctCPNInfo.getCpnDate()) > 0)
					error.rejectValue("hivTestDateOfPartner", "pmtct.cpn.error.dateHIVTPIncompatibleWithEncDate");
				
				if (pmtctCPNInfo.getResultHivTestOfPartner() == 0)
					error.rejectValue("resultHivTestOfPartner", "pmtct.general.error.requiredField");
				
				if (pmtctCPNInfo.getDisclosedStatusToPartner() == 0)
					error.rejectValue("disclosedStatusToPartner", "pmtct.general.error.requiredField");
				
				if (pmtctCPNInfo.getPartnerTestedSeparately() == 0)
					error.rejectValue("partnerTestedSeparately", "pmtct.general.error.requiredField");
			}
			
			if (pmtctCPNInfo.getResultOfHivTest() == 0)
				error.rejectValue("resultOfHivTest", "pmtct.general.error.requiredField");
			/*if (pmtctCPNInfo.getResultSyphilisTest() == 0)
				error.rejectValue("resultSyphilisTest", "pmtct.general.error.requiredField");*/
			
			if (pmtctCPNInfo.getPatientArrivedWithPartner() == 0)
				error.rejectValue("patientArrivedWithPartner", "pmtct.general.error.requiredField");
			
		}
		catch (Exception ex) {
			log.error(">>>>>>PMTCT>>CPN>>INFO>>VALIDATION>>> An error occured when trying to validate the Form:\n");
			ex.printStackTrace();
		}
	}
	
}
