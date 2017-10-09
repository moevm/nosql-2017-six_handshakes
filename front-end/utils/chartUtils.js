import * as React from "react";

const RADIAN = Math.PI / 180;

export const formatTime = value => (value / 100).toFixed(2);
export const renderCustomizedLabel = ({cx, cy, midAngle, innerRadius, outerRadius, percent, index}) => {
    const radius = outerRadius + 30;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);
    return (
        <text x={x} y={y} fill="#567592" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
            {`${(percent * 100).toFixed(0)}%`}
        </text>
    );
};