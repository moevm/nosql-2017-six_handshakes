import React from "react";
export const Result = ({result}) => {

    const list = result.map(elem =>
        <div>
            <div className='image-round' style={{backgroundImage: `url(${elem.photoUrl})`}}/>
            <p>{elem.firstName} {elem.lastName} -> </p>
        </div>
    );

    return (
        <div>
            {list}
        </div>
    )

};