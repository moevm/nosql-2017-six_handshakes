import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import ProcessPanel from "./process/ProcessPanel";
import Form from "./form/Form";
import "../resources/css/imports.css"
import "./Core.css"
import {Header} from "./header/Header";

class App extends React.Component {
    constructor() {
        super();
        this.renderContent = this.renderContent.bind(this);
    }

    componentDidMount() {
        this.props.getUser()
    }

    render() {
        const {user} = this.props;
        return (
            <div>
                <Header user={user}/>
                <div className="background"/>
                <div className="main-wrapper">
                    {this.renderContent()}
                </div>
            </div>
        )
    }

    renderContent() {
        const {socket, loading, connected, result} = this.props;
        if (connected && loading || result.graph) {
            return (<ProcessPanel socket={socket} loading={loading} result={result}/>)
        } else {
            return (<Form socket={socket}/>)
        }
    }
}

export default connect(
    state => {
        return {
            user: state.user,
            result: state.result,
            socket: state.socket,
            loading: state.loadingBar !== 0,
            connected: state.socket.socketState === 'CONNECTED',
        }
    },
    dispatch => {
        return {
            getUser: () => dispatch(fetchUser())
        }
    }
)(App)