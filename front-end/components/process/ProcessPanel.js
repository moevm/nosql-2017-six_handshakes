import React from "react";
import GraphWeb from "./results/graph/GraphWeb";
import {ChartPanel} from "./results/chart/ChartPanel";
import {DetailsPanel} from "./results/details/DetailsPanel";
import "./ProcessPanel.css"
import {connect} from "react-redux";
import {resetApp} from "../../ducks/app";
import {getHasResult} from "../../ducks/result";

const GRAPH = 'PROCESS_PANEL/GRAPH_TAB';
const STAT = 'PROCESS_PANEL/STAT_TAB';
const DETAILS = 'PROCESS_PANEL/DETAILS_TAB';

class ProcessPanel extends React.Component {
    constructor() {
        super();
        this.state = {showLog: false, activeTab: GRAPH}
    }

    renderContent() {
        const {activeTab} = this.state;
        const {result} = this.props;
        let content;
        switch (activeTab) {
            case GRAPH:
                //TODO show message if dataset empty
                content = <GraphWeb data={result}/>;
                break;
            case STAT:
                content = <ChartPanel data={result}/>;
                break;
            case DETAILS:
                content = <DetailsPanel data={result}/>;
                break;
        }
        return (<div className='content'>{content}</div>);
    }

    renderTabs() {
        const {resetApp} = this.props;
        return (
            <div className="nav">
                <div className="tabs">
                    {this.renderTab(GRAPH, 'fa-link')}
                    {this.renderTab(STAT, 'fa-pie-chart')}
                    {this.renderTab(DETAILS, 'fa-ellipsis-h')}
                </div>
                <div className="buttons">
                    <div className={`icon-button`} onClick={resetApp}>
                        <i className="fa fa-repeat"/> Try again
                    </div>
                </div>
            </div>
        );
    }

    renderTab(tabName, faClass) {
        return (
            <div className={`${this.state.activeTab === tabName ? 'active' : ''}`}
                 onClick={() => this.setState({activeTab: tabName})}>
                <i className={`fa ${faClass} fa-2x`}/>
            </div>
        );
    }

    render() {
        const {messageSocket: {messages}, loading} = this.props;

        const {showLog} = this.state;
        const list = messages.map((item, index) => <p key={index}>{item}</p>);
        const stateIconClassName = (loading) ? `fa fa-cog fa-2x fa-spin fa-fw` : `fa fa-check fa-2x`;

        return (
            <div className="content-wrapper process-panel">
                {!loading && this.renderTabs()}
                {!loading && this.renderContent()}
                <div className="loader">
                    <div>
                        <i className={stateIconClassName}/>{messages[messages.length - 1]}
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


export default connect(
    state => ({
        result: state.result,
        messageSocket: state.messageSocket,
        loading: state.loadingBar !== 0,
    }),
    dispatch => ({
        resetApp: () => dispatch(resetApp())
    })
)(ProcessPanel);