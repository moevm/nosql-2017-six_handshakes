import React from "react";
import {socketConnect, socketDisconnect} from "../socketClient";
export class StatusBar extends React.Component{

    constructor (){
        super();
        this.state = {status: 'NONE'};
        this.handleStatusChange = this.handleStatusChange.bind(this);
    }

    handleStatusChange(value){
        this.setState({status: value});
    }

    //TODO refactor: move socket logic in reducer ?
    //TODO add loading while there is no connect
    //TODO add STATUS reset after receiving response
    componentDidMount(){
        socketConnect('/topic/status', json => {
            this.handleStatusChange(JSON.parse(json.body));
        });
    }

    componentWillUnmount(){
        socketDisconnect();
    }

    render(){
        return (
            <div>
                <p>STATUS: {this.state.status}</p>
            </div>
        )
    }
}