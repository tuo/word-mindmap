<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<h1> Word to search: <b>earth</b></h1>
<h4>Result(1 depth) is: </h4>

    <c:forEach var="synonym" items="${result}" varStatus="status">
      <ul style="border:1px dotted gray;">
        <li><b><i><c:out value="${synonym.name}"/></i></b></li>
        <li><c:out value="${synonym.type}"/></li>
        <li><c:out value="${synonym.chineseMeaning}"/></li>
        <li><c:out value="${synonym.meaning}"/></li>
      </ul>

    </c:forEach>


<a href="openSearchNearbySynonyms.html">back to search</a>
