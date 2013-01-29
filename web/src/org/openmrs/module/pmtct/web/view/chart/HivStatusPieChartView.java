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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.openmrs.ConceptAnswer;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.pmtct.PMTCTModuleTag;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.module.pmtct.util.ContextProvider;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.springframework.context.ApplicationContext;

/**
 * View to render date range usage data as a chart image
 */
public class HivStatusPieChartView extends AbstractChartView {
	
	@SuppressWarnings("static-access")
	@Override
	protected JFreeChart createChart(Map<String, Object> model, HttpServletRequest request) {
		
		UserContext userContext = Context.getUserContext();
		ApplicationContext appContext = ContextProvider.getApplicationContext();
		PmtctService pmtct = Context.getService(PmtctService.class);
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		
		List<Object> objects = null;
		PMTCTModuleTag tag = new PMTCTModuleTag();
		
		List<String> hivOptions = new ArrayList<String>();
		List<Integer> hivOptionValues = new ArrayList<Integer>();
		Collection<ConceptAnswer> answers = Context.getConceptService().getConcept(PMTCTConstants.RESULT_OF_HIV_TEST)
		        .getAnswers();
		try {
			objects = pmtct.getCurrentPatientsInPmtct();
			for (ConceptAnswer str : answers) {
				hivOptions.add(str.getAnswerConcept().getName().getName());
				hivOptionValues.add(0);
			}
			hivOptions.add("Others");
			hivOptionValues.add(0);
			
			for (Object ob : objects) {
				int patientId = (Integer) ((Object[]) ob)[0];
				String patientHivStatus = tag.lastObsValueByConceptId(patientId, PMTCTConstants.RESULT_OF_HIV_TEST);
				
				int i = 0;
				boolean found = false;
				for (String s : hivOptions) {
					if ((s.compareToIgnoreCase(patientHivStatus)) == 0) {
						hivOptionValues.set(i, hivOptionValues.get(i) + 1);
						found = true;
					}
					i++;
				}
				
				if (!found) {
					hivOptionValues.set(hivOptionValues.size() - 1, hivOptionValues.get(hivOptionValues.size() - 1) + 1);
				}
				
			}
			
			int i = 0;
			for (String s : hivOptions) {
				if (hivOptionValues.get(i) > 0) {
					Float percentage = new Float(100 * hivOptionValues.get(i) / objects.size());
					pieDataset.setValue(s + " (" + hivOptionValues.get(i) + " , " + percentage + "%)", percentage);
				}
				i++;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		String title = appContext.getMessage("pmtct.menu.patientInPmtct", null, userContext.getLocale());
		
		JFreeChart chart = ChartFactory.createPieChart(title
		        + " : "
		        + Context.getConceptService().getConcept(PMTCTConstants.HIV_STATUS)
		                .getPreferredName(userContext.getLocale()), pieDataset, true, true, false);
		
		return chart;
	}
}
