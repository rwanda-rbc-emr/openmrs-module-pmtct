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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.pmtct.util.PMTCTConstants;

/**
 *
 */
public class PregnancyDateManager {
	
//	private Log log = LogFactory.getLog(this.getClass());
	
	@SuppressWarnings("deprecation")
	public String getNumberOfWeeks(String dateOfPeriod) throws Exception {
		Date lastDateOfPeriod = Context.getDateFormat().parse(dateOfPeriod);
		
		GregorianCalendar last_DateOfPeriod = new GregorianCalendar(lastDateOfPeriod.getYear(), lastDateOfPeriod.getMonth(),
		        lastDateOfPeriod.getDate());
		last_DateOfPeriod.setLenient(false);
		
		Date da= new Date();
		
//		slog.info("xxxxxxxxxxxxxxx"+(new GregorianCalendar(da.getYear()+1900,da.getMonth(),da.getDate())).getTime());
		
		//1 week=604800000 milliseconds = (1000ms*60s*60min*24h*7days)
		
		return "**********************"
		        + (((new GregorianCalendar(da.getYear(),da.getMonth(),da.getDate())).getTimeInMillis()) - (last_DateOfPeriod
		                .getTimeInMillis())) / 604800000;
	}
	
	@SuppressWarnings("deprecation")
	public String getDPA(String dateOfPeriod) throws Exception {
		Date lastDateOfPeriod = Context.getDateFormat().parse(dateOfPeriod);
		
		GregorianCalendar last_DateOfPeriod = new GregorianCalendar(lastDateOfPeriod.getYear() + 1900, lastDateOfPeriod
		        .getMonth(), lastDateOfPeriod.getDate());
		
		last_DateOfPeriod.setLenient(false);
		last_DateOfPeriod.add(Calendar.DAY_OF_YEAR, PMTCTConstants.DELAY_IN_DAYS_OF_PREGNANCY);
		
		return Context.getDateFormat().format(last_DateOfPeriod.getTime());
	}
}
