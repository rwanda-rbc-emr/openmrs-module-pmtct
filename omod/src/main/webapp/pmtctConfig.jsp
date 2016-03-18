<%@ include file="template/localIncludes.jsp"%>

<!-- *************************************** -->

<h2 style="display: inline;"><spring:message code="@MODULE_ID@.overviewBoxTitle"/> :: 
<span style="color:#8FABC7;"><spring:message code="@MODULE_ID@.config.title"/></span></h2>
<br/><br/>

<div style="width: 90%;margin: auto auto auto auto; font-size: 100%">
	<c:if test="${pmtctConfigured.propertyValue=='true'}">
		<div class="box"><spring:message code="@MODULE_ID@.config.configuredproperly"/></div>
	</c:if>
	
	<c:if test="${pmtctConfigured.propertyValue=='false'}">
		<div style="border: 1px solid red; color: red; padding: 3px;"><spring:message code="@MODULE_ID@.config.notConfigured"/></div>
	</c:if>
</div>
<br/>

<form action="config.htm?edit=true" method="post">

<!-- <b class="boxHeader" style="width: 90%;margin: auto auto auto auto;"><spring:message code="@MODULE_ID@.config.criticalLevelOfCD4Count"/></b>
<div class="box" style="width: 90%;margin: auto auto auto auto;">
	<table class="configTable">
			<tr>
				<td class="configDescription">
					<spring:message code="@MODULE_ID@.config.displayHelpMessage"/>
				</td>
				<td class="configSelect">
					<select name="displayHelpMessage">
						<option value="1" <c:if test="${displayHelpMessage=='true'}">selected='selected'</c:if>>Yes</option>
						<option value="0" <c:if test="${displayHelpMessage=='false'}">selected='selected'</c:if>>No</option>
					</select>
				</td>
				<td></td>
			</tr>
	</table>
</div>
<br/> -->

<b class="boxHeader" style="width: 90%;margin: auto auto auto auto;"><spring:message code="@MODULE_ID@.config.types"/></b>
<div class="box" style="width: 90%;margin: auto auto auto auto;">
	
	<table class="configTable">
			<tr class="even">
				<td class="configDescription">
					<c:if test="${gp_IdentifierType.propertyValue!=''}">${pmtcttag:identifierTypeNameById(gp_IdentifierType.propertyValue)}</c:if>
				</td>
				<td class="configSelect"><select name="identifierType" class="<c:if test="${gp_IdentifierType.propertyValue==''}">missingTypeInput</c:if>">
						<option value="">...</option>
						<c:forEach items="${pmtctConfig.existingIdentifierTypes}" var="identifierType">
							<option value="${identifierType.patientIdentifierTypeId}" <c:if test="${currentIdentifierTypeId==identifierType.patientIdentifierTypeId}">selected='selected'</c:if>>${identifierType.name}</option>
						</c:forEach>
					</select>
				</td>				
			</tr>
			<tr class="even">
				<td colspan="3" class="configComment">${gp_IdentifierType.description}</td>					
			</tr>
	</table>
	
	<table class="configTable">
			<tr>
				<td class="configDescription">
					<c:if test="${gp_OrderType.propertyValue!=''}">${pmtcttag:drugOrderTypeNameById(gp_OrderType.propertyValue)}</c:if>
				</td>
				<td class="configSelect"><select name="drugOrderType" class="<c:if test="${gp_OrderType.propertyValue==''}">missingTypeInput</c:if>">
						<option value="">...</option>
						<c:forEach items="${pmtctConfig.existingDrugOrderTypes}" var="drugOrderType">
							<option value="${drugOrderType.orderTypeId}" <c:if test="${currentDrugOrderTypeId==drugOrderType.orderTypeId}">selected='selected'</c:if>>${drugOrderType.name}</option>
						</c:forEach>
					</select>
				</td>				
			</tr>
			<tr>
				<td colspan="2" class="configComment">${gp_OrderType.description}</td>					
			</tr>
	</table>

	<table class="configTable">
			<tr class="even">
				<td class="configDescription">
					<c:if test="${gp_relationShipType.propertyValue!=''}">${pmtcttag:relationshipTypeNameById(gp_relationShipType.propertyValue)}</c:if>
				</td>
				<td class="configSelect"><select name="relationshipType" class="<c:if test="${gp_relationShipType.propertyValue==''}">missingTypeInput</c:if>">
						<option value="">...</option>
						<c:forEach items="${pmtctConfig.existingRelationshipTypes}" var="relation">
							<option value="${relation.relationshipTypeId}" <c:if test="${currentRelationShipTypeId==relation.relationshipTypeId}">selected='selected'</c:if>>${relation.aIsToB} / ${relation.bIsToA}</option>
						</c:forEach>
					</select>
				</td>				
			</tr>
			<tr class="even">
				<td colspan="2" class="configComment">${gp_relationShipType.description}</td>					
			</tr>
			
	</table>
</div>
<br/>

<b class="boxHeader" style="width: 90%;margin: auto auto auto auto;"><spring:message code="@MODULE_ID@.config.encounterTypes"/></b>
<div class="box" style="width: 90%;margin: auto auto auto auto;">
	
	<table class="configTable">
			<c:forEach items="${pmtctConfig.gp_encounterTypes}" var="encType" varStatus="status">
				<tr class="<c:if test="${(status.count)%2==0}">even</c:if>">
					<td class="configDescription"><c:if test="${encType!=null}">${pmtcttag:encounterTypeNameById(encType.propertyValue)}</c:if></td>
					<td class="configSelect"><select name="${pmtcttag:gpParser(encType.property)}">
							<option value="">--</option>
							<c:forEach items="${pmtctConfig.existingEncTypes}" var="encounterType">
								<option value="${encounterType.encounterTypeId}" <c:if test="${encType.propertyValue==encounterType.encounterTypeId}">selected='selected'</c:if>>${encounterType.name}</option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr class="<c:if test="${(status.count)%2==0}">even</c:if>">
					<td colspan="2" class="configComment">${encType.description}</td>					
				</tr>
			</c:forEach>
		</table>
	
</div>
<br/>

<b class="boxHeader" style="width: 90%;margin: auto auto auto auto;"><spring:message code="@MODULE_ID@.config.concepts"/></b>
<div class="box" style="width: 90%;margin: auto auto auto auto;">
	
	<table class="configTable">
		<c:forEach items="${pmtctConfig.gp_pmtctconcepts}" var="concept" varStatus="status">
			<tr class="<c:if test="${(status.count+1)%2==0}">even</c:if>">
				<td class="configDescription">${pmtcttag:conceptNameById(concept.propertyValue)}</td>
				<td class="configSelect"><openmrs:fieldGen formFieldName="${pmtcttag:gpParser(concept.property)}" type="org.openmrs.Concept" val="${pmtcttag:checkIfConceptExistByIdAsString(concept.propertyValue)}"/></td>
			</tr>
			<tr class="<c:if test="${(status.count+1)%2==0}">even</c:if>">
				<td colspan="2" class="configComment">${concept.description}</td>					
			</tr>
		</c:forEach>
	</table>
	
</div>
<br/>

<div style="width: 90%;margin: auto auto auto auto;"><span title="${pmtctConfigured.description}"><input <c:if test="${pmtctConfigured.propertyValue=='true' || pmtctConfigured.propertyValue=='TRUE'}">checked='checked'</c:if> type="checkbox" name="config_chkbx" value="1" id="config_chkbx_id"/><label for="config_chkbx_id"><spring:message code="@MODULE_ID@.config.configuredproperly"/></label></span></div>
<br/>

<div style="width: 90%;margin: auto auto auto auto;">
	<input id="save" type="button" value="<spring:message code="@MODULE_ID@.config.save.configurations"/>" style="min-width: 150px;"/>
</div>
</form>

<script type="text/javascript">
	$j(document).ready( function() {
		$j("#save").click(function(){
			if (confirm("<spring:message code='@MODULE_ID@.general.message.confirm.save'/>")) {
				this.form.submit();
	        }
		});
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>