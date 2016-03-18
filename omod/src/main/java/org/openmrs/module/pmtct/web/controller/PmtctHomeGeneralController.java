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
public class PmtctHomeGeneralController extends ParameterizableViewController {
	
	private Log log = LogFactory.getLog(this.getClass());
		
	/**
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		StringBuilder parameters = new StringBuilder();
		parameters.append("page=1");
		
		String startDate = "";
		
		String endDate = "";
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName(getViewName());
		PmtctService pmtct;
		List<Object> result = new ArrayList<Object>();
		String pageNumber;
		int pageSize;
		
		List<Object> res;
		
		List<Integer> numberOfPages;
		
		try {
			pageNumber = request.getParameter("page");
			pageSize = PMTCTConfigurationUtils.getNumberOfRecordPerPage();
			
//			config = PMTCTConfiguration.getInstance(request.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));
//			constants = config.getConstants();
//			request.getSession().setAttribute("enableModification", "" + config.isModificationEnabled());
			
			request.getSession().setAttribute("pmtctModuleConfigured", "" + PMTCTConfigurationUtils.isConfigured());
			
			if (pageNumber.compareToIgnoreCase("1") == 0 || pageNumber.compareToIgnoreCase("") == 0) {
				pmtct = Context.getService(PmtctService.class);
//				pmtct.setPMTCTProgrmaId(constants);
				res = new ArrayList<Object>();
				
				if (request.getParameter("ckbxPeriod") == null)
					res = pmtct.getCurrentPatientsInPmtct();
				else {
					parameters.append("&ckbxPeriod=" + request.getParameter("ckbxPeriod"));
					if (null != request.getParameter("startDate") && "" != request.getParameter("startDate").trim()) {
						startDate = request.getParameter("startDate").trim();
						parameters.append("&startDate=" + startDate);
					}
					if (null != request.getParameter("endDate") && "" != request.getParameter("endDate").trim()) {
						endDate = request.getParameter("endDate").trim();
						parameters.append("&endDate=" + endDate);
					}
					res = pmtct.getCurrentPatientsInPmtct(startDate, endDate);
					request.getSession().setAttribute("home_gen_res", res);
				}
				
				//collection
				for (int i = 0; i < pageSize; i++) {
					if (res.size() == 0)
						break;
					if (i >= res.size() - 1) {
						result.add(res.get(i));
						break;
					} else
						result.add(res.get(i));
				}
				
				//paging
				int n = (res.size() == ((int) (res.size() / pageSize)) * pageSize) ? (res.size() / pageSize) : ((int) (res
				        .size() / pageSize)) + 1;
				numberOfPages = new ArrayList<Integer>();
				for (int i = 1; i <= n; i++) {
					numberOfPages.add(i);
				}
				request.getSession().setAttribute("home_gen_numberOfPages", numberOfPages);
				
			} else {
				res = (List<Object>) request.getSession().getAttribute("home_gen_res");
				numberOfPages = (List<Integer>) request.getSession().getAttribute("home_gen_numberOfPages");
				for (int i = (pageSize * (Integer.parseInt(pageNumber) - 1)); i < pageSize * (Integer.parseInt(pageNumber)); i++) {
					if (i >= res.size())
						break;
					else
						result.add(res.get(i));
				}
			}
			
			//page infos
			Object[] pagerInfos = new Object[3];
			pagerInfos[0] = (res.size() == 0) ? 0 : (pageSize * (Integer.parseInt(pageNumber) - 1)) + 1;
			pagerInfos[1] = (pageSize * (Integer.parseInt(pageNumber)) <= res.size()) ? pageSize
			        * (Integer.parseInt(pageNumber)) : res.size();
			pagerInfos[2] = res.size();
			
			ApplicationContext appContext = ContextProvider.getApplicationContext();
			
			mav.addObject("pageInfos", appContext.getMessage("pmtct.pagingInfo.showingResults", pagerInfos, Context
			        .getUserContext().getLocale()));
			mav.addObject("numberOfPages", numberOfPages);
			mav.addObject("patientList", result);
			mav.addObject("pageSize", pageSize);
			mav.addObject("parameters", parameters.toString());
			
			mav.addObject("positiveId", PMTCTConstants.POSITIVE);
			mav.addObject("hivTestResultId", PMTCTConstants.RESULT_OF_HIV_TEST);
			
			FileExporter fexp = new FileExporter();
			
			if (request.getParameter("export") != null && request.getParameter("export").compareToIgnoreCase("csv") == 0) {
				fexp.exportToCSVFile(request, response, res, "list_of_patients_in_pmtct_program.csv",
				    "List of Patient in PMTCT Program");
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
