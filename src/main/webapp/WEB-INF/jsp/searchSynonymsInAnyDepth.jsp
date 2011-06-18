<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script language="javascript" type="text/javascript" src="javascripts/search-words-jit-customization.js"></script>

<h2>Search</h2>

<form action="searchSynonymsInAnyDepth.html" id="searchForm" class="round">
   <fieldset>
        <%--<legend>Search</legend>--%>
        <label>Word Name </label>
         <input type="search" name="name" size="20" value="earth"/>
        <br/>


          <input type="checkbox" name="relation" value="synonym_with" checked>Synonym(同义词)</input>
          <input type="checkbox" name="relation" value="antonym_with" checked>Antonym(反义词)</input>
          <input type="checkbox" name="relation" value="extension_with" checked>Extension(扩展词)</input>
          <input type="checkbox" name="relation" value="idiom_with" checked>Idom(短语)</input>
           <br />
        <input type="submit" name="button" value="submit"/>
   </fieldset>
</form>

<hr/>



<cite>Those <i>grey</i> lines and triangle means that those nodes are not in the search result.</cite>
<div id="container">
    <div id="center-container" style="width:100%;height:700px;">
        <div id="infovis"></div>
    </div>

</div>
<div id="log"></div>
<div id="color-to-relationship" style="border: 1px dotted black;"></div>

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
//            url: "searchWords.html",

//            data: ({name : name}, {relationships : relationshipStr}),
            url: "searchWords.html",
            data: data,
            success: function(data){
                //clear previous graph
                $("#infovis").empty();
                $("#color-to-relationship").empty();

                var result =  eval("("  + data + ")");
                var data = result["data"];
                var colorToRelMap =  result["colorToRelMap"];

                var html = ("<h4>Following are the colors corresponding to graph, which stands for what meaning it relates to.</h4><ul>");
                for( i=0; i < colorToRelMap.length; i++){
                    var row = colorToRelMap[i];
                    html += ("<li style=\"color: " + row["color"] + ";\">" + row["meaning"] + "</li>");
                }

                $("#color-to-relationship").append(html + "</ul>");
//                colorToRelMap.each(function(index, row){
//                     var shit = row;
//                   $("#color-to-relationship").append("<p style=\"color: " + row["color"] + ";\">" + row["meaning"] + "</p>");
//                 });


//                $("#color-to-relationship")

                init(data);
        }
    });
    });

    $('#searchForm').submit();

});


</script>

