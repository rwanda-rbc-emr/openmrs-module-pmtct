<%@ include file="template/localIncludes.jsp"%>

<!-- *************************************** -->

<h2 style="display: inline;"><spring:message code="@MODULE_ID@.overviewBoxTitle"/> :: 
<span style="color:#8FABC7;"><spring:message code="@MODULE_ID@.statisticsAndGraphics"/></span></h2>
<br/><br/>

<div>
	<form action="graphicandstatistic.htm" method="get">
		<table>
			<tr>
				<td><spring:message code="@MODULE_ID@.graph.criteria"/></td>
				<td><input type="hidden" name="type" id="type" value="${param.type}"/></td>
				<td><select name="chart" id="selectChart">
						<option id="h1"value="hivStatus"><spring:message code="@MODULE_ID@.menu.patientInPmtct"/> : <spring:message code="@MODULE_ID@.cpn.hivStatus"/></option>
						<option id="m2" value="maternity" <c:if test="${param.chart=='maternity' && param.type==2}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.maternity"/> : <spring:message code="@MODULE_ID@.menu.expectedPatientInMaternity"/> , <spring:message code="@MODULE_ID@.menu.partnerStatus"/></option>
						<option id="m3" value="maternity" <c:if test="${param.chart=='maternity' && param.type==3}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.maternity"/> : <spring:message code="@MODULE_ID@.menu.hivTestInDeliveryRoom"/></option>
						<option id="m1" value="maternity" <c:if test="${param.chart=='maternity' && param.type==1}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.maternity"/> : <spring:message code="@MODULE_ID@.cpn.childBornStatus"/></option>
						<option id="i1" value="infant" <c:if test="${param.chart=='infant' && param.type==1}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.infantFeedingMethod"/></option>
						<option id="i4" value="infant" <c:if test="${param.chart=='infant' && param.type==4}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterPcr"/></option>
						<option id="i2" value="infant" <c:if test="${param.chart=='infant' && param.type==2}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterSer9M"/></option>
						<option id="i3" value="infant" <c:if test="${param.chart=='infant' && param.type==3}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.menu.infantTest"/> : <spring:message code="@MODULE_ID@.menu.resultOfHivTestAfterSer18M"/></option>
					</select>
				</td>
				<td><input type="submit" value="Refresh"/></td>
			</tr>
		</table>
	</form>
	<br/><br/>
	
	<div style="width: 97%; margin: auto;">
		<div style="float: left; width: 35%;">
			<div class="list_container">
				<div class="list_title"><spring:message code="@MODULE_ID@.graph.chart"/></div>
				<div class="chartHolder">
					<center><img src="chart.htm?chart=${param.chart}&type=${param.type}" width="400" height="300" /></center>
				</div>
			</div>
		</div>
		
		<div style="float: right; width: 62%;">
			<!-- < %@ include file="template/vctHistoricalDetails.jsp"%> -->
		</div>
		
		<div style="clear: both;"></div>
	</div>
</div>
<br/><br/><br/>

<script type="text/javascript">

	$j("#selectChart").change(function(){
		var selectedId = $j("#selectChart option:selected").attr('id');
		$j("#type").val(selectedId.substring(1));
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>