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
package org.openmrs.module.pmtct;

import java.util.Date;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.User;

/**
 * @author Yves
 */
public class PmtctCPNInformation {
	
	/**
	 * Gets the Encounter datetime
	 * 
	 * @return the cpnDate
	 */
	public Date getCpnDate() {
		return cpnDate;
	}
	
	/**
	 * @param cpnDate the cpnDate to set
	 */
	public void setCpnDate(Date cpnDate) {
		this.cpnDate = cpnDate;
	}
	
	/**
	 * @return the cpnNumber
	 */
	public String getCpnNumber() {
		return cpnNumber;
	}
	
	/**
	 * @param cpnNumber the cpnNumber to set
	 */
	public void setCpnNumber(String cpnNumber) {
		this.cpnNumber = cpnNumber;
	}
	
	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * @param location the location to set
	 */
	public void setLocation(Location location) {
		this.location = location;
	}
	
	/**
	 * @return the provider
	 */
	public User getProvider() {
		return provider;
	}
	
	/**
	 * @param provider the provider to set
	 */
	public void setProvider(User provider) {
		this.provider = provider;
	}
	
	/**
	 * @return the numberOfPregnancies
	 */
	public Double getNumberOfPregnancies() {
		return numberOfPregnancies;
	}
	
	/**
	 * @param numberOfPregnancies the numberOfPregnancies to set
	 */
	public void setNumberOfPregnancies(Double numberOfPregnancies) {
		this.numberOfPregnancies = numberOfPregnancies;
	}
	
	/**
	 * @return the numberOfWeeksPregnant
	 */
	public Double getNumberOfWeeksPregnant() {
		return numberOfWeeksPregnant;
	}
	
	/**
	 * @param numberOfWeeksPregnant the numberOfWeeksPregnant to set
	 */
	public void setNumberOfWeeksPregnant(Double numberOfWeeksPregnant) {
		this.numberOfWeeksPregnant = numberOfWeeksPregnant;
	}
	
	/**
	 * Gets the DPA
	 * 
	 * @return the pregnantDueDate
	 */
	public Date getPregnantDueDate() {
		return pregnantDueDate;
	}
	
	/**
	 * @param pregnantDueDate the pregnantDueDate to set
	 */
	public void setPregnantDueDate(Date pregnantDueDate) {
		this.pregnantDueDate = pregnantDueDate;
	}
	
	//	/**
	//	 * @return the hivStatus
	//	 */
	//	public int getHivStatus() {
	//		return hivStatus;
	//	}
	//	
	//	/**
	//	 * @param hivStatus the hivStatus to set
	//	 */
	//	public void setHivStatus(int hivStatus) {
	//		this.hivStatus = hivStatus;
	//	}
	
	/**
	 * @return the hivTestDate
	 */
	public Date getHivTestDate() {
		return hiv_TestDate;
	}
	
	/**
	 * @param hivTestDate the hivTestDate to set
	 */
	public void setHivTestDate(Date hivTestDate) {
		this.hiv_TestDate = hivTestDate;
	}
	
	/**
	 * @return the resultOfHivTest
	 */
	public int getResultOfHivTest() {
		return hiv_resultOfTest;
	}
	
	/**
	 * @param resultOfHivTest the resultOfHivTest to set
	 */
	public void setResultOfHivTest(int resultOfHivTest) {
		this.hiv_resultOfTest = resultOfHivTest;
	}
	
	/**
	 * @return the dateResultOfHivTestReceived
	 */
	public Date getDateResultOfHivTestReceived() {
		return hiv_dateResultOfTestReceived;
	}
	
	/**
	 * @param dateResultOfHivTestReceived the dateResultOfHivTestReceived to set
	 */
	public void setDateResultOfHivTestReceived(Date dateResultOfHivTestReceived) {
		this.hiv_dateResultOfTestReceived = dateResultOfHivTestReceived;
	}
	
	/**
	 * @return the resultSyphilisTest
	 */
	public int getResultSyphilisTest() {
		return syphilis_resultTest;
	}
	
	/**
	 * @param resultSyphilisTest the resultSyphilisTest to set
	 */
	public void setResultSyphilisTest(int resultSyphilisTest) {
		this.syphilis_resultTest = resultSyphilisTest;
	}
	
	/**
	 * @return the dateResultOfSyphilisTestReceived
	 */
	public Date getDateResultOfSyphilisTestReceived() {
		return syphilis_dateResultOfTestReceived;
	}
	
	/**
	 * @param dateResultOfSyphilisTestReceived the dateResultOfSyphilisTestReceived to set
	 */
	public void setDateResultOfSyphilisTestReceived(Date dateResultOfSyphilisTestReceived) {
		this.syphilis_dateResultOfTestReceived = dateResultOfSyphilisTestReceived;
	}
	
	/**
	 * @return the dateMosquitoNetReceived
	 */
	public Date getDateMosquitoNetReceived() {
		return dateMosquitoNetReceived;
	}
	
	/**
	 * @param dateMosquitoNetReceived the dateMosquitoNetReceived to set
	 */
	public void setDateMosquitoNetReceived(Date dateMosquitoNetReceived) {
		this.dateMosquitoNetReceived = dateMosquitoNetReceived;
	}
	
	/**
	 * @return the patientArrivedWithPartner
	 */
	public int getPatientArrivedWithPartner() {
		return patientArrivedWithPartner;
	}
	
	/**
	 * @param patientArrivedWithPartner the patientArrivedWithPartner to set
	 */
	public void setPatientArrivedWithPartner(int patientArrivedWithPartner) {
		this.patientArrivedWithPartner = patientArrivedWithPartner;
	}
	
	/**
	 * @return the hivTestDateOfPartner
	 */
	public Date getHivTestDateOfPartner() {
		return partner_hivTestDate;
	}
	
	/**
	 * @param hivTestDateOfPartner the hivTestDateOfPartner to set
	 */
	public void setHivTestDateOfPartner(Date hivTestDateOfPartner) {
		this.partner_hivTestDate = hivTestDateOfPartner;
	}
	
	/**
	 * @return the resultHivTestOfPartner
	 */
	public int getResultHivTestOfPartner() {
		return partner_resultHivTest;
	}
	
	/**
	 * @param resultHivTestOfPartner the resultHivTestOfPartner to set
	 */
	public void setResultHivTestOfPartner(int resultHivTestOfPartner) {
		this.partner_resultHivTest = resultHivTestOfPartner;
	}
	
	/**
	 * @return the disclosedStatusToPartner
	 */
	public int getDisclosedStatusToPartner() {
		return partner_hivStatusDisclosed;
	}
	
	/**
	 * @param disclosedStatusToPartner the disclosedStatusToPartner to set
	 */
	public void setDisclosedStatusToPartner(int disclosedStatusToPartner) {
		this.partner_hivStatusDisclosed = disclosedStatusToPartner;
	}
	
	/**
	 * @return the partnerTestedSeparately
	 */
	public int getPartnerTestedSeparately() {
		return partner_testedSeparately;
	}
	
	/**
	 * @param partnerTestedSeparately the partnerTestedSeparately to set
	 */
	public void setPartnerTestedSeparately(int partnerTestedSeparately) {
		this.partner_testedSeparately = partnerTestedSeparately;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}
	
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * @return the patient
	 */
	public Patient getPatient() {
		return patient;
	}
	
	/**
	 * @param patient the patient to set
	 */
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	/**
	 * @return the syphilis_TestDate
	 */
	public Date getSyphilis_TestDate() {
		return syphilis_TestDate;
	}
	
	/**
	 * @param syphilis_TestDate the syphilis_TestDate to set
	 */
	public void setSyphilis_TestDate(Date syphilis_TestDate) {
		this.syphilis_TestDate = syphilis_TestDate;
	}
	
	/**
	 * @return the dateOfLastMenstrualPeriod
	 */
	public Date getDateOfLastMenstrualPeriod() {
		return dateOfLastMenstrualPeriod;
	}
	
	/**
	 * @param dateOfLastMenstrualPeriod the dateOfLastMenstrualPeriod to set
	 */
	public void setDateOfLastMenstrualPeriod(Date dateOfLastMenstrualPeriod) {
		this.dateOfLastMenstrualPeriod = dateOfLastMenstrualPeriod;
	}
	
	/**
	 * @return the nextVisitDate
	 */
	public Date getNextVisitDate() {
		return nextVisitDate;
	}
	
	/**
	 * @param nextVisitDate the nextVisitDate to set
	 */
	public void setNextVisitDate(Date nextVisitDate) {
		this.nextVisitDate = nextVisitDate;
	}
	
	//required infos
	private Patient patient;
	
	private Date cpnDate;
	
	private String cpnNumber;
	
	private Location location;
	
	private User provider;
	
	//pregnancy infos
	private Date dateOfLastMenstrualPeriod; //This date is the base to calculate the number_of_weeks_pregnant and the dpa
	
	private Double numberOfPregnancies;
	
	private Double numberOfWeeksPregnant = 0.0;
	
	private Date pregnantDueDate; //dpa
	
	//status of HIV
	//	private int hivStatus;
	
	private Date hiv_TestDate;
	
	private int hiv_resultOfTest;
	
	private Date hiv_dateResultOfTestReceived;
	
	//status of Syphilis
	private Date syphilis_TestDate;
	
	private int syphilis_resultTest;
	
	private Date syphilis_dateResultOfTestReceived;
	
	//Partner	
	private int patientArrivedWithPartner;
	
	private Date partner_hivTestDate;
	
	private int partner_resultHivTest;
	
	private int partner_hivStatusDisclosed;
	
	private int partner_testedSeparately;
	
	//other infos
	private String comment;
	
	private Date dateMosquitoNetReceived;
	
	private Date nextVisitDate;
	
}
