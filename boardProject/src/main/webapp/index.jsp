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
				<h1>인기글</h1>
			</caption>
			<thead>
				<tr>
					<th>번호</th>
            		<th>제목</th>
            		<th>글쓴이</th>
            		<th>작성일</th>
            		<th>조회수</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="board" items="${boardList}" varStatus="status">
				<tr>
					<td>${board.board_no}</td>
            		<td class="title"><a href="view?board_no=${board.board_no}">${board.title}</a></td>
            		<td>${board.user_id}</td>
            		<td>${board.reg_date}</td>
           			<td>${board.views}</td>
           			<td>${board.likey}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="bt_wrap bt_list">
        <a href="write">글쓰기</a>
      </div>
	</div>
	<script>
    	<c:if test = "${param.error != null}">
    		alert("${param.error}");
    	</c:if>
    	<c:if test = "${error != null}">
		alert("${error}");
		</c:if>
    </script>
</body>
</html>