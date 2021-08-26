<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="path" value="/resources" />
<c:set var="default_img" value="${path}/assets/img/default_image.jpg" />

<!-- Header -->
<section id="breadcrumbs" class="breadcrumbs">
	<link href="${path}/assets/css/module/searchBar.css" rel="stylesheet">
	<section id="searchbar" class="searchbar">
		<div class="container">
			<div class="row">
				<div class="col-lg-6" style="margin: 0 auto;">
					<div class="input-group mb-3 mt-3">
						<input type="text" class="form-control" id="searchBar_text"
							placeholder="스트리머 찾기">
						<button class="searchBar_btn" id="searchBar_btn">
							<i class="icofont-ui-search icon"></i>
						</button>
					</div>
					<div class="input-preview"></div>
				</div>
			</div>
		</div>
	</section>
	<script src="${path}/assets/js/module/searchBar.js"></script>
</section>
<!-- Header -->
