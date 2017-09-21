export const setSocketState = payload => {
    return {
        type: 'SET_SOCKET_STATE',
        payload
    }
};

export const setSearchState = payload => {
    return {
        type: 'SET_SEARCH_STATE',
        payload
    }
};

export const clearSearchState = () => {
    return {
        type: 'CLEAR_SEARCH_STATE',
    }
};