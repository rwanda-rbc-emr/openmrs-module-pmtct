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
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.PMTCTConfiguration;
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
@SuppressWarnings("deprecation")
public class PatientInCPNController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private String[] monthsList = { "pmtct.month.jan", "pmtct.month.feb", "pmtct.month.mar", "pmtct.month.apr",
	        "pmtct.month.may", "pmtct.month.jun", "pmtct.month.jul", "pmtct.month.aug", "pmtct.month.sep",
	        "pmtct.month.oct", "pmtct.month.nov", "pmtct.month.dec" };
	
//	private PMTCTConfiguration config = null;
	
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@SuppressWarnings("static-access")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		String startDate = "";
		
		String endDate = "";
		
		int year = (new Date()).getYear();
		int month = (new Date()).getMonth();
		
		PmtctService pmtct;
		List<Object> result = new ArrayList<Object>();
		String pageNumber = request.getParameter("page");
		
		List<Object> res;
		
		List<Integer> numberOfPages;
		
//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//		PMTCTConstants constants = config.getListOfConstants().get(0);
//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			if (null != request.getParameter("startDate") && "" != request.getParameter("startDate").trim()) {
				startDate = request.getParameter("startDate").trim();
			}
			if (null != request.getParameter("endDate") && "" != request.getParameter("endDate").trim()) {
				endDate = request.getParameter("endDate").trim();
			}
			//		}
			//		catch (Exception ex) {
			//			ex.printStackTrace();
			//		}
			//		
			//		try {
			if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
				pmtct = Context.getService(PmtctService.class);
//				pmtct.setPMTCTProgrmaId(config.getConstants());
				res = new ArrayList<Object>();
				
				mav.addObject("monthsList", monthsList);
				
				if (request.getParameter("category").equalsIgnoreCase("cpn")) {
					if (request.getParameter("type").equalsIgnoreCase("1")) {
						if (startDate.compareTo("") != 0 && endDate.compareTo("") != 0) {
							res = pmtct.getGeneralStatsForPatientsInCPN(startDate, endDate);
							request.getSession().setAttribute("pat_in_cpn_res", res);
						}
						mav.addObject("listHeaderTitle", "pmtct.menu.generalStatsInCPN");
					} else if (request.getParameter("type").equalsIgnoreCase("2")) {
						if (request.getParameter("month") != null && request.getParameter("month").trim().compareTo("") != 0)
							month = Integer.parseInt(request.getParameter("month"));
						if (request.getParameter("year") != null && request.getParameter("year").trim().compareTo("") != 0)
							year = Integer.parseInt(request.getParameter("year"));
						
						res = pmtct.getPatientByMoisDeRapportageDPA(month, year);
						request.getSession().setAttribute("pat_in_cpn_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.patientWhoMoisDeRapportageDPAIsTheCurrentMois");
					} else if (request.getParameter("type").equalsIgnoreCase("3")) {
						res = pmtct.getCouplesDiscordant();
						request.getSession().setAttribute("pat_in_cpn_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.couplesDiscordant");
					}
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
				request.getSession().setAttribute("pat_in_cpn_numberOfPages", numberOfPages);
			} else {
				res = (List<Object>) request.getSession().getAttribute("pat_in_cpn_res");
				numberOfPages = (List<Integer>) request.getSession().getAttribute("pat_in_cpn_numberOfPages");
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
			
			mav.addObject("hivTestResultId", PMTCTConstants.RESULT_OF_HIV_TEST);
			mav.addObject("dpaId", PMTCTConstants.PREGNANT_DUE_DATE);
			mav.addObject("dateOfConfinementId", PMTCTConstants.DATE_OF_CONFINEMENT);
			mav.addObject("positiveId", PMTCTConstants.POSITIVE);
			
			List<Integer> years = new ArrayList<Integer>();
			for (int yr = 2000; yr < 2016; yr++)
				years.add(yr);
			mav.addObject("years", years);
			
			FileExporter fexp = new FileExporter();
			
			if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
				if (request.getParameter("type").equalsIgnoreCase("1"))
					fexp.exportGeneralStatisticsInCPNToCSVFile(request, response, res, "list_of_patients_in_CPN.csv",
					    "List of Patients in CPN");
				else if (request.getParameter("type").equalsIgnoreCase("2")) {
					fexp.exportToCSVFile(request, response, res, "list_of_patients_in_CPN_DPA_" + month + "_" + year
					        + ".csv", "List of Patients in CPN(DPA=" + month + "-" + year + ")");
				} else if (request.getParameter("type").equalsIgnoreCase("3"))
					fexp.exportToCSVFile(request, response, res, "couples_discordants.csv", "Couples discordants");
			}
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			log.error("An error occured when trying to load data from database: \n" + ex.getMessage());
			ex.printStackTrace();
		}
		return mav;
	}
}
