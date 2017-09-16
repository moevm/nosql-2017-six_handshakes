import {fetchGet} from "../utils/fetchUtils";
export const setUser = user => {
    return {
        type: 'SET_USER',
        user
    }
};

//TODO replace console log by some action
function fetchRequest(){
    console.log('Send request');
    return {
        type: 'NONE'
    }
}

//TODO replace console log by some action
function fetchUserSuccess(user) {
    console.log('Request success');
    return dispatch => {
        // dispatch(fetchSuccess());
        dispatch(setUser(user));
    }
}

//TODO replace console log by some action
function fetchUserFailure(errorMessage) {
    console.log('Request failed');
    return {
        type: 'NONE'
    }
    // return dispatch => {
    //     dispatch(fetchFailure());
    //     dispatch(error({message: errorMessage}));
    // }
}

export function fetchUser() {
    return dispatch => {
        dispatch(fetchRequest());

        return fetchGet(
            `${SERVER_URL}/me`,
            dispatch,
            fetchUserSuccess,
            fetchUserFailure
        );
    }
}

//TODO move constant to separate file
const SERVER_URL = 'http://localhost:8080';






