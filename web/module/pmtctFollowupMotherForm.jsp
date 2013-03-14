<%@ include file="template/localIncludes.jsp"%>
<!-- **************************************************** -->

<openmrs:require privilege="Add ANC and follow-up information" otherwise="/login.htm" redirect="/module/@MODULE_ID@/followupMotherForm.form" />

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${patient.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.pmtctTitle"/>: </h2><span style="font-size: 15px" class="infoPatientLabel"><spring:message code="@MODULE_ID@.portlet.addFollowupMotherInfo" /></span>
<br/><br/>

<div style="margin-bottom: 4px;">
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${patient.patientId}"><spring:message code="@MODULE_ID@.general.viewPatientDashboard"/></a>
	 <!-- :: <span style="font-size: 15px" class="infoPatientLabel"><a href="${pageContext.request.contextPath}/admin/patients/patient.form?patientId=${patient.patientId}">${patient.personName}</a></span> -->
<input type="hidden" value="${patient.patientId}" id="ptId"/>
</div>

<div class="box" id="cpnInfosResume">
	<div style="min-width:250px; float:left; display: inline;">
		<table>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.cpnDate"/></td>
				<td><span class="lastObsValue"><c:if test="${empty encounter}">-</c:if><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></span></td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.cpn.cpnNumberTitle"/></td>
				<td><span class="lastObsValue"><c:if test="${empty pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)}">-</c:if><c:if test="${!empty pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)}">${pmtcttag:personIdentifier(patient,cpnNumberIdentifierID)}</c:if></span></td>
			</tr>
			<tr>
				<td><spring:message code="Encounter.location" /></td>
				<td><span class="lastObsValue"><c:if test="${empty encounter}">-</c:if>${encounter.location.name}</span></td>
			</tr>
			<tr>
				<td>${pmtcttag:conceptNameById(dpaId)}</td>
				<td><span class="lastObsValue">${pmtcttag:observationValueByConcept(encounter,dpaId)}</span></td>
			</tr>
			<tr>
				<td>${pmtcttag:conceptNameById(numberOfPregnanciesId)}</td>
				<td><span class="lastObsValue">${pmtcttag:observationValueByConcept(encounter,numberOfPregnanciesId)}</span></td>
			</tr>
		</table>
	</div>
	
	<div style="min-width:250px; float:right; display: inline;">
		<table>
			<tr>
				<td>${pmtcttag:conceptNameById(hivStatusId)}</td>
				<td><span class="${pmtcttag:observationValue(last_hivStatus)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}" title="The last - ${pmtcttag:observationConceptName(last_hivStatus)} - was tested ${pmtcttag:observationValue(last_hivStatus)} on <openmrs:formatDate date="${last_hivStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_hivStatus)}</span></td>
			</tr>
			<tr>
				<td>${pmtcttag:conceptNameById(CD4CountId)}</td>
				<td><span class="<c:if test="${pmtcttag:observationValue(last_CD4Count)=='-'}">lastObsValue</c:if><c:if test="${pmtcttag:observationValue(last_CD4Count)!='-'}">${pmtcttag:observationValue(last_CD4Count)<criticalLevelOfCD4Count?'lastObsValuePositive':'lastObsValue'}</c:if>" title="The last - ${pmtcttag:observationConceptName(last_CD4Count)} - was '${pmtcttag:observationValue(last_CD4Count)}'  on <openmrs:formatDate date="${last_CD4Count.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_CD4Count)}</span></td>
			</tr>
			<tr>
				<td>${pmtcttag:conceptNameById(partner_hivStatusId)}</td>
				<td><span class="${pmtcttag:observationValueByConcept(encounter,partner_hivStatusId)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}" title="">${pmtcttag:observationValueByConcept(encounter,partner_hivStatusId)}</span></td>
			</tr>
		</table>
	</div>
	<div style="clear: both"></div>

</div>
<br/>

<c:set var="last_returnVisitDateVal" value="${pmtcttag:observationValue(last_returnVisitDate)}"/>
<c:if test="${empty encounter}"><center><span class="error"><spring:message code="@MODULE_ID@.fm.error.cannotDoMotherFollowup"/></span></center></c:if>

<c:if test="${!empty encounter}">
	<div id="patientTabs">
		<ul>
			<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="bilanPreARVTab" class="current "><spring:message code="@MODULE_ID@.fm.bilanPreArv"/></a></li>
			<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="cd4Tab"><spring:message code="@MODULE_ID@.fm.cd4"/></a></li>
			<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="coupleDiscordantTab"><spring:message code="@MODULE_ID@.fm.coupleDiscordant"/></a></li>
		</ul>
	</div>

	<div id="encounterListHolder">
		<div id="bilanPreARV">
			<c:if test="${param.edit}">
				<input type="button" id="newBilanARVEnc" value="<spring:message code='@MODULE_ID@.fm.add.bilanPreArv'/>"/>
			</c:if>
			<br/>
			
			<div class="list_container" style="margin-top: 4px;">
				<div class="list_title">
					<div class="list_title_msg"><spring:message code="@MODULE_ID@.fm.bilanPreArv"/> :: <spring:message code="@MODULE_ID@.fm.relatedEnc"/></div>
					<div class="list_title_bts"></div>
					<div style="clear:both;"></div>
				</div>
				
				<table class="list_data">
					<tr>
						<th class="columnHeader encounterEdit" style="text-align: center;"><spring:message code="general.view"/></th>
						<th class="columnHeader encounterDatetimeHeader"><spring:message code="Encounter.datetime"/></th>
						<th class="columnHeader encounterTypeHeader"><spring:message code="Encounter.type"/></th>
						<th class="columnHeader encounterProviderHeader"><spring:message code="Encounter.provider"/></th>
						<th class="columnHeader encounterLocationHeader"><spring:message code="Encounter.location"/></th>
						<th class="columnHeader encounterEntererHeader"><spring:message code="Encounter.enterer"/></th>
					</tr>
					<c:if test="${empty encList_bPreARV}"><tr><td colspan="6"><center><spring:message code="@MODULE_ID@.general.error.empty"/></center></td></tr></c:if>
					<c:forEach varStatus="status" var="encounter" items="${encList_bPreARV}">
						<tr class="${status.count%2!=0?'even':''}">
							<td class="rowValue" style="text-align: center;"><a onclick="viewEncounter('${encounter.encounterId}');" href="#"><!-- <a href="viewMotherFollowUp.htm?patientId=${patient.patientId}&encounterId=${encounter.encounterId}"> -->
								<img src="${pageContext.request.contextPath}/images/file.gif" title="<spring:message code="general.view"/>" border="0" align="top" />
								</a>
							</td>
							<td class="rowValue"><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
							<td class="rowValue">${encounter.encounterType.name}</td>
							<td class="rowValue">${encounter.provider.personName}</td>
							<td class="rowValue">${encounter.location.name}</td>
							<td class="rowValue">${encounter.creator.personName}</td>
						</tr>
					</c:forEach>
				</table>
				
				<div class="list_footer">
					<div class="list_footer_info">&nbsp;&nbsp;</div>
					<div class="list_footer_pages">&nbsp;&nbsp;</div>
					<div style="clear:both"></div>
				</div>
			</div>
		</div>
		
		<div id="cd4" style="display: none">
			<c:if test="${param.edit}">
				<input type="button" id="newCD4Enc" value="<spring:message code='@MODULE_ID@.fm.add.cd4'/>"/>
			</c:if>
			<br/>
				
			<div class="list_container" style="margin-top: 4px;">
				<div class="list_title">
					<div class="list_title_msg"><spring:message code="@MODULE_ID@.fm.cd4"/> :: <spring:message code="@MODULE_ID@.fm.relatedEnc"/></div>
					<div class="list_title_bts"></div>
					<div style="clear:both;"></div>
				</div>
				
				<table class="list_data">
					<tr>
						<th class="columnHeader encounterEdit" style="text-align: center;"><spring:message code="general.view"/></th>
						<th class="columnHeader encounterDatetimeHeader"><spring:message code="Encounter.datetime"/></th>
						<th class="columnHeader encounterTypeHeader"><spring:message code="Encounter.type"/></th>
						<th class="columnHeader encounterProviderHeader"><spring:message code="Encounter.provider"/></th>
						<th class="columnHeader encounterLocationHeader"><spring:message code="Encounter.location"/></th>
						<th class="columnHeader encounterEntererHeader"><spring:message code="Encounter.enterer"/></th>
					</tr>
					<c:if test="${empty encList_cd4Test}"><tr><td colspan="6"><center><spring:message code="@MODULE_ID@.general.error.empty"/></center></td></tr></c:if>
					<c:forEach varStatus="status" var="encounter" items="${encList_cd4Test}">
						<tr class="${status.count%2!=0?'even':''}">
							<td class="rowValue" style="text-align: center;"><a onclick="viewEncounter('${encounter.encounterId}');" href="#"><!-- <a href="viewMotherFollowUp.htm?patientId=${patient.patientId}&encounterId=${encounter.encounterId}"> -->
								<img src="${pageContext.request.contextPath}/images/file.gif" title="<spring:message code="general.view"/>" border="0" align="top" />
								</a>
							</td>
							<td class="rowValue"><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
							<td class="rowValue">${encounter.encounterType.name}</td>
							<td class="rowValue">${encounter.provider.personName}</td>
							<td class="rowValue">${encounter.location.name}</td>
							<td class="rowValue">${encounter.creator.personName}</td>
						</tr>
					</c:forEach>
				</table>
			
				<div class="list_footer">
					<div class="list_footer_info">&nbsp;&nbsp;</div>
					<div class="list_footer_pages">&nbsp;&nbsp;</div>
					<div style="clear:both"></div>
				</div>
			</div>
		</div>
		
		<div id="coupleDiscordant" style="display: none">
			<c:if test="${pmtcttag:observationValue(last_hivStatus)!=pmtcttag:conceptNameById(positiveId)}">
				<c:if test="${param.edit}">
					<input type="button" id="newCDEnc" value="<spring:message code='@MODULE_ID@.fm.add.hivtest'/>"/>
				</c:if>
			</c:if>
			<br/>
			
			<div class="list_container" style="margin-top: 4px;">
				<div class="list_title">
					<div class="list_title_msg"><spring:message code="@MODULE_ID@.fm.coupleDiscordant"/> :: <spring:message code="@MODULE_ID@.fm.relatedEnc"/></div>
					<div class="list_title_bts"></div>
					<div style="clear:both;"></div>
				</div>
				
				<table class="list_data">
					<tr>
						<th class="columnHeader encounterEdit" style="text-align: center;"><spring:message code="general.view"/></th>
						<th class="columnHeader encounterDatetimeHeader"><spring:message code="Encounter.datetime"/></th>
						<th class="columnHeader encounterTypeHeader"><spring:message code="Encounter.type"/></th>
						<th class="columnHeader encounterProviderHeader"><spring:message code="Encounter.provider"/></th>
						<th class="columnHeader encounterLocationHeader"><spring:message code="Encounter.location"/></th>
						<th class="columnHeader encounterEntererHeader"><spring:message code="Encounter.enterer"/></th>
					</tr>
					<c:if test="${empty encList_cplDisc}"><tr><td colspan="6"><center><spring:message code="@MODULE_ID@.general.error.empty"/></center></td></tr></c:if>
					<c:forEach varStatus="status" var="encounter" items="${encList_cplDisc}">
						<tr class="${status.count%2!=0?'even':''}">
							<td class="rowValue" style="text-align: center;"><a onclick="viewEncounter('${encounter.encounterId}');" href="#"><!-- <a href="viewMotherFollowUp.htm?patientId=${patient.patientId}&encounterId=${encounter.encounterId}"> -->
								<img src="${pageContext.request.contextPath}/images/file.gif" title="<spring:message code="general.view"/>" border="0" align="top" />
								</a>
							</td>
							<td class="rowValue"><openmrs:formatDate date="${encounter.encounterDatetime}" type="medium" /></td>
							<td class="rowValue">${encounter.encounterType.name}</td>
							<td class="rowValue">${encounter.provider.personName}</td>
							<td class="rowValue">${encounter.location.name}</td>
							<td class="rowValue">${encounter.creator.personName}</td>
						</tr>
					</c:forEach>
				</table>
			
				<div class="list_footer">
					<div class="list_footer_info">&nbsp;&nbsp;</div>
					<div class="list_footer_pages">&nbsp;&nbsp;</div>
					<div style="clear:both"></div>
				</div>
			</div>
		</div>			
			
	</div>

	<div id="followupMotherFormContentDiv" style="display: none;">
		<div>
			<!-- <form action="followupMotherForm.form?patientId=${patient.patientId}&edit=${param.edit}&save=true" method="post" id="followupMotherForm">
				<input type="hidden" name="btClicked" value="${btClicked}" id="btClicked"/> -->		
				
				<div id="form_bilanPreARV">				
					<c:if test="${param.edit}">
						<div id="BilanARVPart">
						<div id="errorDiv1Id"></div><br/>
						<form action="followupMotherForm.form?patientId=${patient.patientId}&edit=${param.edit}&save=true&btClicked=1" method="post" id="fmf_1">
							<div class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle" /></div>
							<div class="box">
								<table>
									<tr>
										<td><spring:message code="@MODULE_ID@.cf.scheduledVisitDateTitle" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><b>${last_returnVisitDateVal}</b></td>
										<td></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.datetime" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input id="encounterDate_1" name="encounterDate" value="${encounterDate}" size="11"
												type="text" onclick="showCalendar(this)" /></td>
										<td><span id="encounterDateError_1"></span></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.location" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><select name="location" id="location_1">
													<option value="0">--</option>
													<c:forEach var="loc" items="${locations}">
														<option value="${loc.key}" <c:if test="${loc.key==location}">selected='selected'</c:if>>${loc.value}</option>
													</c:forEach>
												</select></td>
											<td><span id="locationError_1"></span></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.provider" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><select name="provider" id="provider_1">
													<option value="0">--</option>
													<c:forEach var="prov" items="${providers}">
														<option value="${prov.key}" <c:if test="${prov.key==provider}">selected='selected'</c:if>>${prov.value}</option>
													</c:forEach>
												</select></td>
										<td><span id="providerError_1"></span></td>
									</tr>
								</table>
							</div>
							<br/>
					
							<div class="boxHeader"><spring:message code="@MODULE_ID@.fm.bilanPreARVTitle" /></div>
							<div class="box">
								<table>
									<tr>
										<td>${pmtcttag:conceptNameById(hemoglobinId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input name="hemoglobine" id="hemoglobine_1" value="${hemoglobine}" type="text" size="8"/><span title="<spring:message code="@MODULE_ID@.fm.gramsperdecilitertitle"/>"></span></td>
										<td><span id="hemoglobineError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(dateOfCD4CountId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input name="dateOfCD4Count" id="dateOfCD4Count_1" value="${dateOfCD4Count}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="dateOfCD4CountError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(CD4CountId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input name="cd4Count" id="cd4Count_1" value="${cd4Count}" size="8" type="text"/>
											<c:if test="${!empty last_CD4Count}">
												<span class="${pmtcttag:observationValue(last_CD4Count)<criticalLevelOfCD4Count?'lastObsValuePositive':'lastObsValue'}" title="The last - ${pmtcttag:observationConceptName(last_CD4Count)} - was ${pmtcttag:observationValue(last_CD4Count)} on <openmrs:formatDate date="${last_CD4Count.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_CD4Count)}</span>	
											</c:if>
										</td>
										<td><span id="cd4CountError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(dateCD4CountResultReceivedId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input name="dateResultOfCD4CountReceived" id="dateResultOfCD4CountReceived_1" value="${dateResultOfCD4CountReceived}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="dateResultOfCD4CountReceivedError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(tbScreeningId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><select name="tbScreening" id="tbScreening_1">
												<option value="0">--</option>
												<c:forEach var="answer" items="${tbScreeningAnswers}">
													<option value="${answer.key}">${answer.value}</option>
												</c:forEach>
											</select>
											<c:if test="${!empty last_tbScreening}">
												<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_tbScreening)} - was ${pmtcttag:observationValue(last_tbScreening)} on <openmrs:formatDate date="${last_tbScreening.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_tbScreening)}</span>	
											</c:if>
										</td>
										<td><span id="tbScreeningError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(whoStageId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><select name="whoStage" id="whoStage_1">
												<option value="0">--</option>
												<c:forEach var="answer" items="${whoStageAnswers}">
													<option value="${answer.key}">${answer.value}</option>
												</c:forEach>
											</select>
											<c:if test="${!empty last_whoStage}">
												<span class="lastObsValue" title="The last - ${pmtcttag:observationConceptName(last_whoStage)} - was ${pmtcttag:observationValue(last_whoStage)} on <openmrs:formatDate date="${last_whoStage.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_whoStage)}</span>	
											</c:if>
										</td>
										<td><span id="whoStageError_1"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(dateNextRdzId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
											</c:if>
										</td>
										<td><input name="nextVisitDate" id="nextVisitDate_1" value="" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="nextVisitDateError_1"></span></td>
									</tr>
									<tr>
										<td></td>
										<td></td>
										<td><input id="bilanPreArvBt" onclick="submitForm(1);" type="button" style="min-width: 140px;" value="<spring:message code="general.save" />" /></td>
										<td></td>
									</tr>
								</table>
							
								<!-- <br/><a id="hideBilanARVEnc" href="#"><img style="margin-left: 10px;" border="0" src="${pageContext.request.contextPath}/images/moveup.gif" title="<spring:message code="general.hide"/>"/></a> -->
								
							</div><br/>
							</form>
						</div>
					</c:if>			
				</div>
		
				<div id="form_cd4">			
					<c:if test="${param.edit}">
						<div id="cd4Part">
						<div id="errorDiv2Id"></div><br/>
						<form action="followupMotherForm.form?patientId=${patient.patientId}&edit=${param.edit}&save=true&btClicked=2" method="post" id="fmf_2">
							<div class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle" /></div>
							<div class="box">
								<table>
									<tr>
										<td><spring:message code="@MODULE_ID@.cf.scheduledVisitDateTitle" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><b>${last_returnVisitDateVal}</b></td>
										<td></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.datetime" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><input name="encounterDate" id="encounterDate_2" value="${encounterDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="encounterDateError_2"></span></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.location" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><select name="location" id="location_2">
													<option value="0">--</option>
													<c:forEach var="loc" items="${locations}">
														<option value="${loc.key}" <c:if test="${loc.key==location}">selected='selected'</c:if>>${loc.value}</option>
													</c:forEach>
												</select></td>
											<td><span id="locationError_2"></span></td>
									</tr>
									<tr>
										<td><spring:message code="Encounter.provider" /></td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><select name="provider" id="provider_2">
													<option value="0">--</option>
													<c:forEach var="prov" items="${providers}">
														<option value="${prov.key}" <c:if test="${prov.key==provider}">selected='selected'</c:if>>${prov.value}</option>
													</c:forEach>
												</select></td>
										<td><span id="providerError_2"></span></td>
									</tr>
								</table>
							</div>
							<br/>
						
							<div class="boxHeader"><spring:message code="@MODULE_ID@.fm.cd4"/></div>
							<div class="box">
								<table>
									<tr>
										<td>${pmtcttag:conceptNameById(dateOfCD4CountId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><input name="dateOfCD4Count" id="dateOfCD4Count_2" value="${dateOfCD4Count}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="dateOfCD4CountError_2"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(CD4CountId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><input name="cd4Count" id="cd4Count_2" value="" size="8" type="text"/>
											<c:if test="${!empty last_CD4Count}">
												<span class="${pmtcttag:observationValue(last_CD4Count)<criticalLevelOfCD4Count?'lastObsValuePositive':'lastObsValue'}" title="The last - ${pmtcttag:observationConceptName(last_CD4Count)} - was ${pmtcttag:observationValue(last_CD4Count)} on <openmrs:formatDate date="${last_CD4Count.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_CD4Count)}</span>	
											</c:if>
										</td>
										<td><span id="cd4CountError_2"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(dateCD4CountResultReceivedId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><input name="dateCD4CountResultReceived" id="dateCD4CountResultReceived_2" value="${dateCD4CountResultReceived}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="dateCD4CountResultReceivedError_2"></span></td>
									</tr>
									<tr>
										<td>${pmtcttag:conceptNameById(dateNextRdzId)}</td>
										<td><c:if test="${displayHelpMessage=='true'}">
												<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
										</c:if></td>
										<td><input name="nextVisitDate" id="nextVisitDate_2" value="${nextVisitDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
										<td><span id="nextVisitDateError_2"></span></td>
									</tr>
									<tr>
										<td></td>
										<td></td>
										<td><input id="cd4TestBt" onclick="submitForm(2);" type="button" style="min-width: 140px;" value="<spring:message code="general.save" />" /></td>
										<td></td>
									</tr>
								</table>
					
								<!-- <br/><a id="hideCD4Enc" href="#"><img style="margin-left: 10px;" border="0" src="${pageContext.request.contextPath}/images/moveup.gif" title="<spring:message code="general.hide"/>"/></a> -->
					
							</div>
							</form>
						</div>
					</c:if>
				</div>
		
				<div id="form_coupleDiscordant">			
					<c:if test="${pmtcttag:observationValue(last_hivStatus)!=pmtcttag:conceptNameById(positiveId)}">
						<c:if test="${param.edit}">
							<div id="CDPart">
							<div id="errorDiv3Id"></div><br/>
							<form action="followupMotherForm.form?patientId=${patient.patientId}&edit=${param.edit}&save=true&btClicked=3" method="post" id="fmf_3">
								<div class="boxHeader"><spring:message code="@MODULE_ID@.general.requiredInfoTitle" /></div>
								<div class="box">
									<table>
										<tr>
											<td><spring:message code="@MODULE_ID@.cf.scheduledVisitDateTitle" /></td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><b>${pmtcttag:observationValue(last_returnVisitDate)}</b></td>
											<td></td>
										</tr>
										<tr>
											<td><spring:message code="Encounter.datetime" /></td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><input name="encounterDate" id="encounterDate_3" value="${encounterDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
											<td><span id="encounterDateError_3"></span></td>
										</tr>
										<tr>
											<td><spring:message code="Encounter.location" /></td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><select name="location" id="location_3">
													<option value="0">--</option>
													<c:forEach var="loc" items="${locations}">
														<option value="${loc.key}" <c:if test="${loc.key==location}">selected='selected'</c:if>>${loc.value}</option>
													</c:forEach>
												</select></td>
											<td><span id="locationError_3"></span></td>
										</tr>
										<tr>
											<td><spring:message code="Encounter.provider" /></td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><select name="provider" id="provider_3">
													<option value="0">--</option>
													<c:forEach var="prov" items="${providers}">
														<option value="${prov.key}" <c:if test="${prov.key==provider}">selected='selected'</c:if>>${prov.value}</option>
													</c:forEach>
												</select></td>
											<td><span id="providerError_3"></span></td>
										</tr>
									</table>
								</div>
								<br/>
							
								<div class="boxHeader"><spring:message code="@MODULE_ID@.fm.coupleDiscordant"/></div>
								<div class="box">
									<table>
										<tr>
											<td>${pmtcttag:conceptNameById(dateHIVTestId)}</td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><input name="hivTestDate" id="hivTestDate_3" value="${hivTestDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
											<td><span id="hivTestDateError_3"></span></td>
										</tr>
										<tr>
											<td>${pmtcttag:conceptNameById(resultOfHIVTestId)}</td>
											<td><c:if test="${displayHelpMessage=='true'}">
														<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><select name="resultOfHivTest" id="resultOfHivTest_3">
													<option value="0">--</option>
													<c:forEach var="answer" items="${resultHIVTestAnswers}">
														<option value="${answer.key}">${answer.value}</option>
													</c:forEach>
												</select>
												<c:if test="${!empty last_hivStatus}">
													<span class="${pmtcttag:observationValue(last_hivStatus)==pmtcttag:conceptNameById(positiveId)?'lastObsValuePositive':'lastObsValue'}" title="The last - ${pmtcttag:observationConceptName(last_hivStatus)} - was tested ${pmtcttag:observationValue(last_hivStatus)} on <openmrs:formatDate date="${last_hivStatus.obsDatetime}" type="medium" />.">${pmtcttag:observationValue(last_hivStatus)}</span>
												</c:if>
											</td>
											<td><span id="resultOfHivTestError_3"></span></td>
										</tr>
										<tr>
											<td>${pmtcttag:conceptNameById(dateHIVTestReceivedId)}</td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><input name="dateResultOfHivTestReceived" id="dateResultOfHivTestReceived_3" value="${dateResultOfHivTestReceived}" size="11" type="text" onclick="showCalendar(this)" /></td>
											<td><span id="dateResultOfHivTestReceivedError_3"></span></td>
										</tr>
										<tr>
											<td>${pmtcttag:conceptNameById(dateNextRdzId)}</td>
											<td><c:if test="${displayHelpMessage=='true'}">
													<span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span>
												</c:if>
											</td>
											<td><input name="nextVisitDate" id="nextVisitDate_3" value="${nextVisitDate}" size="11" type="text" onclick="showCalendar(this)" /></td>
											<td><span id="nextVisitDateError_3"></span></td>
										</tr>
										<tr>
											<td></td>
											<td></td>
											<td><input id="cplDiscBt" onclick="submitForm(3);" type="button" style="min-width: 140px;" value="<spring:message code="general.save" />" /></td>
											<td></td>
										</tr>
									</table>
								
									<!-- <br/><a id="hideCDEnc" href="#"><img style="margin-left: 10px;" border="0" src="${pageContext.request.contextPath}/images/moveup.gif" title="<spring:message code="general.new"/>"/></a> -->
								
								</div>
								</form>
							</div>
						</c:if>
					</c:if>
				</div>
			<!-- </form> -->
		</div>
	</div>
</c:if>

<c:remove var="last_returnVisitDateVal" />

<div id="divDlg" style="display: none;"></div>

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

	function showEncounterDialog(title, divId, width, height,encId){
		distroyResultDiv();
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='"+title+"'><p><div id='result' style='padding: auto;'><div id='tempDiv'></div></div></p></div>");
		$j("#dialog").dialog({
			zIndex: 980,
			bgiframe: true,
			height: height,
			width: width,
			modal: true			
		});
		
		var ptId=document.getElementById("ptId").value;
		$j("#tempDiv").load('viewMotherFollowUp.htm?patientId='+ptId+'&encounterId='+encId+' #'+divId);
	}

	function showDialog(title, divId, width, height){
		distroyResultDiv();
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='"+title+"'><p><div id='result' style='padding: auto;'><div id='tempDiv'></div></div></p></div>");
		$j("#dialog").dialog({
			zIndex: 980,
			bgiframe: true,
			height: height,
			width: width,
			modal: true			
		});
		
		var ptId=document.getElementById("ptId").value;
		$j("#tempDiv").load('followupMotherForm.form?patientId='+ptId+'&edit=true #'+divId);
	}

	function distroyResultDiv(){
		while(document.getElementById("dialog")){
			var DIVtoRemove = document.getElementById("dialog");
			DIVtoRemove.parentNode.removeChild(DIVtoRemove);
		}
	}

	function submitForm(btId){
		if(validateFormFields(btId)){
			if(confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
				//document.getElementById("btClicked").value=btId;
					document.getElementById("fmf_"+btId).submit();
			}
		}
	}

	function validateFormFields(btId){				
		var valid=true;
		if($j("#encounterDate_"+btId).val()==''){
			$j("#encounterDateError_"+btId).html("*");
			$j("#encounterDateError_"+btId).addClass("error");
			valid=false;
		} else {
			$j("#encounterDateError_"+btId).html("");
			$j("#encounterDateError_"+btId).removeClass("error");
		}

		if($j("#location_"+btId).val()=='0'){
			$j("#locationError_"+btId).html("*");
			$j("#locationError_"+btId).addClass("error");
			valid=false;
		} else {
			$j("#locationError_"+btId).html("");
			$j("#locationError_"+btId).removeClass("error");
		}

		if($j("#provider_"+btId).val()=='0'){
			$j("#providerError_"+btId).html("*");
			$j("#providerError_"+btId).addClass("error");
			valid=false;
		} else {
			$j("#providerError_"+btId).html("");
			$j("#providerError_"+btId).removeClass("error");
		}

		if(btId==1){
			if($j("#hemoglobine_1").val()==''){
				$j("#hemoglobineError_1").html("*");
				$j("#hemoglobineError_1").addClass("error");
				valid=false;
			} else {
				$j("#hemoglobineError_1").html("");
				$j("#hemoglobineError_1").removeClass("error");
			}

			if($j("#dateOfCD4Count_1").val()==''){
				$j("#dateOfCD4CountError_1").html("*");
				$j("#dateOfCD4CountError_1").addClass("error");
				valid=false;
			} else {
				$j("#dateOfCD4CountError_1").html("");
				$j("#dateOfCD4CountError_1").removeClass("error");
			}

			if($j("#cd4Count_1").val()==''){
				$j("#cd4CountError_1").html("*");
				$j("#cd4CountError_1").addClass("error");
				valid=false;
			} else {
				$j("#cd4CountError_1").html("");
				$j("#cd4CountError_1").removeClass("error");
			}

			if($j("#dateResultOfCD4CountReceived_1").val()==''){
				$j("#dateResultOfCD4CountReceivedError_1").html("*");
				$j("#dateResultOfCD4CountReceivedError_1").addClass("error");
				valid=false;
			} else {
				$j("#dateResultOfCD4CountReceivedError_1").html("");
				$j("#dateResultOfCD4CountReceivedError_1").removeClass("error");
			}

			if($j("#tbScreening_1").val()=='0'){
				$j("#tbScreeningError_1").html("*");
				$j("#tbScreeningError_1").addClass("error");
				valid=false;
			} else {
				$j("#tbScreeningError_1").html("");
				$j("#tbScreeningError_1").removeClass("error");
			}

			if($j("#whoStage_1").val()=='0'){
				$j("#whoStageError_1").html("*");
				$j("#whoStageError_1").addClass("error");
				valid=false;
			} else {
				$j("#whoStageError_1").html("");
				$j("#whoStageError_1").removeClass("error");
			}

			if($j("#nextVisitDate_1").val()==''){
				$j("#nextVisitDateError_1").html("*");
				$j("#nextVisitDateError_1").addClass("error");
				valid=false;
			} else {
				$j("#nextVisitDateError_1").html("");
				$j("#nextVisitDateError_1").removeClass("error");
			}
		}

		if(btId==2){
			if($j("#dateOfCD4Count_2").val()==''){
				$j("#dateOfCD4CountError_2").html("*");
				$j("#dateOfCD4CountError_2").addClass("error");
				valid=false;
			} else {
				$j("#dateOfCD4CountError_2").html("");
				$j("#dateOfCD4CountError_2").removeClass("error");
			}

			if($j("#cd4Count_2").val()==''){
				$j("#cd4CountError_2").html("*");
				$j("#cd4CountError_2").addClass("error");
				valid=false;
			} else {
				$j("#cd4CountError_2").html("");
				$j("#cd4CountError_2").removeClass("error");
			}

			if($j("#dateCD4CountResultReceived_2").val()==''){
				$j("#dateCD4CountResultReceivedError_2").html("*");
				$j("#dateCD4CountResultReceivedError_2").addClass("error");
				valid=false;
			} else {
				$j("#dateCD4CountResultReceivedError_2").html("");
				$j("#dateCD4CountResultReceivedError_2").removeClass("error");
			}

			if($j("#nextVisitDate_2").val()==''){
				$j("#nextVisitDateError_2").html("*");
				$j("#nextVisitDateError_2").addClass("error");
				valid=false;
			} else {
				$j("#nextVisitDateError_2").html("");
				$j("#nextVisitDateError_2").removeClass("error");
			}
		}
		
		if(btId==3){
			if($j("#hivTestDate_3").val()==''){
				$j("#hivTestDateError_3").html("*");
				$j("#hivTestDateError_3").addClass("error");
				valid=false;
			} else {
				$j("#hivTestDateError_3").html("");
				$j("#hivTestDateError_3").removeClass("error");
			}
	
			if($j("#resultOfHivTest_3").val()=='0'){
				$j("#resultOfHivTestError_3").html("*");
				$j("#resultOfHivTestError_3").addClass("error");
				valid=false;
			} else {
				$j("#resultOfHivTestError_3").html("");
				$j("#resultOfHivTestError_3").removeClass("error");
			}
	
			if($j("#dateResultOfHivTestReceived_3").val()==''){
				$j("#dateResultOfHivTestReceivedError_3").html("*");
				$j("#dateResultOfHivTestReceivedError_3").addClass("error");
				valid=false;
			} else {
				$j("#dateResultOfHivTestReceivedError_3").html("");
				$j("#dateResultOfHivTestReceivedError_3").removeClass("error");
			}
	
			if($j("#nextVisitDate_3").val()==''){
				$j("#nextVisitDateError_3").html("*");
				$j("#nextVisitDateError_3").addClass("error");
				valid=false;
			} else {
				$j("#nextVisitDateError_3").html("");
				$j("#nextVisitDateError_3").removeClass("error");
			}
		}
		
		if(!valid){
			$j("#errorDiv"+btId+"Id").html("<spring:message code='@MODULE_ID@.general.fillbeforesubmit'/>");
			$j("#errorDiv"+btId+"Id").addClass("error");
		} else {
			$j("#errorDiv"+btId+"Id").html("");
			$j("#errorDiv"+btId+"Id").removeClass("error");
		}
		
		return valid;
	}
	
	function viewEncounter(encId) {
		showEncounterDialog("<spring:message code='@MODULE_ID@.pmtctTitle'/> : View", "encounterViewDiv" , 860, 470, encId);
	}
	
	//JQuery
	$j(document).ready( function() {
		//$j("#cd4Part").hide();
		//$j("#form_coupleDiscordant").hide();
		//$j("#BilanARVPart").hide();
		
		$j("#form_bilanPreARV").html("");
		$j("#form_cd4").html("");
		$j("#form_coupleDiscordant").html("");

		//if($j("#error").val()!=''){
		//	if($j("#btClicked").val("1"))
		//		$j("#bilanPreARV").show();
		//	else if($j("#btClicked").val("2"))
		//		$j("#cd4").show();
		//	else if($j("#btClicked").val("3"))
		//		$j("#coupleDiscordant").show();
		//}

		$j("#newBilanARVEnc").click(function() {
			//$j("#BilanARVPart").show();
			//$j("#form_cd4").hide();
			//$j("#form_coupleDiscordant").hide();
			//$j("#form_bilanPreARV").show();
			showDialog("<spring:message code='@MODULE_ID@.pmtctTitle'/> : <spring:message code='@MODULE_ID@.fm.bilanPreArv'/>", "form_bilanPreARV", 920, 450);
		});
		
		$j("#newCD4Enc").click(function() {
			//$j("#cd4Part").show();			
			//$j("#form_bilanPreARV").hide();
			//$j("#form_coupleDiscordant").hide();
			//$j("#form_cd4").show();
			showDialog("<spring:message code='@MODULE_ID@.pmtctTitle'/> : <spring:message code='@MODULE_ID@.fm.test.cd4'/>", "form_cd4" , 860, 380);
		});
		
		$j("#newCDEnc").click(function() {
			//$j("#CDPart").show();
			//$j("#form_bilanPreARV").hide();
			//$j("#form_cd4").hide();
			//$j("#form_coupleDiscordant").show();
			//$j("#tempDiv").load('followupMotherForm.form?patientId=3262&edit=true #form_coupleDiscordant');
			showDialog("<spring:message code='@MODULE_ID@.pmtctTitle'/> : <spring:message code='@MODULE_ID@.fm.hivtest'/> (<spring:message code='@MODULE_ID@.fm.coupleDiscordant'/>)", "form_coupleDiscordant" , 820, 380);
		});
		
		/*$j("#hideCD4Enc").click(function() {
			$j("#cd4Part").hide();
		});
		$j("#hideCDEnc").click(function() {
			$j("#CDPart").hide();
		});
		$j("#hideBilanARVEnc").click(function() {
			$j("#BilanARVPart").hide();
		});
		
		$j("#bilanPreArvBt").click(function() {
			if(confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
				$j("#btClicked").val("1");
				document.getElementById("followupMotherForm").submit();
			}
		});
		$j("#cd4TestBt").click(function() {
			if(confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
				$j("#btClicked").val("2");
				document.getElementById("followupMotherForm").submit();
			}
		});
		$j("#cplDiscBt").click(function() {
			if(confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")){
				$j("#btClicked").val("3");
				document.getElementById("followupMotherForm").submit();
			}
		});*/
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>