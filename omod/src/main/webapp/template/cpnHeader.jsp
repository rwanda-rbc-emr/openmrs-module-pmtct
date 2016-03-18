<ul id="menu">
	<li class="first<c:if test='${param.type=="1"}'> active</c:if>">
		<a href="generalStatIncpn.list?category=cpn&type=1&page=1"><spring:message code="pmtct.menu.generalStatsInCPN"/></a>
	</li>

	<li <c:if test='${param.type=="2"}'>class="active"</c:if>>
		<a href="cpnMoisDeRapportage.list?category=cpn&type=2&page=1"><spring:message code="pmtct.menu.patientWhoMoisDeRapportageDPAIsTheCurrentMois"/></a>
	</li>
	
	<li <c:if test='${param.type=="3"}'>class="active"</c:if>>
		<a href="cpnCouplesDiscordant.list?category=cpn&type=3&page=1"><spring:message code="pmtct.menu.couplesDiscordant"/></a>
	</li>
</ul>