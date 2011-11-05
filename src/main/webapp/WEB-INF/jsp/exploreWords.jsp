
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



</style>

<div id="container">
  <section>
      <header>
          <h1>Word Mindmap</h1>
          <p>A better yet intuitive way to view relationships among words.</p>
          <p> Try to click 'land'.</p>
      </header>
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

