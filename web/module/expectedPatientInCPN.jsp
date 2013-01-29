<%@ include file="template/localIncludes.jsp"%>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/cpnHeader.jsp" %>

<!-- *************************************** -->

<openmrs:require privilege="View PMTCT patients in ANC" otherwise="/login.htm" redirect="/module/@MODULE_ID@/generalStatIncpn.list?category=cpn&type=1&page=1" />

<div id="list_container">
<div id="list_title">
	<div class="list_title_msg"><spring:message code="${listHeaderTitle}"/></div>
	<div  class="list_title_bts">
		<c:if test="${param.type=='2'}">
			<form style="display: inline;" action="cpnMoisDeRapportage.list?category=cpn&type=${param.type}&page=1" method="post">
				<span><spring:message code="@MODULE_ID@.month"/> : <select name="month">
					<c:forEach items="${monthsList}" var="month" varStatus="status">
						<option value="${status.count}" <c:if test="${param.month==status.count}">selected='selected'</c:if>><spring:message code="${month}"/></option>
					</c:forEach>
					</select>
					&nbsp;&nbsp;<spring:message code="@MODULE_ID@.year"/> : 
					<select name="year">
						<c:forEach items="${years}" var="yr">
							<option value="${yr}" <c:if test="${param.year==yr}">selected='selected'</c:if>>${yr}</option>
						</c:forEach>
					</select>				
					<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
				</span>
			</form>
			<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
				<form style="display: inline;" action="cpnMoisDeRapportage.list?category=cpn&type=2&page=1&export=csv" method="post">	
					<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="CSV"/>
				</form>
			</openmrs:hasPrivilege>
		</c:if>
		<c:if test="${param.type=='1'}">
			<form action="generalStatIncpn.list?category=cpn&type=1&page=1" method="post" style="display: inline;">
				<span><spring:message code="@MODULE_ID@.general.startDate"/> : <input type="text" name="startDate" size="11" value="${startDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<spring:message code="@MODULE_ID@.general.endDate"/> : <input type="text" name="endDate" size="11" value="${endDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<input type="submit" class="list_exportBt" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
				</span>
			</form>
			<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
				<form style="display: inline;" action="generalStatIncpn.list?category=cpn&type=1&page=1&export=csv" method="post">	
					<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="CSV"/>
				</form>	
			</openmrs:hasPrivilege>
		</c:if>

		<c:if test="${param.type=='3'}">
			<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
				<form style="display: inline;" action="cpnCouplesDiscordant.list?category=cpn&type=3&page=1&export=csv" method="post">	
					<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="CSV"/>
				</form>	
			</openmrs:hasPrivilege>
		</c:if>	
	</div>
	<div style="clear:both;"></div>
</div>
<table id="list_data">
	<tr>
		<th class="columnHeader">No.</th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.general.identifier"/></th>
		<openmrs:hasPrivilege privilege='View Patient Names'>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.general.names"/></th>
		</openmrs:hasPrivilege>
		<th class="columnHeader"><spring:message code="Person.birthdate"/></th>
		<th class="columnHeader"><spring:message code="Program.dateEnrolled"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.cpnDate"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.dpa"/></th>
		<c:if test="${param.type=='1'}">
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.hivStatus"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.deliveryDate"/></th>
		</c:if>
	</tr>
	<c:if test="${empty numberOfPages}">
		<c:if test="${param.type=='1'}">
			<c:set var="trSize" value="10"/>
		</c:if>
		<c:if test="${param.type!='1'}">
			<c:set var="trSize" value="7"/>
		</c:if>
		<tr><td colspan="${trSize}"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr>
		<c:remove var="trSize" />
	</c:if>
	<c:forEach items="${patientList}" var="patient" varStatus="status">
		<c:if test="${param.type=='1'}">
			<c:set var="encounter" value="${pmtcttag:lastCPNEncounterByPatientId(patient[1])}"/>
			<c:set var="matEnc" value="${pmtcttag:lastMaternityEncounterByPatientId(patient[1])}"/>
		
			<tr class="${status.count%2!=0?'even':''}">
				<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[1]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
				<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[1]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[1])}</a></td>
				<openmrs:hasPrivilege privilege='View Patient Names'>
					<td class="rowValue">${pmtcttag:personName(patient[1])}</td>
				</openmrs:hasPrivilege>
				<td class="rowValue"><openmrs:formatDate date="${patient[3]}" type="medium" /></td>
				<td class="rowValue"><openmrs:formatDate date="${patient[4]}" type="medium" /></td>
				<td class="rowValue"><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
				<td class="rowValue">${pmtcttag:observationValueByConcept(encounter,dpaId)}</td>
				<td class="rowValue"><span class="${pmtcttag:observationValueByConcept(encounter,hivTestResultId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
					${pmtcttag:observationValueByConcept(encounter,hivTestResultId)}</span></td>
				<td class="rowValue">${pmtcttag:observationValueByConcept(matEnc,dateOfConfinementId)}</td>
			</tr>
			
			<c:remove var="encounter" />
			<c:remove var="matEnc" />
		</c:if>
		<c:if test="${param.type!='1'}">
			<%@ include file="cpnRow.jsp" %>
		</c:if>
	</c:forEach>
</table>
<div id="list_footer">
	<div class="list_footer_info">${pageInfos}</div>
	<div class="list_footer_pages">
		<table><tr>
			<c:forEach items="${numberOfPages}" var="page" varStatus="status">
				<td><form action="generalStatIncpn.list?category=cpn&type=${param.type}&page=${page}" method="post">
					<input type="submit" class="<c:if test="${param.page==page}">list_pageNumberCurrent</c:if><c:if test="${param.page!=page}">list_pageNumber</c:if>" name="page" value="${page}"/>
				</form></td>
			</c:forEach>
		</tr></table>
	</div>
	<div style="clear:both"></div>
</div>
</div>


<%@ include file="/WEB-INF/template/footer.jsp"%>