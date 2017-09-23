import React from 'react';
import {render} from 'react-dom';
import rootReducer from './reducers/index';
import {createStore, applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import App from "./components/App";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {setSearchState, setSocketState} from "./actions/SocketActions";

let store = createStore(
    rootReducer,
    applyMiddleware(thunk)
);

// let stompClient = Stomp.over(new SockJS("/six-handshakes-websocket"));
// store.dispatch(setSocketState('CONNECTING...'));
// stompClient.connect({}, frame => {
//     stompClient.subscribe('/topic/status', json => store.dispatch(setSearchState(json.body)));
//     store.dispatch(setSocketState('CONNECTED'));
// });

render(
    <Provider store={store}>
        <App/>
    </Provider>,
    document.getElementById('root')
);
