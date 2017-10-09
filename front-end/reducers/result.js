const initialState = {
    graph: undefined,
    timeStat: undefined,
    peopleChecked: undefined,
    currentWeb: undefined
};

const result = (state = initialState , action) => {
    switch (action.type) {
        case 'SET_RESULT':
            return  action.payload;
        case 'CLEAR_RESULT':
            return initialState;
        default:
            return state;
    }
};

export default result;