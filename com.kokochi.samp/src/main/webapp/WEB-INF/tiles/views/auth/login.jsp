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

    <form action="/auth/login" method="post" class="entry col-lg-6 col-md-8 col-sm-12" id="loginForm">
      <div class="mb-3">
        <label class="form-label">아이디</label>
        <input type="text" class="form-control" name="user_id">
      </div>
      <div class="mb-3">
        <label class="form-label">비밀번호</label>
        <input type="password" class="form-control" name="user_pwd">
      </div>
      <p style="color:red;">${errMsg}</p>
      <div class="mb-3 form-check">
        <input type="checkbox" class="form-check-input">
        <label class="form-check-label">Check me out</label>
      </div>
      <button type="submit" class="btn btn-primary">로그인</button>
    </form>

  </div>
</section><!-- End Blog Section -->
