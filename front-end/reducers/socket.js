const initialState ={
    socketState: 'DISCONNECTED',
    searchState: 'NONE'
};

const socket = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_SOCKET_STATE':
            return Object.assign({}, state, {socketState: action.payload});
        case 'SET_SEARCH_STATE':
            return Object.assign({}, state, {searchState: action.payload});
        default:
            return state;
    }
};

export default socket;