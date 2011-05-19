<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Populate words successfully.</title>
</head>

<body>
<h1>Populate words successfully</h1>

<form action="searchSimilarWords.html">
    <label>Search word that like for: </label>
    <input type="text" name="word" size="20"/>
    <input type="submit" name="button" value="submit"/>
</form>


<hr/>

<h4><a href="searchAllWords.html">Search all words(synonyms, idioms, extentions)  from <b>'earth'</b></a></h4>


<p>
    You can reference by following picture:
</p>
<img src="images/wordmap.jpg"/>
</body>
</html>