<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/regSong.js"></script>
<link rel="stylesheet" href="/css/regSong.css">
<meta charset="UTF-8">
<title>노래 등록하기</title>
</head>
<body>


<section>
  <!--for demo wrap-->
  <h1>노래 리스트 만들기</h1>
  <form action="/proc/regSong" method="post" enctype="multipart/form-data" onsubmit="return submitChk()">
  <div class="tbl-header">
    <table cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr>
          <th>제목</th>
          <th>비밀번호</th>
          <th>대표 이미지</th>
        </tr>
      </thead>
    </table>
  </div>
  <div class="tbl-content">
    <table cellpadding="0" cellspacing="0" border="0">
      <tbody>
        <tr>
			<td><input type="text" placeholder="제목" name="title" id="title" value="${songBoard == null ? '' : songBoard.title }"></td>
			<td><input type="password" placeholder="비밀번호" name="password" id="password"> </td>
			<td>이미지 : <input type="file" name="songImg" value="${songBoard == null ? '' : songBoard.img }"></td>
        </tr>
      </tbody>
    </table>
  </div>
    <div class="tbl-header">
    <table cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr>
          <th>유튜브 주소</th>
          <th>정답 </th>
          <th>힌트</th>
          <th><button type="button" id="add_songList_btn">노래 목록 추가</button></th>
        </tr>
      </thead>
    </table>
  </div>
    <div class="tbl-content">
    <table cellpadding="0" cellspacing="0" border="0">
      <tbody id="songList_tbody">
      	<c:choose>
      		<c:when test="${songBoard == null}">
      			<tr id="songInfoTr1">
		          <td><input type="text" placeholder="유튜브 주소" name="youtubeUrl" class="youtubeUrl"></td>
		          <td><input type="text" placeholder="정답" name="answer" class="answer"> </td>
		          <td><input type="text" placeholder="힌트" name="hint" class="hint"></td>
		          <td><button class="remove_btn" type="button" onclick="removeTr(1)">X</button></td>
		        </tr>
		        <input type="hidden" id="trNumber" value="1">
      		</c:when>
      		<c:otherwise>
      			<c:forEach var="songinfo" items="${songBoard.songInfoList }" varStatus="status">
	      			<tr id="songInfoTr${status.count}">
			          <td><input type="text" placeholder="유튜브 주소" name="youtubeUrl" class="youtubeUrl" value="${songinfo.youtubeUrl }"></td>
			          <td><input type="text" placeholder="정답" name="answer" class="answer" value="${songinfo.answer }"> </td>
			          <td><input type="text" placeholder="힌트" name="hint" class="hint" value="${songinfo.hint }"></td>
			          <td><button class="remove_btn" type="button" onclick="removeTr(${status.count})">X</button></td>
			        </tr>
			        <c:if test="${status.last}">
			        	<input type="hidden" id="trNumber" value="${status.count}">
			        </c:if>
      			</c:forEach>
      			<input type="hidden" name="boardPk" id="boardPk" value="${songBoard.boardPk }">
      		</c:otherwise>
      	</c:choose>
      </tbody>
    </table>
  </div>
  <c:choose>
  	<c:when test="${songBoard == null }">
  		<button class="submit_btn">등록</button>
  	</c:when>
  	<c:otherwise>
  		<button class="submit_btn">수정</button>
  	</c:otherwise>
  </c:choose>
  
  </form>  
</section>
</body>
<script>
	
</script>
</html>