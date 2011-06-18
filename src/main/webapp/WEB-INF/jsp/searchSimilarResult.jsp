<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h1> Word to search: <c:out value="${wordToSearch}"/></h1>
<h4>Result are: </h4>

<p>
    <c:out value="${result}"/>
</p>


<a href="startSearching.html">back to search</a>
