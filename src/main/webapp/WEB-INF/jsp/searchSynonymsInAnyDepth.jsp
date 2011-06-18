<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<h2>Search All Synonyms(means any depth lookup)</h2>
<br/>
<form action="searchSynonymsInAnyDepth.html" id="searchForm">
    <label>Name of word to search: </label>
    <input type="text" name="name" size="20"/>
    <br/>


  <input type="checkbox" name="relation" value="synonym_with">Synonym(同义词)</input>
  <input type="checkbox" name="relation" value="antonym_with">Antonym(反义词)</input>
  <input type="checkbox" name="relation" value="extension_with">Extension(扩展词)</input>
  <input type="checkbox" name="relation" value="idiom_with">Idom(短语)</input>
    <br/>
    <input type="submit" name="button" value="submit"/>
</form>
<hr/>

<a href="startSearching.html">back to search operation home page.</a>
<div id="container">
    <div id="center-container" style="width:100%;height:600px;">
        <div id="infovis"></div>
    </div>

</div>
<div id="log"></div>


<script type="text/javascript">
//        init();
   function clearCanvas(){
    var canvas = document.getElementById("infovis-canvas");
    var context = canvas.getContext("2d");
    context.clearRect(0, 0, canvas.width, canvas.height);
}

$(document).ready(function() {

    $('#searchForm').submit(function(event){
	    event.preventDefault();
        var name = $('input[name=name]').val();

        var relations = new Array();
        $("input[name='relation']:checked").each(function() {
              relations .push($(this).val());
        });

        var relationshipStr = relations.join(",");
        var  data = 'name=' + name + '&relationships=' + relationshipStr;
        $.ajax({
            url: "searchWords.html",
//            data: ({name : name}, {relationships : relationshipStr}),
            data: data,
            success: function(data){
                //clear previous graph
                $("#infovis").empty();
                init(eval(data));
        }
    });
    });


});


</script>