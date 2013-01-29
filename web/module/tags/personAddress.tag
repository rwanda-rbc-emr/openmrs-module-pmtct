<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ attribute name="personAddress" required="true" type="org.openmrs.PersonAddress" %>

<style>

	.address{
		margin-left: 5px;
		font-weight: bold;
	}
	
</style>

	<span style="padding: 2px; -moz-border-radius:2px; border-style:solid; border-color: #ddd; border-width: 1px 2px 2px 1px;">
		<c:if test="${personAddress.country ne null && personAddress.country!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.country'/>">${personAddress.country};</span>
		</c:if>
		<c:if test="${personAddress.stateProvince ne null && personAddress.stateProvince!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.province'/>">${personAddress.stateProvince};</span>
		</c:if>
		<c:if test="${personAddress.countyDistrict ne null && personAddress.countyDistrict!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.district'/>">${personAddress.countyDistrict};</span>
		</c:if>
		<c:if test="${personAddress.cityVillage ne null && personAddress.cityVillage!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.sector'/>">${personAddress.cityVillage};</span>
		</c:if>
		<c:if test="${personAddress.neighborhoodCell ne null && personAddress.neighborhoodCell!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.cell'/>">${personAddress.neighborhoodCell};</span>
		</c:if>
		<c:if test="${personAddress.address1 ne null && personAddress.address1!=''}">
			<span class="address" title="<spring:message code='@MODULE_ID@.demographic.umudugudu'/>">${personAddress.address1}</span>
		</c:if>
	</span>