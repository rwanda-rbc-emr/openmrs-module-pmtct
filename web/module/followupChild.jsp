<%@ include file="template/localIncludes.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- *************************************** -->

<spring:hasBindErrors name="pmtctChildTest">
	<spring:message code="fix.error"/>
	<br/>
</spring:hasBindErrors>

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${patient.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.pmtctTitle"/> : </h2><span style="font-size: 15px" class="infoPatientLabel"><spring:message code="@MODULE_ID@.cf.title" /></span>
<c:if test="${encounter!=null}">(<a href="${pageContext.request.contextPath}/admin/encounters/encounter.form?encounterId=${encounter.encounterId}" title="<spring:message code="general.edit"/>"><spring:message code="general.edit"/></a>)</c:if>
<br/><br/>

<div style="width: 90%;margin: auto auto auto auto; margin-bottom: 4px;">
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
	 <!-- :: <span style="font-size: 15px" class="infoPatientLabel"><a href="${pageContext.request.contextPath}/admin/patients/patient.form?patientId=${patient.patientId}">${patient.personName}</a></span> -->
</div>

<!-- <div id="list_container">
<div id="list_title">
	<div class="list_title_msg"><spring:message code="@MODULE_ID@.cpn.pmtctDrugOrder"/></div>
	<div class="list_title_bts">
		<form style="display: inline;" action="childTestForm.form?patientId=${patient.patientId}&encounterType=${param.encounterType}&export=csv" method="post">
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
				<td class="rowValue"><a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}" title="<spring:message code="@MODULE_ID@.general.viewPatientDashboard"/>">${((param.page-1)*pageSize)+status.count}.</a></td>
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

<form:form commandName="pmtctChildTest"
	action="childTestForm.form?patientId=${patient.patientId}&encounterType=${param.encounterType}"
	method="post">
	
	<div style="width: 90%;margin: auto auto auto auto;">
	
		<div class="divBox" style="width: 25%; float: left;">
			<table>
				<tr>
					<td><input type="radio" '<c:if test="${param.encounterType==pcrEncId}">checked="checked"</c:if>' onclick=document.location.href='childTestForm.form?patientId=${patient.patientId}&encounterType=${pcrEncId}'; id="pcrTest" name="encounterTypeId" value="${pcrEncId}" />
						<label for="pcrTest"> <spring:message code="@MODULE_ID@.cf.pcrTestTitle" /></label>
					</td>
				</tr>
				<tr>
					<td><input type="radio"'<c:if test="${param.encounterType==ser9MonthEncId}">checked="checked"</c:if>' onclick=document.location.href='childTestForm.form?patientId=${patient.patientId}&encounterType=${ser9MonthEncId}'; id="serology6Month" name="encounterTypeId" value="${ser9MonthEncId}" />
						<label for="serology6Month"> <spring:message code="@MODULE_ID@.cf.serology9MonthsTitle" /> </label>
					</td>
				</tr>
				<tr>
					<td><input type="radio"'<c:if test="${param.encounterType==ser18MonthEncId}">checked="checked"</c:if>' onclick=document.location.href='childTestForm.form?patientId=${patient.patientId}&encounterType=${ser18MonthEncId}'; id="serology18Month" name="encounterTypeId" value="${ser18MonthEncId}" />
						<label for="serology18Month"> <spring:message code="@MODULE_ID@.cf.serology18MonthsTitle" /></label>
					</td>
				</tr>
			</table>
		</div>
		
		<div style="width: 74%; float: right; border: 1px solid #8FABC7; -moz-border-radius: 3px; padding: 2px;">
			<c:if test="${encounter==null}">
			
			<b class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle" /></b>
			<div class="box">
				<table>
					<tr>
						<td><spring:message code="@MODULE_ID@.cf.scheduledVisitDateTitle" /></td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td class="infoPatientLabel">${returnDateVisit}</td>
						<td></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.datetime"/></td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="encounterDate"><input name="${status.expression}" value="${status.value}" size="11" type="text" onclick="showCalendar(this)" /></spring:bind></td>
						<td><form:errors cssClass="error" path="encounterDate" /></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.location" /></td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="location"><openmrs_tag:locationField formFieldName="${status.expression}" initialValue="${status.value}" /></spring:bind></td>
						<td><form:errors cssClass="error" path="location" /></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.provider" /></td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="provider"><openmrs_tag:userField roles="Provider" formFieldName="${status.expression}" initialValue="${status.value}" /></spring:bind></td>
						<td><form:errors cssClass="error" path="provider" /></td>
					</tr>
				</table>
			</div>
			<br />
			
			<b class="boxHeader"><c:if test="${param.encounterType!=ser18MonthEncId}"><spring:message code="@MODULE_ID@.cpn.hivStatus" /></c:if>
				<c:if test="${param.encounterType==ser18MonthEncId}"><spring:message code="@MODULE_ID@.cf.hivFinalStatusTitle" /></c:if></b>
	
			<div class="box">
				<table>			
					<tr>
						<td>${pmtcttag:conceptNameById(dateHIVTestId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="hivTestDate"><input name="${status.expression}" value="${status.value}" size="11" type="text" onclick="showCalendar(this)" /></spring:bind></td>
						<td><form:errors cssClass="error" path="hivTestDate" /></td>
					</tr>
					<tr>
						<td>${pmtcttag:conceptNameById(resultHIVTestId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="resultOfHivTest">
							<select id="<c:if test="${param.encounterType==ser18MonthEncId}">hivStatus</c:if>" name="${status.expression}">
							<option value="0">--</option>
							<c:forEach var="answer" items="${resultHIVTestAnswers}">
								<option value="${answer.key}" <c:if test="${answer.key == status.value}">selected='selected'</c:if>>${answer.value}</option>
							</c:forEach>
							</select></spring:bind>
							<input type="hidden" id="positiveId" value="${positiveId}"/>
						</td>
						<td><form:errors cssClass="error" path="resultOfHivTest" /></td>
					</tr>
					<tr>
						<td>${pmtcttag:conceptNameById(dateOfReturnedResultId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="dateOfReturnedResult"><input name="${status.expression}" value="${status.value}" size="11" type="text" onclick="showCalendar(this)" /></spring:bind></td>
						<td><form:errors cssClass="error" path="dateOfReturnedResult" /></td>
					</tr>
					<c:if test="${param.encounterType!=ser18MonthEncId}">
						<tr>
							<td>${pmtcttag:conceptNameById(returnVisitDateId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td><spring:bind path="returnedVisitDate"><input name="${status.expression}" value="${status.value}" size="11" type="text" onclick="showCalendar(this)" /></spring:bind></td>
							<td><form:errors cssClass="error" path="returnedVisitDate" /></td>
						</tr>
						<tr>
							<td></td>
							<td></td>
							<td><input type="button" id="submit_Enc" value="<spring:message code="@MODULE_ID@.general.save" />"></td>
							<td></td>
						</tr>
					</c:if>
				</table>
			</div>
			<c:if test="${param.encounterType==ser18MonthEncId}">
				<br/>
				<h3 class="exitCare"><spring:message code="@MODULE_ID@.cf.reasonExitCareTitle" /></h3>
				<div class="divBox">
				<table>
					<tr class="exitCare">
						<td>${pmtcttag:conceptNameById(reasonForExitingCareId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
								<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
						</c:if></td>
						<td><spring:bind path="reasonOfExitingCare"><select id="reasonOfExitingCare" name="${status.expression}">
							<option value="0">--</option>
							<c:forEach var="answer" items="${reasonForExitingCareAnswers}">
								<option value="${answer.key}" <c:if test="${answer.key == status.value}">selected='selected'</c:if>>${answer.value}</option>
							</c:forEach>
						</select></spring:bind></td>
						<td><span Class="error" id="reasonOfExitingCareError"></span></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td><input type="button" id="submit_EncSer18Month" value="<spring:message code="@MODULE_ID@.general.save" />"></td>
						<td></td>
					</tr>
				</table>
				</div>
			</c:if>
		</c:if>
	
		<c:if test="${encounter!=null}">
			
			<b class="boxHeader"><spring:message code="@MODULE_ID@.cpn.encounter.summary"/></b>
	
			<div class="box">
				<table>
					<tr>
						<td><spring:message code="Encounter.patient" /></td>
						<td class="infoPatientLabel">: <a href="${pageContext.request.contextPath}/admin/patients/patient.form?patientId=${encounter.patient.patientId}">${encounter.patient.personName}</a></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.datetime" /></td>
						<td class="infoPatientLabel">: <openmrs:formatDate
							date="${encounter.encounterDatetime}" type="medium" /></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.type" /></td>
						<td class="infoPatientLabel">: ${encounter.encounterType.name}</td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.location" /></td>
						<td class="infoPatientLabel">: ${encounter.location.name}</td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.provider" /></td>
						<td class="infoPatientLabel">: <a href="${pageContext.request.contextPath}/admin/users/user.form?userId=${encounter.provider.personId}">${encounter.provider.personName}</a></td>
					</tr>
				</table>
			</div>
			<br/>
			
			<b class="boxHeader"><spring:message code="Encounter.observations"/></b>
			<div class="box">
				<table>
					<c:forEach var="obs" items="${encounter.obs}">
						<tr>
							<td>${pmtcttag:observationConceptName(obs)}</td>
							<td class="infoPatientLabel">${pmtcttag:observationValue(obs)}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
	
		</c:if>
		</div>
	
		<div style="clear:both;"></div>
	</div>
	
</form:form>

<script type="text/javascript">

	$j(document).ready( function() {
		$j("#hivStatus").change(function(){
		    if($j("#hivStatus").val()!=$j("#positiveId").val())
		    	$j(".exitCare").show();
		    else
		    	$j(".exitCare").hide();
		});
		$j("#hivStatus").change();

		$j("#submit_Enc").click(function(){
			if (confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
					this.form.submit();
			}
		});

		$j("#reasonOfExitingCareError").hide();
		$j("#submit_EncSer18Month").click(function(){
			if (confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
				//if($j("#reasonOfExitingCare").val()==$j("#positiveId").val()){
				//alert($j("#hivStatus").val()+"-"+$j("#positiveId").val());
				if($j("#hivStatus").val()==$j("#positiveId").val()){
					$j("#reasonOfExitingCareError").hide();
					this.form.submit();
				}
				else{
					if($j("#reasonOfExitingCare").val()!="0"){
						this.form.submit();
					}
					else {
						$j("#reasonOfExitingCareError").html("This field is required.");
						$j("#reasonOfExitingCareError").show();
					}
				}
			}
		});
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>