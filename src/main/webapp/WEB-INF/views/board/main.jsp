<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication property="principal" var="principal"/>     
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/main.js"></script>
<link rel="stylesheet" href="/css/main.css">
<meta charset="UTF-8">
<title>노래 맞추기</title>
</head>
<body>
<div class="container">
	<div class="top_div">
		<div class="gmae_list_div top_border" id="guessSong_btn">
			<span class="top_span" id="guessSong_span">노래맞추기</span>
		</div>
		<c:choose>
			<c:when test="${principal == 'anonymousUser'}">
				<div>
					<div class="login_div" id="logoinBtn">
						<span class="top_span login_btn">로그인</span>
					</div>
					<div class="login_div" id="joinBtn">
						<span class="top_span login_btn">회원가입</span>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<div>					
					<div class="user_name_div">
						<span>${principal.userInfo.name } 님 </span>
					</div>
					<div class="login_div">
						<span id="logoutBtn"> 로그아웃 </span>
					</div>
					<c:if test="${principal.userInfo.role == 'ROLE_ADMIN' }">
						<div class="login_div">
							<span id="regSong">노래 등록</span>
						</div>
					</c:if>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	
	<div class="create_container">
		<div class="mode_div">
			<div id="title_div">
				<span>방 만들기</span>
			</div>
		</div>
		<div class="create_div">
			<form method="post" id="createRoomFrm">
				<div class="years_div create_span">년도 : 
					<select class="create_input"  id="beforeYears" name="beforeYears">
					</select>
					~
					<select class="create_input" id="afterYears"name="afterYears">
					</select>
				</div>
				<div class="song_category create_span"> 장르 :
					<select class="create_input" name="category" id="category">
						<option value="all"> 전체 </option>
						<option value="ballad"> 발라드 </option>
						<option value="idol"> 아이돌 </option>
						<option value="hiphop"> 힙합 </option>
						<option value="pop"> 팝송 </option>
					</select>
				</div>
				<div class="create_span"> 개수 :
					 <select class="create_input" name="count" id="count">
					 	<option value="8">8</option>
					 	<option value="16">16</option>
					 	<option value="32">32</option>
					 	<option value="64">64</option>
					 	<option value="128">128</option>
					 </select>
				</div>
			  
				
				<c:choose>
					<c:when test="${principal == 'anonymousUser'}">
						<div class="create_span create_span">닉네임 : <input class="create_input" type="text" id="userName" name="name" ></div>
					</c:when>
					<c:otherwise>
						<div class="create_span create_span">닉네임 : <input class="read_input" type="text" id="userName" name="name" value="${principal.userInfo.name }" readOnly></div>
					</c:otherwise>
				</c:choose>
				
				
				<div class="multi_div">
					<div class="create_span">제목 : <input class="create_input multi_input" type="text" id="title" name="title" ></div>
					<div class="create_span">비밀번호 : <input placeholder="공백일 경우 비밀번호 없음" class="create_input multi_input" type="password" id="password" name="password" ></div>
					<div class="create_span">최대인원 :
					<select class="create_input" name="amount">
						<option class="create_input">1</option>
						<option class="create_input">2</option>
						<option class="create_input">3</option>
						<option class="create_input">4</option>
						<option class="create_input">5</option>
						<option class="create_input">6</option>
						<option class="create_input">7</option>
						<option class="create_input">8</option>
					</select> 
					</div>
				</div>

				<div class="create_btn_div"><span id="create_btn">만들기</span></div>
			</form>
		</div>
	</div>
</div>
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
			<form action="/joinProc" method="post">
				<span class="user_span">아이디 : <input type="text" placeholder="아이디" name="username" id="joinId"/></span>
				<span class="user_span">비밀번호 : <input type="password" placeholder="비밀번호" name="password" id="joinPw"/></span>
				<span class="user_span">닉네임 : <input type="text" placeholder="닉네임" name="name" id="joinName"/></span>
				<span class="user_span"><button id="joinBtn">회원가입</button></span>		
			</form>
		</div>
	</div>
</div>

 



<input type="hidden" value="${loginFailed}" id="loginFaileChk">
<input type="hidden" value="${joinResult}" id="joinResult">
</body>
</html>