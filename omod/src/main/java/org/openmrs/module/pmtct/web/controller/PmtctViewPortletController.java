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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.PatientProgram;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.web.controller.PortletController;
import org.springframework.web.bind.ServletRequestUtils;

/**
 * @author Yves GAKUBA
 */
public class PmtctViewPortletController extends PortletController {
		
	/**
	 * @see org.openmrs.web.controller.PortletController#populateModel(javax.servlet.http.HttpServletRequest,
	 *      java.util.Map)
	 */
	@SuppressWarnings( { "deprecation" })
	@Override
	protected void populateModel(HttpServletRequest request, Map<String, Object> model) {
		Map<String, Object> pmtctObjects = new HashMap<String, Object>();
		
		try {
//			config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//			constants = config.getConstants();
			
			request.getSession().setAttribute("mohtracmodulesConfigured", "" + PMTCTConfigurationUtils.isMohTracPortalConfigured());
			request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
			
			int patientId = ServletRequestUtils.getIntParameter(request, "patientId", 0);
			Patient patient = (patientId > 0) ? Context.getPatientService().getPatient(patientId) : null;
			pmtctObjects.put("patient", patient);
			pmtctObjects.put("maxAgeOfChildInPMTCT", PMTCTConstants.CHILD_MAX_AGE_IN_PMTCT);
			
//			if (config.isModificationEnabled()) {
			if(PMTCTConfigurationUtils.isConfigured()){
				ProgramWorkflowService pws = Context.getProgramWorkflowService();
				Collection<PatientProgram> pp = pws.getPatientPrograms(patient);
				Collection<PatientProgram> pmtctPrograms = new ArrayList<PatientProgram>();
				
				EncounterService es = Context.getEncounterService();
				List<Encounter> encList = es.getEncountersByPatient(patient);
				List<Encounter> pmtctCPNEnc = new ArrayList<Encounter>();
				List<Encounter> pmtctMaternityEnc = new ArrayList<Encounter>();
				List<Encounter> pmtctPCREnc = new ArrayList<Encounter>();
				List<Encounter> pmtctSerology9Enc = new ArrayList<Encounter>();
				List<Encounter> pmtctSerology18Enc = new ArrayList<Encounter>();
				List<Encounter> pmtctMotherFollowup = new ArrayList<Encounter>();
				
				for (Encounter enc : encList) {
					if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getCPNEncounterTypeId()) {
						pmtctCPNEnc.add(enc);
					} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMaternityEncounterTypeId()) {
						pmtctMaternityEnc.add(enc);
					} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getPCRTestEncounterTypeId()) {
						pmtctPCREnc.add(enc);
					} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId()) {
						pmtctSerology9Enc.add(enc);
					} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId()) {
						pmtctSerology18Enc.add(enc);
					} else if (enc.getEncounterType().getEncounterTypeId() == PMTCTConfigurationUtils.getMotherFollowUpEncounterTypeId()) {
						pmtctMotherFollowup.add(enc);
					}
				}
				
				pmtctObjects.put("cpnencs", pmtctCPNEnc);
				pmtctObjects.put("maternityencs", pmtctMaternityEnc);
				pmtctObjects.put("motherfollowupencs", pmtctMotherFollowup);
				pmtctObjects.put("pmtctPCREnc", pmtctPCREnc);
				pmtctObjects.put("pmtctSerology9Enc", pmtctSerology9Enc);
				pmtctObjects.put("pmtctSerology18Enc", pmtctSerology18Enc);
				
				//what is the current test?
				if (pmtctSerology9Enc.size() != 0)
					pmtctObjects.put("currentTest", "" + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId());
				else if (pmtctPCREnc.size() != 0)
					pmtctObjects.put("currentTest", "" + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId());
				else
					pmtctObjects.put("currentTest", "" + PMTCTConfigurationUtils.getPCRTestEncounterTypeId());
				
				//patient isn't in the PMTCT Program
				pmtctObjects.put("isInTheProgram", "-1");
				
				for (PatientProgram pprogram : pp) {
					if (pprogram.getProgram().getProgramId().intValue() == PMTCTConfigurationUtils.getPmtctProgramId()
					        .intValue()) {
						//patient found in the PMTCT Program
						pmtctPrograms.add(pprogram);
					}
				}
				if (pmtctPrograms.size() == 0)
					pmtctObjects.put("isInTheProgram", "0");
				request.getSession().setAttribute("pmtctprogram", pmtctPrograms);
				
			}
			request.setAttribute("pmtctObjects", pmtctObjects);
			//super.populateModel(request, model);
		}
		catch (Exception ex) {
//			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR,
//			    PMTCTUtil.getMessage("pmtct.error.loadingData", null));
//			pmtctObjects.put("pmtct_error_msg", PMTCTUtil.getMessage("pmtct.error.loadingData", null));
			log.error(">>>>PMTCT>>Portlet>> An error occured when trying to load.");
			ex.printStackTrace();
		}
			
	}
}
