<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Search Word.</title>
</head>

<body>

<br/>
<form action="searchSpecificWord.html">
    <label>Name of word to search: </label>
    <input type="text" name="word" size="20"/>
    <br/>
    <input type="submit" name="button" value="submit"/>
</form>
<hr/>
<a href="populateSuccess.html">back to search operation home page.</a>
</body>
</html>