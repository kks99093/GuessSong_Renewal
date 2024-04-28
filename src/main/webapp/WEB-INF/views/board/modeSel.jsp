<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/modeSel.js"></script>
<link rel="stylesheet" href="/css/modeSel.css">
<meta charset="UTF-8">
<title>방 만들기</title>
</head>
<body>	
	<div class="title_div">
		<span class="title_span">${songBoard.title }</span>
	</div>
	<div class="modSel_container">
		<div class="mode_div">
			<div id="multiPlay_div">
				<span>방 만들기</span>
			</div>
		</div>
		<div class="create_div">
			<form method="post" id="createRoomFrm">
				<div class="create_span">닉네임 : <input class="create_input" type="text" id="userName" name="userName" ></div>
				<div class="multi_div">
					<div class="create_span">제목 : <input class="create_input multi_input" type="text" id="title" name="title" ></div>
					<div class="create_span">비밀번호 : <input placeholder="공백일 경우 비밀번호 없음" class="create_input multi_input" type="password" id="password" name="password" ></div>
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
</body>
</html>