<c:set var="encounter" value="${pmtcttag:lastCPNEncounterByPatientId(patient[0])}"/>
<tr class="${status.count%2!=0?'even':''}">
	<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
	<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
	<td class="rowValue">${pmtcttag:personName(patient[0])}</td>
	<td class="rowValue"><openmrs:formatDate date="${patient[2]}" type="medium" /></td>
	<td class="rowValue"><openmrs:formatDate date="${patient[3]}" type="medium" /></td>
	<td class="rowValue"><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
	<td class="rowValue">${pmtcttag:observationValueByConcept(encounter,dpaId)}</td>
</tr>
<c:remove var="encounter" />