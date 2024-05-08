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
<title>노래 등록</title>
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
          <th><button type="button" id="add_songList_btn">노래 목록 추가</button></th>
        </tr>
      </thead>
    </table>
  </div>
    <div class="tbl-content">
    <table cellpadding="0" cellspacing="0" border="0">
      <tbody id="songList_tbody">
   			<tr id="songInfoTr1">
				<td><input type="text" placeholder="유튜브 주소" name="youtubeUrl" class="youtubeUrl"></td>
				<td><input type="text" placeholder="정답" name="answer" class="answer"> </td>
				<td><input type="text" placeholder="힌트" name="hint" class="hint"></td>
				<td><input type="number" placeholder="년도" name="year" id="year" class="year"></td>
				<td>
					<select name="category">
						<option value="ballad"> 발라드 </option>
						<option value="idol"> 아이돌 </option>
						<option value="hiphop"> 힙합 </option>
						<option value="pop"> 팝송 </option>
						<option value="R&B"> R&B </option>
					</select>
				</td>				
				<td><button class="remove_btn" type="button" onclick="removeTr(1)">X</button></td>
				<input type="hidden" id="trNumber" value="1">
	       </tr>	       
      </tbody>
    </table>
  </div>
  <div><button class="submit_btn">등록</button></div>
  		  
  </form>  
</section>

</body>
</html>