<%@ include file="template/localIncludes.jsp"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!-- *************************************** -->

<openmrs:require privilege="Add ANC and follow-up information" otherwise="/login.htm" redirect="/module/@MODULE_ID@/addCPNInfoForm.form" />

<spring:hasBindErrors name="pmtctCPNInfo">
	<spring:message code="fix.error"/>
	<br/>
</spring:hasBindErrors>

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${patient.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.pmtctTitle"/>: </h2><span style="font-size: 15px" class="patientNameLabel"><spring:message code="@MODULE_ID@.cpn.title" /></span>
<br/><br/>

<div>
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
</div>

<div id="patientTabs">
	<ul>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="patientInfosTab" class="current "><spring:message code="Patient.information" /></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="requiredInfosTab"><spring:message code="@MODULE_ID@.cpn.pregnancyInfoTitle"/></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="hivSyphilisTab"><spring:message code="@MODULE_ID@.cpn.hivAndSyphilisStatus"/>/<spring:message code="@MODULE_ID@.cpn.sexualPartner"/></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="otherTab"><spring:message code="@MODULE_ID@.general.otherInfo"/></a></li>
	</ul>
</div>

<div id="patientInfos">
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab2"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	<br/><br/>
	
	<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.patientInfo"/></div>
	<div class="box">
		<table>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.attribute.title"/></td>
				<td><span style="padding: 2px; -moz-border-radius:2px; border-style:solid; border-color: #ddd; border-width: 1px 2px 2px 1px;">
						<span style="margin-left: 5px;"><spring:message code="@MODULE_ID@.cpn.civilStatus"/> : <b>${pmtcttag:personAttribute(patient,civilStatusAttributeId)}</b></span>
						<span style="margin-left: 5px;"> / <spring:message code="@MODULE_ID@.cpn.educationLevel"/> : <b>${pmtcttag:personAttribute(patient,educationLevelAttributeId)}</b></span>
						<span style="margin-left: 5px;"> / <spring:message code="@MODULE_ID@.cpn.mainActivity"/> : <b>${pmtcttag:personAttribute(patient,mainActivityAttributeId)}</b></span>
					</span> 
					<span title="Edit person Attributes" class="nextPrevious" id="attributeDiv"><spring:message code="general.edit"/></span>
				</td>
			</tr>
		</table>	
	</div>
	<br/>
		
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab2"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>

</div>

<form:form commandName="pmtctCPNInfo" action="addCPNInfoForm.form?patientId=${patient.patientId}"
	method="post">

	<div id="requiredInfos" style="display: none;">
	<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab1">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab3"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	<br/><br/>
			
	<div class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle" /></div>
		<div class="box">
			<table>
				<tr>
					<td><spring:message code="Encounter.datetime" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.encounterDate"/>"/></span>
						</c:if></td>
					<td><spring:bind path="cpnDate"><input id="encDate" name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="cpnDate" /></td>
				</tr>
				<tr>
					<td><spring:message code="@MODULE_ID@.cpn.cpnNumberTitle"/></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.cpnNumber"/>"/></span>
						</c:if></td>
					<td><spring:bind path="cpnNumber">
							<c:if test="${pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)==null}">
								<input name="${status.expression}" type="text" value="${status.value}" />
							</c:if>
							<c:if test="${pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)!=null}">
								<input name="${status.expression}" type="hidden" value="${pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)}" />
								<input class="lastObsValue" disabled="disabled" type="text" value="${pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)}" />
							</c:if>
						</spring:bind>
					</td>
					<td><form:errors cssClass="error" path="cpnNumber" /></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.location" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.location"/>"/></span>
						</c:if></td>
					<td><spring:bind path="location"><openmrs_tag:locationField formFieldName="${status.expression}"
						initialValue="${status.value}" /></spring:bind></td>
					<td><form:errors cssClass="error" path="location" /></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.provider" /></td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.provider"/>"/></span>
						</c:if></td>
					<td><spring:bind path="provider"><openmrs_tag:userField roles="Provider" formFieldName="${status.expression}" initialValue="${status.value}" /></spring:bind></td>
					<td><form:errors cssClass="error" path="provider" /></td>
				</tr>
			</table>
		</div>
		<br/>
		
		<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.pregnancyInfoTitle" /></div>
		<div class="box"> 
			<table>
				<tr>
					<td>${pmtcttag:conceptNameById(dateOfLastMenstrualPeriodId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.dateOfLastMenstrualPeriod"/>"/></span>
						</c:if></td>
					<td><spring:bind path="dateOfLastMenstrualPeriod"><input id="lastPeriod" onchange="getDPA('<openmrs:datePattern />')" name="${status.expression}" value="${status.value}" size="11" type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="dateOfLastMenstrualPeriod" /></td>
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(numberOfPregnanciesId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.numberOfPregnancies"/>"/></span>
						</c:if></td>
					<td><spring:bind path="numberOfPregnancies"><select name="${status.expression}">
							<c:forEach items="1,2,3,4,5,6,7,8,9" var="i">
								<option value="${i}" <c:if test="${i + '.0' == status.value}">selected='selected'</c:if>>${i}</option>
							</c:forEach>
						</select></spring:bind>
					</td>
					<td><form:errors cssClass="error" path="numberOfPregnancies" /></td>
				</tr>
				<tr><td>${pmtcttag:conceptNameById(numberOfWeeksPregnantId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.numberOfWeeksPregnant"/>"/></span>
						</c:if></td>
					<td><spring:bind path="numberOfWeeksPregnant"><input type="text" id="numberOfWeeksPregnant" value="${status.value}" size="5" name="${status.expression}"/>
							</spring:bind>
					</td>
					<td><form:errors cssClass="error" path="numberOfWeeksPregnant" /></td>
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(pregnancyDueDateId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.pregnantDueDate"/>"/></span>
						</c:if></td>
					<td><spring:bind path="pregnantDueDate"><input id="dpa" name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="pregnantDueDate" /></td>
				</tr>
			</table>
						
		</div>
		<br/>
		
			<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab1">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
			<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab3"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
		
	</div>

	<div id="hivSyphilis" style="display: none;">
	<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab2">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
	<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab4"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	<br/><br/>
	
		<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.hivStatus" /></div>
		<div class="box">
			<table>
				<c:if test="${!empty last_hivStatus}">
					<tr>
						<td>${pmtcttag:conceptNameById(HIVStatusTestNameId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.hivStatus"/>"/></span>
						</c:if></td>
						<td>
							<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_hivStatus)} - was tested ${pmtcttag:observationValue(last_hivStatus)} on <openmrs:formatDate date="${last_hivStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_hivStatus)}</span>
						</td>
						<td></td>
					</tr>
				</c:if>
				<tr>
					<td>${pmtcttag:conceptNameById(dateHIVTestId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.hivTestDate"/>"/></span>
						</c:if></td>
					<td><spring:bind path="hivTestDate"><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="hivTestDate" /></td>
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(resultHIVTestId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.resultOfHivTest"/>"/></span>
						</c:if></td>
					<td><spring:bind path="resultOfHivTest"><select name="${status.expression}">
						<option value="0">--</option>
						<c:forEach var="answer" items="${resultHIVTestAnswers}">
							<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
						</c:forEach>
					</select></spring:bind>
						<c:if test="${!empty last_resultOfHiv}">
							<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_resultOfHiv)} - was tested ${pmtcttag:observationValue(last_resultOfHiv)} on <openmrs:formatDate date="${last_resultOfHiv.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_resultOfHiv)}</span>
						</c:if>
					</td>
					<td><form:errors cssClass="error" path="resultOfHivTest" /></td>
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(dateRemiseResultatVIHId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.dateResultOfHivTestReceived"/>"/></span>
						</c:if></td>
					<td><spring:bind path="dateResultOfHivTestReceived"><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="dateResultOfHivTestReceived" /></td>
				</tr>			
			</table>
		</div><br/>
		
		<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.syphilisStatus" /></div>
		<div class="box">
			<table>
				<tr>
					<td>${pmtcttag:conceptNameById(dateTestSyphilisId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.syphilis_TestDate"/>"/></span>
						</c:if></td>
					<td><!--<spring:bind path="syphilis_TestDate">--><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /><!--</spring:bind>--></td>
					<!--<td><form:errors cssClass="error" path="syphilis_TestDate" /></td>-->
				</tr>
				<tr>	
					<td>${pmtcttag:conceptNameById(resultSyphilisTestId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.resultSyphilisTest"/>"/></span>
						</c:if></td>
					<td><!--<spring:bind path="resultSyphilisTest">--><select name="${status.expression}">
						<option value="0">--</option>
						<c:forEach var="answer" items="${resultSyphilisTestAnswers}">
							<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
						</c:forEach>
						</select><!--</spring:bind>-->
						<c:if test="${!empty last_syphilisStatus}">
							<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_syphilisStatus)} - was tested ${pmtcttag:observationValue(last_syphilisStatus)} on <openmrs:formatDate date="${last_syphilisStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_syphilisStatus)}</span>
						</c:if>
					</td>
					<!--<td><form:errors cssClass="error" path="resultSyphilisTest" /></td>-->
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(dateRemiseResultatSyphilisId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.dateResultOfSyphilisTestReceived"/>"/></span>
						</c:if></td>
					<td><!--<spring:bind path="dateResultOfSyphilisTestReceived">--><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /><!--</spring:bind>--></td>
					<!--<td><form:errors cssClass="error" path="dateResultOfSyphilisTestReceived" /></td>-->
				</tr>
			</table>
		</div><br/>
		
		<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.partnerInfoTitle" /></div>
		<div class="box">
			<table>
				<tr>
					<td>${pmtcttag:conceptNameById(partenaireAttanduId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.patientArrivedWithPartner"/>"/></span>
						</c:if></td>
					<td><spring:bind path="patientArrivedWithPartner"><select id="partenaireAttandu" name="${status.expression}">
						<option value="0">--</option>
						<c:forEach var="answer" items="${resultPartenaireAttenduAnswers}">
							<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
						</c:forEach>
					</select></spring:bind><input type="hidden" id="yesConceptId" value="${yesConceptId}"/></td>
					<td><form:errors cssClass="error" path="patientArrivedWithPartner" /></td>
				</tr>
			</table>
			<div id="partnerInfos">	
				<table>
					<tr>
						<td>${pmtcttag:conceptNameById(dateTestVIHPartenaireId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.hivTestDateOfPartner"/>"/></span>
						</c:if></td>
						<td><spring:bind path="hivTestDateOfPartner"><input name="${status.expression}" value="${status.value}" size="11"
								type="text" onclick="showCalendar(this)" /></spring:bind></td>
						<td><form:errors cssClass="error" path="hivTestDateOfPartner" /></td>
					</tr>
					<tr>
						<td>${pmtcttag:conceptNameById(resultHIVTestForPartnerId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.resultHivTestOfPartner"/>"/></span>
						</c:if></td>
						<td><spring:bind path="resultHivTestOfPartner"><select name="${status.expression}">
							<option value="0">--</option>
							<c:forEach var="answer" items="${resultHIVTestAnswersForPartner}">
								<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
							</c:forEach>
						</select></spring:bind>
							<c:if test="${!empty last_hivPartnerStatus}">
								<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_hivPartnerStatus)} - was tested ${pmtcttag:observationValue(last_hivPartnerStatus)} on <openmrs:formatDate date="${last_hivPartnerStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_hivPartnerStatus)}</span>
							</c:if>
						</td>
						<td><form:errors cssClass="error" path="resultHivTestOfPartner" /></td>
					</tr>
					<tr>
						<td>${pmtcttag:conceptNameById(depistagePartenaireSeparementId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.partnerTestedSeparately"/>"/></span>
						</c:if></td>
						<td><spring:bind path="partnerTestedSeparately"><select name="${status.expression}">
							<option value="0">--</option>
							<c:forEach var="answer" items="${depistagePartenaireSeparementAnswers}">
								<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
							</c:forEach>
						</select></spring:bind></td>
						<td><form:errors cssClass="error" path="partnerTestedSeparately" /></td>
					</tr>
					<tr>
						<td>${pmtcttag:conceptNameById(HIVStatusDisclosedId)}</td>
						<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.disclosedStatusToPartner"/>"/></span>
						</c:if></td>
						<td><spring:bind path="disclosedStatusToPartner"><select name="${status.expression}">
							<option value="0">--</option>
							<c:forEach var="answer" items="${resultHIVStatusDisclosedAnswers}">
								<option value="${answer.key}" <c:if test="${answer.key==status.value}">selected='selected'</c:if>>${answer.value}</option>
							</c:forEach>
						</select></spring:bind></td>
						<td><form:errors cssClass="error" path="disclosedStatusToPartner" /></td>
					</tr>
				</table>
			</div>
		</div>
		<br/>
		
			<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab2">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
			<span title="<spring:message code="@MODULE_ID@.general.nextstep"/>" class="nextPrevious next2Tab4"><spring:message code="@MODULE_ID@.general.nextstep"/>&nbsp;&gt;&gt;</span>
	</div>

	<div id="other" style="display: none;">		
	<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab3">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>
	<br/><br/>
	
		<div class="boxHeader"><spring:message code="@MODULE_ID@.cpn.commentTitle" /></div>
		<div class="box">
			<table>
				<tr>
					<td>${pmtcttag:conceptNameById(dateReceptionMoustiquaireId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.dateMosquitoNetReceived"/>"/></span>
						</c:if></td>
					<td><spring:bind path="dateMosquitoNetReceived"><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="dateMosquitoNetReceived" /></td>
				</tr>
				<tr>
					<td style="vertical-align: top;">${pmtcttag:conceptNameById(commentDescriptionId)}</td>
					<td style="vertical-align: top;"><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.comment"/>"/></span>
						</c:if></td>
					<td><spring:bind path="comment"><textarea cols="40" rows="3" name="${status.expression}">${status.value}</textarea></spring:bind></td>
					<td><form:errors cssClass="error" path="comment" /></td>
				</tr>
				<tr>
					<td>${pmtcttag:conceptNameById(returnVisitDateId)}</td>
					<td><c:if test="${displayHelpMessage=='true'}">
							<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help.cpn.nextVisitDate"/>"/></span>
						</c:if></td>
					<td><spring:bind path="nextVisitDate"><input name="${status.expression}" value="${status.value}" size="11"
							type="text" onclick="showCalendar(this)" /></spring:bind></td>
					<td><form:errors cssClass="error" path="nextVisitDate" /></td>
				</tr>
				<tr>
					<td></td>
					<td></td>
					<td><input type="button" id="bt_save" value="<spring:message code="general.save" />" /></td>
					<td></td>
				</tr>
			</table>
		</div>
		<br/>
		
			<span title="<spring:message code="@MODULE_ID@.general.previousstep"/>" class="nextPrevious previous2Tab3">&lt;&lt;&nbsp;<spring:message code="@MODULE_ID@.general.previousstep"/></span>

	</div>

</form:form>

<div id="divDlg"></div>

<div id="attributeContentDiv" style="display: none;">
	<form id="formField" method="get" action="updatePatientInfo.form">
		<table>
			<tr>
				<td><input type="hidden" value="1" name="formAttribute"/></td>
				<td><input type="hidden" value="${patient.patientId}" name="patientId"/></td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.civilStatus"/></td>
				<td><select name="civilStatus">
					<option value="">..</option>
					<c:forEach var="answer" items="${civilStatusAnswers}">
						<option value="${answer.key}" <c:if test="${answer.value==pmtcttag:personAttribute(patient,civilStatusAttributeId)}">selected='selected'</c:if>>${answer.value}</option>
					</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.educationLevel"/></td>
				<td><select name="educationLevel">
					<option value="">..</option>
					<c:forEach var="answer" items="${educationLevelAnswers}">
						<option value="${answer.key}" <c:if test="${answer.value==pmtcttag:personAttribute(patient,educationLevelAttributeId)}">selected='selected'</c:if>>${answer.value}</option>
					</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.mainActivity"/></td>
				<td><select name="mainActivity">
					<option value="">..</option>
					<c:forEach var="answer" items="${mainActivitiesAnswers}">
						<option value="${answer.key}" <c:if test="${answer.value==pmtcttag:personAttribute(patient,mainActivityAttributeId)}">selected='selected'</c:if>>${answer.value}</option>
					</c:forEach>
					</select>
				</td>
			</tr>
		</table>
			<div style="border-top: 2px solid #ddd; margin-top: 3px; padding-top: 3px;">
				<input type="submit" value="<spring:message code="@MODULE_ID@.cpn.save.attribute"/>"/>
			</div>
	</form>
</div>

<script type="text/javascript">

$j(document).ready(function(){
		$j("#attributeDiv").click(function(){
			showDialog("<spring:message code='@MODULE_ID@.pmtctTitle'/> : <spring:message code='@MODULE_ID@.cpn.attribute.title'/>", $j("#attributeContentDiv").html(), 620, 200);
		});
	});

	function showDialog(title, html, width, height){
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='"+title+"'><p><div id='result'>"+html+"</div></p></div>");
		$j("#dialog").dialog({
			zIndex: 980,
			bgiframe: true,
			height: height,
			width: width,
			modal: true			
		});
	}
</script>

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
	
	function DaysInMonth(days,month,year) {
		var daysInFeb;
		if (month==4 || month==6 || month==9 || month==11) {
			return (days<30)?days:30;
		}
		else if(month==2){
			daysInFeb= (((year % 4 == 0) && ( (!(year % 100 == 0)) || (year % 400 == 0))) ? 29 : 28 );
			return (days<daysInFeb)?days:daysInFeb;
		}
		else {
			return (days<31)?days:31;
		}
	}

	function numberOfWeeks(day,month,year,dateFormat) {
		try{
			var today=getEncounterDate(dateFormat);
			var myDate=new Date();
			var numberOfDays=0;
	
			if(null==today){
				$j("#encDate").val("");
				$j("#encDate").focus();
				return false;
			}
			
			var day=day;
			var month=month;
			var year=year;
			
			myDate.setDate(day);
			myDate.setMonth(month-1);
			myDate.setYear(year);
			myDate.setHours(0);
			myDate.setMinutes(0);
			myDate.setSeconds(0);
	
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);		
	
			if(myDate>today){
				alert("<spring:message code='@MODULE_ID@.date.invalid'/> : <spring:message code='@MODULE_ID@.date.ddr.error.future'/>");
				$j("#numberOfWeeksPregnant").val("0");
				$j("#dpa").val("");
				$j("#lastPeriod").val("");
				$j("#lastPeriod").focus();
				return false;
			}else{
				while(today>myDate){
					myDate=new Date(myDate.getTime() + 86400000);
					myDate.setHours(0);
					myDate.setMinutes(0);
					myDate.setSeconds(0);
					numberOfDays+=1;
					if(numberOfDays>280){
						$j("#lastPeriod").val("");
						alert("<spring:message code='@MODULE_ID@.date.invalid'/> : <spring:message code='@MODULE_ID@.date.error.pregnancy.numberofdays'/>");
						return false;
					}
				}
			}
			var temp1=parseInt(numberOfDays/7);
			var temp2=numberOfDays/7;
			var val=(temp2>temp1)?parseInt(temp1+1):temp1;
			
			$j("#numberOfWeeksPregnant").val(""+val);
			$j("#numberOfWeeksPregnant").change();
		}catch(err){
			alert(err.description);
		}
		return true;
	}

	function getDPA(dateFormat){
		try{
			var myDate = $j("#lastPeriod").val();
			var day;
			var month;
			var year;
			var delimiter = "/";
			
			if(dateFormat=='dd/mm/yyyy' || dateFormat=='jj/mm/aaaa'){
				day=myDate.substring(0,myDate.indexOf(delimiter));
				month=myDate.substring(myDate.indexOf(delimiter)+1,myDate.lastIndexOf(delimiter));
				year=myDate.substring(myDate.lastIndexOf(delimiter)+1);
			}else if(dateFormat=='mm/dd/yyyy' || dateFormat=='mm/jj/aaaa'){
				month=myDate.substring(0,myDate.indexOf(delimiter));
				day=myDate.substring(myDate.indexOf(delimiter)+1,myDate.lastIndexOf(delimiter));
				year=myDate.substring(myDate.lastIndexOf(delimiter)+1);
			}else{
				alert("<spring:message code='@MODULE_ID@.date.invalid'/> : "+dateFormat+": <spring:message code='@MODULE_ID@.date.format.notsupported'/> !");
				$j("#dpa").val("");
				return;
			}
			
			if(numberOfWeeks(day,month,year,dateFormat)){
				if(month<=3){
					month=parseInt(month)+9;
				}else{
					month=month-3;
					year=parseInt(year)+1;
				}
		
				day=DaysInMonth(day,month,year);
				addSevenDays(day,month,year,dateFormat);			
			}
		}catch(err){
			alert(err.description);
		}
	}

	function addSevenDays(day,month,year,dateFormat){
		try{
			var dayzOfMonth=DaysInMonth(((parseInt(day))+7),month,year)

			if(dayzOfMonth>=(parseInt(day)+7)){
				day=parseInt(day)+7;
			}
			else{
				day=(parseInt(day)+7)-dayzOfMonth;
				if(month>11){
					month=1;
					year=parseInt(year)+1;
				}
				else{ 
					month=parseInt(month)+1;
				}
			}
	
			if(dateFormat=='dd/mm/yyyy' || dateFormat=='jj/mm/aaaa')
				$j("#dpa").val(day+"/"+month+"/"+year);
			else if(dateFormat=='mm/dd/yyyy' || dateFormat=='mm/jj/aaaa')
				$j("#dpa").val(month+"/"+day+"/"+year);
		}catch(err){
			alert(err.description);
		}
	}

	function getEncounterDate(dateFormat){
		var myDate = $j("#encDate").val();
		var day;
		var month;
		var year;
		var delimiter = "/";
		try{
			if(dateFormat=='dd/mm/yyyy' || dateFormat=='jj/mm/aaaa'){
				day=myDate.substring(0,myDate.indexOf(delimiter));
				month=myDate.substring(myDate.indexOf(delimiter)+1,myDate.lastIndexOf(delimiter));
				year=myDate.substring(myDate.lastIndexOf(delimiter)+1);
			}else if(dateFormat=='mm/dd/yyyy' || dateFormat=='mm/jj/aaaa'){
				month=myDate.substring(0,myDate.indexOf(delimiter));
				day=myDate.substring(myDate.indexOf(delimiter)+1,myDate.lastIndexOf(delimiter));
				year=myDate.substring(myDate.lastIndexOf(delimiter)+1);
			}else{
				alert("<spring:message code='@MODULE_ID@.date.invalid'/> : "+dateFormat+": <spring:message code='@MODULE_ID@.date.format.notsupported'/> !");
				$j("#encDate").val("");
				$j("#encDate").focus();
				return null;
			}
	
			var myDate=new Date();
			myDate.setDate(day);
			myDate.setMonth(month-1);
			myDate.setYear(year);
			myDate.setHours(0);
			myDate.setMinutes(0);
			myDate.setSeconds(0);
		}catch(err){
			alert(err.description);
		}
		return myDate;
	}

	$j(document).ready( function() {
		$j("#dialog").hide();

		$j(".next2Tab2").click( function() {
			$j("#requiredInfosTab").click();
		});

		$j(".next2Tab3").click( function() {
			$j("#hivSyphilisTab").click();
		});

		$j(".next2Tab4").click( function() {
			$j("#otherTab").click();
		});

		$j(".previous2Tab1").click( function() {
			$j("#patientInfosTab").click();
		});

		$j(".previous2Tab2").click( function() {
			$j("#requiredInfosTab").click();
		});

		$j(".previous2Tab3").click( function() {
			$j("#hivSyphilisTab").click();
		});

		$j("#patientInfoEdit").click( function() {
			$j(".infoPatientEdit").show();
			$j(".infoPatientLabel").hide();
		});

		$j("#btCancel").click( function() {
			$j(".infoPatientEdit").hide();
			$j(".infoPatientLabel").show();
			$j("#patientInfoEdit").show();
		});

		$j("#btSave").click( function() {
			if (confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")) {
				this.form.submit();
	        }
		});

		$j("#bt_save").click( function() {
			if (confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")) {
				this.form.submit();
	        }
		});

		$j("#partenaireAttandu").change(function(){
		    if(document.getElementById("partenaireAttandu").value==document.getElementById("yesConceptId").value)
		     $j("#partnerInfos").show(500);
		    else
		     $j("#partnerInfos").hide(500);
		});

		$j("#partenaireAttandu").change();
		$j(".infoPatientEdit").hide();

	});
</script> 

<%@ include file="/WEB-INF/template/footer.jsp"%>