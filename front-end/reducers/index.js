import {combineReducers} from 'redux';
import user from "./user";
import { reducer as formReducer } from 'redux-form'
import graph from "./graph";
import socket from "./socket";

const rootReducer = combineReducers({
    user,
    graph,
    socket,
    form: formReducer
});

export default rootReducer;