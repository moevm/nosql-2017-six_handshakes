import {UserInfo} from "./UserInfo";
import React from "react";
import {fetchUser} from "../actions/index";
import {connect} from 'react-redux';

class App extends React.Component {
    componentDidMount() {
        this.props.getUser()
    }

    render() {
        return (<div>
            <UserInfo user = {this.props.user}/>
        </div>)
    }
}

export default connect(
    state => {
        return {
            user: state.user
        }
    },
    dispatch => {
        return {
            getUser: () => dispatch(fetchUser())
        }
    }
)(App)