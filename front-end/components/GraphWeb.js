import Graph from "react-graph-vis";
import React from "react";

let options = {
    layout: {
        hierarchical: false
    },
    edges: {
        color: "#000000"
    },
    nodes: {
        shape: 'circularImage',
        brokenImage: 'http://memepedia.ru/wp-content/uploads/2016/03/ffc073891b259c.jpg'
    }
};

let events = {
    select: function (event) {
        let {nodes, edges} = event;
        console.log("Selected nodes:");
        console.log(nodes);
        console.log("Selected edges:");
        console.log(edges);
    }
};

export default class GraphWeb extends React.Component {

    render() {
        return (
            <div>
                {this.props.data && <Graph
                    options={options}
                    graph={this.props.data}
                    style={{height: '400px'}}
                />}
            </div>
        );
    }
}