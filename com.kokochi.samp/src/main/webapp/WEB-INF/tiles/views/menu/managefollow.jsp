<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>




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
          <a href="#" class="btn btn-primary active" aria-current="page">스트리머 추가</a>
          <a href="#" class="btn btn-primary">내 스트리머 검색</a>
        </div>
      </div>

      <div class="col-lg-3 col-md-6 col-sm-12 align-items-stretch mb-5">
        <div class="follow-box">
          <div class="profile_img" style="background-image: url('https://static-cdn.jtvnw.net/jtv_user_pictures/1885f8ff-2f1e-4b5b-8e21-dfe56698640f-profile_image-300x300.png');"></div>
          <div class="info">
            <div class="name">뿡이</div>
            <div class="desc">그냥 뿡이ㅁㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄻㄴㅇㄹㄴㅇㄻㄴㄹㄶ츝ㅊ</div>
            <div class="check">
              <input type="checkbox" class="form-check-input checkInput" name="" id="">
            </div>
          </div>
        </div>
      </div>
  </div>
</section><!-- End Blog Section -->
<script src="/resources/assets/js/menu/managefollow.js"></script>
