<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<c:set var="path" value="/resources" />
<c:set var="default_img" value="${path}/assets/img/default_image.jpg" />

<!-- Main -->
<main id="content">

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
									<button type="button" class="btn btn-outline-primary"
										onclick="location.href='/auth/register/gettoken'">회원가입</button>
									<button type="button" class="btn btn-outline-secondary"
										onclick="location.href='/auth/login'">로그인</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</sec:authorize>

	<sec:authorize access="isAuthenticated()">
		<!-- Sort Button List -->
		<section id="about" class="about">
			<div class="container">

				<div class="row">
					<div class="col-4 btn-danger p-3 text-sm-center listBtn"
						title="관리목록 다시보기">관리목록 다시보기</div>
					<div class="col-4 btn-dark p-3 text-sm-center listBtn"
						title="팔로우 다시보기">관리목록 라이브</div>
					<div class="col-4 btn-primary p-3 text-sm-center listBtn"
						title="관리목록 인기클립">관리목록 인기클립</div>
				</div>

			</div>
		</section>
		<!-- End About Section -->

		<!-- Video List -->
		<section id="services" class="services">
			<div class="container">
				<div class="recent_video">
					<div class="section-title">
						<h2>관리목록 다시보기</h2>
					</div>
					<div class="col-3 btn-danger p-3 text-sm-center addMore m-auto">더 보기</div>
				</div>
				<div class="recent_live displayNone">
					<div class="section-title">
						<h2>관리목록 라이브</h2>
					</div>
					<div class="col-3 btn-danger p-3 text-sm-center addMore m-auto">더 보기</div>
				</div>
				<div class="recent_clip displayNone">
					<div class="section-title">
						<h2>관리목록 인기클립</h2>
					</div>
					<div class="col-3 btn-danger p-3 text-sm-center addMore m-auto">더 보기</div>
				</div>
				<%-- 				<div class="section-title">
					<h2>관리목록 다시보기</h2>
				</div>
	
				<div class="row icon-set">
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
								<div class="user_id displayNone">${replay_video.user_id}</div>
								<div class="next_page displayNone">${replay_video.nextPage}</div>
							</div>
						</div>
					</c:forEach>
				</div>
				<div class="service_last_box"></div> --%>
			</div>
		</section>
		<!-- End Services Section -->
		<script src="${path}/assets/js/home/listvideo.js"></script>
	</sec:authorize>
</main>
<!-- End #main -->
