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
        default:
            return state;
    }
};

export default result;