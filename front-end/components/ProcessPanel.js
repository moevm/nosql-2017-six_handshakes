import React from "react";
import GraphWeb from "./GraphWeb";
import {ChartPanel} from "./ChartPanel";
export class ProcessPanel extends React.Component {
    constructor() {
        super();
        this.state = {showLog: false, activeTab: 'GRAPH'}
    }

    render() {
        const {showLog, activeTab} = this.state;
        const {socket: {searchState}, loading, result, result: {graph}} = this.props;

        const list = searchState.map((item, index) => <p key={index}>{item}</p>);

        const stateIconClassName = (loading) ? `fa fa-cog fa-2x fa-spin fa-fw` : `fa fa-check fa-2x`;
        return (
            <div className="content-wrapper process-panel">
                {graph &&
                <div className="tabs">
                    <div className={`${activeTab === 'GRAPH' ? 'active' : ''}`}
                         onClick={() => this.setState({activeTab: 'GRAPH'})}><i
                        className={`fa fa-link fa-2x`}/></div>
                    <div className={`${activeTab === 'STAT' ? 'active' : ''}`}
                         onClick={() => this.setState({activeTab: 'STAT'})}><i
                        className={`fa fa-pie-chart fa-2x`}/></div>
                </div>
                }
                {activeTab === 'GRAPH'
                    ? <div className={`graph ${graph ? 'active' : ''}`}>
                        <GraphWeb data={graph}/>
                    </div>
                    : <div className={`graph ${graph ? 'active' : ''}`}>
                        <ChartPanel data={result}/>
                    </div>
                }
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