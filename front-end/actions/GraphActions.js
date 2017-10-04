import {fetchGet} from "../utils/fetchUtils";
import {SERVER_URL} from "./index";
import {clearSearchState} from "./SocketActions";
import {fetchFailure, fetchRequest, fetchSuccess} from "./FetchActions";

export const setGraph = payload => {
    return {
        type: 'SET_GRAPH',
        payload
    }
};

function fetchSearchSuccess(json) {
    return dispatch => {
        dispatch(fetchSuccess());
        dispatch(setGraph(json.graph));
    }
}

export function handleFormSubmit(values, dispatch, props) {
    dispatch(clearSearchState());
    dispatch(fetchRequest());
    fetchGet(
        `${SERVER_URL}/find?from=${values.from}&to=${values.to}`,
        dispatch,
        fetchSearchSuccess,
        fetchFailure
    );
}



