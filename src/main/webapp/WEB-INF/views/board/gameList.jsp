<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/gameList.js"></script>
<link rel="stylesheet" href="/css/gameList.css">
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1.0; maximum-scale=1.0; width=device-width;">
<title>게임 목록</title>
<style>

</style>
</head>
<body>
<div>
	<div class="table_title_div">
		<h3>게임 목록</h3>
	</div>	
	<table class="table_div">
		<thead>
			<tr>
			    <th class="text-left th_title">제목</th>
			    <th class="text-left th_reader">방장</th>
			    <th class="text-left th_amount">인원</th>
			    <th class="text-left th_pass">&#128274;</th>
			</tr>
		</thead>
		<tbody class="table-hover">
			<c:forEach var="gameRoom" items="${gameRoomList.content}">
				<tr class="gameList_tr" param1="${gameRoom.roomPk }" param2="${gameRoom.password != null ? '1' : ''}">
					<td class="text-left">${gameRoom.title } </td>
					<td class="text-left">${gameRoom.reader } </td>
					<td class="text-left">${gameRoom.headCount } / ${gameRoom.amount}</td>
					<td>
						<c:if test="${gameRoom.password != null }">
							&#128274;							
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="page_div">
		<ul>
			<li class="${gameRoomList.pageable.pageNumber == 0 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${gameRoomList.pageable.pageNumber-1})">이전</li>	
			<c:forEach begin="${startIdx+1}" end="${gameRoomList.totalPages > startIdx+10 ? startIdx+10 : gameRoomList.totalPages}" varStatus="status">
				<li class="${gameRoomList.pageable.pageNumber == status.index-1 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${status.index-1})">${status.index }</li>
			</c:forEach>
			<li class="${gameRoomList.pageable.pageNumber == gameRoomList.totalPages-1 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${gameRoomList.pageable.pageNumber+1})">다음></li>
		</ul>
	</div>
</div>

<div id="popup" class="hide">
  <div class="content">
  	<div class="pop_input_div">
  		<div id="pop_userName_div"><span>닉네임 : </span><input type="text" id="userName"></div>
  	</div>
  	<button id="playGame"> 들어가기</button>
    <button id="closePopup">닫기</button>
  </div>
</div>
</body>
</html>