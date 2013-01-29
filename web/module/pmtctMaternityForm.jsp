<%@ include file="template/localIncludes.jsp"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!-- ************************************************************* -->

<openmrs:require privilege="Add Maternity information" otherwise="/login.htm" redirect="/module/@MODULE_ID@/addMaternityForm.form" />

<!-- <spring:hasBindErrors name="pmtctMaternity">
	<spring:message code="fix.error"/>
	<br/>
</spring:hasBindErrors> -->

<div id="errorDivId" style="margin-bottom: 5px;"></div>

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${patient.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.pmtctTitle"/> : </h2><span style="font-size: 15px" class="infoPatientLabel"><spring:message code="@MODULE_ID@.maternity.title" /></span>
<br/><br/>

<div>
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
</div>

<div id="patientTabs">
	<ul>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="requiredInfosTab" class="current"><spring:message code="@MODULE_ID@.general.requiredInfoTitle"/></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="motherInfosTab"><spring:message code="@MODULE_ID@.maternity.motherInfo"/></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="childInfosTab"><spring:message code="@MODULE_ID@.maternity.childInfo"/></a></li>
	</ul>
</div>

<form name="pmtctForm" method="post" action="addMaternityForm.form?patientId=${patient.patientId}&save=true">
	
	<div id="requiredInfos">
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab2"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	<br/><br/>
	
		<div class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle"/></div>
		<div class="box">
			<table>
				<tr>
					<td><spring:message code="Encounter.datetime" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
					</c:if></td>
					<td><input name="encounterDate" value="${todayDate}" size="11" id="encounterDate" type="text" onclick="showCalendar(this)" /></td>
					<td><span id="encounterDateError"></span></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.location" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
					</c:if></td>
					<td><openmrs_tag:locationField formFieldName="location" initialValue="${defaultLocation.locationId}" /></td>
					<td><span id="locationError"></span></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.provider" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
					</c:if></td>
					<td><openmrs_tag:userField roles="Provider" formFieldName="provider" initialValue="${defaultProvider.userId}" /></td>
					<td><span id="providerError"></span></td>
				</tr>
			</table>
		</div>
		<br/>
		
		<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab2"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	</div>
	
	<div id="motherInfos" style="display: none">
	<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab1">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab3"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	<br/><br/>
		
		<div>
			<div style="float: left; width: 50%;">
				<div class="boxHeader"><spring:message code="@MODULE_ID@.maternity.motherInfo"/></div>
				<div class="box">
					<table>
						<tr>
							<td width="50%">${pmtcttag:conceptNameById(testHivWorkroomId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<select id="hivTestWorkroomId" name="testHivWorkroom">
									<option value="0"><spring:message code="general.no"/></option>
									<option value="1"><spring:message code="general.yes"/></option>
									</select>
								</td>
							<td></td>
						</tr>
						<tr>
							<td><span class="hivStatusPkg">${pmtcttag:conceptNameById(resultHivTestId)}</span></td>
							<td><span class="hivStatusPkg"><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></span></td>
							<td><span class="hivStatusPkg">
								<select name="resultHivTest" id="resultHivTest">
									<option value="0">--</option>
									<c:forEach var="answer" items="${resultHivTestAnswers}">
										<option value="${answer.key}">${answer.value}</option>
									</c:forEach>
								</select>
								<c:if test="${!empty lastHivResultStatus}">
									<span class="${(pmtcttag:observationValue(lastHivResultStatus)==pmtcttag:conceptNameById(positiveId))?'lastObsValuePositive':'lastObsValue'}" title="The last - ${pmtcttag:observationConceptName(lastHivResultStatus)} - was tested ${pmtcttag:observationValue(lastHivResultStatus)} on <openmrs:formatDate date="${lastHivResultStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(lastHivResultStatus)}</span>
								</c:if>
								<span id="resultHivTestError"></span>
								</span>
							</td>
							<td><span class="hivStatusPkg"></span></td>
						</tr>
						<tr>
							<td>${pmtcttag:conceptNameById(dpaId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<input name="dpaDate" id="dpaDate" value="${dpa}" size="11" type="text" onclick="showCalendar(this)" />
							
								<c:if test="${!empty last_dpa}">
									<span class="lastObsValue" title="The - ${pmtcttag:observationConceptName(last_dpa)} - was ${pmtcttag:observationValue(last_dpa)} (value on <openmrs:formatDate date="${last_dpa.obsDatetime}" type="medium" />).">${pmtcttag:observationValue(last_dpa)}</span>
								</c:if>
							</td>
							<td><span id="dpaDateError"></span></td>
						</tr>
						<tr>
							<td>${pmtcttag:conceptNameById(dateConfinementId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<input id="dateOfConfinementId" name="dateConfinement" value="" size="11"
									type="text" onclick="showCalendar(this)"/>
							</td>
							<td><span id="dateOfConfinementError"></span></td>
						</tr>
						<tr>
							<td>${pmtcttag:conceptNameById(takingAntiRetroviralForPmtctId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td colspan="2"><select name="ARVReceived">
									<option value="0"><spring:message code="general.no"/></option>
									<option value="1"><spring:message code="general.yes"/></option>
								</select>
							</td>
						</tr>
					</table>
				</div>
			</div>
			
			<div style="float: right; width: 50%;">
				<div class="boxHeader"><spring:message code="@MODULE_ID@.maternity.motherInfo"/></div>
				<div class="box">
					<table>
						<tr>
							<td>${pmtcttag:conceptNameById(prophylaxisId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<select name="prophylaxis">
									<option value="0"><spring:message code="general.no"/></option>
									<option value="1"><spring:message code="general.yes"/></option>
								</select>
							</td>
							<td></td>
						</tr>
						<tr>
							<td>${pmtcttag:conceptNameById(transferOutDateId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<input name="transferOutDate" id="transferOutDate" value="" size="11"
									type="text" onclick="showCalendar(this)" />
							</td>
							<td><span id="transferOutDateError"></span></td>
						</tr>
						<tr>
							<td id="childBornStatus">${pmtcttag:conceptNameById(chirdBornStatusId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<input name="bornDeadConceptId" value="" size="5"
									type="hidden"/>
							
							<select id="selectChildStatus" name="childBornStatus">
									<c:forEach var="answer" items="${childBornStatusAnswers}">
										<option value="${answer.key}">${answer.value}</option>
									</c:forEach>
								</select>
							</td>
							<td></td>
						</tr>
						<input id="childBornAliveId" value="${childBornAliveId}" type="hidden" />
						<tr>
							<td>${pmtcttag:conceptNameById(commentId)}</td>
							<td><c:if test="${displayHelpMessage=='true'}">
									<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
							</c:if></td>
							<td>
								<textarea cols="40" rows="3" name="comment"></textarea>
							</td>
							<td></td>
						</tr>
						
					</table>
				</div>
			</div>
			
			<div style="clear: both;"></div>
		</div>
		<br/>
		
		<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab1">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
		<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab3"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	</div>
	
	<!-- ========= Concerning the child======= -->
	<div id="childInfos" style="display: none">
	<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab2">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
	<br/><br/>
	
	<span class="noOrdersMessage" id="childBornDeadMsg" style="display: block;margin:15px">(<spring:message code="@MODULE_ID@.maternity.childBornDead"/>)</span>
	<div id="newBornInfo">
	
		<%@ include file="template/infantBornList.jsp"%><br/><br/>
	
	</div>
	<br/>
		
		<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab2">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
		<input type="button" id="bt_save" value="<spring:message code="general.save" />" />
	</div>
	<div align="center"></div>
</form>

<script type="text/javascript">

	function changeTab(tabObj) {
			if (!document.getElementById || !document.createTextNode) {return;}
			if (typeof tabObj == "string")
				tabObj = document.getElementById(tabObj);
			
			if (tabObj) {
				var tabs = tabObj.parentNode.parentNode.getElementsByTagName('a');
				for (var i=0; i<tabs.length; i++) {
					if (tabs[i].className.indexOf('current') != -1) {
						manipulateClass('remove', tabs[i], 'current');
					}
					var divId = tabs[i].id.substring(0, tabs[i].id.lastIndexOf("Tab"));
					var divObj = document.getElementById(divId);
					if (divObj) {
						if (tabs[i].id == tabObj.id)
							divObj.style.display = "";
						else
							divObj.style.display = "none";
					}
				}
				addClass(tabObj, 'current');
				
				setTabCookie(tabObj.id);
			}
			return false;
	    }
	
	//function getChildDOB(){
		//$j("#birthdateSpan").text($j("#dateOfConfinementId").val());
	//}
		
		$j(document).ready(function(){
			$j(".next2Tab2").click(function(){
				$j("#motherInfosTab").click();
			});
			$j(".previous2Tab1").click(function(){
				$j("#requiredInfosTab").click();
			});
			$j(".next2Tab3").click(function(){
				$j("#childInfosTab").click();
			});
			$j(".previous2Tab2").click(function(){
				$j("#motherInfosTab").click();
			});
			$j("#dialog").hide();
			$j("#viewChild").click(function(){	
				$j("#childInfo").toggle(1000);
			});
			
			$j("#childInfo").hide();			
			
			$j("#selectChildStatus").change(function(){
				if($j("#selectChildStatus").val()==$j("#childBornAliveId").val()){
					$j("#childBornDeadMsg").hide();
					$j("#newBornInfo").show();
				}else {
					$j("#newBornInfo").hide();
					$j("#childBornDeadMsg").show();
				}	
			});
			
			$j("#selectChildStatus").change();

			$j("#bt_save").click( function() {
				if(validateInfantFormFields() && validateMotherFormFields()){
					if(confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>"))
						this.form.submit();
						//alert("It looks Ok for submission !");						
				}
			});

			$j("#hivTestWorkroomId").change(function(){
			    if($j("#hivTestWorkroomId").val()=="1")
			     $j(".hivStatusPkg").show(500);
			    else
			     $j(".hivStatusPkg").hide(500);
			});
			$j("#hivTestWorkroomId").change();
		});

		function validateMotherFormFields(){
			var valid=true;
			//var count=0;
			//while(count<index){
			
				if($j("#encounterDate").val()==''){
					$j("#encounterDateError").html("*");
					$j("#encounterDateError").addClass("error");
					valid=false;
				} else {
					$j("#encounterDateError").html("");
					$j("#encounterDateError").removeClass("error");
				}

				if($j("#location").val()==''){
					$j("#locationError").html("*");
					$j("#locationError").addClass("error");
					valid=false;
				} else {
					$j("#locationError").html("");
					$j("#locationError").removeClass("error");
				}

				if($j("#provider").val()==''){
					$j("#providerError").html("*");
					$j("#providerError").addClass("error");
					valid=false;
				} else {
					$j("#providerError").html("");
					$j("#providerError").removeClass("error");
				}

				if($j("#transferOutDate").val()=='' || $j("#transferOutDate").val()<$j("#dateOfConfinementId").val()){
					$j("#transferOutDateError").html("*");
					$j("#transferOutDateError").addClass("error");
					valid=false;
				} else {
					$j("#transferOutDateError").html("");
					$j("#transferOutDateError").removeClass("error");
				}

				if($j("#dateOfConfinementId").val()=='' || $j("#dateOfConfinement").val()<$j("#encounterDate").val()){
					$j("#dateOfConfinementError").html("*");
					$j("#dateOfConfinementError").addClass("error");
					valid=false;
				} else {
					$j("#dateOfConfinementError").html("");
					$j("#dateOfConfinementError").removeClass("error");
				}

				if($j("#dpaDate").val()==''){
					$j("#dpaDateError").html("*");
					$j("#dpaDateError").addClass("error");
					valid=false;
				} else {
					$j("#dpaDateError").html("");
					$j("#dpaDateError").removeClass("error");
				}
				
				if($j("#hivTestWorkroomId").val()=='1'){
					if($j("#resultHivTest").val()=='0'){
						$j("#resultHivTestError").html("*");
						$j("#resultHivTestError").addClass("error");
						valid=false;
					} else {
						$j("#resultHivTestError").html("");
						$j("#resultHivTestError").removeClass("error");
					}
				}

				if(!valid){
					$j("#errorDivId").html("<spring:message code='@MODULE_ID@.error.fix'/>");
					$j("#errorDivId").addClass("error");
				} else {
					$j("#errorDivId").html("");
					$j("#errorDivId").removeClass("error");
				}
			//}
			return valid;
		}
		
</script> 

<%@ include file="/WEB-INF/template/footer.jsp"%>