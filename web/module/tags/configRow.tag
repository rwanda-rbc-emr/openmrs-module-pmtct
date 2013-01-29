<%@ tag language="java" pageEncoding="UTF-8"%>

<%@ attribute name="item" type="org.openmrs.module.pmtct.PMTCTConfigTemplate" required="true" %>
<%@ attribute name="index" type="java.lang.Integer" required="true" %>

	<tr <c:if test="${index%2==0}">class='even'</c:if>>
		<td class="configDescription">
			<c:if test="${item.value==0}">${item.description}</c:if>
			<c:if test="${item.value!=0}">Hello, I'm the value you're loking</c:if>
		</td>
		<td class="configDefaultValue"><c:if test="${item.value==0}">${item.defaultValue}</c:if></td>
		<td><input size="5" item="text" name="${item.id}" value="${item.value}"/></td>
	</tr>
	<c:if test="${item.value!=0}">
		<tr <c:if test="${index%2==0}">class='even'</c:if>>
			<td colspan="3" class="configComment">${item.description}</td>					
		</tr>
	</c:if>

