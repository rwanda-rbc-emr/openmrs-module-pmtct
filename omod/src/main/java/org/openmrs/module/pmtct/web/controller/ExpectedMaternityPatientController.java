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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.module.pmtct.util.ContextProvider;
import org.openmrs.module.pmtct.util.FileExporter;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.openmrs.web.WebConstants;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

/**
 *
 */
public class ExpectedMaternityPatientController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@SuppressWarnings( { "static-access", "deprecation" })
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		StringBuilder parameters = new StringBuilder();
		parameters.append("category=maternity&page=1");
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		PmtctService pmtct;
		List<Object> result = new ArrayList<Object>();
		String pageNumber = request.getParameter("page");
		
		String startDate = "";
		
		String endDate = "";
		
		List<Object> res;
		
		List<Integer> numberOfPages;
		
//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//		constants = config.getListOfConstants().get(0);
//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			if (null != request.getParameter("startDate") && "" != request.getParameter("startDate").trim()) {
				startDate = request.getParameter("startDate").trim();
				parameters.append("&startDate=" + startDate);
			}
			if (null != request.getParameter("endDate") && "" != request.getParameter("endDate").trim()) {
				endDate = request.getParameter("endDate").trim();
				parameters.append("&endDate=" + endDate);
			}
			
			if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
				pmtct = Context.getService(PmtctService.class);
//				pmtct.setPMTCTProgrmaId(config.getConstants());
				res = new ArrayList<Object>();
				
				if (request.getParameter("type").compareTo("3") == 0) {
					if (startDate.compareTo("") != 0 && endDate.compareTo("") != 0) {
						res = pmtct.getpatientsTestedInDeliveryRoom(startDate, endDate);
						request.getSession().setAttribute("exp_mat_pat_res", res);
					}
					mav.addObject("listHeaderTitle", "pmtct.menu.patientTestedInWorkroom");
				} else if (request.getParameter("type").compareTo("2") == 0) {
					res = pmtct.getExpectedPatientsInMaternity();
					request.getSession().setAttribute("exp_mat_pat_res", res);
					mav.addObject("listHeaderTitle", "pmtct.menu.expectedMaternityPatient");
				} else if (request.getParameter("type").compareTo("1") == 0) {
					if (startDate.compareTo("") != 0 && endDate.compareTo("") != 0) {
						res = pmtct.getGeneralStatsForPatientsInMaternity(startDate, endDate);
						request.getSession().setAttribute("exp_mat_pat_res", res);
					}
					mav.addObject("listHeaderTitle", "pmtct.menu.generalStatsInMaternity");
				} else if (request.getParameter("type").compareTo("4") == 0) {
					res = pmtct.getPatientsMissedTheirMaternityEncounter();
					request.getSession().setAttribute("exp_mat_pat_res", res);
					mav.addObject("listHeaderTitle", "pmtct.menu.patientWhoMissedMaternityEncounter");
				}
				
				for (int i = 0; i < PMTCTConfigurationUtils.getNumberOfRecordPerPage(); i++) {
					if (res.size() == 0)
						break;
					if (i >= res.size() - 1) {
						result.add(res.get(i));
						break;
					} else
						result.add(res.get(i));
				}
				//paging
				int n = (res.size() == ((int) (res.size() / PMTCTConfigurationUtils.getNumberOfRecordPerPage()))
				        * PMTCTConfigurationUtils.getNumberOfRecordPerPage()) ? (res.size() / PMTCTConfigurationUtils
				        .getNumberOfRecordPerPage()) : ((int) (res.size() / PMTCTConfigurationUtils
				        .getNumberOfRecordPerPage())) + 1;
				numberOfPages = new ArrayList<Integer>();
				for (int i = 1; i <= n; i++) {
					numberOfPages.add(i);
				}
				request.getSession().setAttribute("exp_mat_pat_numberOfPages", numberOfPages);
			} else {
				res = (List<Object>) request.getSession().getAttribute("exp_mat_pat_res");
				numberOfPages = (List<Integer>) request.getSession().getAttribute("exp_mat_pat_numberOfPages");
				for (int i = (PMTCTConfigurationUtils.getNumberOfRecordPerPage() * (Integer.parseInt(pageNumber) - 1)); i < PMTCTConfigurationUtils
				        .getNumberOfRecordPerPage()
				        * (Integer.parseInt(pageNumber)); i++) {
					if (i >= res.size())
						break;
					else
						result.add(res.get(i));
				}
			}
			
			//page infos
			Object[] pagerInfos = new Object[3];
			pagerInfos[0] = (res.size() == 0) ? 0 : (PMTCTConfigurationUtils.getNumberOfRecordPerPage() * (Integer
			        .parseInt(pageNumber) - 1)) + 1;
			pagerInfos[1] = (PMTCTConfigurationUtils.getNumberOfRecordPerPage() * (Integer.parseInt(pageNumber)) <= res
			        .size()) ? PMTCTConfigurationUtils.getNumberOfRecordPerPage() * (Integer.parseInt(pageNumber)) : res
			        .size();
			pagerInfos[2] = res.size();
			
			ApplicationContext appContext = ContextProvider.getApplicationContext();
			
			mav.addObject("pageInfos", appContext.getMessage("pmtct.pagingInfo.showingResults", pagerInfos, Context
			        .getUserContext().getLocale()));
			mav.addObject("numberOfPages", numberOfPages);
			mav.addObject("patientList", result);
			mav.addObject("pageSize", PMTCTConfigurationUtils.getNumberOfRecordPerPage());
			mav.addObject("startDate", startDate);
			mav.addObject("endDate", endDate);
			mav.addObject("parameters", parameters.toString());
			
			mav.addObject("dpaId", PMTCTConstants.PREGNANT_DUE_DATE);
			mav.addObject("dateOfConfinementId", PMTCTConstants.DATE_OF_CONFINEMENT);
			mav.addObject("hivTestResultId", PMTCTConstants.RESULT_OF_HIV_TEST);
			mav.addObject("partnerStatusId", PMTCTConstants.TESTING_STATUS_OF_PARTNER);
			mav.addObject("childBornStatusId", PMTCTConfigurationUtils.getBornStatusConceptId());
			mav.addObject("positiveId", PMTCTConstants.POSITIVE);
			mav.addObject("bornDeadId", PMTCTConfigurationUtils.getBornDeadConceptId());
			mav.addObject("cpnIdentifierTypeId", PMTCTConfigurationUtils.getCPNIdentifierTypeId());
			
			FileExporter fexp = new FileExporter();
			
			if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
				if (request.getParameter("type").compareTo("1") == 0)
					fexp.exportGeneralStatisticsInMaternityToCSVFile(request, response, res,
					    "general_statistics_in_Maternity_from_" + startDate + "_to_" + endDate + ".csv",
					    "General statistics in Maternity");
				else if (request.getParameter("type").compareTo("2") == 0)
					fexp.exportExpectedPatientInMaternityToCSVFile(request, response, res,
					    "list_of_expected_patients_in_Maternity.csv", "List of expected patients in Maternity");
				else if (request.getParameter("type").compareTo("3") == 0)
					fexp.exportPatientTestedInDeliveryRoomToCSVFile(request, response, res,
					    "list_of_Patient_tested_in_Delivery_room.csv", "List of Patient tested in Delivery room");
				else if (request.getParameter("type").compareTo("4") == 0)
					fexp.exportExpectedPatientInMaternityToCSVFile(request, response, res,
					    "list_of_patients_who_missed_Maternity_Encounter.csv",
					    "List of patients who missed Maternity Encounter");
			}
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			log
			        .error("MATERNITY STATISTICS : An error occured when trying to load data from database: \n"
			                + ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
}
