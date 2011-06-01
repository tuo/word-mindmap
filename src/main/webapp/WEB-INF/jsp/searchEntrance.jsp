<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Start Searching</title>
</head>

<body>
<h1>Search</h1>


<h4>Now, you can click to do following operations on words:</h4>

<ul>
   <li><a href="openSearchSpecificWord.html">Search specific word by name(Exact Match)</a></li>
   <li><a href="openSearchNearbySynonyms.html">Search nearby synonyms for word</a></li>
   <li><a href="openSearchSynonymsInAnyDepth.html">Search all synonyms for word in any depth</a></li>
</ul>

<cite>Other operations are still working in progress..........</cite>

<div style="display:none">

<hr/>

<h4><a href="searchAllWords.html">Search all words(synonyms, idioms, extentions)  from <b>'earth'</b></a></h4>


<p>
    You can reference by following picture:
</p>
<img src="images/wordmap.jpg"/>

</div>
</body>
</html>