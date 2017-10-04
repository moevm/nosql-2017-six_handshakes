import {combineReducers} from 'redux';
import user from "./user";
import { reducer as formReducer } from 'redux-form'
import graph from "./graph";
import socket from "./socket";
import { loadingBarReducer } from 'react-redux-loading-bar'

const rootReducer = combineReducers({
    user,
    graph,
    socket,
    form: formReducer,
    loadingBar: loadingBarReducer,
});

export default rootReducer;