import {fetchGet} from "../utils/fetchUtils";
import {SERVER_URL} from "./index";
import {clearSearchState} from "./SocketActions";
import {fetchFailure, fetchRequest, fetchSuccess} from "./FetchActions";
import {reset} from "redux-form";

const setResult = payload => {
    return {
        type: 'SET_RESULT',
        payload
    }
};

const clearResult = () => {
    return {
        type: 'CLEAR_RESULT'
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

export function resetSearchResults() {
    return dispatch => {
        dispatch(clearSearchState());
        dispatch(clearResult());
        dispatch(reset("mainForm"));
    }
}

