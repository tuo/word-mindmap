

# Word-Mindmap

////////////////////////////////////////////////////////////////
   
  Word-Mindmap --------a better way to view relationship among words

////////////////////////////////////////////////////////////////

Word Mindmap is a project that aims at alleviating the pain of 
remembering words.The project trys to setup the relationship among
words and provides similar mindset map.

### Using for Local

		    mvn clean jetty:run

Then go to browser and input:

<http://localhost:8888/word-mindmap>

deploy to that:

            ./deploy.sh

### Using directly

Current the project is hosted in [Cloud Foundry](http://www.cloudfoundry.com/) :

<http://word-mindmap.cloudfoundry.com>

### Notes
This project uses [Neo4j](http://neo4j.org/) graph database for storage and spring data graph as a wrapper on it, in the front , using [JavaScript InfoVis Toolkit](http://thejit.org/) and css3 transition/animation. 
Check out the references:

[The Spring Data Graph Guide Book](http://static.springsource.org/spring-data/data-graph/docs/current/reference/html/)


### Prototype

![Prototype](https://lh4.googleusercontent.com/-rkKT0y3b_P8/Td4Qdv5b6DI/AAAAAAAAAeA/IRE_Nr_3WUA/s640/2011-05-26_16-31-58_281.jpg "Optional title")

![Prototype](http://farm3.static.flickr.com/2137/5733798656_32847e94a6.jpg "Optional title")

Want to contribute? Email to: clarkhtse@gmail.com




