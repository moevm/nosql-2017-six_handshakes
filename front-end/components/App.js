import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import {ProcessPanel} from "./ProcessPanel";
import Form from "./Form";
import "./style.css"
import "../resources/css/imports.css"
import "./Core.css"
import {Header} from "./Header";

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
        const {socket, loading, result} = this.props;

        if (socket.socketState === 'CONNECTED' && loading || socket.searchState.length !== 0) {
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
            loading: state.loadingBar !== 0
        }
    },
    dispatch => {
        return {
            getUser: () => dispatch(fetchUser())
        }
    }
)(App)