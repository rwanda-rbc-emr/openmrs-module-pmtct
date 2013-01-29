<!-- < %@ include file="/WEB-INF/template/include.jsp"%> -->

<img src="chart.htm?chart=${param.chart}<c:if test='${param.type!=null}'>&type=${param.type}</c:if>" width="400" height="300" />
