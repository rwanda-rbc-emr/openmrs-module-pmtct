<style>
	.rowValue{
		font-size=1em;
	}
</style>

<div id="list_container" style="width: 100%">
	
	<div id="list_title">
		<div class="list_title_msg">List of Children</div>
		<div class="list_title_bts">
			&nbsp;&nbsp;		
		</div>
		<div style="clear:both;"></div>
	</div>

	<table id="list_data">
		<tr>
			<th class="columnHeader"></th>
			<th class="columnHeader">${pmtcttag:identifierTypeNameById(localIdentifierTypeId)}</th>
			<th class="columnHeader"><spring:message code="PersonName.givenName" /></th>
			<th class="columnHeader"><spring:message code="PersonName.familyName" /></th>
			<th class="columnHeader"><spring:message code="Person.birthdate"/></th>
			<th class="columnHeader"><spring:message code="Person.gender" /></th>
			<th class="columnHeader">${pmtcttag:conceptNameById(weightId)}</th>
			<th class="columnHeader">${pmtcttag:conceptNameById(heightId)}</th>
			<th class="columnHeader">${pmtcttag:conceptNameById(infantFeedingMethodId)}</th>
			<th class="columnHeader">${pmtcttag:conceptNameById(childReturnVisitDateId)}</th>
			<th class="columnHeader">${pmtcttag:conceptNameById(commentId)}</th>
			<th class="columnHeader"></th>
		</tr>		
	</table>
	<div id="list_footer">
		<div class="list_footer_info">&nbsp;&nbsp;</div>
		<div class="list_footer_pages">		
			&nbsp;&nbsp;&nbsp;		
		</div>
		<div style="clear:both"></div>
	</div>
</div>

<input type="hidden" name="numberOfChildren" value="0" id="numberOfChildren"/>
<input type="button" onclick="addRow('list_data');" value="Add Child"/>
<input type="button" onclick="deleteRow('list_data');" value="Remove Child"/>

<script>

	var index=0;

	function getInfantFeedingMethod(name,index){
		var element = "<select name='"+name+"_"+index+"' id='"+name+"_"+index+"'><option value='0'>--</option>";
				<c:forEach var="answer" items="${infantFeedingMethodAnswers}" varStatus="status">  
				  element+="<option value='${answer.key}'>${answer.value}</option>"
				</c:forEach>

		element+="</select>";
		  
		return element;
	}
	
	function addRow(tableID) {
		 
	    var table = document.getElementById(tableID);
	
	    var rowCount = table.rows.length;
	    var row = table.insertRow(rowCount);
	    row.style.verticalAlign="top";
	
	    var cell1 = row.insertCell(0);
	    var element1 = document.createElement("input");
	    element1.type = "checkbox";
	    cell1.appendChild(element1);
	
	    var cell2 = row.insertCell(1);
	    cell2.appendChild(createTextElement('pId',index,13));
	
	    var cell3 = row.insertCell(2);
	    cell3.appendChild(createTextElement('fName',index,25));
	
	    var cell4 = row.insertCell(3);
	    cell4.appendChild(createTextElement('gName',index,25));
	
	    var cell5 = row.insertCell(4);
	    cell5.innerHTML=createDateElement('birthdate',index);
	
	    var cell6 = row.insertCell(5);
	    cell6.innerHTML=createGenderElement('gender',index);
	
	    var cell7 = row.insertCell(6);
	    cell7.appendChild(createTextElement('weight',index,4));
	
	    var cell8 = row.insertCell(7);
	    cell8.appendChild(createTextElement('height',index,4));
	
	    var cell9 = row.insertCell(8);
	    cell9.innerHTML=getInfantFeedingMethod('infantFeedingMethod',index);
	
	    var cell10 = row.insertCell(9);
	    cell10.innerHTML=createDateElement('returnVisitDate',index);
	
	    var cell11 = row.insertCell(10);
	    cell11.appendChild(createTextAreaElement('childComment',index,3,40));

	    var cell12 = row.insertCell(11);
	    cell12.innerHTML="<span id='row_"+index+"'></span>";

	    index+=1;
	    
	    $j("#numberOfChildren").val(index);
	
	}

	function createTextElement(name,index,size){
		var element = document.createElement("input");
	    element.type = "text";
	    element.name = name+"_"+index;
	    element.id = name+"_"+index;
	    element.size = size;
	    return element;
	}

	function createTextAreaElement(name,index,rows,cols){
		var element = document.createElement("textarea");
	    element.name = name+"_"+index;
	    element.rows = rows;
	    element.cols = cols;
	    return element;
	}

	function createGenderElement(name,index){
		var element = "<select name='"+name+"_"+index+"' id='"+name+"_"+index+"'><option value='0'>--</option><option value='F'><spring:message code='Patient.gender.female'/></option><option value='M'><spring:message code='Patient.gender.male'/></option></select>"; 	    
	    
	    return element;
	}

	function createDateElement(name,index){
		var element = "<input type='text' size='11' autocomplete='off' onclick='showCalendar(this);' name='"+name+"_"+index+"' id='"+name+"_"+index+"'/>"; 	    
	    
	    return element;
	}
	
	function deleteRow(tableID) {
        if(confirm("Are you sure you want to remove selected child/children?")){
		    try {
		    var table = document.getElementById(tableID);
		    var rowCount = table.rows.length;
		
		    for(var i=0; i<rowCount; i++) {
		        var row = table.rows[i];
		        var chkbox = row.cells[0].childNodes[0];
		        if(null != chkbox && true == chkbox.checked) {
			    	table.deleteRow(i);
		            rowCount--;
		            i--;
		        }
		
		    }
		    }catch(e) {
		        alert(e);
		    }
    	}
	}
	
	function validateInfantFormFields(){
		var valid=true;
		var count=0;
		while(count<index){
			//alert(document.getElementById("pId_"+count));
			if(document.getElementById("pId_"+count)!=null){
				if($j("#pId_"+count).val()==''){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#fName_"+count).val()==''){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#gName_"+count).val()==''){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#weight_"+count).val()==''){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#height_"+count).val()==''){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#gender_"+count).val()=='0'){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#infantFeedingMethod_"+count).val()=='0'){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#birthdate_"+count).val()=='' || $j("#birthdate_"+count).val()!=$j("#dateOfConfinementId").val()){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}
				if($j("#returnVisitDate_"+count).val()==''){
					// || $j("#returnVisitDate_"+count).val()<=$j("#dateOfConfinementId").val()){
					$j("#row_"+count).html("*");
					$j("#row_"+count).addClass("error");
					valid=false;
				} else {
					if(valid){
						$j("#row_"+count).html("");
						$j("#row_"+count).removeClass("error");
					}
				}

				//alert(new Date($j("#returnVisitDate_"+count).val()));
			
				if(!valid){
					$j("#errorDivId").html("<spring:message code='@MODULE_ID@.error.fix'/>");
					$j("#errorDivId").addClass("error");
				} else {
					$j("#errorDivId").html("");
					$j("#errorDivId").removeClass("error");
				}
			}//else{alert("undefined row "+count);}
			count+=1;
		}
		return valid;
	}

</script>
