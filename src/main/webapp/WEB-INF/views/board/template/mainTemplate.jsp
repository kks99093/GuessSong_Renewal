<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication property="principal" var="principal"/>
<script src="/js/mainTemplate.js"></script>
<link rel="stylesheet" href="/css/mainTemplate.css">         
	<div class="gmae_list_div top_border" id="gameListBtn">
		<span class="top_span">게임 리스트</span>
	</div>
	<div class="gmae_list_div top_border" id="gameCreateBtn">
		<span class="top_span">방 만들기</span>
	</div>
<!-- 
	<div class="gmae_list_div top_border" id="guessSong_btn">
		<span class="top_span" id="guessSong_span">노래맞추기</span>
	</div>
 -->		
	<c:choose>
		<c:when test="${principal == 'anonymousUser' || principal == null}">
			<div>
				<div class="login_div" id="logoinPopBtn">
					<span class="top_span login_btn">로그인</span>
				</div>
				<div class="login_div" id="joinPopBtn">
					<span class="top_span login_btn">회원가입</span>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<div>					
				<div class="user_name_div">
					<span class="login_top_span">${principal.userInfo.name } 님 </span>
				</div>
				<div class="login_div">
					<span class="login_top_span" id="logoutBtn"> 로그아웃 </span>
				</div>
				<c:if test="${principal.userInfo.role == 'ROLE_ADMIN' }">
					<div class="login_div">
						<span class="login_top_span" id="regSong">노래 등록</span>
					</div>
				</c:if>
			</div>
		</c:otherwise>
	</c:choose>
<div class="user_pop hide" id="loginPop">	
	<div class="user_content" id="loginContent">
	<div class="close_btn" id="loginCloseBtn">X</div>
		<div>
			<form action="/auth/loginProc" method="post">
				<span class="user_span">아이디 : <input type="text" placeholder="아이디" name="username" id="loginId"/></span>
				<span class="user_span">비밀번호 : <input type="password" placeholder="비밀번호" name="password" id="loginPw"/></span>
				<span class="user_span"><button id="loginBtn">로그인</button></span>		
			</form>
		</div>
	</div>
</div>


<div class="user_pop hide" id="joinPop">	
	<div class="user_content" id="joinContent">
	<div class="close_btn" id="joinCloseBtn">X</div>
		<div>
			<form action="/joinProc" method="post" id="joinFrm">
				<span class="user_span">아이디 : <input type="text" placeholder="아이디" name="username" id="joinId"/></span>
				<span class="user_span">비밀번호 : <input type="password" placeholder="비밀번호" name="password" id="joinPw"/></span>
				<span class="user_span">닉네임 : <input type="text" placeholder="닉네임" name="name" id="joinName"/></span>
				<span class="user_span"><button type="button" id="joinBtn">회원가입</button></span>		
			</form>
		</div>
	</div>
</div>
<input type="hidden" value="${loginFailed}" id="loginFaileChk">