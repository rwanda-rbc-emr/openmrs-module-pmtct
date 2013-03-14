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
 *@author Yves
 */
public class PmtctChildInformation {
	
	/**
	 * @return the scheduledDate
	 */
	public Date getScheduledDate() {
		return scheduledDate;
	}
	
	/**
	 * @param scheduledDate the scheduledDate to set
	 */
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}
	
	/**
	 * @return the hivTestDate
	 */
	public Date getHivTestDate() {
		return hivTestDate;
	}
	
	/**
	 * @param hivTestDate the hivTestDate to set
	 */
	public void setHivTestDate(Date hivTestDate) {
		this.hivTestDate = hivTestDate;
	}
	
	/**
	 * @return the resultOfHivTest
	 */
	public int getResultOfHivTest() {
		return resultOfHivTest;
	}
	
	/**
	 * @param resultOfHivTest the resultOfHivTest to set
	 */
	public void setResultOfHivTest(int resultOfHivTest) {
		this.resultOfHivTest = resultOfHivTest;
	}
	
	/**
	 * @return the dateOfReturnedResult
	 */
	public Date getDateOfReturnedResult() {
		return dateOfReturnedResult;
	}
	
	/**
	 * @param dateOfReturnedResult the dateOfReturnedResult to set
	 */
	public void setDateOfReturnedResult(Date dateOfReturnedResult) {
		this.dateOfReturnedResult = dateOfReturnedResult;
	}
	
	/**
	 * @return the returnedVisitDate
	 */
	public Date getReturnedVisitDate() {
		return returnedVisitDate;
	}
	
	/**
	 * @param returnedVisitDate the returnedVisitDate to set
	 */
	public void setReturnedVisitDate(Date returnedVisitDate) {
		this.returnedVisitDate = returnedVisitDate;
	}
	
	/**
	 * @return the reasonOfExitingCare
	 */
	public int getReasonOfExitingCare() {
		return reasonOfExitingCare;
	}
	
	/**
	 * @param reasonOfExitingCare the reasonOfExitingCare to set
	 */
	public void setReasonOfExitingCare(int reasonOfExitingCare) {
		this.reasonOfExitingCare = reasonOfExitingCare;
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
	 * @return the encounterDate
	 */
	public Date getEncounterDate() {
		return encounterDate;
	}
	
	/**
	 * @param encounterDate the encounterDate to set
	 */
	public void setEncounterDate(Date encounterDate) {
		this.encounterDate = encounterDate;
	}
	
	private Date scheduledDate;
	
	private Date hivTestDate;
	
	private int resultOfHivTest;
	
	private Date dateOfReturnedResult;
	
	private Date returnedVisitDate;
	
	private int reasonOfExitingCare;
	
	private Location location;
	
	private User provider;
	
	private Date encounterDate;
	
	private Patient patient;
}
