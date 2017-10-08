import Graph from "react-graph-vis";
import React from "react";

let options = {
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
        scaling:{
            min:5,
            max: 25
        }
    },
        nodes: {
            shape: 'circularImage',
            brokenImage: 'http://memepedia.ru/wp-content/uploads/2016/03/ffc073891b259c.jpg',
            image: '',
        }


    };

let events = {
    select: function (event) {
        let {nodes, edges} = event;
        console.log("Selected nodes:");
        console.log(nodes);
    }
};

export default class GraphWeb extends React.Component {

    render() {
        console.log(this.props.data);
        return (
            <div>
                {this.props.data && <Graph
                    options={options}
                    events={events}
                    graph={this.props.data}
                    style={{height: '400px'}}
                />}
            </div>
        );
    }
}