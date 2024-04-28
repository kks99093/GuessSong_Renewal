<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>     
<!DOCTYPE html>
<html>
<head>
<script src="/js/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="/css/test.css">
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
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


</body>
</html>