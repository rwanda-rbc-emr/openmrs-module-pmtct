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
 *
 */
public class PmtctFollowupMother {
	
	/**
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
	 * @return the dpa
	 */
	public int getDpa() {
		return dpa;
	}
	
	/**
	 * @param dpa the dpa to set
	 */
	public void setDpa(int dpa) {
		this.dpa = dpa;
	}
	
	/**
	 * @return the numberOfPregnancies
	 */
	public int getNumberOfPregnancies() {
		return numberOfPregnancies;
	}
	
	/**
	 * @param numberOfPregnancies the numberOfPregnancies to set
	 */
	public void setNumberOfPregnancies(int numberOfPregnancies) {
		this.numberOfPregnancies = numberOfPregnancies;
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
	 * @return the btClicked
	 */
	public String getBtClicked() {
		return btClicked;
	}
	
	/**
	 * @param btClicked the btClicked to set
	 */
	public void setBtClicked(String btClicked) {
		this.btClicked = btClicked;
	}
	
	private String btClicked;
	
	private Patient patient;
	
	private Date cpnDate;
	
	private String cpnNumber;
	
	private int dpa;
	
	private int numberOfPregnancies;
	
	//Bilan pre-ARV -------------------------------------------------------------
	
	/**
	 * @return the hemoglobine
	 */
	public Double getHemoglobine() {
		return hemoglobine;
	}
	
	/**
	 * @param hemoglobine the hemoglobine to set
	 */
	public void setHemoglobine(Double hemoglobine) {
		this.hemoglobine = hemoglobine;
	}
	
	/**
	 * @return the cd4Count
	 */
	public Double getCd4Count() {
		return cd4Count;
	}
	
	/**
	 * @param cd4Count the cd4Count to set
	 */
	public void setCd4Count(Double cd4Count) {
		this.cd4Count = cd4Count;
	}
	
	/**
	 * @return the dateOfCD4Count
	 */
	public Date getDateOfCD4Count() {
		return dateOfCD4Count;
	}
	
	/**
	 * @param dateOfCD4Count the dateOfCD4Count to set
	 */
	public void setDateOfCD4Count(Date dateOfCD4Count) {
		this.dateOfCD4Count = dateOfCD4Count;
	}
	
	/**
	 * @return the dateResultOfCD4CountReceived
	 */
	public Date getDateResultOfCD4CountReceived() {
		return dateResultOfCD4CountReceived;
	}
	
	/**
	 * @param dateResultOfCD4CountReceived the dateResultOfCD4CountReceived to set
	 */
	public void setDateResultOfCD4CountReceived(Date dateResultOfCD4CountReceived) {
		this.dateResultOfCD4CountReceived = dateResultOfCD4CountReceived;
	}
	
	/**
	 * @return the tbScreening
	 */
	public int getTbScreening() {
		return tbScreening;
	}
	
	/**
	 * @param tbScreening the tbScreening to set
	 */
	public void setTbScreening(int tbScreening) {
		this.tbScreening = tbScreening;
	}
	
	/**
	 * @return the whoStage
	 */
	public int getWhoStage() {
		return whoStage;
	}
	
	/**
	 * @param whoStage the whoStage to set
	 */
	public void setWhoStage(int whoStage) {
		this.whoStage = whoStage;
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
	
	private Date encounterDate;
	
	private User provider;
	
	private Location location;
	
	private Double hemoglobine;
	
	private Double cd4Count;
	
	private Date dateOfCD4Count;
	
	private Date dateResultOfCD4CountReceived;
	
	private int tbScreening;
	
	private int whoStage;
	
	private Date nextVisitDate;
	
	//CD4 Count Test -------------------------------------------------------------------
	
	/**
	 * @return the encounterDate_cd4
	 */
	public Date getEncounterDate_cd4() {
		return encounterDate_cd4;
	}
	
	/**
	 * @param encounterDate_cd4 the encounterDate_cd4 to set
	 */
	public void setEncounterDate_cd4(Date encounterDate_cd4) {
		this.encounterDate_cd4 = encounterDate_cd4;
	}
	
	/**
	 * @return the provider_cd4
	 */
	public User getProvider_cd4() {
		return provider_cd4;
	}
	
	/**
	 * @param provider_cd4 the provider_cd4 to set
	 */
	public void setProvider_cd4(User provider_cd4) {
		this.provider_cd4 = provider_cd4;
	}
	
	/**
	 * @return the location_cd4
	 */
	public Location getLocation_cd4() {
		return location_cd4;
	}
	
	/**
	 * @param location_cd4 the location_cd4 to set
	 */
	public void setLocation_cd4(Location location_cd4) {
		this.location_cd4 = location_cd4;
	}
	
	/**
	 * @return the dateOfCD4Count_cd4
	 */
	public Date getDateOfCD4Count_cd4() {
		return dateOfCD4Count_cd4;
	}
	
	/**
	 * @param dateOfCD4Count_cd4 the dateOfCD4Count_cd4 to set
	 */
	public void setDateOfCD4Count_cd4(Date dateOfCD4Count_cd4) {
		this.dateOfCD4Count_cd4 = dateOfCD4Count_cd4;
	}
	
	/**
	 * @return the cD4Count_cd4
	 */
	public Double getCD4Count_cd4() {
		return CD4Count_cd4;
	}
	
	/**
	 * @param count_cd4 the cD4Count_cd4 to set
	 */
	public void setCD4Count_cd4(Double count_cd4) {
		CD4Count_cd4 = count_cd4;
	}
	
	/**
	 * @return the dateCD4CountResultReceived_cd4
	 */
	public Date getDateCD4CountResultReceived_cd4() {
		return dateCD4CountResultReceived_cd4;
	}
	
	/**
	 * @param dateCD4CountResultReceived_cd4 the dateCD4CountResultReceived_cd4 to set
	 */
	public void setDateCD4CountResultReceived_cd4(Date dateCD4CountResultReceived_cd4) {
		this.dateCD4CountResultReceived_cd4 = dateCD4CountResultReceived_cd4;
	}
	
	/**
	 * @return the nextVisitDate_cd4
	 */
	public Date getNextVisitDate_cd4() {
		return nextVisitDate_cd4;
	}
	
	/**
	 * @param nextVisitDate_cd4 the nextVisitDate_cd4 to set
	 */
	public void setNextVisitDate_cd4(Date nextVisitDate_cd4) {
		this.nextVisitDate_cd4 = nextVisitDate_cd4;
	}
	
	private Date encounterDate_cd4;
	
	private User provider_cd4;
	
	private Location location_cd4;
	
	private Date dateOfCD4Count_cd4;
	
	private Double CD4Count_cd4;
	
	private Date dateCD4CountResultReceived_cd4;
	
	private Date nextVisitDate_cd4;
	
	//Cpl Discordant -------------------------------------------------------------------
	
	/**
	 * @return the encounterDate_cplDisc
	 */
	public Date getEncounterDate_cplDisc() {
		return encounterDate_cplDisc;
	}
	
	/**
	 * @param encounterDate_cplDisc the encounterDate_cplDisc to set
	 */
	public void setEncounterDate_cplDisc(Date encounterDate_cplDisc) {
		this.encounterDate_cplDisc = encounterDate_cplDisc;
	}
	
	/**
	 * @return the provider_cplDisc
	 */
	public User getProvider_cplDisc() {
		return provider_cplDisc;
	}
	
	/**
	 * @param provider_cplDisc the provider_cplDisc to set
	 */
	public void setProvider_cplDisc(User provider_cplDisc) {
		this.provider_cplDisc = provider_cplDisc;
	}
	
	/**
	 * @return the location_cplDisc
	 */
	public Location getLocation_cplDisc() {
		return location_cplDisc;
	}
	
	/**
	 * @param location_cplDisc the location_cplDisc to set
	 */
	public void setLocation_cplDisc(Location location_cplDisc) {
		this.location_cplDisc = location_cplDisc;
	}
	
	/**
	 * @return the hivTestDate_cplDisc
	 */
	public Date getHivTestDate_cplDisc() {
		return hivTestDate_cplDisc;
	}
	
	/**
	 * @param hivTestDate_cplDisc the hivTestDate_cplDisc to set
	 */
	public void setHivTestDate_cplDisc(Date hivTestDate_cplDisc) {
		this.hivTestDate_cplDisc = hivTestDate_cplDisc;
	}
	
	/**
	 * @return the resultOfHivTest_cplDisc
	 */
	public int getResultOfHivTest_cplDisc() {
		return resultOfHivTest_cplDisc;
	}
	
	/**
	 * @param resultOfHivTest_cplDisc the resultOfHivTest_cplDisc to set
	 */
	public void setResultOfHivTest_cplDisc(int resultOfHivTest_cplDisc) {
		this.resultOfHivTest_cplDisc = resultOfHivTest_cplDisc;
	}
	
	/**
	 * @return the dateResultOfHivTestReceived_cplDisc
	 */
	public Date getDateResultOfHivTestReceived_cplDisc() {
		return dateResultOfHivTestReceived_cplDisc;
	}
	
	/**
	 * @param dateResultOfHivTestReceived_cplDisc the dateResultOfHivTestReceived_cplDisc to set
	 */
	public void setDateResultOfHivTestReceived_cplDisc(Date dateResultOfHivTestReceived_cplDisc) {
		this.dateResultOfHivTestReceived_cplDisc = dateResultOfHivTestReceived_cplDisc;
	}
	
	/**
	 * @return the returnVisitDate_cplDisc
	 */
	public Date getReturnVisitDate_cplDisc() {
		return returnVisitDate_cplDisc;
	}
	
	/**
	 * @param returnVisitDate_cplDisc the returnVisitDate_cplDisc to set
	 */
	public void setReturnVisitDate_cplDisc(Date returnVisitDate_cplDisc) {
		this.returnVisitDate_cplDisc = returnVisitDate_cplDisc;
	}
	
	private Date encounterDate_cplDisc;
	
	private User provider_cplDisc;
	
	private Location location_cplDisc;
	
	private Date hivTestDate_cplDisc;
	
	private int resultOfHivTest_cplDisc;
	
	private Date dateResultOfHivTestReceived_cplDisc;
	
	private Date returnVisitDate_cplDisc;
	
	private String error = "";
	
	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}
	
	/**
	 * @param error the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}
	
}
