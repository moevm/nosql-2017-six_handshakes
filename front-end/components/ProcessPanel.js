import React from "react";
import GraphWeb from "./GraphWeb";
export class ProcessPanel extends React.Component {
    constructor() {
        super();
        this.state = {showLog: false}
    }

    render() {
        const {showLog} = this.state;
        const {socket: {searchState}, loading, graph} = this.props;
        const list = searchState.map((item, index) => <p key={index}>{item}</p>);

        const stateIconClassName = (loading) ? `fa fa-cog fa-2x fa-spin fa-fw` : `fa fa-check fa-2x`;
        return (
            <div className="content-wrapper process-panel">
                <div className={`graph ${graph ? 'active' : ''}`}>
                    <GraphWeb
                        data={this.props.graph}
                    />
                </div>
                <div className="loader">
                    <div>
                        <i className={stateIconClassName}/>{searchState[searchState.length - 1]}
                    </div>
                    <div className={`show-log-button`} onClick={() => this.setState({showLog: !showLog})}>
                        <i className={`fa fa-caret-down fa-2x ${showLog ? 'active' : ''}`}/>
                    </div>
                </div>
                <div className={`log ${showLog ? 'active' : ''}`}>
                    {list}
                </div>
            </div>
        )
    }
}