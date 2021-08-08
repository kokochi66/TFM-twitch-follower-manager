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
<!-- End HeadSlider -->


<!-- Main -->
<main id="content">

	<!-- SearchBar  -->
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
				</div>
			</div>
		</div>
	</section>

	<sec:authorize access="isAnonymous()">
		<!-- Main Notice -->
		<section id="mainNotice" class="mainNotice">
			<div class="container">
				<div class="row">
					<div class="col-lg-6 container">
						<div class="notice_box">
							<div class="title pt-5 pb-3">팔로우 목록 관리하기</div>
							<div class="cont pt-2 pb-2 ml-5 mr-5">지금 바로 트위치 계정을 사이트와
								연동하여 여러분의 스트리머 목록을 관리하고, 서비스를 이용하세요!</div>
							<div class="row">
								<div class="btn-group col-6 container pt-4 pb-5">
									<button type="button" class="btn btn-outline-primary" onclick="location.href='/auth/register/gettoken'">회원가입</button>
									<button type="button" class="btn btn-outline-secondary" onclick="location.href='/auth/login'">로그인</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</sec:authorize>

	<sec:authorize access="isAuthenticated()">
	<!-- My Video -->
		<section id="featured" class="featured">
			<div class="container">
				<div class="row">
					<div class="col-lg-4 mt-4 mt-lg-0">
						<div class="icon-box" title="최근에 확인한 다시보기">
							<img alt="" width="100%" height="100%">
						</div>
					</div>
					<div class="col-lg-4 mt-4 mt-lg-0">
						<div class="icon-box" title="최근에 확인한 다시보기">
							<img alt="" width="100%" height="100%">
						</div>
					</div>
					<div class="col-lg-4 mt-4 mt-lg-0">
						<div class="icon-box" title="최근에 확인한 다시보기">
							<img alt="" width="100%" height="100%">
						</div>
					</div>
				</div>
			</div>
		</section>
		<!-- End Featured Section -->
	
		<!-- Sort Button List -->
		<section id="about" class="about">
			<div class="container">
	
				<div class="row">
					<div class="col-4 btn-danger p-3 text-sm-center"
						title="내 팔로우 관리 목록의 최신 다시보기">내 관리목록의 최신 다시보기</div>
					<div class="col-4 btn-dark p-3 text-sm-center"
						title="내가 담은 다시보기">내 다시보기 목록</div>
					<div class="col-4 btn-primary p-3 text-sm-center"
						title="최신 인기 다시보기">최신 인기영상</div>
				</div>
	
			</div>
		</section>
		<!-- End About Section -->
	
		<!-- Video List -->
		<section id="services" class="services">
			<div class="container">
	
				<div class="section-title" data-aos="fade-up">
					<h2>My Follow</h2>
				</div>
	
				<div class="row">
					<c:forEach items="${replay_video_list}" var="replay_video">
						<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
							<div class="icon-box" title="방송목록">
								<a href="${replay_video.url}" class="linkBox" target="_blank">
									<img alt="" src="${replay_video.thumbnail_url == null || replay_video.thumbnail_url.equals("") ? default_img : replay_video.thumbnail_url}" 
										width="100%" />
								</a>
							</div>
							<div class="icon-info">
								<div class="profile">
									<img alt="" src="${replay_video.profile_url == null || replay_video.profile_url.equals("") ? default_img : replay_video.profile_url}" width="100%" height="100%"/>
								</div>
								<div class="title text">${replay_video.title}</div>
								<div class="name text">${replay_video.user_name}</div>
							</div>
						</div>
					</c:forEach>
				</div>
	
			</div>
		</section>
		<!-- End Services Section -->
	</sec:authorize>
</main>
<!-- End #main -->
