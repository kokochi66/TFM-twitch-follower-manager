<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="path" value="/resources" />
<c:set var="default_img" value="${path}/assets/img/default_image.jpg" />

<!-- HeadSlider -->
<section id="headslider">
	<div id="headslider-container" class="swiper-container">
		<div class="swiper-wrapper"></div>
		<div id="hero-pagination" class="swiper-pagination"></div>
		<div class="swiper-button-prev"></div>
		<div class="swiper-button-next"></div>
	</div>

</section>
<!-- Vendor JS Files -->
<script src="${path}/assets/js/swiper-bundle.min.js"></script>
<!-- End HeadSlider -->
<script src="${path}/assets/js/home/headSlider.js"></script>