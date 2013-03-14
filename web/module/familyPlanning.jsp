<%@ include file="template/localIncludes.jsp"%>

<!-- *************************************** -->

<openmrs:portlet url="patientHeader" id="patientDashboardHeader" patientId="${param.patientId}"/>
<br/>

<h2 style="display:inline;"><spring:message code="@MODULE_ID@.pmtctTitle"/>: </h2><span style="font-size: 15px" class="infoPatientLabel"><spring:message code="@MODULE_ID@.familyplanning.title"/></span>
<br/><br/>

<div>
	<a href="<openmrs:contextPath/>/patientDashboard.form?patientId=${param.patientId}"><spring:message code="patientDashboard.viewDashboard"/></a>
</div>
<br/>

<div class="box" id="cpnInfosResume">
	<table width="100%;">
		<tr>
			<td>Current family planning</td>
			<td>Date d'accouchement</td>
			<td>Date rendez-vous PF</td>
			<td>Date venue PF</td>
			<td>Methode contraceptive</td>
			<td>Clinical Comments</td>
		</tr>
		<tr>
			<td><input id="stop_pf" type="button" value="Stop Family Planning"/></td>
			<td><b>dd-MMM-yyyy</b></td>
			<td><b>dd-MMM-yyyy</b></td>
			<td><b>dd-MMM-yyyy</b></td>
			<td><b>methode</b></td>
			<td><input id="add_view_comment" type="button" value="Add/View"/></td>
		</tr>
	</table>
</div>
<br/>

<div id="patientTabs">
	<ul>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="appointmentTab" class="current "><spring:message code="@MODULE_ID@.familyplanning.visits"/></a></li>
		<li><a hidefocus="hidefocus" onclick="return changeTab(this);" href="#" id="historyTab"><spring:message code="@MODULE_ID@.familyplanning.history"/></a></li>
	</ul>
</div>
		
<div id="list_container">
	<div id="appointment">
		<input id="add_visit" type="button" value="Add Next Visit Date"/><br/>
		<div>
			<div class="list_title">
				<div class="list_title_msg"><spring:message code="@MODULE_ID@.familyplanning.history"/></div>
				<div class="list_title_bts">
					&nbsp;			
				</div>
				<div style="clear:both;"></div>
			</div>
			<table class="list_data">
				<tr>
					<th class="columnHeader">No.</th>
					<th class="columnHeader">Date Rendez-vous pour la PF</th>
					<th class="columnHeader">Date venue pour la PF</th>
					<th class="columnHeader">Observations</th>
					<th class="columnHeader">Provider</th>
					<th class="columnHeader">Created</th>
				</tr>
				<!-- <c:if test="${empty numberOfPages}"><tr><td colspan="2"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if> -->
				<c:forEach items="1,2,3,4" var="history" varStatus="status">
					<tr class="${status.count%2!=0?'even':''}">
						<td class="rowValue">${status.count}.</td>
						<td class="rowValue">DRDZPF-${status.count}</td>
						<td class="rowValue">DVPF-${status.count}</td>
						<td class="rowValue">Obs-${status.count}</td>
						<td class="rowValue">Pr-${status.count}</td>
						<td class="rowValue">Dt-${status.count}</td>
					</tr>
				</c:forEach>
			</table>
			<div class="list_footer">
				<div class="list_footer_info">&nbsp;</div>
					<div class="list_footer_pages">
					&nbsp;
				</div>
				<div style="clear:both"></div>
			</div>
		</div>
	</div>
	
	<div id="history" style="display: none;">
		<div id="list_title">
			<div class="list_title_msg"><spring:message code="@MODULE_ID@.familyplanning.history"/></div>
			<div class="list_title_bts">
				&nbsp;			
			</div>
			<div style="clear:both;"></div>
		</div>
		<table id="list_data">
			<tr>
				<th class="columnHeader">No.</th>
				<th class="columnHeader">Date d'Accouchement</th>
				<th class="columnHeader">Date Rendez-vous pour la PF</th>
				<th class="columnHeader">Date venue pour la PF</th>
				<th class="columnHeader">Methode contraceptive utilise</th>
				<th class="columnHeader">Raison de sortie du Programme</th>
				<th class="columnHeader">Observations</th>
			</tr>
			<!-- <c:if test="${empty numberOfPages}"><tr><td colspan="2"><center><spring:message code="@MODULE_ID@.tablelist.empty"/></center></td></tr></c:if> -->
			<c:forEach items="1,2,3,4" var="history" varStatus="status">
				<tr class="${status.count%2!=0?'even':''}">
					<td class="rowValue">${status.count}.</td>
					<td class="rowValue">Dt-${status.count}</td>
					<td class="rowValue">DRDZPF-${status.count}</td>
					<td class="rowValue">DVPF-${status.count}</td>
					<td class="rowValue">MCU-${status.count}</td>
					<td class="rowValue">RSP-${status.count}</td>
					<td class="rowValue">Obs-${status.count}</td>
				</tr>
			</c:forEach>
		</table>
		<div id="list_footer">
			<div class="list_footer_info">&nbsp;</div>
				<div class="list_footer_pages">
				&nbsp;
			</div>
			<div style="clear:both"></div>
		</div>
	</div>
</div>

<div id="divDlg"></div>
<div id="stopProgram" style="display: none;">
	<h3>Stop Family Planning</h3>
	<form action="familyPlanning.form?patientId=${param.patientId}&stopProgram=true" method="post">
		<table>
			<tr>
				<td>Date de sortie</td>
				<td><input name="endDate" size="11" type="text" onclick="showCalendar(this)" /></td>
			</tr>
			<tr>
				<td>Raison de sortie</td>
				<td>
					<select name="reason">
						<option value="">--</option>
					</select>
				</td>
			</tr>
		</table>
		<hr/>
		<input type="submit" value="Save"/>
	</form>
</div>

<div id="addComment" style="display: none;">
	<h3>Add/View Clinical Comments</h3>
	<form action="familyPlanning.form?patientId=${param.patientId}&saveComment=true" method="post">
		<table>
			<tr>
				<td>New Comment</td>
				<td><textarea cols="30" rows="3"></textarea></td>
			</tr>
		</table>
		<hr/>
		<input type="submit" value="Save"/>
	</form>
	<br/>
	<i>Clinical Comments</i>
	<div style="overflow: scroll; width: 570px; height: 170px;"><c:forEach items="1,2,3" var="comment">
			<p class="commentBody">Clinical comment impression---${comment}</p>
		</c:forEach>
	</div>
</div>

<div id="addNextVisit" style="display: none;">
	<h3>Add a Next Visit</h3>
	<form action="familyPlanning.form?patientId=${param.patientId}&saveVisit=true" method="post">
		<table>
			<tr>
				<td>Date</td>
				<td><input name="nextVisitDate" size="11" type="text" onclick="showCalendar(this)" /></td>
			</tr>
		</table>
		<hr/>
		<input type="submit" value="Save"/>
	</form>
</div>

<br/><br/>
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

	function showDialog(title, html, width, height){
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='"+title+"'><p><div id='result' style='padding: auto;'>"+html+"</div></p></div>");
		$j("#dialog").dialog({
			zIndex: 980,
			bgiframe: true,
			height: height,
			width: width,
			modal: true			
		});
	}

	$j(document).ready(function(){		
		$j("#stop_pf").click(function() {
			showDialog("Family Planning : Stop Program", $j("#stopProgram").html(), 655, 175);
		});

		$j("#add_view_comment").click(function() {
			showDialog("Family Planning : Add/View Clinical Comment", $j("#addComment").html(), 605, 400);
		});

		$j("#add_visit").click(function() {
			showDialog("Family Planning : Add Next Visit", $j("#addNextVisit").html(), 655, 195);
		});
	});
	
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>