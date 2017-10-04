const graph = (state = {data: null}, action) => {
    switch (action.type) {
        case 'SET_GRAPH':
            return {
                data: action.payload
            };
        default:
            return state;
    }
};

export default graph;