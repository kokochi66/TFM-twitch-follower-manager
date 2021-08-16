<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<!DOCTYPE html>
<html lang="ko">
<head>
  <title><spring:message code="main.title"/> <tiles:getAsString name="title" /></title>
  <tiles:insertAttribute name="style" />
</head> 
<body>
	<tiles:insertAttribute name="topbar" />
	
	<tiles:insertAttribute name="content" />
	
	<tiles:insertAttribute name="footer" />
	<tiles:insertAttribute name="script" />
</body>

</html>