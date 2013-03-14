<%@ include file="template/localIncludes.jsp"%>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/maternityHeader.jsp" %>

<!-- *************************************** -->

<openmrs:require privilege="View PMTCT patients in maternity" otherwise="/login.htm" redirect="/module/@MODULE_ID@/generalStatsInMaternity.list?category=maternity&type=1&page=1" />

<div id="list_container">
<!-- <span><a href="#" id="viewChart"><spring:message code="@MODULE_ID@.tablelist.viewChart"/></a></span> -->
<div id="list_title">
	<div class="list_title_msg"><spring:message code="${listHeaderTitle}"/></div>
	<div class="list_title_bts">
		<c:if test="${param.type!='2' && param.type!='4'}">
			<form action="<c:if test="${param.type=='1'}">generalStatsInMaternity</c:if><c:if test="${param.type=='3'}">patientTestedInDeliveryroom</c:if>.list?category=maternity&type=${param.type}&page=1" method="post" style="display: inline;">
				<span><spring:message code="@MODULE_ID@.general.startDate"/> : <input type="text" name="startDate" size="11" value="${startDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<spring:message code="@MODULE_ID@.general.endDate"/> : <input type="text" name="endDate" size="11" value="${endDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<input type="submit" class="list_exportBt" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
				</span>
			</form>
		</c:if>
		<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
			<form style="display: inline;" action="<c:if test="${param.type=='1'}">generalStatsInMaternity</c:if><c:if test="${param.type=='2'}">expectedPatientInMaternity</c:if><c:if test="${param.type=='3'}">patientTestedInDeliveryroom</c:if><c:if test="${param.type=='4'}">patientWhoMissedMaternityEncounter</c:if>.list?${parameters}&type=${param.type}&export=csv" method="post">
				<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="CSV"/>
			</form>	
		</openmrs:hasPrivilege>			
	</div>
	<div style="clear:both;"></div>
</div>

<c:if test="${param.type!='1'}">
<c:set var="col" value="6"/>
</c:if>
<c:if test="${param.type=='1'}">
<c:set var="col" value="8"/>
</c:if>

<c:if test="${param.type!='3'}">
<table id="list_data">
	<tr>
		<th class="columnHeader">No.</th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.general.identifier"/></th>
		<openmrs:hasPrivilege privilege='View Patient Names'>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.general.names"/></th>
		</openmrs:hasPrivilege>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.dpa"/></th>
		<c:if test="${param.type=='1'}"><th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.deliveryDate"/></th></c:if>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.hivStatus"/></th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.hivStatusOfPartner"/></th>
		<c:if test="${param.type=='1'}"><th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.childBornStatus"/></th></c:if>
	</tr>
	<c:if test="${empty numberOfPages}"><tr><td colspan="${col}"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if>
	<c:forEach items="${patientList}" var="patient" varStatus="status">
		<c:set var="encounter" value="${pmtcttag:lastCPNEncounterByPatientId(patient[0])}"/>
		<c:if test="${param.type=='1'}"><c:set var="matEnc" value="${pmtcttag:lastMaternityEncounterByPatientId(patient[0])}"/></c:if>
		<tr class="${status.count%2!=0?'even':''}">
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
			<openmrs:hasPrivilege privilege='View Patient Names'>
				<td class="rowValue">${pmtcttag:personName(patient[0])}</td>
			</openmrs:hasPrivilege>
			<td class="rowValue">${pmtcttag:observationValueByConcept(encounter,dpaId)}</td>
			<c:if test="${param.type=='1'}"><td class="rowValue">${pmtcttag:observationValueByConcept(matEnc,dateOfConfinementId)}</td></c:if>
			<td class="rowValue"><span class="${pmtcttag:observationValueByConcept(encounter,hivTestResultId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
				${pmtcttag:observationValueByConcept(encounter,hivTestResultId)}</span></td>
			<td class="rowValue"><span class="${pmtcttag:observationValueByConcept(encounter,partnerStatusId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
				${pmtcttag:observationValueByConcept(encounter,partnerStatusId)}</span></td>
			<c:if test="${param.type=='1'}"><td class="rowValue"><span class="${pmtcttag:observationValueByConcept(matEnc,childBornStatusId)==pmtcttag:conceptNameById(bornDeadId)?'lastObsValuePositive':'lastObsValue'}">
				${pmtcttag:observationValueByConcept(matEnc,childBornStatusId)}</span></td></c:if>
		</tr>
		<c:remove var="encounter" />
		<c:if test="${param.type=='1'}"><c:remove var="matEnc" /></c:if>
	</c:forEach>
</table>
</c:if>

<c:if test="${param.type=='3'}">

<table id="list_data">
	<tr>
		<th class="columnHeader">No.</th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.general.identifier"/></th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.cpnNumberTitle"/></th>
		<openmrs:hasPrivilege privilege='View Patient Names'>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.general.names"/></th>
		</openmrs:hasPrivilege>
		<th class="columnHeader">Date</th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.hivStatus"/></th>
	</tr>
	<c:if test="${empty numberOfPages}"><tr><td colspan="${col}"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if>
	<c:forEach items="${patientList}" var="patient" varStatus="status">
		<c:set var="matEnc" value="${pmtcttag:lastMaternityEncounterByPatientId(patient[0])}"/>
		<tr class="${status.count%2!=0?'even':''}">
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:personIdentifierByIds(patient[0],cpnIdentifierTypeId)}</a></td>
			<openmrs:hasPrivilege privilege='View Patient Names'>
				<td class="rowValue">${pmtcttag:personName(patient[0])}</td>
			</openmrs:hasPrivilege>
			<td class="rowValue"><openmrs:formatDate date="${matEnc.encounterDatetime}" type="medium" /></td>
			<td class="rowValue"><span class="${pmtcttag:observationValueByConcept(matEnc,hivTestResultId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
				${pmtcttag:observationValueByConcept(matEnc,hivTestResultId)}</span></td>
		</tr>
		<c:remove var="matEnc" />
	</c:forEach>
</table>
<c:remove var="col" />

</c:if>
<div id="list_footer">
	<div class="list_footer_info">${pageInfos}</div>
	<div class="list_footer_pages">
		<table><tr>
			<c:forEach items="${numberOfPages}" var="page" varStatus="status">
				<td><form action="<c:if test="${param.type=='1'}">generalStatsInMaternity</c:if><c:if test="${param.type=='2'}">expectedPatientInMaternity</c:if><c:if test="${param.type=='3'}">patientTestedInDeliveryroom</c:if>.list?category=maternity&type=${param.type}&page=${page}" method="post">
					<input type="submit" class="<c:if test="${param.page==page}">list_pageNumberCurrent</c:if><c:if test="${param.page!=page}">list_pageNumber</c:if>" name="page" value="${page}"/>
				</form></td>
			</c:forEach>
		</tr></table>
	</div>
	<div style="clear:both"></div>
</div>

</div>

<!-- <div id="chartHolder" style="display: none;">
	<span><a href="#" id="viewData"><spring:message code="@MODULE_ID@.tablelist.viewData"/></a></span>
	<div style="text-align: center; border: 1px solid #8FABC7; padding: 5px 2px;">
		<img src="chart.htm?chart=maternity&type=2" width="380" height="300" />
		<img src="chart.htm?chart=maternity&type=3" width="380" height="300" />
		<img src="chart.htm?chart=maternity&type=1" width="380" height="300" />
	</div>
</div> -->

<script type="text/javascript">
	$j(document).ready( function() {
		$j("#viewChart").click(function(){
		    $j("#list_container").hide();
		    $j("#chartHolder").show();
		});
		$j("#viewData").click(function(){
		    $j("#chartHolder").hide();
		    $j("#list_container").show();
		});
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>