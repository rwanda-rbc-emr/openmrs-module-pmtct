<ul id="menu">
	<li class="first<c:if test='${param.type=="1"}'> active</c:if>">
		<a href="infantResume.list?category=child&type=1&page=1"><spring:message code="pmtct.menu.childTestResumePerPeriod"/></a>
	</li>

	<li <c:if test='${param.type=="2"}'>class="active"</c:if>>
		<a href="expectedChildInPCR.list?category=child&type=2&page=1"><spring:message code="pmtct.menu.expectedChildInPCR"/></a>
	</li>

	<li <c:if test='${param.type=="3"}'>class="active"</c:if>>
		<a href="expectedChildInSerologyFor9Month.list?category=child&type=3&page=1"><spring:message code="pmtct.menu.expectedChildInSerologyFor9Month"/></a>
	</li>

	<li <c:if test='${param.type=="4"}'>class="active"</c:if>>
		<a href="expectedChildInSerologyFor18Month.list?category=child&type=4&page=1"><spring:message code="pmtct.menu.expectedChildInSerologyFor18Month"/></a>
	</li>

	<li <c:if test='${param.type=="5"}'>class="active"</c:if>>
		<a href="moisDeRapportageInfantTest.list?category=child&type=5&page=1"><spring:message code="pmtct.menu.moisDeRapportageInfantTests"/></a>
	</li>
</ul>