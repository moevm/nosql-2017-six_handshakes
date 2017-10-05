import React from "react";
import "./Header.css"
export class Header extends React.Component {
    render() {
        const {user: {firstName, lastName, photoUrl}} = this.props;

        return (
            <div className="header">
                <div>
                    <h2 className="title">six handshakes</h2>
                </div>
                <div className="current-user-panel">
                    <h4>
                        {firstName} {lastName}
                    </h4>
                    <div className='image-round' style={{backgroundImage: `url(${photoUrl})`}}/>
                </div>
            </div>
        );
    }
}
