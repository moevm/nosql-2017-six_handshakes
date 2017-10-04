import React from "react";
export class ProcessPanel extends React.Component {
    constructor() {
        super();
        this.state = {showLog: false}
    }

    render() {
        const {showLog} = this.state;
        const {socket: {searchState}, loading} = this.props;
        const list = searchState.map((item, index) => <p key={index}>{item}</p>);
        return (
            <div className="content-wrapper process-panel">
                <div className="loader">
                    <div>
                        <i className={`fa fa-cog fa-2x ${loading ? 'fa-spin  fa-fw' : ''}`}/>{searchState[searchState.length - 1]}
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

{/*<p>SEARCH STATE:</p>*/
}
// <ul>{list}</ul>