<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld"%>
<%@tag import="org.openmrs.module.pmtct.PMTCTConfiguration"%>
<%@tag import="org.openmrs.module.pmtct.util.PMTCTConstants"%>
<%@tag import="org.openmrs.api.context.Context"%>
<%@tag import="org.openmrs.module.pmtct.util.PMTCTConfigurationUtils"%>

<%
	if (Context.getAuthenticatedUser() == null)
		response.sendRedirect(request.getContextPath() + "/login.htm");

	//PMTCTConfiguration conf = PMTCTConfiguration.getInstance(request
			//.getRealPath(PMTCTConstants.CONFIGURATION_FILE_LOC));

	request.getSession().setAttribute("pmtctModuleConfigured",
			"" + PMTCTConfigurationUtils.isConfigured());

	request.getSession().setAttribute("mohtracmodulesConfigured",
			PMTCTConfigurationUtils.isMohTracPortalConfigured());
%>



<c:if test="${mohtracmodulesConfigured==false}">
	<c:redirect url="/module/mohtracportal/configuration.form" />
</c:if>


<c:if test="${pmtctModuleConfigured=='false'}">
	<c:redirect url="/module/@MODULE_ID@/config.htm" />
</c:if>