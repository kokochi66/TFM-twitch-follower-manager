<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<c:set var="path" value="/resources"/>

<!-- HeadSlider -->
<section id="headslider">
  <div id="headslider-container" class="swiper-container">
    <div class="swiper-wrapper">
    	<c:forEach items="${headslide_list}" var="stream">
		  <div class="swiper-slide">
		  	<div class="imageBox" style="background: url('${stream.thumbnail_url}') center center no-repeat;
		  			background-size: cover;" width="100%">1</div>
	      </div>
    	</c:forEach>
    	
    </div>
    <div id="hero-pagination" class="swiper-pagination"></div>
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
  </div>

</section><!-- End Hero -->


<!-- Main -->
<main id="main">

  <!-- ======= Featured Section ======= -->
  <section id="featured" class="featured">
    <div class="container">
      <div class="row">
        <div class="col-lg-4">
          <div class="icon-box" title="최근에 확인한 다시보기"></div>
        </div>
        <div class="col-lg-4 mt-4 mt-lg-0">
          <div class="icon-box" title="최근에 확인한 다시보기"></div>
        </div>
        <div class="col-lg-4 mt-4 mt-lg-0">
          <div class="icon-box" title="최근에 확인한 다시보기"></div>
        </div>
      </div>

    </div>
  </section><!-- End Featured Section -->

  <!-- ======= About Section ======= -->
  <section id="about" class="about">
    <div class="container">

      <div class="row">
        <div class="col-lg-3 col-md-6 btn-danger p-lg-3 text-sm-center" title="나의 구독한 스트리머 목록">My Follow</div>
        <div class="col-lg-3 col-md-6 btn-dark p-lg-3 text-sm-center" title="현재 시청자 순 조회">Viewer</div>
        <div class="col-lg-3 col-md-6 btn-primary p-lg-3 text-sm-center" title="구독자 순 조회">Follower</div>
        <div class="col-lg-3 col-md-6 btn-secondary p-lg-3 text-sm-center" title="게임 별 조회">Game</div>
      </div>

    </div>
  </section><!-- End About Section -->

  <!-- ======= Services Section ======= -->
  <section id="services" class="services">
    <div class="container">

      <div class="section-title" data-aos="fade-up">
        <h2>My Follow</h2>
      </div>

      <div class="row">
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box" title="방송목록"></div>
        </div>
        <div class="col-lg-3 col-md-4 col-sm-6 d-flex align-items-stretch mb-5">
          <div class="icon-box"></div>
        </div>
      </div>

    </div>
  </section><!-- End Services Section -->
</main><!-- End #main -->
