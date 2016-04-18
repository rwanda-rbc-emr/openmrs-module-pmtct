<!-- OpenMRS includes -->
<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />

<!-- PMTCT CSS files -->
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/pmtctstyle.css" />
<c:if test='<%= ! request.getRequestURI().contains("pmtctConfig") %>'>
	<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/pmtcttableliststyle.css" />
</c:if>

<!-- PMTCT Tags files -->
<%@ taglib prefix="pmtcttag" uri="/WEB-INF/view/module/pmtct/taglibs/pmtcttag.tld"%>
<%@ taglib prefix="pmtct_tag" tagdir="/WEB-INF/tags/module/pmtct"%>

<!-- Jquery includes -->
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.bgiframe.js" />

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/theme/ui.all.css" />
<!-- <openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/theme/demos.css" /> -->

<!-- Checks if the configurations is done before accessing 
 any of the PMTCT page -->
<c:if test='<%= ! request.getRequestURI().contains("pmtctConfig") %>'>
	<pmtct_tag:checkConfigurations />
</c:if>

<!-- Avoiding conflicts with dojo -->
<script type="text/javascript">
		var $j = jQuery.noConflict(); 
</script>