//http://thejit.org/static/v20/Jit/Examples/ForceDirected/example2.html
var labelType, useGradients, nativeTextSupport, animate;

(function() {
    var ua = navigator.userAgent,
            iStuff = ua.match(/iPhone/i) || ua.match(/iPad/i),
            typeOfCanvas = typeof HTMLCanvasElement,
            nativeCanvasSupport = (typeOfCanvas == 'object' || typeOfCanvas == 'function'),
            textSupport = nativeCanvasSupport
                    && (typeof document.createElement('canvas').getContext('2d').fillText == 'function');
    //I'm setting this based on the fact that ExCanvas provides text support for IE
    //and that as of today iPhone/iPad current text support is lame
    labelType = (!nativeCanvasSupport || (textSupport && !iStuff)) ? 'Native' : 'HTML';
    nativeTextSupport = labelType == 'Native';
    useGradients = nativeCanvasSupport;
    animate = !(iStuff || !nativeCanvasSupport);
})();

var Log = {
    elem: false,
    write: function(text) {
        if (!this.elem)
            this.elem = document.getElementById('log');
        this.elem.innerHTML = text;
        this.elem.style.left = (500 - this.elem.offsetWidth / 2) + 'px';
    }
};


function init(json) {
    // init data
//    var json = [
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "sky",
//                    "nodeFrom": "earth",
//                    "data": {
//                        "$color":"#dd99dd",
//                        "relatedOn": "from sky to earth"
//                    }
//                },
//                {
//                    "nodeTo": "ocean",
//                    "nodeFrom": "earth",
//                    "data": {
//                        "relatedOn": "from ocean to earth"
//                    }
//                },
//                {
//                    "nodeTo": "soil",
//                    "nodeFrom": "earth",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "ground",
//                    "nodeFrom": "earth",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "land",
//                    "nodeFrom": "earth",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "globe",
//                    "nodeFrom": "earth",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "mud",
//                    "nodeFrom": "earth",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "world",
//                    "nodeFrom": "earth",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "red",
//                "$type": "star"
//            },
//            "id": "earth",
//            "name": "earth"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "ocean",
//                    "nodeFrom": "sky",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "earth",
//                    "nodeFrom": "sky",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "sky",
//            "name": "sky"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "sky",
//                    "nodeFrom": "ocean",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "earth",
//                    "nodeFrom": "ocean",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "blue",
//                    "nodeFrom": "ocean",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "ocean",
//            "name": "ocean"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "ocean",
//                    "nodeFrom": "blue",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "depressed",
//                    "nodeFrom": "blue",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "happy",
//                    "nodeFrom": "blue",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "yellow",
//                    "nodeFrom": "blue",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "blue"
//            },
//            "id": "blue",
//            "name": "blue"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "blue",
//                    "nodeFrom": "depressed",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "happy",
//                    "nodeFrom": "depressed",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "depressed",
//            "name": "depressed"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "blue",
//                    "nodeFrom": "happy",
//                    "data": {}
//                },
//                {
//                    "nodeTo": "depressed",
//                    "nodeFrom": "happy",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "happy",
//            "name": "happy"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "blue",
//                    "nodeFrom": "yellow",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "yellow"
//            },
//            "id": "yellow",
//            "name": "yellow"
//        }
//        ,
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "earth",
//                    "nodeFrom": "mud",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "mud",
//            "name": "mud"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "word",
//                    "nodeFrom": "world",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "world",
//            "name": "world"
//        },
//        {
//            "adjacencies": [
//                {
//                    "nodeTo": "in a word",
//                    "nodeFrom": "word",
//                    "data": {}
//                }
//            ],
//            "data": {
//                "$color": "#83548B"
//            },
//            "id": "word",
//            "name": "word"
//        }
//    ];
    // end

    $jit.ForceDirected.Plot.EdgeTypes.implement({
        'label-arrow-line': {

          'render': function(adj, canvas) {
            //plot arrow edge
//            this.edgeTypes.arrow.call(this, adj, canvas);
            //get nodes cartesian coordinates
            var pos = adj.nodeFrom.pos.getc(true);
            var posChild = adj.nodeTo.pos.getc(true);
            //check for edge label in data
              this.edgeHelper.line.render({ x: pos.x, y: pos.y }, { x: posChild.x, y: posChild.y }, canvas);
            var data = adj.data;
            if (data.relatedOn) {
                //now adjust the label placement
                var radius = this.viz.canvas.getSize();
//                var x = parseInt((pos.x + posChild.x +
//                        radius.width) / 2);
//                var y = parseInt((pos.y + posChild.y +
//                        radius.height) / 2);

var x = parseInt((pos.x + posChild.x - (data.relatedOn.length * 5)) /
2);
var y = parseInt((pos.y + posChild.y ) /2);

                canvas.getCtx().textBaseline = "top";
                  canvas.getCtx().font = "8pt Arial";


canvas.getCtx().fillStyle = "orange";


                canvas.getCtx().fillText(data.relatedOn, x, y);



            }
          }
        }
    });

    // init ForceDirected
    var fd = new $jit.ForceDirected({
        //id of the visualization container
        injectInto: 'infovis',
        //Enable zooming and panning
        //with scrolling and DnD
        Navigation: {
            enable: true,
            type: 'Native',
            //Enable panning events only if we're dragging the empty
            //canvas (and not a node).
            panning: 'avoid nodes',
            zooming: 10 //zoom speed. higher is more sensible
        },
        Label:{
//            type: 'Native',
//            size: 11,
//            color: '#ccc' ,
//            textAlign: 'center'
        },
        // Change node and edge styles such as
        // color and width.
        // These properties are also set per node
        // with dollar prefixed data-properties in the
        // JSON structure.
        Node: {
            overridable: true,
            dim: 10
        },
        Edge: {
            overridable: true,
            color: '#23A4FF',
            type: 'label-arrow-line'
//            type: 'line'
        },
        // Add node events
        Events: {
            enable: true,
            type: 'Native',
            //Change cursor style when hovering a node
            onMouseEnter: function() {
                fd.canvas.getElement().style.cursor = 'move';
            },
            onMouseLeave: function() {
                fd.canvas.getElement().style.cursor = '';
            },
            //Update node positions when dragged
            onDragMove: function(node, eventInfo, e) {
                var pos = eventInfo.getPos();
                node.pos.setc(pos.x, pos.y);
                fd.plot();
            },
            //Implement the same handler for touchscreens
            onTouchMove: function(node, eventInfo, e) {
                $jit.util.event.stop(e); //stop default touchmove event
                this.onDragMove(node, eventInfo, e);
            }
        },
        //Number of iterations for the FD algorithm
        iterations: 200,
        //Edge length
        levelDistance: 130,
        // This method is only triggered
        // on label creation and only for DOM labels (not native canvas ones).
        onCreateLabel: function(domElement, node) {
            // Create a 'name' and 'close' buttons and add them
            // to the main node label
            var nameContainer = document.createElement('span'),
                    closeButton = document.createElement('span'),
                    style = nameContainer.style;
            nameContainer.className = 'name';
            nameContainer.innerHTML = node.name;
            domElement.appendChild(nameContainer);
            domElement.appendChild(closeButton);
            style.fontSize = "1.2em";
            style.color = "#ddd";
            //Fade the node and its connections when
            //clicking the close button
            closeButton.onclick = function() {
                node.setData('alpha', 0, 'end');
                node.eachAdjacency(function(adj) {
                    adj.setData('alpha', 0, 'end');
                });
                fd.fx.animate({
                    modes: ['node-property:alpha',
                        'edge-property:alpha'],
                    duration: 500
                });
            };

            //when first time user coming to graph, highlight the earth as
//            if (node.name == "earth") {
//                var isAnyOneSelected = false;
//                fd.graph.eachNode(function(n) {
//                    if (n.selected) {
//                        isAnyOneSelected = true;
//                    }
//                });
//                if (!isAnyOneSelected) {
//                    node.selected = true;
//                    node.data.$dim = 17;
//                    node.eachAdjacency(function(adj) {
//                        adj.data.$lineWidth = 3;
//                        adj.data.$color = '#FF69B4';
//                    });
//                }
//
//            }
//            ;
            //Toggle a node selection when clicking
            //its name. This is done by animating some
            //node styles like its dimension and the color
            //and lineWidth of its adjacencies.
            nameContainer.onclick = function() {
                //set final styles
                fd.graph.eachNode(function(n) {
                    if (n.id != node.id) delete n.selected;
                    n.setData('dim', 9, 'end');
                    n.eachAdjacency(function(adj) {
                        adj.setDataset('end', {
                            lineWidth: 0.4,
                            color: '#23a4ff'
                        });
                    });
                });
                if (!node.selected) {
                    node.selected = true;
                    node.setData('dim', 17, 'end');
                    node.eachAdjacency(function(adj) {
                        adj.setDataset('end', {
                            lineWidth: 3,
                            //36acfb
                            color: '#FF69B4'
                        });
                    });
                } else {
                    delete node.selected;
                }
                //trigger animation to final styles
                fd.fx.animate({
                    modes: ['node-property:dim',
                        'edge-property:lineWidth:color'],
                    duration: 500
                });
                // Build the right column relations list.
                // This is done by traversing the clicked node connections.
                var html = "<h4>" + node.name + "</h4><b> connections:</b><ul><li>",
                        list = [];
                node.eachAdjacency(function(adj) {
                    if (adj.getData('alpha')) list.push(adj.nodeTo.name);
                });
                //append connections information
                $jit.id('inner-details').innerHTML = html + list.join("</li><li>") + "</li></ul>";
            };
        },
        // Change node styles when DOM labels are placed
        // or moved.
        onPlaceLabel: function(domElement, node) {
            var style = domElement.style;
            var left = parseInt(style.left);
            var top = parseInt(style.top);
            var w = domElement.offsetWidth;
            style.left = (left - w / 2) + 'px';
            style.top = (top + 10) + 'px';
            style.display = '';
        }
    });
    // load JSON data.
    fd.loadJSON(json);
    // compute positions incrementally and animate.
    fd.computeIncremental({
        iter: 40,
        property: 'end',
        onStep: function(perc) {
            Log.write(perc + '% loaded...');
        },
        onComplete: function() {
            Log.write('done');
            fd.animate({
                modes: ['linear'],
                transition: $jit.Trans.Elastic.easeOut,
                duration: 2500
            });
        }
    });
    // end
}
