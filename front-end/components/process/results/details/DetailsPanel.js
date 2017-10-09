import React from "react";
import "./DetailsPanel.css"

export class DetailsPanel extends React.Component {
    render() {
        if (this.props.data) {
            const {peopleChecked, currentWeb} = this.props.data;
            return (
                <div className="details-panel">
                    <h1>Details</h1>
                    <div className="numbers">
                        <div><i className="fa fa-2x fa-male icon"/> Checked persons: {peopleChecked}</div>
                        <div><i className="fa fa-2x fa-male icon"/> Total in database: {currentWeb}</div>
                    </div>
                </div>
            )
        } else {
            return <div/>;
        }

    }
}

