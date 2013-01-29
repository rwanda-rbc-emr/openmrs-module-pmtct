/**
 * 
 */
package org.openmrs.module.pmtct.util;

import java.util.List;

import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracConfigurationUtil;

/**
 * @author Yves GAKUBA
 */
public class PMTCTConfigurationUtils {
	
	/**
	 * constructor
	 */
	private PMTCTConfigurationUtils() {
	}
	
	/**
	 * @return Number of entries to display per page, if not configured, 25 is the default.
	 * @throws Exception
	 */
	public static Integer getNumberOfRecordPerPage() throws Exception {
		return MohTracConfigurationUtil.getNumberOfRecordPerPage();
	}
	
	/**
	 * @return The configured default location id
	 * @throws Exception
	 */
	public static Integer getDefaultLocationId() throws Exception {
		return MohTracConfigurationUtil.getDefaultLocationId();
	}
	
	/**
	 * @return Returns true or false.<br/>
	 *         If the TRAC Portal module is configured it returns true, otherwise it returns false.
	 */
	public static boolean isMohTracPortalConfigured() throws Exception {
		return MohTracConfigurationUtil.isConfigured();
	}

	/**
	 * @return Returns true or false.<br/>
	 *         If PMTCT module is configured it returns true, otherwise it returns
	 *         false.
	 */
	public static boolean isConfigured() throws Exception {
		GlobalProperty gp = Context.getAdministrationService()
				.getGlobalPropertyObject("pmtct.configured");
		return (gp != null) ? ((gp.getPropertyValue().compareToIgnoreCase(
				"true") == 0) ? true : false) : true;
	}
	
	/**
	 * @return Returns the Hiv Program Id if it has been configured, otherwise, it will return -1.
	 */
	public static Integer getHivProgramId() throws Exception {
		return MohTracConfigurationUtil.getHivProgramId();
	}
	
	/**
	 * @return Returns the Pmtct Program Id if it has been configured, otherwise, it will return -1.
	 */
	public static Integer getPmtctProgramId() throws Exception {
		return MohTracConfigurationUtil.getPmtctProgramId();
	}
	
	/**
	 * @return The Civil Status attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getCivilStatusAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getCivilStatusAttributeTypeId();
	}
	
	/**
	 * @return The Education Level attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getEducationLevelAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getEducationLevelAttributeTypeId();
	}
	
	/**
	 * @return The Main Activity attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getMainActivityAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getMainActivityAttributeTypeId();
	}
	
	/**
	 * @return The NID identifier type id, in case it has not been configured, it will return null.
	 */
	public static int getNIDIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getNIDIdentifierTypeId();
	}
	
	/**
	 * @return The NID identifier type id, in case it has not been configured, it will return null.
	 */
	public static int getTRACNETIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getTracNetIdentifierTypeId();
	}
	
	/**
	 * @return The NID identifier type id, in case it has not been configured, it will return null.
	 */
	public static int getLocalHealthCenterIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getLocalHealthCenterIdentifierTypeId();
	}
	
	/**
	 * The ANC (consultation pre-natal) encounter type, in case it has not been configured, it will
	 * return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getCPNEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.encounterType.anc");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * The Maternity encounter type, in case it has not been configured, it will return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getMaternityEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.encounterType.maternity");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * The encounter type for follow-up of pregnant women, in case it has not been configured, it
	 * will return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getMotherFollowUpEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.encounterType.motherfollowup");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * The PCR encounter type, in case it has not been configured, it will return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getPCRTestEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.encounterType.pcr");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * The Serology at 9 months encounter type, in case it has not been configured, it will return
	 * null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getSerology9MonthEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.encounterType.serology9month");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * The Serology at 18 months encounter type, in case it has not been configured, it will return
	 * null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static int getSerology18MonthEncounterTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService()
		        .getGlobalPropertyObject("pmtct.encounterType.serology18month");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * @return Returns the list of global properties starting with
	 *         <i>mohtracportal.attributeType</i>
	 */
	public static List<GlobalProperty> getNeededEncounterType() {
		List<GlobalProperty> gpList = null;
		gpList = Context.getAdministrationService().getGlobalPropertiesByPrefix("pmtct.encounterType");
		
		return gpList;
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public static int getBornAliveConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.bornalive");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getBornDeadConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.borndead");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getBornStatusConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.bornstatus");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getHivTestInDeliveryRoomConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService()
		        .getGlobalPropertyObject("pmtct.concept.hivtestindeliveryroom");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getPatientArrivedWithPartnerConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "pmtct.concept.patientarrivedwithpartner");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getPartnerTestedSeparatelyConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "pmtct.concept.partnertestedseparately");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getHivTestDateForPartnerConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.hivtestdateofpartner");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getResultHivTestReceptionDateConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "pmtct.concept.resulthivtestreceptiondate");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getSyphilisTestDateConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.syphilistestdate");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getResultSyphilisTestReceptionDateConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "pmtct.concept.resultsyphilistestreceptiondate");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getReceptionDateCD4CountConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService()
		        .getGlobalPropertyObject("pmtct.concept.receptiondatecd4count");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	public static int getReceptionDateBedNetConceptId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.concept.receptiondatebednet");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * @return Returns the list of global properties starting with <i>pmtct.concept</i>
	 */
	public static List<GlobalProperty> getNeededConcept() {
		List<GlobalProperty> gpList = null;
		gpList = Context.getAdministrationService().getGlobalPropertiesByPrefix("pmtct.concept");
		
		return gpList;
	}
	
	/**
	 * The order type for PMTCT related drugs, in case it has not been configured, it will
	 * return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Integer getDrugOrderTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.ordertype");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * This should be the patient identifier type for ANC pregnant woman, in case it has not been configured, it will
	 * return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Integer getCPNIdentifierTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.identifiertype.anc");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
	
	/**
	 * This should be the Parent-Child relationship Type ID, in case it has not been configured, it will
	 * return null.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Integer getRelationshipTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("pmtct.relationshiptype.motherchild");
		return (gp != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue())
		        : null;
	}
}
