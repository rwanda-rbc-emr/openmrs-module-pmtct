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
package org.openmrs.module.pmtct.db.hibernate;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.pmtct.db.PmtctService;
import org.openmrs.module.pmtct.util.PMTCTConfigurationUtils;
import org.openmrs.module.pmtct.util.PMTCTConstants;

/**
 * @author Yves GAKUBA
 */
public class HibernatePmtctDAO implements PmtctService {
	
	private SessionFactory sessionFactory;
	
	private Log log = LogFactory.getLog(HibernatePmtctDAO.class);
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	private Session getSession() {
		if (getSessionFactory().isClosed())
			log.info(">>>>PMTCT_DAO>> sessionFactory is closed!");
		Session session = getSessionFactory().getCurrentSession();
		if (session == null) {
			Context.closeSession();
			Context.openSession();
			try {
				session = getSessionFactory().getCurrentSession();
			}
			catch (Exception e) {
				log.error(">>>>>>>>PMTCT_DAO>> Session Error : " + session);
				e.printStackTrace();
			}
		}
		return session;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getCurrentPatientsInPmtct(java.lang.String)
	 */
	public List<Object> getCurrentPatientsInPmtct() {
		String query = "";
		List<Object> results = null;
		try {
			query = "SELECT pt.patient_id, p.gender, p.birthdate, "
			        + "CAST(pp.date_enrolled AS DATE) AS date_enrolled FROM patient pt INNER JOIN patient_program pp ON pt.patient_id = pp.patient_id "
			        + "INNER JOIN person p ON pt.patient_id = p.person_id INNER JOIN person_name pn ON pt.patient_id = pn.person_id "
			        + "WHERE p.dead = 0 AND p.voided = 0 AND pp.voided = 0 AND pt.voided=0 AND pn.voided = 0 AND pp.date_enrolled IS NOT NULL AND pp.date_completed IS NULL AND pp.program_id ="
			        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
			
			results = execute(query);
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
		}
		
		return results;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getCurrentPatientsInPmtct(java.lang.String,
	 *      java.lang.String)
	 */
	public List<Object> getCurrentPatientsInPmtct(String startDate, String endDate) throws Exception {
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		String query = "";
		try {
			query = "SELECT pt.patient_id, p.gender, p.birthdate, "
			        + "CAST(pp.date_enrolled AS DATE) AS date_enrolled FROM patient pt INNER JOIN patient_program pp ON pt.patient_id = pp.patient_id "
			        + "INNER JOIN person p ON pt.patient_id = p.person_id INNER JOIN person_name pn ON pt.patient_id = pn.person_id "
			        + "WHERE p.dead  = 0 AND p.voided = 0 AND pp.voided = 0 AND pt.voided = 0 AND pn.voided = 0 AND pp.program_id ="
			        + PMTCTConfigurationUtils.getPmtctProgramId() + " AND CAST(pp.date_enrolled AS DATE)>= '"
			        + df.format(Context.getDateFormat().parse(startDate)) + "' AND CAST(pp.date_enrolled AS DATE)<= '"
			        + df.format(Context.getDateFormat().parse(endDate)) + "' ORDER BY pp.date_enrolled, pn.given_name";
			
			return execute(query);
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws ParseException
	 * @see org.openmrs.module.pmtct.db.PmtctService#getExpectedPatientsInCPN(java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatsForPatientsInCPN(String startDate, String endDate) throws Exception {
		List<Object> listOfPatientWithCPNEnc;
		String query = "";
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		try {
			query = "SELECT distinct(e.encounter_id),pt.patient_id,p.gender,p.birthdate, CAST(pp.date_enrolled AS DATE) AS date_enrolled "
			        + "FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
			        + "INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
			        + "INNER JOIN encounter e ON pt.patient_id=e.patient_id "
			        + "WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND e.voided=0 "
			        + "AND pp.program_id="
			        + PMTCTConfigurationUtils.getPmtctProgramId()
			        + " AND e.encounter_type="
			        + PMTCTConfigurationUtils.getCPNEncounterTypeId()
			        + " AND CAST(e.encounter_datetime AS DATE)<= '"
			        + df.format(Context.getDateFormat().parse(endDate))
			        + "' AND CAST(e.encounter_datetime AS DATE)>= '"
			        + df.format(Context.getDateFormat().parse(startDate))
			        + "' ORDER BY pp.date_enrolled,pt.patient_id, pn.given_name";
			
			listOfPatientWithCPNEnc = execute(query);
			
			return listOfPatientWithCPNEnc;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getCouplesDiscordant()
	 */
	@Override
	public List<Object> getCouplesDiscordant() throws Exception {
		List<Object> couplesDiscordant = new ArrayList<Object>();
		List<Object> partnerInfected;
		List<Object> patientNotInfected;
		String query = "", query1 = "";
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		try {
			for (Object patient : currentPatientInPMTCT) {
				Object[] o = (Object[]) patient;
				partnerInfected = new ArrayList<Object>();
				
				query = "SELECT o.obs_id FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id "
				        + "WHERE o.voided=0 AND o.concept_id=" + PMTCTConstants.RESULT_OF_HIV_TEST + " AND o.value_coded="
				        + PMTCTConstants.NEGATIVE + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getCPNEncounterTypeId() + " AND e.encounter_datetime >= '"
				        + o[3].toString() + "' AND o.person_id=" + o[0].toString();
				
				patientNotInfected = execute(query);
				
				if (patientNotInfected.size() > 0) {
					query1 = "SELECT o.obs_id FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id "
					        + "WHERE o.voided=0 AND o.concept_id=" + PMTCTConstants.TESTING_STATUS_OF_PARTNER
					        + " AND o.value_coded=" + PMTCTConstants.POSITIVE + " AND e.voided=0 AND e.encounter_type="
					        + PMTCTConfigurationUtils.getCPNEncounterTypeId() + " AND e.encounter_datetime >= '"
					        + o[3].toString() + "' AND o.person_id=" + o[0].toString();
					
					partnerInfected = execute(query1);
				}
				
				if (partnerInfected.size() > 0)
					couplesDiscordant.add(o);
			}
			
			return couplesDiscordant;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " | " + query1 + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getPatientByMoisDeRapportageDPA(int, int)
	 */
	@Override
	public List<Object> getPatientByMoisDeRapportageDPA(int month, int year) throws Exception {
		List<Object> patientList = new ArrayList<Object>();
		List<Object> patientFound;
		String query = "";
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		try {
			for (Object patient : currentPatientInPMTCT) {
				Object[] o = (Object[]) patient;
				
				query = "SELECT o.obs_id FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id "
				        + " WHERE o.voided=0 AND o.concept_id=" + PMTCTConstants.PREGNANT_DUE_DATE
				        + " AND (SELECT MONTH(o.value_datetime)+1)=" + month + " AND (SELECT YEAR(o.value_datetime))="
				        + year + " AND e.voided=0 AND e.encounter_type=" + PMTCTConfigurationUtils.getCPNEncounterTypeId()
				        + " AND e.encounter_datetime >= '" + o[3].toString() + "' AND o.person_id=" + o[0].toString();
				
				patientFound = execute(query);
				
				if (patientFound.size() > 0)
					patientList.add(o);
			}
			
			return patientList;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getExpectedPatientsInMaternity()
	 */
	@Override
	public List<Object> getExpectedPatientsInMaternity() throws Exception {
		List<Object> expectedPatientInMaternity = new ArrayList<Object>();
		List<Object> listOfPatientWithCPNEnc;
		List<Object> listOfPatientWithMaternityEnc;
		String query = "", query1 = "";
		List<Object> currentPatientInPMTCT = null;
		
		try {
			currentPatientInPMTCT = getCurrentPatientsInPmtct();
//			log.info(">>>>>>>>>>>>>>>> currentPatientInPMTCT "+currentPatientInPMTCT);
			for (Object patient : currentPatientInPMTCT) {
//				log.info(">>>>>>>>>>>> Object : "+patient);
				Object[] o = (Object[]) patient;
//				log.info(">>>>>>>>>>>> Casted Object : "+o);
				listOfPatientWithCPNEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = null;
				
				for(Object ob:o)
					log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "+ob);
				
				log.info(">>>>>>>>>>>>>>> Enctype "+PMTCTConfigurationUtils.getCPNEncounterTypeId());
				
				query = "SELECT e.patient_id FROM encounter e " + "WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getCPNEncounterTypeId();
				
				log.info(">>>>>>>>>> QUERY 1 : "+query);
				
				listOfPatientWithCPNEnc = execute(query);
				
				if (listOfPatientWithCPNEnc.size() > 0) {
					query1 = "SELECT e.patient_id FROM encounter e " + "WHERE e.encounter_datetime >= '" + o[3].toString()
					        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
					        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
					
					log.info(">>>>>>>>>> QUERY 2 : "+query1);
					
					listOfPatientWithMaternityEnc = new ArrayList<Object>();
					listOfPatientWithMaternityEnc = execute(query1);
				}
				
				if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() == 0)
					expectedPatientInMaternity.add(o);
			}
			
			return expectedPatientInMaternity;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " | " + query1 + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws ParseException
	 * @see org.openmrs.module.pmtct.db.PmtctService#getPatientsWhoCompletedPmtctProgram() A patient
	 *      is set to be in this group, if the date_enrolled is between startDate and endDate <br/>
	 *      (<i>startDate >= date_enrolled >= endDate</i>) <br/>
	 *      and has a date_completed for the program
	 */
	@Override
	public List<Object> getPatientsWhoCompletedPmtctProgram(String startDate, String endDate) throws Exception {
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		String query = "";
		
		try {
			query = "SELECT pt.patient_id,p.gender,p.birthdate, "
			        + "CAST(pp.date_enrolled AS DATE) AS date_enrolled, CAST(pp.date_completed AS DATE) AS date_completed FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
			        + "INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
			        + "WHERE (CAST(pp.date_enrolled AS DATE))>'"
			        + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'"
			        + df.format(Context.getDateFormat().parse(endDate))
			        + "' AND p.dead=0 AND pp.voided=0 AND pt.voided=0 AND pt.voided=0 AND pp.date_completed IS NOT NULL AND pp.program_id="
			        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
			
			return execute(query);
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getPatientsWhoCompletedPmtctProgramByDateOfCompletion(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getPatientsWhoCompletedPmtctProgramByDateOfCompletion(String startDate, String endDate)
	                                                                                                           throws Exception {
		
		String query = "";
		
		try {
			query = "SELECT pt.patient_id,p.gender,p.birthdate, "
			        + "CAST(pp.date_enrolled AS DATE) AS date_enrolled, CAST(pp.date_completed AS DATE) AS date_completed FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
			        + "INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
			        + "WHERE (CAST(pp.date_completed AS DATE))>='"
			        + startDate
			        + "' AND (CAST(pp.date_completed AS DATE))<='"
			        + endDate
			        + "' AND p.dead=0 AND pp.voided=0 AND pt.voided=0 AND pt.voided=0 AND pp.date_completed IS NOT NULL AND pp.program_id="
			        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
			
			return execute(query);
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatsForPatientsInMaternity()
	 */
	@Override
	public List<Object> getGeneralStatsForPatientsInMaternity(String startDate, String endDate) throws Exception {
		List<Object> patientCompletedPMTCTWithMaternityEnc = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query = "";
		List<Object> patientsWhoCompletedPMTCT = getPatientsWhoCompletedPmtctProgramByDateOfCompletion(startDate, endDate);
		
		try {
			for (Object patient : patientsWhoCompletedPMTCT) {
				Object[] o = (Object[]) patient;
				listOfPatientWithMaternityEnc = null;
				
				query = "SELECT distinct e.patient_id FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id "
				        + "WHERE o.concept_id=" + PMTCTConstants.DATE_OF_CONFINEMENT + " AND e.encounter_datetime >= '"
				        + o[3].toString() + "' AND e.encounter_datetime <='" + o[4].toString() + "' AND e.patient_id="
				        + o[0].toString() + " AND e.voided=0 AND o.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithMaternityEnc = execute(query);
				
				if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0)
					patientCompletedPMTCTWithMaternityEnc.add(o);
			}
			
			return patientCompletedPMTCTWithMaternityEnc;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getExpectedPatientsInPCR()
	 */
	@Override
	public List<Object> getExpectedPatientsInPCR() throws Exception {
		List<Object> expectedPatientInPCR = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		List<Object> listOfPatientWithCPNEnc;
		List<Object> listOfPatientWithPCREnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			listOfPatientWithCPNEnc = new ArrayList<Object>();
			listOfPatientWithPCREnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getCPNEncounterTypeId();
			
			listOfPatientWithCPNEnc = execute(query);
			
			if (listOfPatientWithCPNEnc == null || listOfPatientWithCPNEnc.size() == 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithMaternityEnc = execute(query);
				
				if (listOfPatientWithMaternityEnc.size() > 0) {
					query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
					        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
					        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
					
					listOfPatientWithPCREnc = new ArrayList<Object>();
					listOfPatientWithPCREnc = execute(query);
				}
			}
			
			if (listOfPatientWithPCREnc != null && listOfPatientWithPCREnc.size() == 0) {
				
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithPCREnc = new ArrayList<Object>();
				listOfPatientWithPCREnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = listOfPatientWithPCREnc.get(0);
				ob[o.length] = returnVisitdate;
				
				expectedPatientInPCR.add(ob);
			}
		}
		
		return expectedPatientInPCR;
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getExpectedPatientsForSerologyAt9Months()
	 */
	@Override
	public List<Object> getExpectedPatientsForSerologyAt9Months() throws Exception {
		List<Object> expectedPatientInSerology9Month = new ArrayList<Object>();
		List<Object> listOfPatientWithPCREnc;
		List<Object> listOfPatientWithSerology9MEnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithPCREnc = new ArrayList<Object>();
			listOfPatientWithSerology9MEnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
			
			listOfPatientWithPCREnc = execute(query);
			
			if (listOfPatientWithPCREnc.size() > 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
				
				listOfPatientWithSerology9MEnc = new ArrayList<Object>();
				listOfPatientWithSerology9MEnc = execute(query);
			}
			
			if (listOfPatientWithSerology9MEnc != null && listOfPatientWithSerology9MEnc.size() == 0) {
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
				
				listOfPatientWithSerology9MEnc = new ArrayList<Object>();
				listOfPatientWithSerology9MEnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = listOfPatientWithSerology9MEnc.get(0);
				ob[o.length] = returnVisitdate;
				
				expectedPatientInSerology9Month.add(ob);
			}
		}
		
		return expectedPatientInSerology9Month;
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getExpectedPatientsForSerologyAt18Months()
	 */
	@Override
	public List<Object> getExpectedPatientsForSerologyAt18Months() throws Exception {
		List<Object> expectedPatientInSerology18Month = new ArrayList<Object>();
		List<Object> listOfPatientWithSerology9MEnc;
		List<Object> listOfPatientWithSerology18MEnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithSerology9MEnc = new ArrayList<Object>();
			listOfPatientWithSerology18MEnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
			
			listOfPatientWithSerology9MEnc = execute(query);
			
			if (listOfPatientWithSerology9MEnc.size() > 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId();
				
				listOfPatientWithSerology18MEnc = new ArrayList<Object>();
				listOfPatientWithSerology18MEnc = execute(query);
			}
			
			if (listOfPatientWithSerology18MEnc != null && listOfPatientWithSerology18MEnc.size() == 0) {
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
				
				listOfPatientWithSerology18MEnc = new ArrayList<Object>();
				listOfPatientWithSerology18MEnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = listOfPatientWithSerology18MEnc.get(0);
				ob[o.length] = returnVisitdate;
				expectedPatientInSerology18Month.add(ob);
			}
		}
		
		return expectedPatientInSerology18Month;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatForInfantTests(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatForInfantTests(String startDate, String endDate) throws Exception {
		List<Object> children = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> patientInPMTCT = new ArrayList<Object>();
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT pt.patient_id,p.gender,p.birthdate, "
		        + " CAST(pp.date_enrolled AS DATE) AS date_enrolled FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
		        + " INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
		        + " WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND pp.program_id="
		        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
		
		patientInPMTCT = execute(query);
		
		for (Object patient : patientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			
			query = "SELECT e.patient_id FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id INNER JOIN "
			        + " patient_program pp ON p.person_id=pp.patient_id WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getMaternityEncounterTypeId() + " AND e.encounter_datetime = p.birthdate "
			        + " AND (CAST(pp.date_enrolled AS DATE))>'" + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'" + df.format(Context.getDateFormat().parse(endDate)) + "'";
			
			listOfPatientWithMaternityEnc = execute(query);
			
			if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) {
				Object[] ob = new Object[o.length + 3];
				
				//-----------PCR----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				//-----------Ser. 9 months----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length + 1] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				//-----------Ser. 18 months----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length + 2] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				children.add(ob);
			}
		}
		
		return children;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatForInfantTests_Charting_IFM(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatForInfantTests_Charting_IFM(String startDate, String endDate) throws SQLException,
	                                                                                               Exception {
		List<Object> children = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> patientInPMTCT = new ArrayList<Object>();
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT pt.patient_id, CAST(pp.date_enrolled AS DATE) AS date_enrolled "
		        + " FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
		        + " INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
		        + " WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND pp.program_id="
		        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
		
		patientInPMTCT = execute(query);
		
		for (Object patient : patientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			
			query = "SELECT e.patient_id FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id INNER JOIN "
			        + " patient_program pp ON p.person_id=pp.patient_id WHERE e.encounter_datetime >= '" + o[1].toString()
			        + "' AND e.patient_id=" + (Integer) o[0] + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getMaternityEncounterTypeId() + " AND e.encounter_datetime = p.birthdate "
			        + " AND (CAST(pp.date_enrolled AS DATE))>'" + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'" + df.format(Context.getDateFormat().parse(endDate)) + "'";
			
			listOfPatientWithMaternityEnc = execute(query);
			
			if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) {
				children.add(o);
			}
		}
		
		return children;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatForInfantTests_Charting_PCR(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatForInfantTests_Charting_PCR(String startDate, String endDate) throws SQLException,
	                                                                                               Exception {
		List<Object> children = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> patientInPMTCT = new ArrayList<Object>();
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT pt.patient_id, CAST(pp.date_enrolled AS DATE) AS date_enrolled "
		        + " FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
		        + " INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
		        + " WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND pp.program_id="
		        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
		
		patientInPMTCT = execute(query);
		
		for (Object patient : patientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			
			query = "SELECT e.patient_id FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id INNER JOIN "
			        + " patient_program pp ON p.person_id=pp.patient_id WHERE (CAST(e.encounter_datetime AS DATE)) >= '"
			        + o[1].toString() + "' AND e.patient_id=" + (Integer) o[0] + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getMaternityEncounterTypeId() + " AND e.encounter_datetime = p.birthdate "
			        + " AND (CAST(pp.date_enrolled AS DATE))>'" + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'" + df.format(Context.getDateFormat().parse(endDate)) + "'";
			
			listOfPatientWithMaternityEnc = execute(query);
			
			if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) {
				Object[] ob = new Object[o.length + 1];
				
				//-----------PCR----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[1]
				        + "' AND e.patient_id="
				        + o[0]
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type=" + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				children.add(ob);
			}
		}
		
		return children;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatForInfantTests_Charting_SerAt9Months(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatForInfantTests_Charting_SerAt9Months(String startDate, String endDate)
	                                                                                                        throws Exception {
		List<Object> children = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> patientInPMTCT = new ArrayList<Object>();
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT pt.patient_id, CAST(pp.date_enrolled AS DATE) AS date_enrolled "
		        + " FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
		        + " INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
		        + " WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND pp.program_id="
		        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
		
		patientInPMTCT = execute(query);
		
		for (Object patient : patientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			
			query = "SELECT e.patient_id FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id INNER JOIN "
			        + " patient_program pp ON p.person_id=pp.patient_id WHERE e.encounter_datetime >= '" + o[1].toString()
			        + "' AND e.patient_id=" + (Integer) o[0] + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getMaternityEncounterTypeId() + " AND e.encounter_datetime = p.birthdate "
			        + " AND (CAST(pp.date_enrolled AS DATE))>'" + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'" + df.format(Context.getDateFormat().parse(endDate)) + "'";
			
			listOfPatientWithMaternityEnc = execute(query);
			
			if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) {
				Object[] ob = new Object[o.length + 1];
				
				//-----------Ser. 9 months----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[1]
				        + "' AND e.patient_id="
				        + o[0]
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				Object returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				children.add(ob);
			}
		}
		
		return children;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getGeneralStatForInfantTests_Charting_SerAt18Months(java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public List<Object> getGeneralStatForInfantTests_Charting_SerAt18Months(String startDate, String endDate)
	                                                                                                         throws Exception {
		List<Object> children = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> patientInPMTCT = new ArrayList<Object>();
		SimpleDateFormat df = MohTracUtil.getMySQLDateFormat();
		
		query = "SELECT pt.patient_id, CAST(pp.date_enrolled AS DATE) AS date_enrolled "
		        + " FROM patient pt INNER JOIN patient_program pp ON pt.patient_id=pp.patient_id "
		        + " INNER JOIN person p ON pt.patient_id=p.person_id INNER JOIN person_name pn ON pt.patient_id=pn.person_id "
		        + " WHERE p.dead=0 AND p.voided=0 AND pp.voided=0 AND pt.voided=0 AND pn.voided=0 AND pp.program_id="
		        + PMTCTConfigurationUtils.getPmtctProgramId() + " ORDER BY pp.date_enrolled, pn.given_name";
		
		patientInPMTCT = execute(query);
		
		for (Object patient : patientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			
			query = "SELECT e.patient_id FROM encounter e INNER JOIN person p ON e.patient_id = p.person_id INNER JOIN "
			        + " patient_program pp ON p.person_id=pp.patient_id WHERE e.encounter_datetime >= '" + o[1].toString()
			        + "' AND e.patient_id=" + (Integer) o[0] + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getMaternityEncounterTypeId() + " AND e.encounter_datetime = p.birthdate "
			        + " AND (CAST(pp.date_enrolled AS DATE))>'" + df.format(Context.getDateFormat().parse(startDate))
			        + "' AND (CAST(pp.date_enrolled AS DATE))<'" + df.format(Context.getDateFormat().parse(endDate)) + "'";
			
			listOfPatientWithMaternityEnc = execute(query);
			
			if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) {
				Object[] ob = new Object[o.length + 1];
				
				//-----------Ser. 18 months----------------
				query = "SELECT o.value_coded FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[1]
				        + "' AND e.patient_id="
				        + o[0]
				        + " AND o.concept_id="
				        + PMTCTConstants.RESULT_OF_HIV_TEST
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
				
				Object returnVisitdate = (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0) ? listOfPatientWithMaternityEnc
				        .get(0)
				        : 0;
				ob[o.length] = (returnVisitdate == null) ? 0 : returnVisitdate;
				
				children.add(ob);
			}
		}
		
		return children;
	}
	
	/**
	 * @see org.openmrs.module.pmtct.db.PmtctService#getInfantByMoisDeRapportageByInfantTest(java.lang.String,
	 *      java.lang.String, int)
	 */
	@Override
	public List<Object> getInfantByMoisDeRapportageByInfantTest(int month, int year, int test) throws Exception {
		
		List<Object> res = new ArrayList<Object>();
		
		switch (test) {
			case 1:
				res = getInfantByMoisDeRappForPCR(month, year);
				break;
			case 2:
				res = getInfantByMoisDeRappForSero9Month(month, year);
				break;
			case 3:
				res = getInfantByMoisDeRappForSero18Month(month, year);
				break;
			
			default:
				res = new ArrayList<Object>();
				break;
		}
		
		return res;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param month
	 * @param year
	 * @return
	 * @throws Exception
	 */
	private List<Object> getInfantByMoisDeRappForPCR(int month, int year) throws Exception {
		List<Object> expectedPatientInPCR = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		List<Object> listOfPatientWithCPNEnc;
		List<Object> listOfPatientWithPCREnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithMaternityEnc = new ArrayList<Object>();
			listOfPatientWithCPNEnc = new ArrayList<Object>();
			listOfPatientWithPCREnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getCPNEncounterTypeId();
			
			listOfPatientWithCPNEnc = execute(query);
			
			if (listOfPatientWithCPNEnc == null || listOfPatientWithCPNEnc.size() == 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithMaternityEnc = execute(query);
				
				if (listOfPatientWithMaternityEnc.size() > 0) {
					query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
					        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
					        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
					
					listOfPatientWithPCREnc = new ArrayList<Object>();
					listOfPatientWithPCREnc = execute(query);
				}
			}
			
			if (listOfPatientWithPCREnc != null && listOfPatientWithPCREnc.size() == 0) {
				
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId()
				        + " AND (SELECT MONTH(o.value_datetime)+1)="
				        + month + " AND (SELECT YEAR(o.value_datetime))=" + year;
				
				listOfPatientWithPCREnc = new ArrayList<Object>();
				listOfPatientWithPCREnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = (listOfPatientWithPCREnc.size() > 0) ? listOfPatientWithPCREnc.get(0) : null;
				ob[o.length] = returnVisitdate;
				
				if (listOfPatientWithPCREnc.size() > 0)
					expectedPatientInPCR.add(ob);
			}
		}
		
		return expectedPatientInPCR;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param month
	 * @param year
	 * @return
	 * @throws Exception
	 */
	private List<Object> getInfantByMoisDeRappForSero9Month(int month, int year) throws Exception {
		List<Object> expectedPatientInSerology9Month = new ArrayList<Object>();
		List<Object> listOfPatientWithPCREnc;
		List<Object> listOfPatientWithSerology9MEnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithPCREnc = new ArrayList<Object>();
			listOfPatientWithSerology9MEnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId();
			
			listOfPatientWithPCREnc = execute(query);
			
			if (listOfPatientWithPCREnc.size() > 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
				
				listOfPatientWithSerology9MEnc = new ArrayList<Object>();
				listOfPatientWithSerology9MEnc = execute(query);
			}
			
			if (listOfPatientWithSerology9MEnc != null && listOfPatientWithSerology9MEnc.size() == 0) {
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getPCRTestEncounterTypeId()
				        + " AND (SELECT MONTH(o.value_datetime)+1)="
				        + month + " AND (SELECT YEAR(o.value_datetime))=" + year;
				
				listOfPatientWithSerology9MEnc = new ArrayList<Object>();
				listOfPatientWithSerology9MEnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = (listOfPatientWithSerology9MEnc.size() > 0) ? listOfPatientWithSerology9MEnc.get(0)
				        : null;
				ob[o.length] = returnVisitdate;
				
				if (listOfPatientWithSerology9MEnc.size() > 0)
					expectedPatientInSerology9Month.add(ob);
			}
		}
		
		return expectedPatientInSerology9Month;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param month
	 * @param year
	 * @return
	 * @throws Exception
	 */
	private List<Object> getInfantByMoisDeRappForSero18Month(int month, int year) throws Exception {
		List<Object> expectedPatientInSerology18Month = new ArrayList<Object>();
		List<Object> listOfPatientWithSerology9MEnc;
		List<Object> listOfPatientWithSerology18MEnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithSerology9MEnc = new ArrayList<Object>();
			listOfPatientWithSerology18MEnc = null;
			
			query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId();
			
			listOfPatientWithSerology9MEnc = execute(query);
			
			if (listOfPatientWithSerology9MEnc.size() > 0) {
				query = "SELECT e.patient_id FROM encounter e WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology18MonthEncounterTypeId();
				
				listOfPatientWithSerology18MEnc = new ArrayList<Object>();
				listOfPatientWithSerology18MEnc = execute(query);
			}
			
			if (listOfPatientWithSerology18MEnc != null && listOfPatientWithSerology18MEnc.size() == 0) {
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id WHERE e.encounter_datetime >= '"
				        + o[3].toString()
				        + "' AND e.patient_id="
				        + o[0].toString()
				        + " AND o.concept_id="
				        + PMTCTConstants.RETURN_VISIT_DATE
				        + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getSerology9MonthEncounterTypeId()
				        + " AND (SELECT MONTH(o.value_datetime)+1)="
				        + month
				        + " AND (SELECT YEAR(o.value_datetime))="
				        + year;
				
				listOfPatientWithSerology18MEnc = new ArrayList<Object>();
				listOfPatientWithSerology18MEnc = execute(query);
				
				Object[] ob = new Object[o.length + 1];
				
				for (int i = 0; i < o.length; i++)
					ob[i] = o[i];
				
				Object returnVisitdate = (listOfPatientWithSerology18MEnc.size() > 0) ? listOfPatientWithSerology18MEnc
				        .get(0) : null;
				ob[o.length] = returnVisitdate;
				
				if (listOfPatientWithSerology18MEnc.size() > 0)
					expectedPatientInSerology18Month.add(ob);
			}
		}
		
		return expectedPatientInSerology18Month;
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getpatientsTestedInWorkroom()
	 */
	@Override
	public List<Object> getpatientsTestedInDeliveryRoom(String startDate, String endDate) throws Exception {
		List<Object> patientCompletedPMTCTWithMaternityEnc = new ArrayList<Object>();
		List<Object> listOfPatientWithMaternityEnc;
		String query = "";
		List<Object> patientsWhoCompletedPMTCT = getPatientsWhoCompletedPmtctProgramByDateOfCompletion(startDate, endDate);
		//log.info(">>>>>>>>>>>>>>>>>>>>>>>patientsWhoCompletedPMTCT >> " + patientsWhoCompletedPMTCT.size());
		
		try {
			for (Object patient : patientsWhoCompletedPMTCT) {
				Object[] o = (Object[]) patient;
				listOfPatientWithMaternityEnc = null;
				
				query = "SELECT DISTINCT e.patient_id FROM encounter e INNER JOIN obs o ON e.encounter_id=o.encounter_id "
				        + "WHERE o.concept_id=" + PMTCTConfigurationUtils.getHivTestInDeliveryRoomConceptId()
				        + " AND o.value_numeric=1 AND e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.encounter_datetime <='" + o[4].toString() + "' AND e.patient_id=" + o[0].toString()
				        + " AND e.voided=0 AND o.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithMaternityEnc = execute(query);
				
				if (listOfPatientWithMaternityEnc != null && listOfPatientWithMaternityEnc.size() > 0)
					patientCompletedPMTCTWithMaternityEnc.add(o);
			}
			
			return patientCompletedPMTCTWithMaternityEnc;
		}
		catch (Exception e) {
			log.error(">>>>>>>PMTCT_DAO>>>>>An error occured trying to execute [ " + query + " ] ", e);
			return null;
		}
	}
	
	/**
	 * @throws Exception
	 * @see org.openmrs.module.pmtct.db.PmtctService#getPatientsMissedTheirMaternityEncounter()
	 */
	@Override
	public List<Object> getPatientsMissedTheirMaternityEncounter() throws Exception {
		List<Object> expectedPatientInMaternity = new ArrayList<Object>();
		List<Object> listOfPatientWithCPNEnc, listOfPatientsWhoMissedTheirMaternityEnc;
		List<Object> listOfPatientWithMaternityEnc;
		String query;
		List<Object> currentPatientInPMTCT = getCurrentPatientsInPmtct();
		
		for (Object patient : currentPatientInPMTCT) {
			Object[] o = (Object[]) patient;
			listOfPatientWithCPNEnc = new ArrayList<Object>();
			listOfPatientWithMaternityEnc = null;
			
			query = "SELECT e.patient_id FROM encounter e " + "WHERE e.encounter_datetime >= '" + o[3].toString()
			        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
			        + PMTCTConfigurationUtils.getCPNEncounterTypeId();
			
			listOfPatientWithCPNEnc = execute(query);
			
			if (listOfPatientWithCPNEnc.size() > 0) {
				query = "SELECT e.patient_id FROM encounter e " + "WHERE e.encounter_datetime >= '" + o[3].toString()
				        + "' AND e.patient_id=" + o[0].toString() + " AND e.voided=0 AND e.encounter_type="
				        + PMTCTConfigurationUtils.getMaternityEncounterTypeId();
				
				listOfPatientWithMaternityEnc = new ArrayList<Object>();
				listOfPatientWithMaternityEnc = execute(query);
			}
			
			if (listOfPatientWithCPNEnc.size() > 0 && listOfPatientWithMaternityEnc != null
			        && listOfPatientWithMaternityEnc.size() == 0) {
				
				query = "SELECT o.value_datetime FROM encounter e INNER JOIN obs o ON o.encounter_id=e.encounter_id "
				        + "WHERE e.encounter_datetime >= '" + o[3].toString() + "' AND e.patient_id=" + o[0].toString()
				        + " AND e.voided=0 AND e.encounter_type=" + PMTCTConfigurationUtils.getCPNEncounterTypeId()
				        + " AND o.concept_id=" + PMTCTConstants.PREGNANT_DUE_DATE;
				listOfPatientsWhoMissedTheirMaternityEnc = new ArrayList<Object>();
				listOfPatientsWhoMissedTheirMaternityEnc = execute(query);
				
				if (listOfPatientsWhoMissedTheirMaternityEnc != null && listOfPatientsWhoMissedTheirMaternityEnc.size() > 0) {
					Object ob = (Object) listOfPatientsWhoMissedTheirMaternityEnc.get(0);
					if (((Date) (ob)).compareTo(new Date()) < 0)
						expectedPatientInMaternity.add(o);
				}
			}
		}
		
		return expectedPatientInMaternity;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Object> execute(String sql) {
		SQLQuery query = getSession().createSQLQuery(sql);
		return query.list();
	}
}
