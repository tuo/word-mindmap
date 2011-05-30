<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Nearby Synonyms Result.</title>
</head>

<body>
<h1> Word to search: <b>earth</b></h1>
<h4>Result(1 depth) is: </h4>

    <c:forEach var="synonym" items="${result}" varStatus="status">
      <ul style="border:1px dotted gray;">
        <li><b><i><c:out value="${synonym.name}"/></i></b></li>
        <li><c:out value="${synonym.type}"/></li>
        <li><c:out value="${synonym.chineseMeaning}"/></li>
        <li><c:out value="${synonym.englishMeaning}"/></li>
      </ul>

    </c:forEach>

<a href="populateSuccess.html">back to search</a>
</body>
</html>