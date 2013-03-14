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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class PmtctFlowsheet extends ParameterizableViewController {
		
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings( { "deprecation", "static-access" })
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//		constants = config.getConstants();
//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		List<Encounter> encounterList = new ArrayList<Encounter>();
		List<Encounter> results = new ArrayList<Encounter>();
		
		try {
			encounterList = Context.getEncounterService().getEncountersByPatientId(
			    Integer.parseInt(request.getParameter("patientId")));
			
			for (Encounter e : encounterList) {
				if (e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getCPNEncounterTypeId()
				        || e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()
				        || e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMaternityEncounterTypeId()
				        || e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getPCRTestEncounterTypeId()
				        || e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId()
				        || e.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId())
					results.add(e);
			}
			
			mav.addObject("encounters", results);
			mav.addObject("patient", Context.getPatientService().getPatient(
			    Integer.parseInt(request.getParameter("patientId"))));
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			ex.printStackTrace();
		}
		return mav;
	}
}
