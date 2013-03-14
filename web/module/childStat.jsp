<%@ include file="template/localIncludes.jsp"%>
<%@ include file="template/localHeader.jsp" %>
<%@ include file="template/infantHeader.jsp" %>

<!-- *************************************** -->

<openmrs:require privilege="View PMTCT pediatric tests" otherwise="/login.htm" redirect="/module/@MODULE_ID@/infantResume.list?category=child&type=1&page=1" />

<div id="list_container">
<!-- <span><a href="#" id="viewChart"><spring:message code="@MODULE_ID@.tablelist.viewChart"/></a></span> -->
<div id="list_title">
	<div class="list_title_msg"><spring:message code="${listHeaderTitle}"/></div>
	<div class="list_title_bts">
		<c:if test="${param.type=='1'}">
			<form action="infantResume.list?category=child&type=1&page=1" method="post" style="display: inline;">
				<span><spring:message code="@MODULE_ID@.general.startDate"/> : <input type="text" name="startDate" size="11" value="${startDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<spring:message code="@MODULE_ID@.general.endDate"/> : <input type="text" name="endDate" size="11" value="${endDate}" onclick="showCalendar(this)" />
					&nbsp;&nbsp;<input type="submit" class="list_exportBt" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
				</span>
			</form>
		</c:if>
		<c:if test="${param.type=='5'}">
			<form action="infantResume.list?category=child&type=5&page=1" method="post" style="display: inline;">
				<span><spring:message code="@MODULE_ID@.month"/> : <select name="month">
					<c:forEach items="${monthsList}" var="month" varStatus="status">
						<option value="${status.count}" <c:if test="${param.month==status.count}">selected='selected'</c:if>><spring:message code="${month}"/></option>
					</c:forEach>
					</select>
					&nbsp;<spring:message code="@MODULE_ID@.year"/> : 
					<select name="year">
						<c:forEach items="${years}" var="yr">
							<option value="${yr}" <c:if test="${param.year==yr}">selected='selected'</c:if>>${yr}</option>
						</c:forEach>
					</select>&nbsp;
					<spring:message code="@MODULE_ID@.menu.infantTest"/> : 
					<select name="infantTestSelect">
						<option value="1" <c:if test="${param.infantTestSelect=='1'}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.cf.pcrTestTitle"/></option>
						<option value="2" <c:if test="${param.infantTestSelect=='2'}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.cf.serology9MonthsTitle"/></option>
						<option value="3" <c:if test="${param.infantTestSelect=='3'}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.cf.serology18MonthsTitle"/></option>
					</select>
					&nbsp;<input type="submit" class="list_exportBt" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>" title="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/>
				</span>
			</form>
		</c:if>
		<openmrs:hasPrivilege privilege='Export Collective Patient Data'>
			<form style="display: inline;" action="infantResume.list?category=child&type=${param.type}&page=1&export=csv" method="post">
				<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="CSV"/>
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
			<th class="columnHeader"><spring:message code="@MODULE_ID@.general.motherNames"/></th>
		</openmrs:hasPrivilege>
		<th class="columnHeader"><spring:message code="Person.gender"/></th>
		<th class="columnHeader"><spring:message code="Person.birthdate"/></th>
		<c:if test="${param.type!='1'}">
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cf.scheduledVisitDateTitle"/></th>
		</c:if>
		<c:if test="${param.type=='1'}">
			<th class="columnHeader"><spring:message code="@MODULE_ID@.menu.infantFeedingMethod"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.pcrResult"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.serologyTest9MonthsResult"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.cpn.serologyTest18MonthsResult"/></th>
		</c:if>
	</tr>
	<c:if test="${param.type!='1'}"><c:set var="columns" value="7"/></c:if>
	<c:if test="${param.type=='1'}"><c:set var="columns" value="9"/></c:if>
	<c:if test="${empty numberOfPages}"><tr><td colspan="${columns}"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if>
	<c:remove var="columns" />
	<c:forEach items="${patientList}" var="patient" varStatus="status">
		<c:set var="mother" value="${pmtcttag:childMother(patient[0])}"/>
		<c:if test="${param.type!='1'}">
			<tr class="${status.count%2!=0?'even':''}">
				<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
				<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient[0]}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(patient[0])}</a></td>
				<openmrs:hasPrivilege privilege='View Patient Names'>
					<td class="rowValue">${pmtcttag:personName(patient[0])}</td>
					<td class="rowValue">${pmtcttag:personName(mother.patientId)}&nbsp;(<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${mother.patientId}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${pmtcttag:patientIdentifier(mother.patientId)}</a>)</td>
				</openmrs:hasPrivilege>
				<td class="rowValue"><img border="0"
								src="<c:if test="${patient[1]=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${patient[1]=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
				<td class="rowValue"><openmrs:formatDate date="${patient[3]}" type="medium" /></td>
				<td class="rowValue"><openmrs:formatDate date="${patient[4]}" type="medium" /></td>
			</tr>
		</c:if>
		<c:if test="${param.type=='1'}">
			<%@ include file="infantGenStatRow.jsp" %>
		</c:if>
		<c:remove var="mother" />
	</c:forEach>
</table>
<div id="list_footer">
	<div class="list_footer_info">${pageInfos}</div>
	<div class="list_footer_pages">
		<table><tr>
			<c:forEach items="${numberOfPages}" var="page" varStatus="status">
				<td><form action="infantResume.list?category=child&type=${param.type}&page=${page}" method="post">
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
	<div style="text-align: center;border: 1px solid #8FABC7; padding: 5px 2px;">
	<div id="graph"><img src="chart.htm?chart=infant&type=1" width="400" height="300" /></div>	
		<ul>
			<li><a id="graph_1" href="javascript:void(0)">[ <spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.infantFeedingMethod"/> ]</a></li>
			<li><a id="graph_2" href="javascript:void(0)">[ <spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterPcr"/> ]</a></li>
			<li><a id="graph_3" href="javascript:void(0)">[ <spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterSer9M"/> ]</a></li>
			<li><a id="graph_4" href="javascript:void(0)">[ <spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterSer18M"/> ]</a></li>
		</ul>	
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
		$j("#graph_1").click(function(){
		    $j("#graph").html("<img src='chart.htm?chart=infant&type=1' width='400' height='300' />");
		});
		$j("#graph_2").click(function(){
		    $j("#graph").html("<img src='chart.htm?chart=pcr' width='400' height='300' />");
		});
		$j("#graph_3").click(function(){
		    $j("#graph").html("<img src='chart.htm?chart=infant&type=2' width='400' height='300' />");
		});
		$j("#graph_4").click(function(){
		    $j("#graph").html("<img src='chart.htm?chart=infant&type=3' width='400' height='300' />");
		});
	});
</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>