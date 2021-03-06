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
				<div class="recent_video videoList">
					<div id="recent_video" class="section-title">
						<h2>관리목록 다시보기</h2>
						<button id="video_refresh_btn" class="refresh-btn btn btn-primary">새로고침</button>
					</div>
				</div>
				<div class="recent_live videoList displayNone">
					<div id="recent_live" class="section-title">
						<h2>관리목록 라이브</h2>
						<button id="live_refresh_btn" class="refresh-btn btn btn-primary">새로고침</button>
					</div>
				</div>
				<div class="recent_clip videoList displayNone">
					<div id="recent_clip" class="section-title">
						<h2>관리목록 인기클립</h2>
						<button id="clip_refresh_btn" class="refresh-btn btn btn-primary">새로고침</button>
					</div>
				</div>
				<div id="addMore" class="col-3 btn-danger p-3 text-sm-center addMore m-auto" label-videoIdx="0">더 보기</div>
			</div>
		</section>
	</sec:authorize>
</main>
<!-- End #main -->
<script src="${path}/assets/js/home/home.js"></script>
