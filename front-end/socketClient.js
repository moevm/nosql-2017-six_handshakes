import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

let stompClient = null;

export function socketConnect(url, listener) {
    stompClient = Stomp.over(new SockJS("/six-handshakes-websocket"));
    stompClient.connect({}, frame => {
        stompClient.subscribe(url, listener);
        console.log('Successfully connected to socket!');
    });
}

export function socketSubscribe(url, listener){
    // stompClient.subscribe(url, listener);
}

export function socketDisconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

