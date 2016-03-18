
<ul id="menu">
	<li class="first<c:if test='${param.type=="1"}'> active</c:if>">
		<a href="generalStatsInMaternity.list?category=maternity&type=1&page=1"><spring:message code="pmtct.menu.generalStatsInMaternity"/></a>
	</li>

	<li <c:if test='${param.type=="2"}'>class="active"</c:if>>
		<a href="expectedPatientInMaternity.list?category=maternity&type=2&page=1"><spring:message code="pmtct.menu.expectedPatientInMaternity"/></a>
	</li>
	
	<li <c:if test='${param.type=="3"}'>class="active"</c:if>>
		<a href="patientTestedInDeliveryroom.list?category=maternity&type=3&page=1"><spring:message code="pmtct.menu.patientTestedInWorkroom"/></a>
	</li>
	
	<li <c:if test='${param.type=="4"}'>class="active"</c:if>>
		<a href="patientWhoMissedMaternityEncounter.list?category=maternity&type=4&page=1"><spring:message code="pmtct.menu.patientWhoMissedMaternityEncounter"/></a>
	</li>
</ul>