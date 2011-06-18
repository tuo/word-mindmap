<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h1> Name of word to search: <c:out value="${wordName}"/></h1>
<h4>Result are: </h4>

<p>
    <c:out value="${result}"/>
</p>

<a href="openSearchSpecificWord.html">back to search specific word.</a>
