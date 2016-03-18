<%@ include file="template/localIncludes.jsp"%>

<!-- *************************************** -->

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${patient.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.portlet.flowsheet"/></h2>
<br/><br/>

<div style="width: 90%;margin: auto auto auto auto;">
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
	 :: <span style="font-size: 15px" class="infoPatientLabel"><a href="${pageContext.request.contextPath}/admin/patients/patient.form?patientId=${patient.patientId}">${patient.personName}</a></span>
</div>

<b class="boxHeader" style="width: 90%;margin: auto auto auto auto; margin-top: 4px;"><spring:message code="patientDashboard.encounters"/>&nbsp;&amp;&nbsp;<spring:message code="Encounter.observations"/></b>
<div class="box" style="width: 90%;margin: auto auto auto auto;">
	<table style="width:95%; margin-left:5px;" border="0">
		<tr>
			<th class="flowsheetObsName"><spring:message code="Obs.concept"/></th>
			<th class="flowsheetObsValue"><spring:message code="general.value"/></th>
			<th class="flowsheetObsCreator"><spring:message code="Obs.creator.or.changedBy"/></th>
		</tr>
	</table>
	<c:forEach items="${encounters}" var="encounter">
		<div class="box" style="color: navy !important;">
			<b>${encounter.encounterType.name} @ ${encounter.location.name}</b>	| 
			<b><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium"/></b> | 
			<b><a href="${pageContext.request.contextPath}/admin/users/user.form?userId=${encounter.provider.id}">${encounter.provider.personName}</a></b> | 
			(<a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}" title="<spring:message code="general.edit"/>"><spring:message code="general.edit"/></a>)	
		</div>
		<table style="width:95%; margin-left:5px;"  border="0">
		<c:if test="${empty encounter.obs}">
		<tr><td colspan="3"><spring:message code="@MODULE_ID@.tablelist.empty"/></c:if></td></tr>
			<c:forEach var="obs" items="${encounter.obs}" varStatus="status">
				<tr style="background:${status.count%2!=0?'whitesmoke':''}">
					<td class="flowsheetObsName">${pmtcttag:observationConceptName(obs)}</td>
					<td class="flowsheetObsValue">${pmtcttag:observationValue(obs)}</td>
					<td class="flowsheetObsCreator">${obs.creator.personName} - 
					<c:if test="${obs.dateCreated!=''}"><openmrs:formatDate date="${obs.dateCreated}" type="medium"/></c:if>
					<c:if test="${obs.dateCreated==''}"><openmrs:formatDate date="${obs.obsDatetime}" type="medium"/></c:if></td>
				</tr>
			</c:forEach>
		</table>
	</c:forEach>
	
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>