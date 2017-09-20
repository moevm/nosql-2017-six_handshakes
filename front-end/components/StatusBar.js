import React from "react";
export class StatusBar extends React.Component{
    render(){
        const {socketState, searchState} = this.props.socket;
        return (
            <div>
                <p>SOCKET STATE: {socketState}</p>
                <p>SEARCH STATE: {searchState}</p>
            </div>
        )
    }
}