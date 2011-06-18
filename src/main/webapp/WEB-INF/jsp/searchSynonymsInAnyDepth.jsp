<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script language="javascript" type="text/javascript" src="javascripts/search-words-jit-customization.js"></script>

<h2>Search / Create</h2>

<div id="form-container">
<form action="searchSynonymsInAnyDepth.html" id="search" class="round">
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

<form action="createWord.html" id="create-word" class="round">
   <fieldset>
        <%--<legend>Search</legend>--%>
        <label>Word Name </label>
         <input type="input" name="name" size="6" value=""/>
            <label>Chinese Meaning </label>
         <input type="input" name="chineseMeaning" size="20" value=""/>

        <br/>

            <label>Word Type </label>
             <input type="input" name="type" size="7" value=""/>


            <label>English Meaning </label>
         <input type="input" name="englishMeaning" size="20" value=""/>
        <br/>
        <input type="submit" name="button" value="submit"/>
   </fieldset>
</form>

<form action="createRelationship.html" id="create-relationship" class="round">
   <fieldset>
        <%--<legend>Search</legend>--%>
        <label>Word Name</label>
         <input type="input" name="wordName" size="20" value=""/>
        <br/>
        <label>Another Word Name </label>
         <input type="input" name="anotherWordName" size="20" value=""/>
        <br/>
        <label>onChinese</label>
         <input type="input" name="onChinese" size="20" value=""/>
        <br/>
        <label>onEnglish </label>
         <input type="input" name="onEnglish" size="20" value=""/>
        <br/>
        <input type="radio" name="whichType" value="synonym_with" checked> synonym
        <input type="radio" name="whichType" value="antonym_with"> antonym<br>
        <input type="radio" name="whichType" value="idiom_with"> idiom
            <input type="radio" name="whichType" value="extension_with"> extension <br>
        <input type="submit" name="button" value="submit"/>
   </fieldset>
</form>
</div>

<hr class="clear"/>



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

    $('#search').submit(function(event){
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



    $('#search').submit();

});


</script>

