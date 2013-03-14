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

import java.util.HashMap;
import java.util.List;

import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Location;
import org.openmrs.User;
import org.openmrs.api.context.Context;

/**
 * @author Yves GAKUBA
 */
public class PMTCTUtil {
	
	/**
	 * Creates a map of all providers in the system
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createProviderOptions() {
		HashMap<Integer, String> providerOptions = new HashMap<Integer, String>();
		List<User> providers = Context.getUserService().getUsersByRole(Context.getUserService().getRole("Provider"));
		if (providers != null) {
			for (User user : providers) {
				providerOptions.put(user.getUserId(), user.getPersonName().getGivenName() + "  "
				        + user.getPersonName().getFamilyName());
			}
		}
		return providerOptions;
	}
	
	/**
	 * Creates a map of all locations in the system.
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createLocationOptions() {
		HashMap<Integer, String> locationOptions = new HashMap<Integer, String>();
		List<Location> locations = Context.getLocationService().getAllLocations();
		if (locations != null) {
			for (Location location : locations) {
				locationOptions.put(location.getLocationId(), location.getName());
			}
		}
		return locationOptions;
	}
	
	/**
	 * Retrieve a hashmap of concept answers (concept id, bestname) given the id of the coded
	 * concept question
	 * 
	 * @param codedConceptQuestionId
	 * @return
	 */
	public static HashMap<Integer, String> createCodedOptions(int codedConceptQuestionId) {
		HashMap<Integer, String> answersMap = new HashMap<Integer, String>();
		Concept questionConcept = Context.getConceptService().getConcept(codedConceptQuestionId);
		if (questionConcept != null) {
			for (ConceptAnswer ca : questionConcept.getAnswers()) {
				answersMap.put(ca.getAnswerConcept().getConceptId(), ca.getAnswerConcept().getDisplayString());
			}
		}
		return answersMap;
	}
	
	/**
	 * Get a message from message propertie file
	 * 
	 * @param messageId
	 * @param parameters
	 * @return
	 */
	public static String getMessage(String messageId, Object[] parameters) {
		String msg = ContextProvider.getApplicationContext().getMessage(messageId, parameters, Context.getLocale());
		return msg;
	}
	
	/**
	 * Return a map of 4 possible result of WHO Stage for adults
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createWHOStageAdultOptions() {
		HashMap<Integer, String> whoStageOptions = new HashMap<Integer, String>();
		for (int conceptId : PMTCTConstants.WHO_STAGE_ADULT_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			whoStageOptions.put(c.getConceptId().intValue(), c.getDisplayString());
		}
		return whoStageOptions;
	}
	
	/**
	 * Return a map of 4 possible result of WHO Stage for pediatric
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createWHOStagePediatricOptions() {
		HashMap<Integer, String> whoStageOptions = new HashMap<Integer, String>();
		for (int conceptId : PMTCTConstants.WHO_STAGE_PEDIATRIC_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			whoStageOptions.put(c.getConceptId().intValue(), c.getDisplayString());
		}
		return whoStageOptions;
	}
	
	/**
	 * Return a map of 3 possible result of hiv test {indeterminate, negative, positive}
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createResultOfHivTestOptions() {
		HashMap<Integer, String> hivResultOptions = new HashMap<Integer, String>();
		for (int conceptId : PMTCTConstants.RESULT_OF_HIV_TEST_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			hivResultOptions.put(c.getConceptId().intValue(), c.getDisplayString());
		}
		return hivResultOptions;
	}
	
	/**
	 * Return a map of 5 possible infant feeding method {BREASTFED PREDOMINATELY (1150), EXPRESSED
	 * BREASTMILK (1173), INFANT FORMULA (5254), BREASTFED EXCLUSIVELY (5526), MIXED FEEDING (6046)}
	 * 
	 * @return
	 */
	public static HashMap<Integer, String> createInfantFeedingMethodOptions() {
		HashMap<Integer, String> infantFeedingMethodOptions = new HashMap<Integer, String>();
		for (int conceptId : PMTCTConstants.INFANT_FEEDING_METHOD_POSSIBLE_OPTIONS) {
			Concept c = Context.getConceptService().getConcept(conceptId);
			infantFeedingMethodOptions.put(c.getConceptId().intValue(), c.getDisplayString());
		}
		return infantFeedingMethodOptions;
	}
	
}
