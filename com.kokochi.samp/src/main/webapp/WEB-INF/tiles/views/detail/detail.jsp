<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="path" value="/resources" />
<c:set var="default_img" value="${path}/assets/img/default_image.jpg" />

<link rel="stylesheet" href="${path}/assets/css/detail/detail.css" />
<!-- HeadSlider -->
<section id="breadcrumbs" class="breadcrumbs">
	<!-- SearchBar  -->
	<section id="searchbar" class="searchbar p-0">
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

</section>
<!-- End HeadSlider -->


<!-- Main -->
<main id="content">
	<section id="about">
		<div class="container">
			<div class="profile">
				<div class="img-box">
					<div class="img">
						<img src="https://static-cdn.jtvnw.net/jtv_user_pictures/21af3d33e8fdcfe9-profile_image-300x300.jpeg" alt="" width="100%">
					</div>
				</div>
				<div class="info-box">
					<div class="name">
						이춘향<span class="login">leechunhyang</span>
					</div>
					<div class="btn-list">
						<div class="btn btn-danger">팔로우</div>
						<div class="btn btn-primary">구독</div>
						<div class="btn btn-success">알림설정</div>
						<div class="btn btn-secondary">관리목록</div>
					</div>
					
				</div>
				<div class="rank-box">200위</div>
			</div>
		</div>
	</section>
	<section id="tab">
		<div class="container">
			<div class="row">
				<div class="menu-box col-3 text-center p-4 border-bottom border-right border-top border-dark active">라이브</div>
				<div class="menu-box col-3 text-center p-4 border-bottom border-right border-top border-dark">다시보기</div>
				<div class="menu-box col-3 text-center p-4 border-bottom border-right border-top border-dark">클립</div>
				<div class="menu-box col-3 text-center p-4 border-bottom border-top border-dark">연관 스트리머</div>
			</div>
		</div>
	</section>
	<section id="services">
		<div class="container">
			<div class="live-box displayNone">
				<div class="live-info">
					<div class="video-box">
						<div class="thumbnail">
							<img src="https://static-cdn.jtvnw.net/previews-ttv/live_user_leechunhyang-1920x1080.jpg" alt="" width="100%">
						</div>
						<div class="play_btn">
							<img src="assets/img/play.png" alt="" width="100%">
						</div>
						<div class="text"></div>
					</div>
					<div class="info-box">
						<div class="title">[이춘향] (๑˘  ꇴ ˘ ๑)</div>
						<div class="viewer">1896 명</div>
						<div class="game">Just Chatting</div>
						<div class="start_at">30분전</div>
						<div class="rank">라이브 12위</div>
						<div class="tags">
							<div class="tag">한국어</div>
							<div class="tag">e스포츠</div>
							<div class="tag">e스포츠</div>
							<div class="tag">e스포츠</div>
							<div class="tag">e스포츠</div>
							<div class="tag">e스포츠</div>
							<div class="tag">e스포츠</div>
						</div>
					</div>
				</div>
			</div>
			<div class="replay-detail-box displayNone">
				<div class="row">
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
				</div>
			</div>
			<div class="clip-box displayNone">
				<div class="row">
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
					<div class="col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5">
						<div class="icon-box"></div>
					</div>
				</div>
			</div>
			<div class="relative-box">
				<div class="row">
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
					<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
						<div class="follow-box">
							<div class="profile"></div>
							<div class="info"></div>
						</div>
					</div>
				</div>
			</div>
			<div class="addMore btn-danger col-2 m-auto text-center p-3">더 보기</div>
		</div>
	</section>
</main>
<!-- End #main -->
<script src="${path}/assets/js/main.js"></script>
