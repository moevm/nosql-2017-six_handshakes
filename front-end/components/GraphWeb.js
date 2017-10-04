import Graph from "react-graph-vis";
import React from "react";

let data = {
    nodes: [
        {id: 1, label: "User Name", circularImage: "sdfsf"},
        {id: 2, label: "USERR NNN"},
        {id: 3, label: "Node 3"},
        {id: 4, label: "Node 4"},
        {id: 5, label: "Node 5"}
    ],
    edges: [{from: 1, to: 2 }, {from: 1, to: 3}, {from: 2, to: 4}, {from: 2, to: 5}]
};


let options = {
    layout: {
        hierarchical: false
    },
    edges: {
        color: "#000000"
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
                <Graph
                   // graph={data}
                    //options={options}
                    graph={this.props.data}
                    events={events}
                    style={{height: "640px"}}
                />
            </div>
        );
    }
}