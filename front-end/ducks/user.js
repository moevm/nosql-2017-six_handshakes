import {SERVER_URL} from "../constants/constants";
import {fetchGet} from "../utils/fetchUtils";
const SET_USER = 'USER/SET_USER';
const FETCH_USER_REQUEST = 'USER/FETCH_USER_REQUEST';
const FETCH_USER_SUCCESS = 'USER/FETCH_USER_SUCCESS';
const FETCH_USER_FAILURE = 'USER/FETCH_USER_FAILURE';

export default (state = {}, action) => {
    switch (action.type) {
        case SET_USER:
            return action.payload;
        default:
            return state;
    }
}

export const setUser = user => ({type: SET_USER, payload: user});

const fetchUserRequest = () => ({type: FETCH_USER_REQUEST});
const fetchUserSuccess = user => dispatch => {
    dispatch({type: FETCH_USER_SUCCESS});
    dispatch(setUser(user));
};
const fetchUserFailure = (error) => ({type: FETCH_USER_FAILURE});

export const fetchUser = () => dispatch => {
    dispatch(fetchUserRequest());
    return fetchGet(
        `${SERVER_URL}/me`,
        dispatch,
        fetchUserSuccess,
        (error) => console.log('USER ERROR', error)
    );
};
