<tr class="${status.count%2!=0?'even':''}">
	<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
	<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
	<openmrs:hasPrivilege privilege='View Patient Names'>
		<td class="rowValue" style="color:${pmtcttag:isPatientDead(patient[0])}">${pmtcttag:personName(patient[0])}</td>
		<td class="rowValue">${pmtcttag:personName(mother.patientId)}&nbsp;(<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${mother.patientId}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(mother.patientId)}</a>)</td>
	</openmrs:hasPrivilege>
	<td class="rowValue"><img border="0"
					src="<c:if test="${patient[1]=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient[1]=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
	<td class="rowValue"><openmrs:formatDate date="${patient[3]}" type="medium" /></td>
	<td class="rowValue">${pmtcttag:lastObsValueByConceptId(patient[0],infantFeedingMethodId)}</td>
	<td class="rowValue">
		<span class="${pmtcttag:conceptNameById(patient[4])==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
			${pmtcttag:conceptNameById(patient[4])}
		</span>
	</td>
	<td class="rowValue"><span class="${pmtcttag:conceptNameById(patient[5])==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
			${pmtcttag:conceptNameById(patient[5])}
		</span>
	</td>
	<td class="rowValue"><span class="${pmtcttag:conceptNameById(patient[6])==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
			${pmtcttag:conceptNameById(patient[6])}
		</span>
	</td>
</tr>