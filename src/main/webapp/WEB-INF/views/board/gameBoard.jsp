<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>     
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="/css/gameBoard.css">
<meta charset="UTF-8">
<title>노래 맞추기</title>
</head>
<body oncontextmenu="return false" onselect="return false">

<div class="gameBoard_div">
	<div class="gameBoard_container">
		<div class="gameBoard_songInfo">
			<div class="skipSong_div" id="skip_div"><span id="skip_span">넘기기 <span id="skip_count_span"></span></span></div>
			<div class="skipSong_div" id="result_div"><span id="skip_span">결과창 보기 <span id="result_count_span"></span></span></div>
			<div id="songHint_div"><span></span></div>
		</div>
		<div class="gameBoard_main">
			<div class="gameBoard_chat">
				<div class="chatOutput_div border" id="chatData"></div>
				<div class="chatInput_div">
					<input type="text" class="chatInput_input" id="chatInput"> 
					<span class="chat_submit" id="chat_submit">전송</span>
				</div>
			</div>
			<div class="gameBoard_songList" id="before_songList">
			</div>
			<div class="gameBoard_notice">
				<h3>안내사항</h3>
				<span>정답은 공백, 소문자, 대문자 상관없이 입력 가능합니다.</span><br>
				<span>(Good Boy => gOo D b oY  라고 입력해도 정답으로 인식합니다.)</span><br>
				<span>과반수 이상 넘기기를 누를 시 다음 노래로 넘어갑니다.</span><br>
				<span>정답을 맞췄을 경우 10초후 자동으로 다음 노래로 넘어 갑니다.</span><br>
				<span>(그전에 바로 넘기려면 넘기기를 누르세요)</span>
			</div>
		</div>
		
		<div class="gameConf_div">
			<div class="leftSongNum_div">
				<span>남은 곡 : </span><span id="currentSong">&nbsp;</span>&nbsp;/&nbsp;<span id="totalSong">&nbsp;</span>
			</div>
			<div class="volumeControl_div">
				<span> 볼륨 조절 : <input type="text" id="soundVolumeInput" value="0" readonly> <button id="soundVolumeBtn">볼륨 변경</button></span>
			</div>
		</div>
		<div class="gameBoard_userInfo border">
		</div>			
	</div>
	<div id="youtubePlayer">
		<div id="player"></div>
	</div>
	<input type="hidden" value="${userInfo.name }" id="name">
	<input type="hidden" value="" id="sessionId">
	<input type="hidden" value="${gameRoom.roomPk }" id="roomNumber">
	<input type="hidden" value="" id="color">		
</div>

<div class="result_table_div">
	<h3>게임 결과 </h3>
	<table class="result_table">
		<thead class="result_table_thead">
			<tr class="result_table_tr">
				<th class="result_table_th">등수</th>
				<th class="result_table_th">닉네임</th>
				<th class="result_table_th">점수</th>
			</tr>
		</thead>
		<tbody class="result_table_tbody">

		</tbody>
	</table>
	<div class="result_home" id="result_home_btn">
		<span class="result_home_span">홈 으로</span>
	</div>
</div>

<script src="/js/gameBoard.js"></script>
</body>
</html>