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
import org.openmrs.Encounter;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.UserContext;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.pmtct.PMTCTModuleTag;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.module.pmtct.util.ContextProvider;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.springframework.context.ApplicationContext;

/**
 *
 */
public class MaternityPieChartView extends AbstractChartView {
	
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
		PmtctService pmtct;
		pmtct = Context.getService(PmtctService.class);
		
		String startDate = "";
		String endDate = "";
		
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		String title = "", descriptionTitle = "", dateInterval = "";
		Concept concept = null;
		Encounter enc = null;
		
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();//new SimpleDateFormat("dd/MM/yyyy");
		
		if (request.getParameter("type").compareTo("1") == 0) {
			
			try {
				
				//				Date myDate = new Date("1/1/" + ((new Date()).getYear() + 1900));
				//				startDate = df.format(myDate);
				Date today=new Date();
				Date oneYearFromNow=new Date(new Date().getTime() - PMTCTConstants.YEAR_IN_MILLISECONDS);
				
				endDate = df.format(today);
				startDate = df.format(oneYearFromNow);
				
				dateInterval = "(" + new SimpleDateFormat("dd-MMM-yyyy").format(oneYearFromNow) + " - " + new SimpleDateFormat("dd-MMM-yyyy").format(today) + ")";
				res = pmtct.getGeneralStatsForPatientsInMaternity(startDate, endDate);
				
				List<String> childBornOptions = new ArrayList<String>();
				List<Integer> childBornStatusValues = new ArrayList<Integer>();
				Collection<ConceptAnswer> answers = Context.getConceptService().getConcept(
				    PMTCTConfigurationUtils.getBornStatusConceptId()).getAnswers();
				for (ConceptAnswer str : answers) {
					childBornOptions.add(str.getAnswerConcept().getName().getName());
					childBornStatusValues.add(0);
				}
				childBornOptions.add("Others");
				childBornStatusValues.add(0);
				
				for (Object ob : res) {
					int patientId = (Integer) ((Object[]) ob)[0];
					
					Encounter matEnc = tag.getMaternityEncounterInfo(patientId);
					
					String childResult = tag.observationValueByConcept(matEnc, PMTCTConfigurationUtils
					        .getBornStatusConceptId());
					
					int i = 0;
					boolean found = false;
					for (String s : childBornOptions) {
						if ((s.compareToIgnoreCase(childResult)) == 0) {
							childBornStatusValues.set(i, childBornStatusValues.get(i) + 1);
							found = true;
						}
						i++;
					}
					
					if (!found) {
						childBornStatusValues.set(childBornStatusValues.size() - 1, childBornStatusValues
						        .get(childBornStatusValues.size() - 1) + 1);
					}
					
				}
				
				int i = 0;
				for (String s : childBornOptions) {
					if (childBornStatusValues.get(i) > 0) {
						Float percentage = new Float(100 * childBornStatusValues.get(i) / res.size());
						pieDataset.setValue(s + " (" + childBornStatusValues.get(i) + " , " + percentage + "%)", percentage);
					}
					i++;
				}
				concept = Context.getConceptService().getConcept(PMTCTConfigurationUtils.getBornStatusConceptId());
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
			title = MohTracUtil.getMessage("pmtct.menu.maternity", null);
		} else if (request.getParameter("type").compareTo("2") == 0) {
			try {
				res = pmtct.getExpectedPatientsInMaternity();
				String patientHivStatus = "", partnerHivStatus = "";
				int allPositive = 0, allNegative = 0, negPos = 0, posNeg = 0, other = 0;
				
				for (Object ob : res) {
					int patientId = (Integer) ((Object[]) ob)[0];
					enc = tag.getCPNEncounterInfo(patientId);
					
					patientHivStatus = tag.observationValueByConcept(enc, PMTCTConstants.RESULT_OF_HIV_TEST);
					partnerHivStatus = tag.observationValueByConcept(enc, PMTCTConstants.TESTING_STATUS_OF_PARTNER);
					
					if (patientHivStatus.compareTo(partnerHivStatus) == 0
					        && patientHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.POSITIVE)
					                .getName().getName()) == 0)
						allPositive += 1;
					else if (patientHivStatus.compareTo(partnerHivStatus) == 0
					        && patientHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.NEGATIVE)
					                .getName().getName()) == 0)
						allNegative += 1;
					else if (patientHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.NEGATIVE)
					        .getName().getName()) == 0
					        && partnerHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.POSITIVE)
					                .getName().getName()) == 0)
						negPos += 1;
					else if (patientHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.POSITIVE)
					        .getName().getName()) == 0
					        && partnerHivStatus.compareTo(Context.getConceptService().getConcept(PMTCTConstants.NEGATIVE)
					                .getName().getName()) == 0)
						posNeg += 1;
					else
						other += 1;
				}
				
				Float percentage = 0f;
				if (allPositive > 0) {
					percentage = new Float(100 * allPositive / res.size());
					title = MohTracUtil.getMessage("pmtct.menu.allPositive", null);
					pieDataset.setValue(title + " (" + allPositive + " , " + percentage + "%)", percentage);
				}
				
				if (allNegative > 0) {
					percentage = new Float(100 * allNegative / res.size());
					title = MohTracUtil.getMessage("pmtct.menu.allNegative", null);
					pieDataset.setValue(title + " (" + allNegative + " , " + percentage + "%)", percentage);
				}
				
				if (posNeg > 0) {
					percentage = new Float(100 * posNeg / res.size());
					title = MohTracUtil.getMessage("pmtct.menu.posNeg", null);
					pieDataset.setValue(title + " (" + posNeg + " , " + percentage + "%)", percentage);
				}
				
				if (negPos > 0) {
					percentage = new Float(100 * negPos / res.size());
					title = MohTracUtil.getMessage("pmtct.menu.negPos", null);
					pieDataset.setValue(title + " (" + negPos + " , " + percentage + "%)", percentage);
				}
				
				if (other > 0) {
					percentage = new Float(100 * other / res.size());
					title = MohTracUtil.getMessage("pmtct.menu.otherStatus", null);
					pieDataset.setValue(title + " (" + other + " , " + percentage + "%)", percentage);
				}
				
				title = MohTracUtil.getMessage("pmtct.menu.expectedMaternityPatient", null);
				descriptionTitle = MohTracUtil.getMessage("pmtct.menu.partnerStatus", null);
				concept = null;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		} else if (request.getParameter("type").compareTo("3") == 0) {
			log.info(">>>>>>>>>>>>>>GRAPHICS>>patientsTestedInDeliveryRoom>>>Starting...");
			
			try {
				//				Date myDate = new Date("1/1/" + ((new Date()).getYear() + 1900));
				//				startDate = df.format(myDate);
				Date today=new Date();
				Date oneYearFromNow=new Date(new Date().getTime() - PMTCTConstants.YEAR_IN_MILLISECONDS);
				
				endDate = df.format(today);
				startDate = df.format(oneYearFromNow);
				
				log.info(">>>>>>>>>>>>>>GRAPHICS>>patientsTestedInDeliveryRoom>>>startdate="+startDate+"  -  endDate"+endDate);
				
				dateInterval = "(" + new SimpleDateFormat("dd-MMM-yyyy").format(oneYearFromNow) + " - " + new SimpleDateFormat("dd-MMM-yyyy").format(today) + ")";
				res = pmtct.getpatientsTestedInDeliveryRoom(startDate, endDate);
				log.info(">>>>>>>>>>>>>>GRAPHICS>>patientsTestedInDeliveryRoom>>>Result="+res.size());
//				log.info(">>>>>>>>>>>>>>MaternityPieChartView>> startdate=" + startDate + "----- enddate=" + endDate
//				        + "<<=====>>" + res.size());
				
				List<String> resultOfHIVTestOptions = new ArrayList<String>();
				List<Integer> resultOfHIVTestValues = new ArrayList<Integer>();
				Collection<ConceptAnswer> answers = Context.getConceptService().getConcept(
				    PMTCTConstants.RESULT_HIV_TEST_IN_DELIVERY_ROOM).getAnswers();
				for (ConceptAnswer str : answers) {
					resultOfHIVTestOptions.add(str.getAnswerConcept().getName().getName());
					resultOfHIVTestValues.add(0);
				}
				resultOfHIVTestOptions.add("Others");
				resultOfHIVTestValues.add(0);
				
				for (Object ob : res) {
					int patientId = (Integer) ((Object[]) ob)[0];
					
					Encounter matEnc = tag.getMaternityEncounterInfo(patientId);
					
					String hivTestResult = tag.observationValueByConcept(matEnc,
					    PMTCTConstants.RESULT_HIV_TEST_IN_DELIVERY_ROOM);
					
					int i = 0;
					boolean found = false;
					for (String s : resultOfHIVTestOptions) {
						if ((s.compareToIgnoreCase(hivTestResult)) == 0) {
							resultOfHIVTestValues.set(i, resultOfHIVTestValues.get(i) + 1);
							found = true;
						}
						i++;
					}
					
					if (!found) {
						resultOfHIVTestValues.set(resultOfHIVTestValues.size() - 1, resultOfHIVTestValues
						        .get(resultOfHIVTestValues.size() - 1) + 1);
					}
					
				}
				
				int i = 0;
				for (String s : resultOfHIVTestOptions) {
					if (resultOfHIVTestValues.get(i) > 0) {
						Float percentage = new Float(100 * resultOfHIVTestValues.get(i) / res.size());
						pieDataset.setValue(s + " (" + resultOfHIVTestValues.get(i) + " , " + percentage + "%)", percentage);
					}
					i++;
				}
				concept = Context.getConceptService()
				        .getConcept(PMTCTConfigurationUtils.getHivTestInDeliveryRoomConceptId());
				log.info(">>>>>>>>>>>>>>GRAPHICS>>patientsTestedInDeliveryRoom>>>End");
			}
			catch (Exception ex) {
				log.error(">>>>>>>>>An error occured when trying to create piechart...");
				ex.printStackTrace();
			}
			
			title = appContext.getMessage("pmtct.menu.maternity", null, userContext.getLocale());
		}
		
		JFreeChart chart = ChartFactory.createPieChart(title + " : "
		        + ((null != concept) ? concept.getPreferredName(userContext.getLocale()) + dateInterval : descriptionTitle),
		    pieDataset, true, true, false);
		
		return chart;
	}
}
