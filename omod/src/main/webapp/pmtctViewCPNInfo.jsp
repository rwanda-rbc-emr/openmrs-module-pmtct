<%@ include file="template/localIncludes.jsp"%>
<!-- <openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/print.js" />  -->

<!-- *************************************** -->


<div id="contentPrintable">
	
	<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${encounter.patient.patientId}"/>
	<br/>
	
	<div id="encounterViewDiv">
		<h2 style="display: inline;"><spring:message code="@MODULE_ID@.general.pmtctEncounterTitle"/></h2>
		<c:if test="${encounter!=null}">(<a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}" title="<spring:message code="general.edit"/>"><spring:message code="general.edit"/></a>)</c:if>
		<br/><br/>
		
		<div style="width: 90%;margin: auto auto auto auto;">
			<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${encounter.patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
			 <!-- :: <span style="font-size: 15px" class="infoPatientLabel"><a href="${pageContext.request.contextPath}/admin/patients/patient.form?patientId=${encounter.patient.patientId}">${encounter.patient.personName}</a></span> -->
		
		<!-- <input type="button" onclick="printContent('contentPrintable');" class="list_exportBt" title="<spring:message code="@MODULE_ID@.print.description"/>" value="<spring:message code="@MODULE_ID@.print"/>"/> -->
		
		</div>
		
		<b class="boxHeader" style="width: 90%;margin: auto auto auto auto; margin-top: 4px;"><spring:message code="@MODULE_ID@.cpn.encounter.summary"/></b>
		
		<div class="box" style="width: 90%;margin: auto auto auto auto;">
			<table>
				<tr>
					<td><spring:message code="@MODULE_ID@.cpn.cpnNumberTitle" /></td>
					<td class="infoPatientLabel">&nbsp;:&nbsp; ${pmtcttag:personIdentifier(encounter.patient,cpnNumberIdentifierID)}</td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.datetime"/></td>
					<td class="infoPatientLabel">&nbsp;:&nbsp; <openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.type"/></td>
					<td class="infoPatientLabel">&nbsp;:&nbsp; ${encounter.encounterType.name}</td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.location"/></td>
					<td class="infoPatientLabel">&nbsp;:&nbsp; ${encounter.location.name}</td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.provider"/></td>
					<td class="infoPatientLabel">&nbsp;:&nbsp; <a href="${pageContext.request.contextPath}/admin/users/user.form?userId=${encounter.provider.personId}">${encounter.provider.personName}</a></td>
				</tr>
			</table>
		</div>
		<br/>
		
		<!-- <div id="list_container">
		<div id="list_title">
			<div class="list_title_msg"><spring:message code="@MODULE_ID@.cpn.pmtctDrugOrder"/></div>
			<div class="list_title_bts">
				<form style="display: inline;" action="viewMaternityInfo.htm?patientId=${encounter.patient.patientId}&encounterId=${encounter.encounterId}&export=csv" method="post">
					<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.exportToCSV"/>" value="CSV"/>
				</form>				
			</div>
			<div style="clear:both;"></div>
		</div>
		<table id="list_data">
			<tr>
				<th class="columnHeader">No.</th>
				<th class="columnHeader"><spring:message code="Order.item.ordered" /></th>
				<th class="columnHeader"><spring:message code="DrugOrder.dose"/>/<spring:message code="DrugOrder.units"/></th>
				<th class="columnHeader"><spring:message code="DrugOrder.frequency"/></th>
				<th class="columnHeader"><spring:message code="general.instructions" /></th>
				<th class="columnHeader"><spring:message code="general.dateStart"/></th>
				<th class="columnHeader"><spring:message code="general.dateDiscontinued"/></th>
			</tr>
			<c:if test="${empty drugOrders}"><tr><td colspan="7"><center>(<spring:message code="DrugOrder.list.noOrders" />)</center></td></tr></c:if>
			<c:forEach items="${drugOrders}" var="drugOrder" varStatus="status">
				<c:if test="${!empty drugOrder.drug}">
					<tr class="${status.count%2!=0?'even':''}">
						<td class="rowValue"><a href="<openmrs:contextPath/>/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${((param.page-1)*pageSize)+status.count}.</a></td>
						<td class="rowValue"><a class="patientRegimenDrugName" href="${pageContext.request.contextPath}/admin/orders/orderDrug.form?orderId=${drugOrder.orderId}">${drugOrder.drug.name}</a></td>
						<td class="rowValue">${drugOrder.dose} ${drugOrder.units}</td>
						<td class="rowValue">${drugOrder.frequency}</td>
						<td class="rowValue">${drugOrder.instructions}</td>
						<td class="rowValue"><openmrs:formatDate date="${drugOrder.startDate}" type="medium" /></td>
						<td class="rowValue"><openmrs:formatDate date="${drugOrder.discontinuedDate}" type="medium" /></td>
					</tr>
				</c:if>
			</c:forEach>
		</table>
		<div id="list_footer">
		</div>
		</div>
				
		<br/> -->
		
		<b class="boxHeader" style="width: 90%;margin: auto auto auto auto;"><spring:message code="Encounter.observations"/></b>
		<div class="box" style="width: 90%;margin: auto auto auto auto;">
			<div style="margin-bottom:6px;">
				<table style="width:100%" border="0">
					<tr>
						<th class="obsName"><spring:message code="Obs.concept"/></th>
						<th class="obsValue"><spring:message code="general.value"/></th>
					</tr>
				</table>
			</div>
			<table style="width:100%"  border="0">
				<c:forEach var="obs" items="${encounter.obs}" varStatus="status">
					<tr style="background:${status.count%2!=0?'whitesmoke':''}">
						<td class="obsName">${pmtcttag:observationConceptName(obs)}</td>
						<td class="obsValue">${pmtcttag:observationValue(obs)}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
	
	<br/><br/>
</div>
<br/><br/>

<%@ include file="/WEB-INF/template/footer.jsp"%>