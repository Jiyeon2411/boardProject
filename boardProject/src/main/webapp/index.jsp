<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>인기게시판</title>
<link rel="stylesheet" href="./css/style.css" />
</head>
<body>
	<div class="wrap">
		<table class="board_list">
			<caption>
				<h1>인기게시판</h1>
			</caption>
			<thead>
				<tr>
					<th>번호</th>
            		<th>제목</th>
            		<th>글쓴이</th>
            		<th>작성일</th>
            		<th>조회수</th>
            		<th>추천수</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="board" items="${boardList}" varStatus="status">
			
			</c:forEach>
			</tbody>
		</table>
	</div>
</body>
</html>