import React from "react";
export const UserInfo = ({user: {firstName, lastName}}) => (
    <p>Current user: {firstName} {lastName}</p>
);