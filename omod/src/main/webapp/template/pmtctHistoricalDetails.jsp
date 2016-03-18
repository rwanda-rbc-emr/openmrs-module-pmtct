<div class="list_container">
	<div class="list_title"><spring:message code="@MODULE_ID@.graph.details"/></div>
		<div style="overflow: scroll; max-height: 150px;">
			<table class="list_data">
				<tr>
					<th class="columnHeader">Det</th>
					<th class="columnHeader">Hd</th>
				</tr>
				<c:forEach items="1,2,3,4,5,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,4" var="i" varStatus="status">
					<tr class="row <c:if test="${status.count%2==0}">even</c:if>"><td class="rowValue">det-${i}</td><td class="rowValue">hd-${i}</td></tr>
				</c:forEach>
			<tr class="list_title"><td class="rowValue">-</td><td class="rowValue">-</td></tr>
		</table>
	</div>
</div>