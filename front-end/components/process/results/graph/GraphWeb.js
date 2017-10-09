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
        return (
            <div>
                {graph && <Graph
                    options={options}
                    events={events}
                    graph={graph}
                    style={{height: '400px'}}
                />}
            </div>
        );
    }
}