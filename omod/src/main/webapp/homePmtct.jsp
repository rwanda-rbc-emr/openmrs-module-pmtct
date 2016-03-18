<%@ include file="template/localIncludes.jsp"%>
<%@ include file="template/localHeader.jsp" %>

<!-- *************************************** -->

<openmrs:require privilege="View patients in PMTCT" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientInPmtct.list?page=1" />

<div id="list_container">
<!-- <span><a href="#" id="viewChart"><spring:message code="@MODULE_ID@.tablelist.viewChart"/></a></span> -->
<div id="list_title">
	<div class="list_title_msg"><spring:message code="@MODULE_ID@.patientInPmtctTitle"/></div>
	<div class="list_title_bts">
		<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
			<form action="patientInPmtct.list" method="get" style="display: inline;">
				<input type="hidden" name="page" value="1"/>
				<span title="<spring:message code="@MODULE_ID@.tablelist.periodBasedOnEnrollmentDate"/>">
					<input type="checkbox" <c:if test="${param.ckbxPeriod=='0'}">checked=checked</c:if> id="ckbxPeriod" name="ckbxPeriod" value="<c:if test="${param.ckbxPeriod=='0'}">0</c:if><c:if test="${param.ckbxPeriod!='0'}">1</c:if>"/><label for="ckbxPeriod"><spring:message code="@MODULE_ID@.tablelist.period"/></label>
					<span id="period"><spring:message code="@MODULE_ID@.general.startDate"/> : <input type="text" name="startDate" size="11" value="${param.startDate}" onclick="showCalendar(this)" />
						&nbsp;&nbsp;<spring:message code="@MODULE_ID@.general.endDate"/> : <input type="text" name="endDate" size="11" value="${param.endDate}" onclick="showCalendar(this)" />
						&nbsp;&nbsp;<input type="submit" class="list_exportBt" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
					</span>
				</span>
			</form>
			<form style="display: inline;" action="patientInPmtct.list?${parameters}&export=csv" method="post">
				<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.exportToCSV"/>" value="CSV"/>
			</form>
		</openmrs:hasPrivilege>			
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
		<th class="columnHeader"><spring:message code="Person.gender"/></th>
		<th class="columnHeader"><spring:message code="Person.birthdate"/></th>
		<th class="columnHeader"><spring:message code="Program.dateEnrolled"/></th>
		<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.hivStatus"/></th>
	</tr>
	<c:if test="${empty numberOfPages}"><tr><td colspan="7"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if>
	<c:forEach items="${patientList}" var="patient" varStatus="status">
		<tr class="${status.count%2!=0?'even':''}">
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
			<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
			<openmrs:hasPrivilege privilege='View Patient Names'>
				<td class="rowValue">${pmtcttag:personName(patient[0])}</td>
			</openmrs:hasPrivilege>
			<td class="rowValue"><img border="0"
						src="<c:if test="${patient[1]=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient[1]=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
			<td class="rowValue"><openmrs:formatDate date="${patient[2]}" type="medium" /></td>
			<td class="rowValue"><openmrs:formatDate date="${patient[3]}" type="medium" /></td>
			<td class="rowValue"><span class="${pmtcttag:lastObsValueByConceptId(patient[0],hivTestResultId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}">
				${pmtcttag:lastObsValueByConceptId(patient[0],hivTestResultId)}</span></td>
		</tr>
	</c:forEach>
</table>
<div id="list_footer">
	<div class="list_footer_info">${pageInfos}</div>
	<div class="list_footer_pages">
		<table><tr>
			<c:forEach items="${numberOfPages}" var="page" varStatus="status">
				<td><form action="patientInPmtct.list?page=${page}" method="post">
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
	<div class="box" style="text-align: center;">
		<img src="chart.htm?chart=hivStatus" width="400" height="300" />
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

		if($j("#ckbxPeriod").val()=="1")
			$j("#period").hide();
		$j("#ckbxPeriod").click(function(){
			if($j("#ckbxPeriod").val()=="1"){
				$j("#period").show();
				$j("#ckbxPeriod").val("0");
			}
			else {
				$j("#period").hide();
				$j("#ckbxPeriod").val("1");
			}
		});
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>