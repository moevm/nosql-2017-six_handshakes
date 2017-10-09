import React from "react";
import {Legend, Pie, PieChart} from "recharts";
import {formatTime, renderCustomizedLabel} from "../../../../utils/chartUtils";
import "./ChartPanel.css"

export class ChartPanel extends React.Component {
    render() {
        if (this.props.data) {
            const {peopleChecked, currentWeb, timeStat: {dbTime, vkTime, pathTime, csvTime}} = this.props.data;
            const total = formatTime(dbTime + vkTime + pathTime + csvTime);

            const chartData = [
                {name: `Downloading data from VK - ${formatTime(vkTime)}s`, value: vkTime, fill: '#507299'},
                {name: `Saving data to DB - ${formatTime(dbTime)}s`, value: dbTime, fill: '#b1c0d8'},
                {name: `Working with CSV - ${formatTime(csvTime)}s`, value: csvTime, fill: '#d8ac45'},
                {name: `Finding path - ${formatTime(pathTime)}s`, value: pathTime, fill: '#d80950'}];

            const style = {
                right: 0,
                lineHeight: '24px'
            };

            return (
                <div className="chart-panel">
                    <h1>Elapsed time: {total}s</h1>
                    <PieChart width={800} height={400}>
                        <Pie data={chartData} innerRadius={80} outerRadius={150} fill="#82ca9d" cx={200}
                             label={renderCustomizedLabel}/>
                        <Legend iconSize={10} width={300} height={140} layout='vertical' verticalAlign='middle'
                                wrapperStyle={style}/>
                    </PieChart>
                </div>
            )
        } else {
            return <div/>;
        }
    }
}
