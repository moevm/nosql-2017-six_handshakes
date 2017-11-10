const SET_SOCKET_STATE = 'MESSAGE/SET_SOCKET_STATE';
const ADD_MESSAGE = 'MESSAGE/ADD_MESSAGE';
const CLEAR_MESSAGES = 'MESSAGE/CLEAR_MESSAGES';

export const CONNECTED = 'MESSAGE/SOCKET_STATE/CONNECTED';
export const DISCONNECTED = 'MESSAGE/SOCKET_STATE/DISCONNECTED';
export const CONNECTING = 'MESSAGE/SOCKET_STATE/CONNECTING';


const initialState ={
    socketState: DISCONNECTED,
    messages: []
};

export default (state = initialState, action) => {
    switch (action.type) {
        case SET_SOCKET_STATE:
            return Object.assign({}, state, {socketState: action.payload});
        case ADD_MESSAGE:
            return Object.assign({}, state, {messages: [...state.messages, action.payload]});
        case CLEAR_MESSAGES:
            return Object.assign({}, state, {messages: []});
        default:
            return state;
    }
};

export const setSocketState = state => ({type: SET_SOCKET_STATE, payload: state});
export const addMessage = msg => ({type: ADD_MESSAGE, payload: msg});
export const clearMessages = () => ({type: CLEAR_MESSAGES});

export const getIsConnected = (state) => state.messageSocket.socketState === CONNECTED;