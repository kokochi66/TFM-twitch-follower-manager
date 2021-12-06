<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<link href="${path}/assets/css/menu/clipShorts.css" rel="stylesheet">

<!-- ======= Breadcrumbs ======= -->
<section id="breadcrumbs" class="breadcrumbs">
  <div class="container">
    <h2>트위치 클립 쇼츠</h2>
  </div>
</section><!-- End Breadcrumbs -->

<!-- ======= Hero Section ======= -->
<section id="content" class="content">
  <div class="container">

    <div class="entry row">
      <div class="col--12 align-items-stretch mb-5">
        <div id="clip-swiper" class="swiper-container">
          <div class="swiper-wrapper">
            <div class="swiper-slide">
              <div class="clips">1</div>
            </div>
            <div class="swiper-slide">
              <div class="clips">2</div>
            </div>
            <div class="swiper-slide">
              <div class="clips">3</div>
            </div>
            <div class="swiper-slide">
              <div class="clips">4</div>
            </div>
          </div>
          <div class="swiper-button-prev"></div>
          <div class="swiper-button-next"></div>
        </div>
      </div>
    </div>
  </div>
  <%--	<iframe
			src="https://clips.twitch.tv/embed?clip=OpenOptimisticTrollEagleEye-Sz_ZgOJPNNMnf3N_&parent=localhost&autoplay=true"
			height="300"
			width="500"
			allowfullscreen="true">
	</iframe>--%>
</section><!-- End Blog Section -->
<script src="${path}/assets/js/swiper-bundle.min.js"></script>
<script src="${path}/assets/js/menu/clipShorts.js"></script>
