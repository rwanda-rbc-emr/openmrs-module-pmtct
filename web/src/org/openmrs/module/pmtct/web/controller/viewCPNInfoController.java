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

import org.openmrs.DrugOrder;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.FileExporter;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 * @author Yves GAKUBA
 */
public class viewCPNInfoController extends ParameterizableViewController {
		
	/**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			mav.addObject("encounter", Context.getEncounterService().getEncounter(
			    Integer.parseInt(request.getParameter("encounterId"))));
			mav.addObject("cpnNumberIdentifierID", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
			
			List<DrugOrder> drugOrdersTmp = Context.getOrderService().getDrugOrdersByPatient(
			    Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId"))));
			List<DrugOrder> drugOrders = new ArrayList<DrugOrder>();
			for (DrugOrder d : drugOrdersTmp) {
				if (d.getConcept().getConceptId() == PMTCTConstants.PMTCT_DRUG_ORDER_CONCEPT_RELATED_ID)
					drugOrders.add(d);
			}
			
			mav.addObject("drugOrders", drugOrders);
			
			FileExporter fexp = new FileExporter();
			
			if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
				fexp.exportDrugToCSVFile(request, response, drugOrders, "list_of_drugs_for_patients_in_pmtct_program.csv",
				    "List of Drugs related to PMTCT Program for a Patient");
			}
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			ex.printStackTrace();
		}
		return mav;
	}
	
}
