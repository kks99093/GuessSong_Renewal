<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authentication property="principal" var="principal"/>      
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/gameList.js"></script>
<link rel="stylesheet" href="/css/gameList.css">
<meta charset="UTF-8">
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, width=device-width">
<title>게임 목록</title>
<style>

</style>
</head>
<body>
<div>
	<div class="top_div">
		<jsp:include page="/WEB-INF/views/board/template/mainTemplate.jsp"></jsp:include>		
	</div>
	<div class="table_title_div">
		<h3>게임 목록</h3>
	</div>	
	<table class="table_div">
		<thead>
			<tr>
			    <th class="text-left th_title">제목</th>
			    <th class="text-left th_category">장르</th>
			    <th class="text-left th_count">개수</th>
			    <th class="text-left th_reader">방장</th>
			    <th class="text-left th_amount">인원</th>
			    <th class="text-left th_pass">&#128274;</th>
			</tr>
		</thead>
		<tbody class="table-hover">
			<c:forEach var="gameRoom" items="${gameRoomList.content}">
				<tr class="gameList_tr" param1="${gameRoom.roomPk }" param2="${gameRoom.password != null ? '1' : ''}">
					<td class="text-left">${gameRoom.title } </td>
					<td class="text-left">${gameRoom.category } </td>
					<td class="text-left">${gameRoom.count } </td>
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
				<li class="${gameRoomList.pageable.pageNumber == status.index-1 ? 'disable_evt disable_cursor current_page' : '' }" onclick="pageMove(${status.index-1})">${status.index }</li>
			</c:forEach>
			<li class="${gameRoomList.pageable.pageNumber >= gameRoomList.totalPages-1 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${gameRoomList.pageable.pageNumber+1})">다음></li>
		</ul>
	</div>
</div>

<div id="popup" class="hide">
  <div class="content">
  	<div class="pop_input_div">
  	<c:choose>
  		<c:when test="${principal == 'anonymousUser'}">
			<div id="pop_userName_div"><span class="pop_name_span">닉네임 : </span><input class="input_name" type="text" id="name" name="name"></div>
		</c:when>
		<c:otherwise>
			<div id="pop_userName_div"><span class="pop_name_span">닉네임 : </span><input class="read_input_name" type="text" id="
" name="name" value="${principal.userInfo.name }" readOnly></div>
			<input type="hidden" id="loginName">
		</c:otherwise>
  	</c:choose>  	
  	</div>
  	<button class="pop_btn" id="playGame"> 들어가기</button>
    <button class="pop_btn" id="closePopup">닫기</button>
  </div>
</div>







</body>
</html>