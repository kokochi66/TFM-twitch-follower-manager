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
		<div class="swiper-wrapper">
			<c:forEach items="${headslide_list}" var="stream">
				<div class="swiper-slide">
					<div class="imageBox">
						<a href="https://www.twitch.tv/${stream.user_login}"
							class="linkBox" target="_blank">
							<div class="img">
								<img src="${stream.thumbnail_url}" alt="방송이미지" width="100%">
							</div>
						</a>
						<div class="info">
							<div class="profile_img">
								<img src="${stream.profile_image_url}" alt="" width="100%">
							</div>
							<div class="user_name">${stream.user_name}(${stream.user_login})</div>
							<div class="stream_game">${stream.game_name}</div>
							<div class="viewer_count">시청자 ${stream.viewer_count}명</div>
							<div class="started_date">${stream.started_at}</div>
						</div>
					</div>
				</div>
			</c:forEach>

		</div>
		<div id="hero-pagination" class="swiper-pagination"></div>
		<div class="swiper-button-prev"></div>
		<div class="swiper-button-next"></div>
	</div>

</section>
<!-- Vendor JS Files -->
<script src="${path}/assets/js/swiper-bundle.min.js"></script>
<!-- End HeadSlider -->
<script src="${path}/assets/js/home/headSlider.js"></script>