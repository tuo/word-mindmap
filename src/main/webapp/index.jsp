<html>
<!-- index.jsp  -->
<head>
    <title>Word Mindmap</title>
    <link type="text/css" href="css/base.css" rel="stylesheet" />
    <link type="text/css" href="css/ForceDirected.css" rel="stylesheet" />
    <link type="text/css" href="css/bird-view-nodes.css" rel="stylesheet" />


    <!-- JIT Library File -->
    <script language="javascript" type="text/javascript" src="javascripts/jit-yc.js"></script>


    <script language="javascript" type="text/javascript" src="javascripts/jquery-1.6.1.min.js"></script>

    <!-- Example File -->
    <script language="javascript" type="text/javascript" src="javascripts/bird-view-nodes.js"></script>

</head>
<body>
<h1>Word MindMap</h1>
<blockquote>
    A better way to view the relationship among words.
</blockquote>
<h4 id="shit"><a href="populateData.html"> start experiencing...........</a></h4>

<hr/>
<i>contribute to github: <a href="https://github.com/Tuo-Huang/word-mindmap">https://github.com/Tuo-Huang/word-mindmap</a></i>

<%--<img src="images/wordmap.jpg"/>--%>
<div id="container">
<div id="center-container">
    <div id="infovis"></div>
</div>
<cite><b>Try to click the nodes.</b></cite>
<cite>You can scroll to scale down the graph if graph filled in too much or you can move the nodes</cite>
<div id="right-container">
<div id="inner-details"></div>
</div>
</div>
<div id="log"></div>

</body>
</html>
<script type="text/javascript">
//        init();
$(document).ready(function() {
    $.ajax({
        url: "words.html",
        success: function(data){
            init(eval(data));
        }
    });
});
</script>