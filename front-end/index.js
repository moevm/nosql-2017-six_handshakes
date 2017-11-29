import React from 'react';
import {render} from 'react-dom';
import rootReducer from './ducks/reducer';
import {createStore, applyMiddleware} from 'redux';
import {Provider} from 'react-redux';
import thunk from 'redux-thunk';
import App from "./components/App";
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {loadingBarMiddleware} from 'react-redux-loading-bar'
import {setSocketState, addMessage, CONNECTING, CONNECTED} from "./ducks/messageSocket";

let store = createStore(
    rootReducer,
    applyMiddleware(thunk, loadingBarMiddleware({
        promiseTypeSuffixes: ['REQUEST', 'SUCCESS', 'FAILURE'],
    }))
);

let stompClient = Stomp.over(new SockJS("/six-handshakes-websocket"));
store.dispatch(setSocketState(CONNECTING));
stompClient.connect({}, frame => {
    stompClient.subscribe('/topic/status', json => store.dispatch(addMessage(json.body)));
    store.dispatch(setSocketState(CONNECTED));
});

store.subscribe(() => console.log('STORE', store.getState()));

render(
    <Provider store={store}>
        <App/>
    </Provider>,
    document.getElementById('root')
);
