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
					</div>
				</div>
				<div class="recent_live videoList displayNone">
					<div id="recent_live" class="section-title">
						<h2>관리목록 라이브</h2>
					</div>
				</div>
				<div class="recent_clip videoList displayNone">
					<div id="recent_clip" class="section-title">
						<h2>관리목록 인기클립</h2>
					</div>
				</div>
				<div id="addMore" class="col-3 btn-danger p-3 text-sm-center addMore m-auto" label-videoIdx="0">더 보기</div>
			</div>
		</section>
	</sec:authorize>
</main>
<!-- End #main -->
<script>

	document.addEventListener("DOMContentLoaded", function(){

		// 더보기 버튼 및
		let addMoreBtn = document.querySelector('#addMore');
		// 리스트 버튼 클릭 이벤트
		let videoList = document.querySelectorAll('#services .videoList');
		document.querySelectorAll('#about .listBtn').forEach((elem, idx) => {
			elem.addEventListener('click',(e)=> {
				videoList.forEach(elem => {if(!elem.classList.contains('displayNone')) elem.classList.add('displayNone')})
				videoList[idx].classList.remove('displayNone')
				addMoreBtn.setAttribute("label-videoIdx", idx);
				// 클릭한 목록 활성화
			})
		})

		let addMoreFlage = 1;
		function addMoreClick(e) {

		}

		addMoreBtn.addEventListener('click', (e) => {
			if(addMoreFlage === 1) return false;
			addMoreFlage = 1;
			if(e.target.getAttribute('label-videoIdx') === '0') request_getMyRecentVideoNext(videoList[0].querySelector('.section-title').lastChild.lastChild.querySelector('.created_at').innerText);
		})


		// 관리목록 다시보기 데이터 요청
		let recentVideoFlag = 0;
		async function request_getMyRecentVideoNext(body) {
			console.log('다시보기 가져오기 함수실행')
			if(recentVideoFlag === 1) return false;
			recentVideoFlag = 1;
			// 메인 헤드 슬라이더의 데이터 값 가져오기
			ajaxAwait('<c:url value="/home/request/getMyRecentVideo" />', 'POST', body, (res) => {
				// console.log('라이브 비디오 가져오기 선언')
				try {
					// console.log(JSON.parse(res))
					addService_IconSet(JSON.parse(res), document.getElementById('recent_video'), addMoreBtn);
					recentVideoFlag = 0;
					addMoreFlage = 0;
				} catch(e) {
					recentVideoFlag = 0;
					addMoreFlage = 0;
					// console.log(e)
					return e;
				}
			})
		} // 관리목록 다시보기 더보기 데이터 요청
		request_getMyRecentVideoNext('none');

		// 관리목록 라이브 데이터 요청
		function request_getMyLiveVideo(body) {
			ajaxAwait('<c:url value="/home/request/getMyLiveVideo" />', 'POST', body, (res) => {
				// console.log('라이브 비디오 가져오기 선언')
				try {
					console.log(JSON.parse(res))
					if(res !== 'error') addService_IconSet(JSON.parse(res), document.getElementById('recent_live'))
				} catch(e) {
					// console.log(e)
					return e;
				}
			})

		} // 관리목록 라이브 데이터 요청
		request_getMyLiveVideo('none')

		// 관리목록 인기클립 데이터 요청


		// 데이터 셋 추가하기
		function addService_IconSet(data, target, last) {
			let s_row = document.createElement('div');
			s_row.className = 'row icon-set';

			for(let i=0;i<data.length;i++) {
				let s_col_box = document.createElement('div');
				s_col_box.className = 'col-lg-3 col-md-4 col-sm-6 d-flex flex-column mb-5';
				s_col_box.innerHTML = `
					<div class="icon-box" title="방송목록">
					  <a href="\${data[i].url}" class="linkBox" target="_blank">
						<img alt="" src="\${data[i].thumbnail_url ? data[i].thumbnail_url : default_img}" width="100%">
					  </a>
					</div>
					<div class="icon-info">
					  <div class="profile">
						<img alt="" src="\${data[i].profile_image_url ? data[i].profile_image_url : default_img}" width="100%" height="100%">
					  </div>
					  <div class="title text">\${data[i].title}</div>
					  <div class="name text">\${data[i].user_name}</div>
					  <div class="user_id displayNone">\${data[i].user_id}</div>
					  <div class="next_page displayNone">\${data[i].nextPage}</div>
					  <div class="created_at displayNone">\${data[i].created_at}</div>
					</div>
				  `;
				s_row.appendChild(s_col_box)
			}

			target.appendChild(s_row)
			// target.removeChild(target.querySelector('.loading'));
		} /// IconSet 추가하기
	});
	//

</script>