import {SERVER_URL} from "./index";
import {fetchGet} from "../utils/fetchUtils";
import {fetchRequest} from "../actions/FetchActions";
import {fetchFailure, fetchSuccess} from "./FetchActions";

export const setUser = user => {
    return {
        type: 'SET_USER',
        user
    }
};

function fetchUserSuccess(user) {
    return dispatch => {
        dispatch(fetchSuccess());
        dispatch(setUser(user));
    }
}

export function fetchUser() {
    return dispatch => {
        dispatch(fetchRequest());
        return fetchGet(
            `${SERVER_URL}/me`,
            dispatch,
            fetchUserSuccess,
            fetchFailure
        );
    }
}