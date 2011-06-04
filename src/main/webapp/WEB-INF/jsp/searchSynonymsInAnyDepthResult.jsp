<h1> Word to search: <b>earth</b></h1>
<h4>Result(any depth) is: </h4>

    <c:forEach var="synonym" items="${result}" varStatus="status">
      <ul style="border:1px dotted gray;">
        <li><b><i><c:out value="${synonym.name}"/></i></b></li>
        <li><c:out value="${synonym.type}"/></li>
        <li><c:out value="${synonym.chineseMeaning}"/></li>
        <li><c:out value="${synonym.englishMeaning}"/></li>
      </ul>

    </c:forEach>

<a href="openSearchSynonymsInAnyDepth.html">back to search</a>
