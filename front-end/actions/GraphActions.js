import {fetchGet} from "../utils/fetchUtils";
import {SERVER_URL} from "./index";
import {clearSearchState} from "./SocketActions";

export function handleFormSubmit(values, dispatch, props) {
    dispatch(clearSearchState());
    fetchGet(
        `${SERVER_URL}/find?from=${values.from}&to=${values.to}`,
        dispatch,
        fetchSearchSuccess,
        fetchSearchFailure
    );
}


export const setGraph = payload => {
    return {
        type: 'SET_GRAPH',
        payload
    }
};


//TODO replace console log by some action
function fetchSearchSuccess(json) {
    return dispatch => {
        console.log('search finished');
        console.log(json);
        dispatch(setGraph(json));
    }
}

//TODO replace console log by some action
function fetchSearchFailure(errorMessage) {
    console.log('search failed');
    return {
        type: 'NONE'
    }
    // return dispatch => {
    //     dispatch(fetchFailure());
    //     dispatch(error({message: errorMessage}));
    // }
}