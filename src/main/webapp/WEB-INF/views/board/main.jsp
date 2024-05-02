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
		<jsp:include page="/WEB-INF/views/board/template/mainTemplate.jsp"></jsp:include>		
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
						<option value="pop"> 팝 </option>
						<option value="R&B"> R&B </option>
					</select>
				</div>
				<div class="create_span"> 개수 :
					 <select class="create_input" name="count" id="count">
					 <option value="4">4</option>
					 	<option value="8">8</option>
					 	<option value="16">16</option>
					 	<option value="32">32</option>
					 	<option value="64">64</option>
					 	<option value="128">128</option>
					 </select>
				</div>
			  
				
				<c:choose>
					<c:when test="${principal == 'anonymousUser'}">
						<div class="create_span create_span">닉네임 : <input class="create_input" type="text" id="name" name="name" ></div>
					</c:when>
					<c:otherwise>
						<div class="create_span create_span">닉네임 : <input class="read_input" type="text" id="name" name="name" value="${principal.userInfo.name }" readOnly></div>
						<input type="hidden" id="loginName">
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

</body>
</html>