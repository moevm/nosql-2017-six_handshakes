import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import {StatusBar} from "./StatusBar";
import Form from "./Form";
import {Result} from "./Result";
import "./style.css"
import "../resources/css/imports.css"
import "./Core.css"
import {Header} from "./Header";
import {ChartPanel} from "./ChartPanel";
import GraphWeb from "./GraphWeb";

class App extends React.Component {
    componentDidMount() {
        this.props.getUser()
    }


    render() {

        const {user, graph, socket, loading} = this.props;

        return (
            <div>
                <Header user={user}/>
                <div className="main-wrapper">
                    <Form socket={socket}/>
                    <StatusBar socket={socket} loading={loading}/>
                    <Result result={graph}/>
                </div>
                {/*<GraphWeb*/}
                    {/*data={this.props.graph.data}*/}
                {/*/>*/}
            </div>
        )
    }
}

export default connect(
    state => {
        return {
            user: state.user,
            graph: state.graph.data,
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