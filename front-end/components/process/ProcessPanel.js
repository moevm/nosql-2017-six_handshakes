import React from "react";
import GraphWeb from "./results/graph/GraphWeb";
import {ChartPanel} from "./results/chart/ChartPanel";
import {DetailsPanel} from "./results/details/DetailsPanel";
import "./ProcessPanel.css"
import {connect} from "react-redux";
import {resetSearchResults} from "../../actions/SearchActions";
class ProcessPanel extends React.Component {
    constructor() {
        super();
        this.state = {showLog: false, activeTab: 'GRAPH'}
    }

    renderContent() {
        const {activeTab} = this.state;
        const {result} = this.props;
        let content;
        switch (activeTab) {
            case 'GRAPH':
                content = <GraphWeb data={result}/>;
                break;
            case 'STAT':
                content = <ChartPanel data={result}/>;
                break;
            case 'DETAILS':
                content = <DetailsPanel data={result}/>;
                break;
        }

        return (
            <div className={`content ${result.graph ? 'active' : ''}`}>
                {content}
            </div>
        );
    }

    renderTabs() {
        const {activeTab} = this.state;
        const {result: {graph}, reset} = this.props;
        if (graph) {
            return (
                <div className="nav">
                    <div className="tabs">
                        {this.renderTab('GRAPH', activeTab === 'GRAPH', 'fa-link')}
                        {this.renderTab('STAT', activeTab === 'STAT', 'fa-pie-chart')}
                        {this.renderTab('DETAILS', activeTab === 'DETAILS', 'fa-ellipsis-h')}
                    </div>
                    <div className={`icon-button`}
                         onClick={reset}>
                        <i className="fa fa-repeat"/>
                        Try again
                    </div>
                </div>
            );
        } else {
            return <div/>;
        }
    }

    renderTab(tabName, isActive, faClass) {
        return (
            <div className={`${isActive ? 'active' : ''}`}
                 onClick={() => this.setState({activeTab: tabName})}>
                <i className={`fa ${faClass} fa-2x`}/>
            </div>
        );
    }

    render() {
        const {showLog} = this.state;
        const {socket: {searchState}, loading} = this.props;

        const list = searchState.map((item, index) => <p key={index}>{item}</p>);

        const stateIconClassName = (loading) ? `fa fa-cog fa-2x fa-spin fa-fw` : `fa fa-check fa-2x`;
        return (
            <div className="content-wrapper process-panel">
                {this.renderTabs()}
                {this.renderContent()}
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


export default connect(
   null,
    dispatch => ({
        reset: () => dispatch(resetSearchResults())
    })
)(ProcessPanel);