<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<script src="/js/updSong.js"></script>
<link rel="stylesheet" href="/css/updSong.css">
<meta charset="UTF-8">
<title>노래 관리</title>
</head>
<body>
<div class="top_div">
	<jsp:include page="/WEB-INF/views/board/template/mainTemplate.jsp"></jsp:include>		
</div>

<section>
  <!--for demo wrap-->
  <h1>노래 등록</h1>
  <form action="/proc/insSong" method="post" enctype="multipart/form-data">
    <div class="tbl-header">
    <table cellpadding="0" cellspacing="0" border="0">
      <thead>
        <tr>
          <th>유튜브 주소</th>
          <th>정답 </th>
          <th>힌트</th>
          <th>년도</th>
          <th>카테고리</th>
          <th><button type="button" id="delete_btn">수정 및 삭제</button></th>
        </tr>
      </thead>
    </table>
  </div>
    <div class="tbl-content">
    <table cellpadding="0" cellspacing="0" border="0">
      <tbody id="songList_tbody">
      	<c:forEach var="songInfo" items="${songInfoList.content }">
      		<tr>
      			<td><input type="text" id="youtubeUrl${songInfo.songPk}" class="youtubeUrl song_input readonly input${songInfo.songPk}" value="${songInfo.youtubeUrl }"></td>
				<td><input type="text" id="answer${songInfo.songPk}" class="answer song_input readonly input${songInfo.songPk}" value="${songInfo.answer }"> </td>
				<td><input type="text" id="hint${songInfo.songPk}" class="hint song_input readonly input${songInfo.songPk}" value="${songInfo.hint }"></td>
				<td><input type="number" id="year${songInfo.songPk}" class="year song_input readonly input${songInfo.songPk}" value="${songInfo.year}"></td>
				<td>
					<select id="category${songInfo.songPk}" class="readonly input${songInfo.songPk }">					
						<option value="ballad" ${songInfo.category == 'ballad' ? 'selected' : '' }> 발라드 </option>
						<option value="idol" ${songInfo.category == 'idol' ? 'selected' : '' }> 아이돌 </option>
						<option value="hiphop" ${songInfo.category == 'hiphop' ? 'selected' : '' }> 힙합 </option>
						<option value="pop" ${songInfo.category == 'pop' ? 'selected' : '' }> 팝송 </option>
						<option value="R&B" ${songInfo.category == 'R&B' ? 'selected' : '' }> R&B </option>
					</select>
				</td>				
				<td id="songBtn${songInfo.songPk}">
					<button class="remove_btn" type="button" id="updSongBtn${songInfo.songPk}" onclick="updSong(this)" value="${songInfo.songPk }">수정</button>
					<button class="remove_btn" type="button" onclick="delSong(this)" value="${songInfo.songPk }">X</button>					
				</td>
      		</tr>
      	</c:forEach>	       
      </tbody>
    </table>
  </div>
  <div><button class="submit_btn">등록</button></div>
  		  
  </form>  
</section>
<div class="page_div">
	<ul>
		<li class="${songInfoList.pageable.pageNumber == 0 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${songInfoList.pageable.pageNumber-1})">이전</li>	
		<c:forEach begin="${startIdx+1}" end="${songInfoList.totalPages > startIdx+10 ? startIdx+10 : songInfoList.totalPages}" varStatus="status">
			<li class="${songInfoList.pageable.pageNumber == status.index-1 ? 'disable_evt disable_cursor current_page' : '' }" onclick="pageMove(${status.index-1})">${status.index }</li>
		</c:forEach>
		<li class="${songInfoList.pageable.pageNumber >= songInfoList.totalPages-1 ? 'disable_evt disable_cursor' : '' }" onclick="pageMove(${songInfoList.pageable.pageNumber+1})">다음></li>
	</ul>
</div>
</body>
</html>