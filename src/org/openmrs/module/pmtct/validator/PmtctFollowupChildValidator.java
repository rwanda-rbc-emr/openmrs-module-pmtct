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
import org.openmrs.module.pmtct.PmtctChildInformation;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Yves
 */
public class PmtctFollowupChildValidator implements Validator {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class c) {
		return PmtctChildInformation.class.isAssignableFrom(c);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object obj, Errors error) {
		PmtctChildInformation pmtctChild = (PmtctChildInformation) obj;
		try {
			if (pmtctChild.getHivTestDate() == null || pmtctChild.getHivTestDate().equals(""))
				error.rejectValue("hivTestDate", "pmtct.cpn.error.hivTestDate");
			if (pmtctChild.getLocation() == null || pmtctChild.getLocation().equals(""))
				error.rejectValue("location", "pmtct.general.error.location");
			if (pmtctChild.getProvider() == null || pmtctChild.getProvider().equals(""))
				error.rejectValue("provider", "pmtct.general.error.provider");
			if (pmtctChild.getDateOfReturnedResult() == null || pmtctChild.getDateOfReturnedResult().equals(""))
				error.rejectValue("dateOfReturnedResult", "pmtct.cf.error.dateOfReturnedResult");
			if(pmtctChild.getResultOfHivTest()==0)
				error.rejectValue("resultOfHivTest", "pmtct.general.error.requiredField");
		}
		catch (Exception ex) {
			log.error("An error occured when trying to validate the Form:\n");
			ex.printStackTrace();
		}
	}
	
}
