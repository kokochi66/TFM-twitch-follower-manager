<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %><c:set var="path" value="/resources"/>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!-- ======= Breadcrumbs ======= -->
<section id="breadcrumbs" class="breadcrumbs">
  <div class="container">
    <h2>로그인</h2>
  </div>
</section><!-- End Breadcrumbs -->

<!-- ======= Hero Section ======= -->
<section id="content" class="content">
  <div class="container">

    <form action="/test/form" method="post" class="entry col-lg-6 col-md-8 col-sm-12" id="loginForm">
      <div class="mb-3">
        <label class="form-label">아이디</label>
        <input type="hidden" class="form-control" name="user_id" value="test">
      </div>
      <div class="mb-3">
        <label class="form-label">비밀번호</label>
        <input type="password" class="form-control" name="user_pwd">
      </div>
      <div class="mb-3">
        <label class="form-label">닉네임</label>
        <input type="text" class="form-control" name="user_nickname">
      </div>
      <div class="mb-3">
        <label class="form-label">이메일</label>
        <input type="text" class="form-control" name="user_email">
      </div>
      <button type="submit" class="btn btn-primary">로그인</button>
    </form>

  </div>
</section><!-- End Blog Section -->
