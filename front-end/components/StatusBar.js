import React from "react";
export class StatusBar extends React.Component {
    render() {
        const {socketState, searchState} = this.props.socket;
        console.log(searchState);
        const list = searchState.map((item, index) => <li key={index}>{item}</li>);
        return (
            <div>
                <p>SOCKET STATE: {socketState}</p>
                <p>SEARCH STATE:</p>
                <ul>{list}</ul>
            </div>
        )
    }
}