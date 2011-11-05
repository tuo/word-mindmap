<%--<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<h2>Exact Match</h2>--%>
<%--<br/>--%>
<%--<form action="searchSpecificWord.html">--%>
    <%--<label>Name of word to search: </label>--%>
    <%--<input type="text" name="word" size="20"/>--%>
    <%--<br/>--%>
    <%--<input type="submit" name="button" value="submit"/>--%>
<%--</form>--%>
<%--<hr/>--%>


<%--<a href="startSearching.html">back to search operation home page.</a>--%>
<style type="text/css">
  *{
    margin: 0;
    padding: 0;
  }
  html,body{
   margin: 0;
    padding: 0;
  }
  body{
    background: #080e24;
/*    background: white;*/
    color: #626668;
/*    padding:40px;*/
  }
  a{
    color: red;
    text-decoration: none;
  }
  section {
      display: block;
      position: relative;
  }
  header{
    text-align: center;
  }
  header h1{
    font-size: 46px;
    line-height: 1;
    color: #eee5a2;
    text-transform: uppercase;
    text-shadow: 5px 5px #32312b;
  }


  /* the word mindmap system stles*/
  ul.wordsystem {
    position: relative;
    height: 640px;
    list-style: none;
    /*set the tone*/
    -webkit-transition: all 0.09s ease-in;
    -moz-transition: all 0.09s ease-in;
    -o-transition: all 0.09s ease-in;
    transition: all 0.09s ease-in;
  }

  ul.wordsystem li {
/*    text-indent: -9999px;*/
    overflow: hidden;
    display: block;
    position: absolute;
    border: 2px solid #394057;
    width: 60px;
    height: 60px;
    -webkit-border-radius: 30px;
    -webkit-transition: -webkit-transform 1s ease-in
  }

  ul.wordsystem li a{
    display: block;
    width:100%;
    height:100%;
        padding: 10px 10px;
  }

  ul.wordsystem li.active{
    border-color: #aa4200;
  }
  ul.wordsystem li.active.earth , ul.wordsystem li.active span{
    -webkit-transform: scale(1.3);
  }
  ul.wordsystem li.start-word{
/*    width: 100px;
    height: 100px;
    -webkit-border-radius: 50px;*/
    background: #fc3;
    background-image: -webkit-gradient(
            linear,
            left bottom,
            left top,
            color-stop(0.22, rgb(204,153,0)),
            color-stop(1, rgb(255,219,112))
    );
    top: 302px;
    left: 462px;
    border: none;
    -webkit-box-shadow: 0  0 90px #c90;
    z-index: 100;
    -webkit-transform: scale(1.3);

  }

  ul.wordsystem li:nth-child(2) {
    background: green;
    top: 192px;
    left: 292px;
    border: none;
  }

  ul.wordsystem li:nth-child(3) {
    background: purple;
    top: 202px;
    left: 492px;
    border: none;
  }

  ul.wordsystem li:nth-child(4) {
    background: #4ff;
    top: 202px;
    left: 322px;
    border: none;
  }

  ul.wordsystem li:nth-child(5) {
    background: #cc0;
    top: 202px;
    left: 642px;
    border: none;
  }

  ul.wordsystem li.non-active{
    display: none;
  }


</style>

<!-- <div id="sun"><span>Sun</span></div> -->
<div id="container">
  <section>
      <header>
          <h1>Word Mindmap</h1>
          <p>A better yet intuitive way to view relationships among words.</p>
          <p> Try to click 'land'.</p>
      </header>
  </section>

  <section>
    <ul class="wordsystem">
       <li class="start-word"><a href="#earth"><span>earth</span></a></li>
       <li class='related'><a href="#world"><span>world</span></a></li>
       <li class='related'><a href="#land"><span>land</span></a></li>
       <!-- <li><a href="#dirt"><span>dirt</span></a></li>
       <li><a href="#ground"><span>ground</span></a></li>  -->
       <li class='related non-active'><a href="#sky"><span>sky</span></a></li>
       <li class='related non-active'><a href="#ocean"><span>ocean</span></a></li>
    </ul>
  </section>
</div>
<script>
$(document).ready(function(){

    $.ajax({
        url: "getWordsNearTo.html",
        data: {"name" : "earth"},
        success: function(data){
               alert(data);
        }
    });

});
</script>

