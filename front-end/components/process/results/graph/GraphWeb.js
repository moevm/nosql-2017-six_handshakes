import Graph from "react-graph-vis";
import React from "react";

const options = {
    layout: {
        hierarchical: {
            enabled: false
        }
    },
    edges: {
        color: "#000000",
        physics: false,
        arrows: {
            to: {enabled: false},
            from: {enabled: false}
        },
        scaling: {
            min: 5,
            max: 25
        }
    },
    nodes: {
        color: {
            border: '#6cd0ff'
        },
        shape: 'circularImage',
        brokenImage: 'http://memepedia.ru/wp-content/uploads/2016/03/ffc073891b259c.jpg',
        image: '',
    }
};

const events = {
    doubleClick: function (event) {
        let {nodes, edges} = event;
        console.log(nodes);
        window.open(`https://vk.com/id${nodes[0]}`, '_blank');
    },
};

export default class GraphWeb extends React.Component {
    render() {

        const {graph} = this.props.data;
        console.log('graph wothiut formating', graph);
        let  formattedGraph;
        if(graph){
            const formattedNodes = Array.from(graph.nodes, node => {
                let x,y,borderWidth;
                switch (node.nodeType) {
                    case 'START_NODE':
                        x = -350;
                        y = 0;
                        borderWidth = 10;
                        break;
                    case 'REGULAR_NODE':
                        x = null;
                        y = null;
                        borderWidth = 0;
                        break;
                    case 'END_NODE':
                        x = 350;
                        y = 0;
                        borderWidth = 10;
                        break;
                }
                return {
                    id: node.id,
                    label: node.label,
                    image: node.image,
                    x,
                    y,
                    borderWidth
                }
            });

            formattedGraph = Object.assign({}, graph, {nodes: formattedNodes});
            console.log('formatted graph', formattedGraph);
            console.log('json fro', JSON.stringify(formattedGraph))
        }


        return (
            <div>
                {graph && <Graph
                    options={options}
                    events={events}
                    graph={formattedGraph}
                    style={{height: '400px'}}
                />}
            </div>
        );
    }
}