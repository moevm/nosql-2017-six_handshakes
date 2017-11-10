import {combineReducers} from 'redux';
import messageSocket from './messageSocket';
import user from './user';
import result from "./result";
import {reducer as form} from 'redux-form'
import {loadingBarReducer as loadingBar} from 'react-redux-loading-bar'

export default combineReducers({
    messageSocket,
    user,
    result,
    form,
    loadingBar,
});