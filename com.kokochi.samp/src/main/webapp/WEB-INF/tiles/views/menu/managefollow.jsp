<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<link href="${path}/assets/css/menu/managedfollow.css" rel="stylesheet">
<!-- ======= Breadcrumbs ======= -->
<section id="breadcrumbs" class="breadcrumbs">
  <div class="container">
    <h2>팔로우 채널 관리</h2>
  </div>
</section><!-- End Breadcrumbs -->

<!-- ======= Hero Section ======= -->
<section id="content" class="content">
	<div class="container">
		<div class="entry row">
			<div class="col-6 mb-5">
				<select class="form-select" aria-label="Default select example">
					<option selected>나의 팔로우 순</option>
					<option value="1">라이브 중</option>
					<option value="2">총 팔로워 순</option>
					<option value="3">팔로우 한 날짜 순</option>
				</select>
			</div>
			<div class="col-6 mb-5">
				<div class="btn-group float-right">
					<a href="#" class="btn btn-primary active" aria-current="page">스트리머
						추가</a> <a href="#" class="btn btn-primary">내 스트리머 검색</a>
				</div>
			</div>
			<c:forEach items="${follow_list}" var="follow">
				<div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
					<div class="follow-box ${follow.isManaged ? "mylist" : ""}">
						<div class="profile_img"
							style="background-image: url('${follow.profile_image_url}');"></div>
						<div class="info">
							<div class="name">${follow.display_name}<span class="login">(${follow.login})</span>
							</div>
							<div class="desc">${follow.description}</div>
							<div class="user_id displayNone">${follow.id}</div>
							<div class="managed_id displayNone">${follow.managed_id}</div>
							<div class="check">
								<input type="checkbox" class="form-check-input checkInput"
									name="" id="" ${follow.isManaged ? "checked" : ""}>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
</section>
<!-- End Blog Section -->
<%--
<script src="${path}/assets/js/menu/managefollow.js"></script>
--%>
<script>
	document.addEventListener("DOMContentLoaded", function(){
		let followBoxes = document.querySelectorAll('#content .follow-box'),
				followBtn = document.querySelectorAll('#content .follow-box .checkInput');

		followBtn.forEach((elem, idx) => {
			elem.addEventListener('change', (e) => {
                if(elem.checked) {
					console.log('추가하기 요청')
                    ajaxSubmit('/menu/request/managedfollow/add', 'POST', followBoxes[idx].querySelector('.user_id').innerHTML, (res) => {
                        // console.log('라이브 비디오 가져오기 선언')
                        if(res !== 'error') console.log(res);
						location.reload();
                    })
                } else {
                    ajaxSubmit('/menu/request/managedfollow/remove', 'POST', followBoxes[idx].querySelector('.managed_id').innerHTML, (res) => {
                        // console.log('라이브 비디오 가져오기 선언')
                        if(res !== 'error') console.log(res);
						location.reload();
                    })
                }
			})
		})

	});
</script>