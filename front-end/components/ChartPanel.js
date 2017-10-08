import React from "react";
import {Legend, Pie, PieChart} from "recharts";
import {formatTime} from "../utils/mathUtils";
export class ChartPanel extends React.Component {
    render() {
        const RADIAN = Math.PI / 180;
        const renderCustomizedLabel = ({cx, cy, midAngle, innerRadius, outerRadius, percent, index}) => {
            const radius = outerRadius + 30;
            const x = cx + radius * Math.cos(-midAngle * RADIAN);
            const y = cy + radius * Math.sin(-midAngle * RADIAN);

            return (
                <text x={x} y={y} fill="#567592" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
                    {`${(percent * 100).toFixed(0)}%`}
                </text>
            );
        };


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
                <div className="stat-panel">
                    <div className="chart-panel">
                        <h1>Elapsed time: {total}s</h1>
                        <PieChart width={800} height={400}>
                            <Pie data={chartData} innerRadius={80} outerRadius={150} fill="#82ca9d" cx={200}
                                 label={renderCustomizedLabel}/>
                            <Legend iconSize={10} width={300} height={140} layout='vertical' verticalAlign='middle'
                                    wrapperStyle={style}/>
                        </PieChart>
                    </div>
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
