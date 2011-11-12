
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
      position: relative;
    height: 800px;
    list-style: none;
    /*set the tone*/
    -webkit-transition: all 0.09s ease-in;
    -moz-transition: all 0.09s ease-in;
    -o-transition: all 0.09s ease-in;
    transition: all 0.09s ease-in;

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

  ul#word-system {
    width:90%;
    height:600px;
    margin: 0 auto;
    padding: 0 20px;
  }
  ul#word-system li {
      list-style: none;
      margin: 0px;
      width: 80px;
      height: 80px;
      background: green;
      border: 1px solid black;
      position: absolute;
      border-radius: 40px;
      background: pink;
      border: none;
      display: block;
      text-align: center;
      vertical-align: middle;;
  }
  ul#word-system li:first-child {
      background: #fc3;
      -webkit-box-shadow: 0  0 40px #c90;
      -moz-box-shadow: 0  0 40px #c90;
      -webkit-transform: scale(1.3);
      -moz-transform: scale(1.3);
  }

</style>

<link href="css/jquery-ui.css" rel="stylesheet" type="text/css"/>

<script src="javascripts/jquery-1.8-ui.min.js"></script>

<%--<script src="javascripts/effects.explode.js"></script>--%>
<%--<script src="javascripts/effects.core.js"></script>--%>

<%--<script src="javascripts/jquery.circlelist.js"></script>--%>
<%--<script src="javascripts/jquery.path.js"></script>--%>
<%--http://weepy.github.com/jquery.path/--%>

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
    <ul id="word-system">
    </ul>
  </section>
</div>
<script>
$(document).ready(function(){
    var RADIUS = 200;
    var NODE_RADIUS = 40;
    var calculatePoints = function(steps, centerX, centerY) {
        var points = [];
        for (var i = 0; i < steps; i++) {
           var angle = 2 * Math.PI  * i / steps;
           var pointX = (centerX + RADIUS * Math.cos(angle));
           var pointY = (centerY + RADIUS * Math.sin(angle));
           points.push({x : parseInt(pointX - NODE_RADIUS)   , y : parseInt(pointY + NODE_RADIUS)});
        }
        return points;
    }

    var grabWord = function(word) {
        $.ajax({
            url: "getWordsNearTo.html",
            data: {"name" : word},
            success: function(data){
                data = $.parseJSON(data);
                var start = data.startWord;
                var wordSystem = $("#word-system")
                var relatedWords = data.relatedWords;
                wordSystem.empty();
                var startNode = $("<li id='start-word'>" + start.name + '</li>')
                        .appendTo(wordSystem)
                        .position({ my: "center", at: "center", of: wordSystem})
//                    .show('explode', { pieces: 32}, 2000);

                var startOffset = startNode.position();
                var centerX = startOffset.left + NODE_RADIUS;
                var centerY = startOffset.top - NODE_RADIUS;
                var points = calculatePoints(relatedWords.length, centerX, centerY);

                var textToInsert = "";
                var fragment = document.createDocumentFragment();
                $.each(relatedWords, function(index, value){
                    var point = points[index];
                    textToInsert += '<li style=\"left:' + point.x + 'px;top:' + point.y + 'px;\">';
                    textToInsert += value.name;
                    textToInsert += '</li>';
                });
                wordSystem.append(textToInsert);
                wordSystem.find('li').click(function(event){
                    grabWord($(this).text());
                });
            }
        });
    }//grab

    grabWord("earth");
});
</script>

