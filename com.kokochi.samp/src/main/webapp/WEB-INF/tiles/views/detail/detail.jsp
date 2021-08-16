<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
						<img src="${stream.profile_image_url}" alt="" width="100%">
					</div>
				</div>
				<div class="info-box">
					<div class="name">
						${stream.display_name}<span class="login">${stream.login}</span>
					</div>
					<div class="user_id displayNone">${stream.id}</div>
					<div class="info-num">
                        <div class="follow">팔로우 <fmt:formatNumber value="${stream_total}"/>명</div>
                        <div class="language">${stream_channel.broadcaster_language}</div>
                        <div class="started_at">
                        	<fmt:formatDate var="resultRegDt" value="${stream.created_at}" pattern="yyyy/MM/dd"/>${resultRegDt} 생성</div>
                    </div>
					
				</div>
				<div class="rank-box"></div>
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
			<div class="live-box">
				<div class="live-info">
					<div class="video-box">
						<a href="" class="linkBox url"  target="_blank">
							<div class="thumbnail">
								<img src="" alt="" width="100%">
							</div>
							<div class="play_btn">
								<img src="${path}/assets/img/play.png" alt="" width="100%">
							</div>
							<div class="text"></div>
						</a>
					</div>
					<div class="info-box">
						<div class="title"></div>
						<div class="viewer"></div>
						<div class="game"></div>
						<div class="start_at"></div>
						<div class="rank"></div>
						<div class="tags">
							<div class="tag">한국어</div>
							<div class="tag">e스포츠</div>
						</div>
					</div>
				</div>
			</div>
			<div class="replay-detail-box displayNone">
				<div class="addMore btn-danger col-2 m-auto text-center p-3">더 보기</div>
			</div>
			<div class="clip-box displayNone">
				<div class="addMore btn-danger col-2 m-auto text-center p-3">더 보기</div>
			</div>
			<div class="relative-box displayNone">
				<div class="addMore btn-danger col-2 m-auto text-center p-3">더 보기</div>
			</div>
		</div>
	</section>
</main>
<!-- End #main -->
<script src="${path}/assets/js/detail/detail.js"></script>
