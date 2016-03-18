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

/***************************************************************************************************
 * This class contains the most important configurations of PMTCT Module. All configuration made
 * here should be accessible from the configuration page of the module. <br/>
 * <br/>
 * <b>Exception: </b> There exists some configuration made statically, those configurations will not
 * be accessible from the configurations page.
 ***************************************************************************************************/

public class PMTCTConstants {
	
	public final static Long YEAR_IN_MILLISECONDS = 31536000000L;
	
	public static final int[] WHO_STAGE_ADULT_POSSIBLE_OPTIONS = { 1204, 1205, 1206, 1207 };
	
	public static final int[] WHO_STAGE_PEDIATRIC_POSSIBLE_OPTIONS = { 1220, 1221, 1222, 1223 };
	
	public static final int[] RESULT_OF_HIV_TEST_POSSIBLE_OPTIONS = { 1138, 664, 703 };
	
	public static final int[] INFANT_FEEDING_METHOD_POSSIBLE_OPTIONS = { 1150, 1173, 5254, 5526, 6046 };
	
	//--------------------- Default values ---------------
	
	//concepts
	
	public final static int MOTHER_NAME_ATTRIBUTE_TYPE_ID = 4;
	
	public final static int HEMOGLOBIN = 21;
	
	public final static int DELAY_IN_DAYS_OF_PREGNANCY = 280;
	
	public final static int SYPHILIS_TEST = 299;
	
	public final static int NEGATIVE = 664;
	
	public final static int POSITIVE = 703;
	
	public final static int DATE_OF_LAST_MENSTRUAL_PERIOD = 968;
	
	public final static int CIVIL_STATUS = 1054;
	
	public final static int YES = 1065;
	
	public final static int NO = 1066;
	
	public final static int INFANT_FEEDING_METHOD = 1151;
	
	public final static int INFANT_FORMULA = 5254;
	
	public final static int NUMBER_OF_WEEKS_PREGNANT = 1279;
	
	public final static int MAIN_ACTIVITY = 1304;
	
	public final static int CLINICAL_COMMENT_IMPRESSION = 1364;
	
	public final static int ARV_FOR_PMTCT = 1446;
	
	public final static int WHO_STAGE = 1480;
	
	public final static int PMTCT_DRUG_ORDER_CONCEPT_RELATED_ID = 1647;
	
	public final static int EDUCATION_LEVEL = 1688;
	
	public final static int PATIENT_DIED = 1742;
	
	public final static int REASON_FOR_EXITING_CARE = 1811;
	
	public final static int HIV_TEST_DATE = 1837;
	
	public final static int BREASTFED_UNTIL_WHAT_AGE = 1838;
	
	public final static int HAVE_YOU_DISCLOSED_YOUR_HIV_STATUS_TO_YOUR_PARTNER = 2061;
	
	public final static int TB_SCREENING = 2136;
	
	public final static int RESULT_OF_HIV_TEST = 2169;
	
	public final static int RESULT_HIV_TEST_IN_DELIVERY_ROOM = 2169; //HIV TEST RESULT
	
	public final static int PROPHYLAXIS_FOR_MOTHER = 2207;
	
	public static final int ANTIRETROVIRAL_DRUGS = 2589;
	
	public final static int PROPHYLAXIS_STOPPED = 2744;
	
	public final static int TRANSFER_OUT_DATE = 3001;
	
	public final static int TESTING_STATUS_OF_PARTNER = 3082;
	
	public final static int DATE_OF_CD4_COUNT = 3461;
	
	public final static int PROPHYLAXIS_FOR_INFANT = 3530;
	
	public final static int HIV_STATUS = 3753;
	
	public final static int WEIGHT = 5089;
	
	public final static int HEIGHT = 5090;
	
	public final static int RETURN_VISIT_DATE = 5096;
	
	public final static int CD4_COUNT = 5497;
	
	public final static int PREGNANT_DUE_DATE = 5596;
	
	public final static int DATE_OF_CONFINEMENT = 5599;
	
	public final static int NUMBER_OF_PREGNANCIES = 5624;
	
	public final static int DATE_OF_RETURNED_RESULT = 6110;
	
	public final static int CHILD_MAX_AGE_IN_PMTCT = 2;
	
}
