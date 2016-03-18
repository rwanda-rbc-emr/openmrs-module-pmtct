<!-- < %@ taglib prefix="pmtct_tag" tagdir="/WEB-INF/tags/module/pmtct" %>

<pmtct_tag:checkConfigurations/> -->

<openmrs:require privilege="View patients in PMTCT" otherwise="/login.htm" redirect="/module/@MODULE_ID@/patientInPmtct.list?page=1" />

<h2 style="display: inline;"><spring:message code="pmtct.overviewBoxTitle"/> :: 
<span style="color:#8FABC7;"><spring:message code="pmtct.menu.statisticsTitle"/></span></h2>
<br/><br/>

<ul id="menu">
		<li class="first<c:if test='<%= request.getRequestURI().contains("homePmtct") %>'> active</c:if>">
			<a href="patientInPmtct.list?page=1"><spring:message code="pmtct.menu.patientInPmtct"/></a>
		</li>
	
	<openmrs:hasPrivilege privilege='View PMTCT patients in ANC'>
		<li <c:if test='${param.category=="cpn"}'>class="active"</c:if>>
			<a href="generalStatIncpn.list?category=cpn&type=1&page=1"><spring:message code="pmtct.menu.cpn"/></a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege='View PMTCT patients in maternity'>
		<li <c:if test='${param.category=="maternity"}'>class="active"</c:if>>
			<a href="generalStatsInMaternity.list?category=maternity&type=1&page=1"><spring:message code="pmtct.menu.maternity"/></a>
		</li>
	</openmrs:hasPrivilege>
	
	<openmrs:hasPrivilege privilege='View PMTCT pediatric tests'>
		<li <c:if test='${param.category=="child"}'>class="active"</c:if>>
			<a href="infantResume.list?category=child&type=1&page=1"><spring:message code="pmtct.menu.infantTest"/></a>
		</li>
	</openmrs:hasPrivilege>
	
</ul>