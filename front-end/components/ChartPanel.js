import React from "react";
import {Legend, Pie, PieChart} from "recharts";
export class ChartPanel extends React.Component {
    render() {
        if (this.props.data) {
            const {peopleChecked, currentWeb, timeStat: {dbTime, vkTime, pathTime, csvTime}} = this.props.data;
            const chartData = [
                {name: 'Downloading data from VK', value: vkTime, fill: '#507299'},
                {name: 'Saving data to DB', value: dbTime, fill: '#b1c0d8'},
                {name: 'Working with CSV', value: csvTime, fill: '#d8ac45'},
                {name: 'Finding path', value: pathTime, fill: '#d80950'}];
            const style = {
                top: 0,
                left: 0,
                lineHeight: '24px'
            };
            return (
                <div className="stat-panel">
                    <PieChart width={250} height={400}>
                        <Pie data={chartData} innerRadius={40} outerRadius={80} fill="#82ca9d" label/>
                        <Legend iconSize={10} width={120} height={140} layout='vertical' verticalAlign='middle' wrapperStyle={style}/>
                    </PieChart>
                    <div className="numbers">
                        <div><i className="fa fa-male"/> Checked persons {peopleChecked}</div>
                        <div><i className="fa fa-male"/> Total in database {currentWeb}</div>
                    </div>
                </div>
            )
        } else {
            return <div/>;
        }

    }
}



