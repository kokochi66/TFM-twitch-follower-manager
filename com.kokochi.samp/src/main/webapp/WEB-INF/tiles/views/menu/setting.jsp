<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>


<!-- ======= Breadcrumbs ======= -->
<section id="breadcrumbs" class="breadcrumbs">
  <div class="container">

    <ol>
      <li>설정</li>
    </ol>
    <h2>프로필</h2>

  </div>
</section><!-- End Breadcrumbs -->

<!-- ======= Hero Section ======= -->
<section id="content" class="content">
  <div class="container">

    <div class="row">

      <div class="col-lg-8 entries">

        <article class="entry">
          <form class="row">
            <div class="mb-3 col-12">
              <label class="form-label">트위치 아이디</label>
              <input type="text" class="form-control col-4" value="${setting_twich_user_login}" disabled>
            </div>
            <div class="mb-3 col-12">
              <label class="form-label">아이디</label>
              <input type="text" class="form-control col-4" value="${setting_user_id}">
            </div>
            <div class="mb-3 col-12">
              <label class="form-label">비밀번호</label>
              <input type="password" class="form-control col-4">
            </div>
            <div class="mb-5 col-12">
              <label class="form-label d-block">이메일</label>
              <input type="text" class="form-control col-4 d-inline mr-3" value="${setting_user_email}">
            </div>
			
			<div class="btn-group" role="group">
	            <button type="submit" class="btn btn-success mr-3">변경하기</button>
	            <button type="submit" class="btn btn-danger">삭제하기</button>
			</div>
          </form>
        </article>

      </div><!-- End blog entries list -->

      <div class="col-lg-4">

        <div class="sidebar">
          <h3 class="sidebar-title">설정</h3>
          <div class="sidebar-item setting">
            <ul>
              <li class="active"><a href="#">프로필</a></li>
            </ul>

          </div><!-- End sidebar categories-->

        </div><!-- End sidebar -->

      </div><!-- End blog sidebar -->

    </div>

  </div>
</section><!-- End Blog Section -->
