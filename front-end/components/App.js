import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import {StatusBar} from "./StatusBar";
import Form from "./Form";
import {Result} from "./Result";
import "./style.css"
import "../resources/css/imports.css"
import {Header} from "./Header";
import {ChartPanel} from "./ChartPanel";

class App extends React.Component {
    componentDidMount() {
        this.props.getUser()
    }

    render() {

        const {user, graph, socket} = this.props;

        return (<div>
            <Header user={user}/>
            <div className="main-wrapper">
                <Form socket={socket}/>
                <StatusBar socket={socket}/>
                <Result result={graph}/>
            </div>
        </div>)
    }
}

export default connect(
    state => {
        return {
            user: state.user,
            graph: state.graph,
            socket: state.socket
        }
    },
    dispatch => {
        return {
            getUser: () => dispatch(fetchUser())
        }
    }
)(App)