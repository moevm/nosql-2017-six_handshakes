import React from "react";

//TODO send request via fetch
export const Form = () => (
    <form name="mainForm" method="post" action="/find">
        <div>
            <input type="text" name="from" placeholder="from"/>
        </div>
        <div>
            <input type="text" name="to" placeholder="to"/>
        </div>
        <button type="submit">Check!</button>
    </form>
);