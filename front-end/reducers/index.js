import {combineReducers} from 'redux';
import user from "./user";
import { reducer as formReducer } from 'redux-form'
import result from "./result";
import socket from "./socket";
import { loadingBarReducer } from 'react-redux-loading-bar'

const rootReducer = combineReducers({
    user,
    result,
    socket,
    form: formReducer,
    loadingBar: loadingBarReducer,
});

export default rootReducer;