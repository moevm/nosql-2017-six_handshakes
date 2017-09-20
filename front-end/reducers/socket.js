const initialState ={
    socketState: 'DISCONNECTED',
    searchState: []
};

const socket = (state = initialState, action) => {
    switch (action.type) {
        case 'SET_SOCKET_STATE':
            return Object.assign({}, state, {socketState: action.payload});
        case 'SET_SEARCH_STATE':
            return Object.assign({}, state, {searchState: [...state.searchState, action.payload]});
        case 'CLEAR_SEARCH_STATE':
            return Object.assign({}, state, {searchState: []});
        default:
            return state;
    }
};

export default socket;