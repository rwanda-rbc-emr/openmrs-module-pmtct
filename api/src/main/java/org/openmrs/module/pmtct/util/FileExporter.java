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
package org.openmrs.module.pmtct.util;

import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.DrugOrder;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.PMTCTModuleTag;

/**
 *
 */
public class FileExporter {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private PMTCTModuleTag pmtctTag = new PMTCTModuleTag();
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportToCSVFile(HttpServletRequest request, HttpServletResponse response, List<Object> patientList,
	                            String filename, String title) throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No.,Identifier, Names, Gender, BirthDay, Enrollment Date, HIV Status");
			outputStream.println();
			
			int ids = 0;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + p.getGender() + "," + sdf.format(p.getBirthdate()) + "," + o[3].toString() + ","
				        + pmtctTag.lastObsValueByConceptId(p.getPatientId(), PMTCTConstants.RESULT_OF_HIV_TEST));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	public void exportToCSVFile2(HttpServletRequest request, HttpServletResponse response, List<Object> patientList,
	                             String filename, String title) throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No.,Identifier, Names, Gender, BirthDay, Enrollment Date");
			outputStream.println();
			
			int ids = 0;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				p = ps.getPatient(Integer.parseInt(o[1].toString()));
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + p.getGender() + "," + sdf.format(p.getBirthdate()) + "," + o[4].toString());
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportGeneralStatisticsInCPNToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                  List<Object> patientList, String filename, String title)
	                                                                                                          throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream
			        .println("No. ,Identifier, Names, Gender, BirthDay, Enrollment Date, CPN Date, HIV Status, DPA, Date of Confiment");
			outputStream.println();
			
			int ids = 0;
			
			Encounter cpnEnc;
			Encounter matEnc;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[1].toString()));
				cpnEnc = pmtctTag.getCPNEncounterInfo(p.getPatientId());
				matEnc = pmtctTag.getMaternityEncounterInfo(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + p.getGender() + "," + sdf.format(p.getBirthdate()) + "," + o[4].toString() + ","
				        + sdf.format(cpnEnc.getEncounterDatetime()) + ","
				        + pmtctTag.lastObsValueByConceptId(p.getPatientId(), PMTCTConstants.RESULT_OF_HIV_TEST) + ","
				        + pmtctTag.observationValueByConcept(cpnEnc, PMTCTConstants.PREGNANT_DUE_DATE) + ","
				        + pmtctTag.observationValueByConcept(matEnc, PMTCTConstants.DATE_OF_CONFINEMENT));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	public void exportGeneralStatisticsInMaternityToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                        List<Object> patientList, String filename, String title)
	                                                                                                                throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream
			        .println("No. ,Identifier, Names, Enrollment Date, DPA, Date of Confiment, HIV Status, Child Born Status");
			outputStream.println();
			
			int ids = 0;
			
			Encounter cpnEnc;
			Encounter matEnc;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				cpnEnc = pmtctTag.getCPNEncounterInfo(p.getPatientId());
				matEnc = pmtctTag.getMaternityEncounterInfo(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + o[3].toString() + ","
				        + pmtctTag.observationValueByConcept(cpnEnc, PMTCTConstants.PREGNANT_DUE_DATE) + ","
				        + pmtctTag.observationValueByConcept(matEnc, PMTCTConstants.DATE_OF_CONFINEMENT) + ","
				        + pmtctTag.lastObsValueByConceptId(p.getPatientId(), PMTCTConstants.RESULT_OF_HIV_TEST) + ","
				        + pmtctTag.observationValueByConcept(matEnc, PMTCTConfigurationUtils.getBornStatusConceptId()));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportExpectedPatientInMaternityToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                      List<Object> patientList, String filename, String title)
	                                                                                                              throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No. ,Identifier, Names, Enrollment Date, DPA, HIV Status, HIV Status of Partner");
			outputStream.println();
			
			int ids = 0;
			
			Encounter cpnEnc;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				cpnEnc = pmtctTag.getCPNEncounterInfo(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + o[3].toString() + ","
				        + pmtctTag.observationValueByConcept(cpnEnc, PMTCTConstants.PREGNANT_DUE_DATE) + ","
				        + pmtctTag.lastObsValueByConceptId(p.getPatientId(), PMTCTConstants.RESULT_OF_HIV_TEST) + ","
				        + pmtctTag.observationValueByConcept(cpnEnc, PMTCTConstants.TESTING_STATUS_OF_PARTNER));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportPatientTestedInDeliveryRoomToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                      List<Object> patientList, String filename, String title)
	                                                                                                              throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No. ,Identifier, Names, Enrollment Date, DPA, Delivery Date, Result of HIV in Delivery room");
			outputStream.println();
			
			int ids = 0;
			
			Encounter cpnEnc,matEnc;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				cpnEnc = pmtctTag.getCPNEncounterInfo(p.getPatientId());
				matEnc = pmtctTag.getMaternityEncounterInfo(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," + o[3].toString() + ","
				        + pmtctTag.observationValueByConcept(cpnEnc, PMTCTConstants.PREGNANT_DUE_DATE) + ","
				        + pmtctTag.observationValueByConcept(matEnc, PMTCTConstants.DATE_OF_CONFINEMENT) + ","
				        + pmtctTag.observationValueByConcept(matEnc, PMTCTConstants.RESULT_OF_HIV_TEST));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportInfantsTestResumeToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                      List<Object> patientList, String filename, String title)
	                                                                                                              throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			Patient mother;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No. ,Identifier, Names, Mother's Names, Gender, Birthdate, Infant Feeding method, PCR Test Result, Ser. Test 9 months Result, Ser. Test 18 months Result");
			outputStream.println();
			
			int ids = 0;
			
			Encounter cpnEnc;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				mother=pmtctTag.getChildMother(p.getPatientId());
				
				cpnEnc = pmtctTag.getCPNEncounterInfo(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," +mother.getPersonName()+ "," + p.getGender()+ "," + sdf.format(p.getBirthdate())+ "," 
				        + pmtctTag.lastObsValueByConceptId(p.getPatientId(), PMTCTConstants.INFANT_FEEDING_METHOD) + ","
				        + pmtctTag.getConceptNameById(""+o[4]) + ","
				        + pmtctTag.getConceptNameById(""+o[5]) + ","
				        + pmtctTag.getConceptNameById(""+o[6]));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param patientList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportInfantsTestToCSVFile(HttpServletRequest request, HttpServletResponse response,
	                                                      List<Object> patientList, String filename, String title)
	                                                                                                              throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p;
			Patient mother;
			PatientService ps = Context.getPatientService();
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println("Number of Patients: " + patientList.size());
			outputStream.println();
			outputStream.println("No. ,Identifier, Names, Mother's Names, Gender, Birthdate, Scheduled Visit");
			outputStream.println();
			
			int ids = 0;
			
			for (Object patient : patientList) {
				Object[] o = (Object[]) patient;
				
				p = ps.getPatient(Integer.parseInt(o[0].toString()));
				mother=pmtctTag.getChildMother(p.getPatientId());
				
				ids += 1;
				outputStream.println(ids + "," + p.getActiveIdentifiers().get(0).getIdentifier() + "," + p.getPersonName()
				        + "," +mother.getPersonName()+ "," + p.getGender()+ "," + sdf.format(p.getBirthdate())+ ","
				        + sdf.format(o[4]));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @param request
	 * @param response
	 * @param drugOrderList
	 * @param filename
	 * @param title
	 * @throws Exception
	 */
	public void exportDrugToCSVFile(HttpServletRequest request, HttpServletResponse response, List<DrugOrder> drugOrderList,
	                                String filename, String title) throws Exception {
		ServletOutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			Patient p = (drugOrderList != null) ? drugOrderList.get(0).getPatient() : null;
			int count = 0;
			
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
			outputStream.println("" + title);
			outputStream.println();
			
			if (null != p) {
				outputStream.println("Identifier, " + p.getActiveIdentifiers().get(0).getIdentifier());
				outputStream.println("Names, " + p.getPersonName());
				outputStream.println("Gender, " + p.getGender());
			}
			outputStream.println();
			
			outputStream.println("No., Item ordered, Dose/Units, Frequency, Instructions, Start date, Discontinued date");
			outputStream.println();
			for (DrugOrder drugOrder : drugOrderList) {
				count += 1;
				outputStream.println(count + ", " + drugOrder.getDrug().getName() + ", " + drugOrder.getDose() + " "
				        + drugOrder.getUnits() + ", " + drugOrder.getFrequency() + ", " + drugOrder.getInstructions() + ", "
				        + sdf.format(drugOrder.getStartDate()) + ", " + sdf.format(drugOrder.getDiscontinuedDate()));
			}
			
			outputStream.flush();
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		finally {
			if (null != outputStream)
				outputStream.close();
		}
	}
}
