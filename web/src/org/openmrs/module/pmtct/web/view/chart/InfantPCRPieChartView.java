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
package org.openmrs.module.pmtct.web.view.chart;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.pmtct.PMTCTModuleTag;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.module.pmtct.util.ContextProvider;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.springframework.context.ApplicationContext;

/**
 *
 */
public class InfantPCRPieChartView extends AbstractChartView {
	
	/**
	 * @see org.openmrs.module.pmtct.web.view.chart.AbstractChartView#createChart(java.util.Map,
	 *      javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		UserContext userContext = Context.getUserContext();
		ApplicationContext appContext = ContextProvider.getApplicationContext();
		PMTCTModuleTag tag = new PMTCTModuleTag();
		
		List<Object> res = new ArrayList<Object>();
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		String title = "", descriptionTitle = "", dateInterval = "";
		Concept concept = null;
		SimpleDateFormat df = Context.getDateFormat();
		
		//				Date myDate1 = new Date("1/1/" + ((new Date()).getYear() + 1900));
		//				String startDate1 = df.format(myDate1);
		
		Date today = new Date();
		Date oneYearFromNow = new Date(new Date().getTime() - PMTCTConstants.YEAR_IN_MILLISECONDS);
		
		String endDate1 = df.format(today);
		String startDate1 = df.format(oneYearFromNow);
		
		dateInterval = "(" + new SimpleDateFormat("dd-MMM-yyyy").format(oneYearFromNow) + " - "
		        + new SimpleDateFormat("dd-MMM-yyyy").format(today) + ")";
		
		try {
			
			PmtctService pmtct;
			pmtct = Context.getService(PmtctService.class);
			try {
				res = pmtct.getGeneralStatForInfantTests_Charting_PCR(startDate1, endDate1);
			}
			catch (SQLException ex) {
				ex.printStackTrace();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			List<String> hivTestResultOptions = new ArrayList<String>();
			
			List<Integer> pcr_hivTestResultValues = new ArrayList<Integer>();
			
			Collection<ConceptAnswer> answers = Context.getConceptService().getConcept(PMTCTConstants.RESULT_OF_HIV_TEST)
			        .getAnswers();
			for (ConceptAnswer str : answers) {
				hivTestResultOptions.add(str.getAnswerConcept().getName().getName());
				
				pcr_hivTestResultValues.add(0);
			}
			hivTestResultOptions.add("Others");
			pcr_hivTestResultValues.add(0);
			
			for (Object ob : res) {
				int val = 0;
				String temp = "", pcr_hivTestResult = "";
				
				temp = "" + ((Object[]) ob)[2];
				val = (temp.compareTo("") == 0) ? 0 : Integer.parseInt(temp);
				if (val > 0)
					pcr_hivTestResult = tag.getConceptNameById(temp);
				
				int i = 0;
				boolean pcr_found = false;
				for (String s : hivTestResultOptions) {
					if ((s.compareToIgnoreCase(pcr_hivTestResult)) == 0) {
						pcr_hivTestResultValues.set(i, pcr_hivTestResultValues.get(i) + 1);
						pcr_found = true;
					}
					i++;
				}
				
				if (!pcr_found) {
					pcr_hivTestResultValues.set(pcr_hivTestResultValues.size() - 1, pcr_hivTestResultValues
					        .get(pcr_hivTestResultValues.size() - 1) + 1);
				}
			}
			
			int i = 0;
			for (String s : hivTestResultOptions) {
				if (pcr_hivTestResultValues.get(i) > 0) {
					Float percentage = new Float(100 * pcr_hivTestResultValues.get(i) / res.size());
					pieDataset.setValue(s + " (" + pcr_hivTestResultValues.get(i) + " , " + percentage + "%)", percentage);
				}
				i++;
			}
			
			title = appContext.getMessage("pmtct.menu.infantTest", null, userContext.getLocale());
			concept = null;
			descriptionTitle = Context.getEncounterService().getEncounterType(
			    PMTCTConfigurationUtils.getPCRTestEncounterTypeId()).getName();
			descriptionTitle += dateInterval;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title + " : "
		        + ((null != concept) ? concept.getPreferredName(userContext.getLocale()) : descriptionTitle), pieDataset,
		    true, true, false);
		
		return chart;
	}
	
}
