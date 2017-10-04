import React from "react";
import {fetchUser} from "../actions/UserActions";
import {connect} from 'react-redux';
import {StatusBar} from "./StatusBar";
import {ProcessPanel} from "./ProcessPanel";
import Form from "./Form";
import {Result} from "./Result";
import "./style.css"
import "../resources/css/imports.css"
import "./Core.css"
import {Header} from "./Header";
import {ChartPanel} from "./ChartPanel";
import GraphWeb from "./GraphWeb";

class App extends React.Component {
    constructor(){
        super();
        this.renderContent = this.renderContent.bind(this);
    }
    componentDidMount() {
        this.props.getUser()
    }

    render() {

        const {user, graph} = this.props;

        return (<div>
            <Header user={user}/>
            <div className="main-wrapper">
                {this.renderContent()}
                <Result result={graph}/>
            </div>
            {/*<GraphWeb*/}
            {/*data={this.props.graph.data}*/}
            {/*/>*/}
            </div>
        )
    }

    renderContent() {
        const {socket, loading} = this.props;

        if (socket.socketState === 'CONNECTED' && loading || socket.searchState.length !== 0) {
            return (<ProcessPanel socket={socket} loading={loading}/>)
        } else {
            return (<Form socket={socket}/>)
        }
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