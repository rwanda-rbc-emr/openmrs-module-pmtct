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

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.PMTCTModuleTag;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class PmtctConfigurationController extends ParameterizableViewController {
		
	private Log log = LogFactory.getLog(this.getClass());
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		Map<String, Object> pmtctConfig = new HashMap<String, Object>();
		try {
			if (request.getParameter("edit") != null) {
				saveVCTConfiguration(request, mav);
				//boolean reset = (request.getParameter("resetField").compareTo("1") == 0) ? true : false;
//				saveChanges(request, reset);
			}
			
//			config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//			constants = config.getListOfConstants().get(0);
			
			request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
			request.getSession().setAttribute("displayHelpMessage", "" + true);
//			pmtctConfig.put("identifierTypes", config.getIdentifierTypes());
			pmtctConfig.put("gp_encounterTypes", PMTCTConfigurationUtils.getNeededEncounterType());
			pmtctConfig.put("gp_pmtctconcepts", PMTCTConfigurationUtils.getNeededConcept());
//			pmtctConfig.put("drugOrderTypes", config.getDrugOrderTypes());
//			pmtctConfig.put("relationshipTypes", config.getRelationshipTypes());
//			pmtctConfig.put("concepts", config.getConcepts());
//			pmtctConfig.put("encounterTypes", config.getEncounterTypes());
//			pmtctConfig.put("programs", config.getPrograms());
//			pmtctConfig.put("attributeTypes", config.getAttributeTypes());
//			pmtctConfig.put("pageSize", "" + PMTCTConfigurationUtils.getNumberOfRecordPerPage());
//			pmtctConfig.put("defaultLocation", "" + PMTCTConfigurationUtils.getDefaultLocationId());
//			pmtctConfig.put("criticalLevelOfCD4Count", constants.getCriticalLevelOfCD4Count());
			
//			pmtctConfig.put("existingLocations", Context.getLocationService().getAllLocations());
//			pmtctConfig.put("existingPrograms", Context.getProgramWorkflowService().getAllPrograms());
//			pmtctConfig.put("existingAttributeTypes", Context.getPersonService().getAllPersonAttributeTypes());
			pmtctConfig.put("existingEncTypes", Context.getEncounterService().getAllEncounterTypes());
			pmtctConfig.put("existingIdentifierTypes", Context.getPatientService().getAllPatientIdentifierTypes());
			pmtctConfig.put("existingDrugOrderTypes", Context.getOrderService().getOrderTypes(false));
			pmtctConfig.put("existingRelationshipTypes", Context.getPersonService().getAllRelationshipTypes());
			
			mav.addObject("pmtctConfigured", Context.getAdministrationService().getGlobalPropertyObject("pmtct.configured"));
			
			mav.addObject("currentRelationShipTypeId", PMTCTConfigurationUtils.getRelationshipTypeId());
			mav.addObject("gp_relationShipType", Context.getAdministrationService().getGlobalPropertyObject("pmtct.relationshiptype.motherchild"));
			
			mav.addObject("currentDrugOrderTypeId", PMTCTConfigurationUtils.getDrugOrderTypeId());
			mav.addObject("gp_OrderType", Context.getAdministrationService().getGlobalPropertyObject("pmtct.ordertype"));
			
			mav.addObject("currentIdentifierTypeId", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
			mav.addObject("gp_IdentifierType", Context.getAdministrationService().getGlobalPropertyObject("pmtct.identifiertype.anc"));
			
			mav.addObject("pmtctConfig", pmtctConfig);
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "An error occured when trying to load data. Find the error in the log file.");
			log.error(ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
	
	//	@SuppressWarnings("deprecation")
//	private void saveChanges(HttpServletRequest request, boolean reset) {
//		try {
//			boolean b = config.save(request, reset);
//			if (b) {
//				String msg = getMessageSourceAccessor().getMessage("pmtct.general.saveSuccess");
//				request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
//				
//				log.info("PMTCT Configurations saved Successfully.");
//			} else {
//				String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
//				request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
//				
//				log.error("An error occured when trying to save PMTCT Configurations");
//			}
//		}
//		catch (Exception e) {
//			String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
//			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
//			
//			log.error("An error occured when trying to save PMTCT Configurations\n");
//			e.printStackTrace();
//		}
//	}
	
	private void saveVCTConfiguration(HttpServletRequest request, ModelAndView mav) {
		AdministrationService as = Context.getAdministrationService();
		
		try {	
			//config_chkbx
			GlobalProperty gpPmtctConfig = as.getGlobalPropertyObject("pmtct.configured");
			gpPmtctConfig.setPropertyValue("" + (request.getParameter("config_chkbx") != null));
			as.saveGlobalProperty(gpPmtctConfig);

			//identifierType
			GlobalProperty gpIdentifierType = as.getGlobalPropertyObject("pmtct.identifiertype.anc");
			gpIdentifierType.setPropertyValue(request.getParameter("identifierType"));
			as.saveGlobalProperty(gpIdentifierType);

			//drugOrderType
			GlobalProperty gpOrderType = as.getGlobalPropertyObject("pmtct.ordertype");
			gpOrderType.setPropertyValue(request.getParameter("drugOrderType"));
			as.saveGlobalProperty(gpOrderType);

			//relationshipType
			GlobalProperty gpRelationshipType = as.getGlobalPropertyObject("pmtct.relationshiptype.motherchild");
			gpRelationshipType.setPropertyValue(request.getParameter("relationshipType"));
			as.saveGlobalProperty(gpRelationshipType);
			
			//encounter types
			for(GlobalProperty gp:PMTCTConfigurationUtils.getNeededEncounterType()){
				gp.setPropertyValue("" + request.getParameter(PMTCTModuleTag.globalPropertyParser(gp.getProperty())));
				as.saveGlobalProperty(gp);
			}
			
			//concepts
			for(GlobalProperty gp:PMTCTConfigurationUtils.getNeededConcept()){
				gp.setPropertyValue("" + request.getParameter(PMTCTModuleTag.globalPropertyParser(gp.getProperty())));
				as.saveGlobalProperty(gp);
			}
			
			String msg = getMessageSourceAccessor().getMessage("Form.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
		}
		catch (Exception ex) {
			String msg = getMessageSourceAccessor().getMessage("Form.not.saved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			
			log.error("An error occured when trying to save the PMTCT Modules Configurations: \n");
			ex.printStackTrace();
		}
		
	}
}
