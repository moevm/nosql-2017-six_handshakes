import React from "react";
export class StatusBar extends React.Component {
    render() {
        const {socket: {socketState, searchState}, loading} = this.props;
        const list = searchState.map((item, index) => <li key={index}>{item}</li>);
        return (
            <div>
                <div>{loading && <i className="fa fa-cog fa-spin fa-fw"/>}</div>
                <p>SOCKET STATE: {socketState}</p>
                <p>SEARCH STATE:</p>
                <ul>{list}</ul>
            </div>
        )
    }
}