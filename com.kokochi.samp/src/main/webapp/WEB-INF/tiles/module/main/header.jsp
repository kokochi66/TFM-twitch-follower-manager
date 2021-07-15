<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- HeadSlider -->
<section id="headslider">
  
  <!-- Slider main container -->
  <div id="headslider-container" class="swiper-container">
    <!-- Additional required wrapper -->
    <div class="swiper-wrapper">
      <!-- Slides -->
      <div class="swiper-slide">
        <div class="Hbox">1</div>
      </div>
      <div class="swiper-slide">
        <div class="Hbox">2</div>
      </div>
      <div class="swiper-slide">
        <div class="Hbox">3</div>
      </div>
      <div class="swiper-slide">
        <div class="Hbox">4</div>
      </div>
      <div class="swiper-slide">
        <div class="Hbox">5</div>
      </div>
    </div>
    <!-- If we need pagination -->
    <div id="hero-pagination" class="swiper-pagination"></div>

    <!-- If we need navigation buttons -->
    <div class="swiper-button-prev"></div>
    <div class="swiper-button-next"></div>
  </div>

</section><!-- End Hero -->