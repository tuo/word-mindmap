<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Exact Word Result.</title>
</head>

<body>
<h1> Name of word to search: <c:out value="${wordName}"/></h1>
<h4>Result are: </h4>

<p>
    <c:out value="${result}"/>
</p>

<a href="openSearchSpecificWord.html">back to search specific word.</a>
</body>
</html>