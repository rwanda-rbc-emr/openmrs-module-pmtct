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
package org.openmrs.module.pmtct.db;

import java.util.List;

import org.openmrs.module.pmtct.util.PMTCTConstants;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public interface PmtctService {
	
//	public void setPMTCTProgrmaId(PMTCTConstants constants) throws Exception;
	
	public List<Object> getCurrentPatientsInPmtct() throws Exception;
	
	public List<Object> getCurrentPatientsInPmtct(String startDate, String endDate) throws Exception;
	
	public List<Object> getPatientsWhoCompletedPmtctProgram(String startDate, String endDate) throws Exception;
	
	public List<Object> getPatientsWhoCompletedPmtctProgramByDateOfCompletion(String startDate, String endDate)
	                                                                                                          throws Exception;
	
	public List<Object> getGeneralStatsForPatientsInCPN(String startDate, String endDate) throws Exception;
	
	public List<Object> getCouplesDiscordant( ) throws Exception;
	
	public List<Object> getPatientByMoisDeRapportageDPA(int month, int year ) throws Exception;
	
	public List<Object> getGeneralStatsForPatientsInMaternity(String startDate, String endDate) throws Exception;
	
	public List<Object> getExpectedPatientsInMaternity( ) throws Exception;

	public List<Object> getPatientsMissedTheirMaternityEncounter( ) throws Exception;
	
	public List<Object> getpatientsTestedInDeliveryRoom(String startDate, String endDate) throws Exception;
	
	public List<Object> getExpectedPatientsInPCR( ) throws Exception;
	
	public List<Object> getExpectedPatientsForSerologyAt9Months( ) throws Exception;
	
	public List<Object> getExpectedPatientsForSerologyAt18Months( ) throws Exception;
	
	public List<Object> getGeneralStatForInfantTests(String startDate, String endDate) throws Exception;
	
	/**
	 * Used to get data necessary to draw chart showing statistics on infant feeding methods
	 */
	@Transactional(readOnly = true)
	public List<Object> getGeneralStatForInfantTests_Charting_IFM(String startDate, String endDate) throws Exception;
	
	/**
	 * Used to get data necessary to draw chart showing statistics on Result of HIV Test at PCR Test
	 */
	@Transactional(readOnly = true)
	public List<Object> getGeneralStatForInfantTests_Charting_PCR(String startDate, String endDate) throws Exception;
	
	/**
	 * Used to get data necessary to draw chart showing statistics on Result of HIV Test at Serology
	 * 9 months Test
	 */
	@Transactional(readOnly = true)
	public List<Object> getGeneralStatForInfantTests_Charting_SerAt9Months(String startDate, String endDate)
	                                                                                                        throws Exception;
	
	/**
	 * Used to get data necessary to draw chart showing statistics on Result of HIV Test at Serology
	 * 18 months Test
	 */
	@Transactional(readOnly = true)
	public List<Object> getGeneralStatForInfantTests_Charting_SerAt18Months(String startDate, String endDate)
	                                                                                                         throws Exception;
	
	public List<Object> getInfantByMoisDeRapportageByInfantTest(int month, int year, int test) throws Exception;
}
