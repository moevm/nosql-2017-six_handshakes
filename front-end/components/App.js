import {UserInfo} from "./UserInfo";
import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import {StatusBar} from "./StatusBar";
import Form from "./Form";
import {Result} from "./Result";
import "./style.css"

class App extends React.Component {
    componentDidMount() {
        this.props.getUser()
    }

    render() {

        const {user, graph, socket} = this.props;

        return (<div>
            <UserInfo user = {user}/>
            <Form socket={socket}/>
            <StatusBar socket={socket}/>
            <Result result = {graph}/>
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