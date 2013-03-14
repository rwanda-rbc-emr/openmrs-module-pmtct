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
 * @author Yves GAKUBA
 */
public class PmtctInfantStatController extends ParameterizableViewController {
	
	private static Log log = LogFactory.getLog(PmtctInfantStatController.class);
	
	private String[] monthsList = { "pmtct.month.jan", "pmtct.month.feb", "pmtct.month.mar", "pmtct.month.apr",
	        "pmtct.month.may", "pmtct.month.jun", "pmtct.month.jul", "pmtct.month.aug", "pmtct.month.sep",
	        "pmtct.month.oct", "pmtct.month.nov", "pmtct.month.dec" };
		
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		
		PmtctService pmtct;
		List<Object> result = new ArrayList<Object>();
		String pageNumber = request.getParameter("page");
		
		String startDate = "";
		
		String endDate = "";
		
		int infantTestSelect = -1, year = -1, month = -1;
		
		List<Object> res;
		
		List<Integer> numberOfPages;
		
//		config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//		constants = config.getListOfConstants().get(0);
//		request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
		
		request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
		
		try {
			if (null != request.getParameter("startDate") && "" != request.getParameter("startDate").trim()) {
				startDate = request.getParameter("startDate").trim();
			}
			if (null != request.getParameter("endDate") && "" != request.getParameter("endDate").trim()) {
				endDate = request.getParameter("endDate").trim();
			}
			if (null != request.getParameter("infantTestSelect") && "" != request.getParameter("infantTestSelect").trim()) {
				infantTestSelect = Integer.parseInt(request.getParameter("infantTestSelect").trim());
			}
			if (null != request.getParameter("year") && "" != request.getParameter("year").trim()) {
				year = Integer.parseInt(request.getParameter("year").trim());
			}
			if (null != request.getParameter("month") && "" != request.getParameter("month").trim()) {
				month = Integer.parseInt(request.getParameter("month").trim());
			}
			//		}
			//		catch (Exception ex) {
			//			log.error("An error occured when trying to get parameters: \n");
			//			ex.printStackTrace();
			//		}
			//		
			//		try {
			if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
				pmtct = Context.getService(PmtctService.class);
//				pmtct.setPMTCTProgrmaId(config.getConstants());
				res = new ArrayList<Object>();
				
				mav.addObject("monthsList", monthsList);
				
				if (request.getParameter("category").equalsIgnoreCase("child")) {
					if (request.getParameter("type").compareTo("1") == 0) {
						if (startDate.compareTo("") != 0 && endDate.compareTo("") != 0) {
							res = pmtct.getGeneralStatForInfantTests(startDate, endDate);
							request.getSession().setAttribute("inf_stat_res", res);
						}
						mav.addObject("listHeaderTitle", "pmtct.menu.childTestResumePerPeriod");
					} else if (request.getParameter("type").compareTo("2") == 0) {
						res = pmtct.getExpectedPatientsInPCR();
						request.getSession().setAttribute("inf_stat_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.expectedChildInPCR");
					} else if (request.getParameter("type").compareTo("3") == 0) {
						res = pmtct.getExpectedPatientsForSerologyAt9Months();
						request.getSession().setAttribute("inf_stat_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.expectedChildInSerologyFor9Month");
					} else if (request.getParameter("type").compareTo("4") == 0) {
						res = pmtct.getExpectedPatientsForSerologyAt18Months();
						request.getSession().setAttribute("inf_stat_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.expectedChildInSerologyFor18Month");
					} else if (request.getParameter("type").compareTo("5") == 0) {
						res = pmtct.getInfantByMoisDeRapportageByInfantTest(month, year, infantTestSelect);
						request.getSession().setAttribute("inf_stat_res", res);
						mav.addObject("listHeaderTitle", "pmtct.menu.moisDeRapportageInfantTests");
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
				request.getSession().setAttribute("inf_stat_numberOfPages", numberOfPages);
			} else {
				res = (List<Object>) request.getSession().getAttribute("inf_stat_res");
				numberOfPages = (List<Integer>) request.getSession().getAttribute("inf_stat_numberOfPages");
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
			mav.addObject("positiveId", PMTCTConstants.POSITIVE);
			mav.addObject("infantFeedingMethodId", PMTCTConstants.INFANT_FEEDING_METHOD);
			
			List<Integer> years = new ArrayList<Integer>();
			for (int yr = 2000; yr < 2016; yr++)
				years.add(yr);
			mav.addObject("years", years);
			
			FileExporter fexp = new FileExporter();
			
			if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
				if (request.getParameter("type").compareTo("1") == 0)
					fexp.exportInfantsTestResumeToCSVFile(request, response, res,
					    "general_statistics_for_infant_tests_from_" + startDate + "_to_" + endDate + ".csv",
					    "General Statistics for Infant Tests");
				else if (request.getParameter("type").compareTo("2") == 0)
					fexp.exportInfantsTestToCSVFile(request, response, res, "list_of_expected_infants_for_pcr_test.csv",
					    "List of Expected Infants for PCR Test");
				else if (request.getParameter("type").compareTo("3") == 0)
					fexp.exportInfantsTestToCSVFile(request, response, res,
					    "list_of_expected_infants_for_serology_test_at_9months.csv",
					    "List of Expected Infants for Serology Test at 9 months");
				else if (request.getParameter("type").compareTo("4") == 0)
					fexp.exportInfantsTestToCSVFile(request, response, res,
					    "list_of_expected_infants_for_serology_test_at_18months.csv",
					    "List of Expected Infants for Serology Test at 18 months");
				else if (request.getParameter("type").compareTo("5") == 0) {
					fexp.exportInfantsTestToCSVFile(request, response, res, "list_of_infant_mois_de_rapportage_"
					        + ((infantTestSelect == 1) ? "PCR" : (infantTestSelect == 2) ? "Serology at 9 months"
					                : "Serology at 18 months") + "_" + month + "_" + year + ".csv",
					    "Infant mois de rapportage "
					            + ((infantTestSelect == 1) ? "PCR" : (infantTestSelect == 2) ? "Serology at 9 months"
					                    : "Serology at 18 months") + " (" + month + "_" + year + ")");
				}
			}
		}
		catch (Exception ex) {
			request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
			    "An error occured when trying to load data. Find the error in the log file.");
			log.error("INFANTS STATISTICS : An error occured when trying to load data from database: \n" + ex.getMessage());
			ex.printStackTrace();
		}
		
		return mav;
	}
	
}
