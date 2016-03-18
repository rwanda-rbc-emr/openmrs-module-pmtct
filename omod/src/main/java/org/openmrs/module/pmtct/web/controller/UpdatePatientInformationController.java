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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.web.WebConstants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * @author Yves GAKUBA
 */
public class UpdatePatientInformationController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
		
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings( { "deprecation" })
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		if (Context.getAuthenticatedUser() == null)
			return new ModelAndView(new RedirectView(request.getContextPath() + "/login.htm"));
		
		Patient p = null;
		
		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(getViewName() + "?patientId=" + request.getParameter("patientId")));
		
		try {
			
//			config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//			request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
//			constants = config.getConstants();
			
			request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
			
			p = Context.getPatientService().getPatient(Integer.parseInt(request.getParameter("patientId")));
			//			PersonService ps = Context.getPersonService();
			//BIRTHDATE_ATTRIBUTE
			//p.setBirthdate(Context.getDateFormat().parse(request.getParameter("birthdate")));
			//p.setGender(request.getParameter("addGender"));
			
			if (request.getParameter("formAttribute") != null)
				p = updatePersonAttributes(request, p);
//			if (request.getParameter("formAddress") != null)
//				p = updatePersonAddress(request, p);
			
			//saving the changes
			Context.getPatientService().updatePatient(p);
			
			log.info("Information updated successifully for patient with id " + request.getParameter("patientId"));
			
			String msg = getMessageSourceAccessor().getMessage("pmtct.general.saveSuccess");
			request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, msg);
			
			//refreshing the patient object
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("patient", p);
//			model.put("location", p.getPersonAddress());
			
			//			request.getSession().setAttribute("model", model);
			
			//			because the gender cant be updated from here, no need to check if the patient is male
			//			if(p.getGender().compareToIgnoreCase("M")==0)
			//				mav.setView(new RedirectView(request.getContextPath()+"/patientDashboard.form?patientId=" + request.getParameter("patientId")));
			
		}
		catch (Exception e) {
			log.error("An error occured when trying to update information for patient with id "
			        + request.getParameter("patientId"));
			e.printStackTrace();
			
			String msg = getMessageSourceAccessor().getMessage("pmtct.general.notSaved");
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
			
		}
		
		return mav;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param p
	 * @return
	 * @throws Exception 
	 */
	private Patient updatePersonAttributes(HttpServletRequest request, Patient p) throws Exception {
		PersonAttribute pa_civilStatus = null;
		PersonAttribute pa_educationLevel = null;
		PersonAttribute pa_mainActivity = null;
		PersonService ps = Context.getPersonService();
		
		//CIVIL_STATUS_ATTRIBUTE
		pa_civilStatus = p.getAttribute(PMTCTConfigurationUtils.getCivilStatusAttributeTypeId());
		if (pa_civilStatus != null) {
			pa_civilStatus.setValue(request.getParameter("civilStatus"));
			pa_civilStatus.setDateChanged(new Date());
		} else {
			if (request.getParameter("civilStatus") != null
			        && request.getParameter("civilStatus").trim().compareToIgnoreCase("") != 0) {
				pa_civilStatus = new PersonAttribute();
				/*set person attribute values*/
				pa_civilStatus.setValue(request.getParameter("civilStatus"));
				pa_civilStatus.setAttributeType(ps.getPersonAttributeType(PMTCTConfigurationUtils.getCivilStatusAttributeTypeId()));
				pa_civilStatus.setDateCreated(new Date());
				pa_civilStatus.setCreator(Context.getAuthenticatedUser());
				p.addAttribute(pa_civilStatus);//adding the attribute of the patient
				//log.info("New person attribute of type "+PMTCTConstants.CIVIL_STATUS_ATTRIBUTE_TYPE_ID+" added to patient with id "+request.getParameter("patientId"));
			}
		}
		
		//EDUCATION_LEVEL_ATTRIBUTE
		pa_educationLevel = p.getAttribute(PMTCTConfigurationUtils.getEducationLevelAttributeTypeId());
		if (pa_educationLevel != null) {
			pa_educationLevel.setValue(request.getParameter("educationLevel"));
			pa_educationLevel.setDateChanged(new Date());
		} else {
			if (request.getParameter("educationLevel") != null
			        && request.getParameter("educationLevel").trim().compareToIgnoreCase("") != 0) {
				pa_educationLevel = new PersonAttribute();
				/*set person attribute values*/
				pa_educationLevel.setValue(request.getParameter("educationLevel"));
				pa_educationLevel.setAttributeType(ps.getPersonAttributeType(PMTCTConfigurationUtils.getEducationLevelAttributeTypeId()));
				pa_educationLevel.setDateCreated(new Date());
				pa_educationLevel.setCreator(Context.getAuthenticatedUser());
				p.addAttribute(pa_educationLevel);//adding the attribute of the patient
				//log.info("New person attribute of type "+PMTCTConstants.EDUCATION_LEVEL_ATTRIBUTE_TYPE_ID+" added to patient with id "+request.getParameter("patientId"));
			}
		}
		//MAIN_ACTIVITY_ATTRIBUTE
		pa_mainActivity = p.getAttribute(PMTCTConfigurationUtils.getMainActivityAttributeTypeId());
		if (pa_mainActivity != null) {
			pa_mainActivity.setValue(request.getParameter("mainActivity"));
			pa_mainActivity.setDateChanged(new Date());
		} else {
			if (request.getParameter("mainActivity") != null
			        && request.getParameter("mainActivity").trim().compareToIgnoreCase("") != 0) {
				pa_mainActivity = new PersonAttribute();
				/*set person attribute values*/
				pa_mainActivity.setValue(request.getParameter("mainActivity"));
				pa_mainActivity.setAttributeType(ps.getPersonAttributeType(PMTCTConfigurationUtils.getMainActivityAttributeTypeId()));
				pa_mainActivity.setDateCreated(new Date());
				pa_mainActivity.setCreator(Context.getAuthenticatedUser());
				p.addAttribute(pa_mainActivity);//adding the attribute of the patient
				//log.info("New person attribute of type "+PMTCTConstants.MAIN_ACTIVITY_ATTRIBUTE_TYPE_ID+" added to patient with id "+request.getParameter("patientId"));
			}
		}
		return p;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param p
	 * @return
	 */
//	private Patient updatePersonAddress(HttpServletRequest request, Patient p) {
//		PersonAddress pa = null;
//		if (p.getPersonAddress() == null)
//			pa = new PersonAddress();
//		else
//			pa = p.getPersonAddress();
//		
//		if (request.getParameter("country") != null)
//			pa.setCountry(request.getParameter("country").trim());
//		pa.setStateProvince(request.getParameter("stateProvince").trim());
//		pa.setCountyDistrict(request.getParameter("countyDistrict").trim());
//		pa.setCityVillage(request.getParameter("cityVillage").trim());
//		pa.setNeighborhoodCell(request.getParameter("neighborhoodCell").trim());
//		pa.setAddress1(request.getParameter("address1").trim());
//		pa.setCreator(Context.getAuthenticatedUser());
//		pa.setDateCreated(new Date());
//		
//		p.addAddress(pa);
//		
//		return p;
//	}
}
