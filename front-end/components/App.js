import React from "react";
import {connect} from 'react-redux';
import ProcessPanel from "./process/ProcessPanel";
import Form from "./form/Form";
import "../resources/css/imports.css"
import "./Core.css"
import {Header} from "./header/Header";
import {fetchUser} from "../ducks/user";
import {getIsConnected} from "../ducks/messageSocket";
import {getHasResult} from "../ducks/result";

class App extends React.Component {
    constructor() {
        super();
    }

    componentDidMount() {
        this.props.getUser()
    }

    render() {
        const {user, showProcessPanel} = this.props;
        return (
            <div>
                <Header user={user}/>
                <div className="background"/>
                <div className="main-wrapper">
                    {(showProcessPanel) ? <ProcessPanel/> : <Form/>}
                </div>
            </div>
        )
    }
}

export default connect(
    state => ({
        user: state.user,
        showProcessPanel: getIsConnected(state)  && (state.loadingBar !== 0) || getHasResult(state),
    }),
    dispatch => ({
        getUser: () => dispatch(fetchUser())
    })
)(App)