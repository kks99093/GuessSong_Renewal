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
		<div class="regSong_div top_border" id="guessSong_btn">
			<span class="top_span" id="guessSong_span">노래 맞추기</span>
		</div>
	</div>
	
	<div class="bottom_div">
		<div class="create_room_title">
			<span>방 만들기</span>
		</div>
		<div class="create_div">
			<form method="post" id="createRoomFrm">
				<div class="create_span">솔로 플레이 : <input type="checkbox" name="solo" class="check_box_input" id="soloChk"></div>
				<div class="create_span">년도 : 
				<select id="startYear" class="year_select">
				</select>
				 ~ 
				<select id="endYear" class="year_select">
				</select>
				</div>
				
				<div class="create_span">장르 :
					<select class="select_option">
						<option>전체</option>
						<option>아이돌</option>
						<option>발라드</option>
						<option>힙합</option>
					</select> 
				</div>
				<div class="create_span"> 곡 수 :
					<select class="songCnt" id="songCnt">
						<option value="0">랜덤</option>
						<option value="4">4</option>
						<option value="16">16</option>
						<option value="32">32</option>
						<option value="64">64</option>
						<option value="128">128</option>
					</select>
				</div>
				<div class="multi_div">
					<div class="create_span">닉네임 : <input class="create_input" type="text" id="userName" name="userName" ></div>				
					<div class="create_span">제목 : <input class="create_input multi_input" type="text" id="title" name="title" ></div>
					<div class="create_span">비밀번호 : <input placeholder="공백일 경우 비밀번호 없음" type="password" id="password" name="password" ></div>
					<div class="create_span">최대인원 :
					<select class="create_input" name="amount">
						<option class="create_input ">1</option>
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
				<input type="hidden" name="userRole" value="1">
				<input type="hidden" name="boardPk" value="${songBoard.boardPk}" >
				<input type="hidden" name="createRoom" value="1">
				<div class="create_div"><span id="create_btn">만들기</span></div>
			</form>
		</div>
	</div>
</div>	

</body>
</html>