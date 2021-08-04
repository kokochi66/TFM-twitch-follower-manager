<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<form action="/auth/login" method="post" id="loginForm">
   <input type="hidden" name="user_id" value="${user_id}">
   <input type="hidden" name="user_pwd"  value="${user_pwd}">
</form>

<script>
	document.querySelector('#loginForm').submit();
</script>