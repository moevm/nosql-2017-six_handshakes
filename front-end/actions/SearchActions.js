import {fetchGet} from "../utils/fetchUtils";
import {SERVER_URL} from "./index";
import {clearSearchState} from "./SocketActions";
import {fetchFailure, fetchRequest, fetchSuccess} from "./FetchActions";

export const setResult = payload => {
    return {
        type: 'SET_RESULT',
        payload
    }
};

function fetchSearchSuccess(json) {
    return dispatch => {
        dispatch(fetchSuccess());
        dispatch(setResult(json));
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



