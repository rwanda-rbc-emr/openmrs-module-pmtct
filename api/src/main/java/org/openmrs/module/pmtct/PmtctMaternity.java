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

import org.openmrs.*;

/**
 *
 */
public class PmtctMaternity {
	
	private int bornDeadConceptId;
	
	/**
	 * @return the bornDeadConceptId
	 */
	public int getBornDeadConceptId() {
		return bornDeadConceptId;
	}
	
	/**
	 * @param bornDeadConceptId the bornDeadConceptId to set
	 */
	public void setBornDeadConceptId(int bornDeadConceptId) {
		this.bornDeadConceptId = bornDeadConceptId;
	}
	
	private Patient patient;
	
	private Location location;
	
	private User provider;
	
	private Date encounterDate;
	
	private Boolean testHivWorkroom;
	
	private int resultHivTest;
	
	private Date dpaDate;
	
	private Date dateConfinement;
	
	private Date dateReportingConfinement;
	
	private int prophylaxisForMother;
	
	private Boolean ARVReceived;
	
	private Boolean prophylaxis;
	
	private int chirdBornStutus;
	
	private int prophylaxisInfant;
	
	private int infantFeedingMethod;
	
	private String comment;
	
	private double childWeight;
	
	private double childHeight;
	
	private String childName;
	
	private String childGender;
	
	private String childSurname;
	
	private String childIdentifier;
	
	private String childTracNetId;
	
	private double childAgeBreasted;
	
	private String childComment;
	
	private Date transferOutDate;
	
	private Date childReturnVisitDate;
	
	private int drugId;
	
	private double dose;
	
	private String frequency;
	
	private Date drugStartDate;
	
	private int concept;
	
	private double cd4Value;
	
	//	private String orderType;
	
	private int childDrugId;
	
	private double childDose;
	
	private String childFrequency;
	
	private Date childDrugStartDate;
	
	/**
	 * @return the childDrugId
	 */
	public int getChildDrugId() {
		return childDrugId;
	}
	
	/**
	 * @param childDrugId the childDrugId to set
	 */
	public void setChildDrugId(int childDrugId) {
		this.childDrugId = childDrugId;
	}
	
	/**
	 * @return the childDose
	 */
	public double getChildDose() {
		return childDose;
	}
	
	/**
	 * @param childDose the childDose to set
	 */
	public void setChildDose(double childDose) {
		this.childDose = childDose;
	}
	
	/**
	 * @return the childFrequency
	 */
	public String getChildFrequency() {
		return childFrequency;
	}
	
	/**
	 * @param childFrequency the childFrequency to set
	 */
	public void setChildFrequency(String childFrequency) {
		this.childFrequency = childFrequency;
	}
	
	/**
	 * @return the childDrugStartDate
	 */
	public Date getChildDrugStartDate() {
		return childDrugStartDate;
	}
	
	/**
	 * @param childDrugStartDate the childDrugStartDate to set
	 */
	public void setChildDrugStartDate(Date childDrugStartDate) {
		this.childDrugStartDate = childDrugStartDate;
	}
	
	/**
	 * @return the childTracNetId
	 */
	public String getChildTracNetId() {
		return childTracNetId;
	}
	
	//    
	//    /**
	//     * @return the orderType
	//     */
	//    public String getOrderType() {
	//    	return orderType;
	//    }
	//
	//
	//	
	//    /**
	//     * @param orderType the orderType to set
	//     */
	//    public void setOrderType(String orderType) {
	//    	this.orderType = orderType;
	//    }
	//
	//
	/**
	 * @return the concept
	 */
	public int getConcept() {
		return concept;
	}
	
	/**
	 * @param concept the concept to set
	 */
	public void setConcept(int concept) {
		this.concept = concept;
	}
	
	/**
	 * @return the frequency
	 */
	public String getFrequency() {
		return frequency;
	}
	
	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	
	/**
	 * @return the cd4Value
	 */
	public double getCd4Value() {
		return cd4Value;
	}
	
	/**
	 * @param cd4Value the cd4Value to set
	 */
	public void setCd4Value(double cd4Value) {
		this.cd4Value = cd4Value;
	}
	
	/**
	 * @return the drugId
	 */
	public int getDrugId() {
		return drugId;
	}
	
	/**
	 * @param drugId the drugId to set
	 */
	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}
	
	/**
	 * @return the dose
	 */
	public double getDose() {
		return dose;
	}
	
	/**
	 * @param dose the dose to set
	 */
	public void setDose(double dose) {
		this.dose = dose;
	}
	
	/**
	 * @return the drugStartDate
	 */
	public Date getDrugStartDate() {
		return drugStartDate;
	}
	
	/**
	 * @param drugStartDate the drugStartDate to set
	 */
	public void setDrugStartDate(Date drugStartDate) {
		this.drugStartDate = drugStartDate;
	}
	
	/**
	 * @param childTracNetId the childTracNetId to set
	 */
	public void setChildTracNetId(String childTracNetId) {
		this.childTracNetId = childTracNetId;
	}
	
	/**
	 * @return the childReturnVisitDate
	 */
	public Date getChildReturnVisitDate() {
		return childReturnVisitDate;
	}
	
	/**
	 * @param childReturnVisitDate the childReturnVisitDate to set
	 */
	public void setChildReturnVisitDate(Date childReturnVisitDate) {
		this.childReturnVisitDate = childReturnVisitDate;
	}
	
	/**
	 * @return the transferOutDate
	 */
	public Date getTransferOutDate() {
		return transferOutDate;
	}
	
	/**
	 * @param transferOutDate the transferOutDate to set
	 */
	public void setTransferOutDate(Date transferOutDate) {
		this.transferOutDate = transferOutDate;
	}
	
	/**
	 * @return the childComment
	 */
	public String getChildComment() {
		return childComment;
	}
	
	/**
	 * @param childComment the childComment to set
	 */
	public void setChildComment(String childComment) {
		this.childComment = childComment;
	}
	
	/**
	 * @return the childAgeBreasted
	 */
	public double getChildAgeBreasted() {
		return childAgeBreasted;
	}
	
	/**
	 * @param childAgeBreasted the childAgeBreasted to set
	 */
	public void setChildAgeBreasted(double childAgeBreasted) {
		this.childAgeBreasted = childAgeBreasted;
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
	 * @return the maternityDate
	 */
	public Date getEncounterDate() {
		return encounterDate;
	}
	
	/**
	 * @param maternityDate the maternityDate to set
	 */
	public void setEncounterDate(Date encounterDate) {
		this.encounterDate = encounterDate;
	}
	
	/**
	 * @return the testHivWorkroom
	 */
	public Boolean getTestHivWorkroom() {
		return testHivWorkroom;
	}
	
	/**
	 * @param testHivWorkroom the testHivWorkroom to set
	 */
	public void setTestHivWorkroom(Boolean testHivWorkroom) {
		this.testHivWorkroom = testHivWorkroom;
	}
	
	/**
	 * @return the resultHivTest
	 */
	public int getResultHivTest() {
		return resultHivTest;
	}
	
	/**
	 * @param resultHivTest the resultHivTest to set
	 */
	public void setResultHivTest(int resultHivTest) {
		this.resultHivTest = resultHivTest;
	}
	
	/**
	 * @return the dpaDate
	 */
	public Date getDpaDate() {
		return dpaDate;
	}
	
	/**
	 * @param dpaDate the dpaDate to set
	 */
	public void setDpaDate(Date dpaDate) {
		this.dpaDate = dpaDate;
	}
	
	/**
	 * @return the dateConfinement
	 */
	public Date getDateConfinement() {
		return dateConfinement;
	}
	
	/**
	 * @param dateConfinement the dateConfinement to set
	 */
	public void setDateConfinement(Date dateConfinement) {
		this.dateConfinement = dateConfinement;
	}
	
	/**
	 * @return the dateReportingConfinement
	 */
	public Date getDateReportingConfinement() {
		return dateReportingConfinement;
	}
	
	/**
	 * @param dateReportingConfinement the dateReportingConfinement to set
	 */
	public void setDateReportingConfinement(Date dateReportingConfinement) {
		this.dateReportingConfinement = dateReportingConfinement;
	}
	
	/**
	 * @return the prophylaxisForMother
	 */
	public int getProphylaxisForMother() {
		return prophylaxisForMother;
	}
	
	/**
	 * @param prophylaxisForMother the prophylaxisForMother to set
	 */
	public void setProphylaxisForMother(int prophylaxisForMother) {
		this.prophylaxisForMother = prophylaxisForMother;
	}
	
	/**
	 * @return the aRVReceived
	 */
	public Boolean getARVReceived() {
		return ARVReceived;
	}
	
	/**
	 * @param received the aRVReceived to set
	 */
	public void setARVReceived(Boolean received) {
		ARVReceived = received;
	}
	
	/**
	 * @return the prophylaxis
	 */
	public Boolean getProphylaxis() {
		return prophylaxis;
	}
	
	/**
	 * @param prophylaxis the prophylaxis to set
	 */
	public void setProphylaxis(Boolean prophylaxis) {
		this.prophylaxis = prophylaxis;
	}
	
	/**
	 * @return the chirdBornStutus
	 */
	public int getChirdBornStutus() {
		return chirdBornStutus;
	}
	
	/**
	 * @param chirdBornStutus the chirdBornStutus to set
	 */
	public void setChirdBornStutus(int chirdBornStutus) {
		this.chirdBornStutus = chirdBornStutus;
	}
	
	/**
	 * @return the prophylaxisInfant
	 */
	public int getProphylaxisInfant() {
		return prophylaxisInfant;
	}
	
	/**
	 * @param prophylaxisInfant the prophylaxisInfant to set
	 */
	public void setProphylaxisInfant(int prophylaxisInfant) {
		this.prophylaxisInfant = prophylaxisInfant;
	}
	
	/**
	 * @return the infantFeedingMethod
	 */
	public int getInfantFeedingMethod() {
		return infantFeedingMethod;
	}
	
	/**
	 * @param infantFeedingMethod the infantFeedingMethod to set
	 */
	public void setInfantFeedingMethod(int infantFeedingMethod) {
		this.infantFeedingMethod = infantFeedingMethod;
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
	 * @return the childWeight
	 */
	public double getChildWeight() {
		return childWeight;
	}
	
	/**
	 * @param childWeight the childWeight to set
	 */
	public void setChildWeight(double childWeight) {
		this.childWeight = childWeight;
	}
	
	/**
	 * @return the childHeight
	 */
	public double getChildHeight() {
		return childHeight;
	}
	
	/**
	 * @param childHeight the childHeight to set
	 */
	public void setChildHeight(double childHeight) {
		this.childHeight = childHeight;
	}
	
	/**
	 * @return the childName
	 */
	public String getChildName() {
		return childName;
	}
	
	/**
	 * @param childName the childName to set
	 */
	public void setChildName(String childName) {
		this.childName = childName;
	}
	
	/**
	 * @return the childGender
	 */
	public String getChildGender() {
		return childGender;
	}
	
	/**
	 * @param childGender the childGender to set
	 */
	public void setChildGender(String childGender) {
		this.childGender = childGender;
	}
	
	/**
	 * @return the childSurname
	 */
	public String getChildSurname() {
		return childSurname;
	}
	
	/**
	 * @param childSurname the childSurname to set
	 */
	public void setChildSurname(String childSurname) {
		this.childSurname = childSurname;
	}
	
	/**
	 * @return the childIdentifier
	 */
	public String getChildIdentifier() {
		return childIdentifier;
	}
	
	/**
	 * @param childIdentifier the childIdentifier to set
	 */
	public void setChildIdentifier(String childIdentifier) {
		this.childIdentifier = childIdentifier;
	}
	
}
