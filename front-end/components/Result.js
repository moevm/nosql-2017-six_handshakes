import React from "react";
export const Result = ({result}) => {

    const list = result.map(elem =>
        <p>{elem.firstName} {elem.lastName} -> </p>
    );

    return (
        <div>
            {result}
        </div>
    )

};