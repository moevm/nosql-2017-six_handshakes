import {SERVER_URL} from "../constants/constants";
import {fetchGet} from "../utils/fetchUtils";

const SET_RESULT = 'RESULT/SET_RESULT';
const CLEAR_RESULT = 'RESULT/CLEAR_RESULT';

const FETCH_RESULT_REQUEST = 'RESULT/FETCH_RESULT_REQUEST';
const FETCH_RESULT_SUCCESS = 'RESULT/FETCH_RESULT_SUCCESS';
const FETCH_RESULT_FAILURE = 'RESULT/FETCH_RESULT_FAILURE';

const initialState = null;

export default (state = initialState, action) => {
    switch (action.type) {
        case SET_RESULT:
            return action.payload;
        case CLEAR_RESULT:
            return initialState;
        default:
            return state;
    }
}

export const setResult = result => ({type: SET_RESULT, payload: result});
export const clearResult = () => ({type: CLEAR_RESULT});

const fetchResultRequest = () => ({type: FETCH_RESULT_REQUEST});
const fetchSearchSuccess = json => dispatch => {
    dispatch({type: FETCH_RESULT_SUCCESS});
    dispatch(setResult(json));
};


export const fetchResult = (values, dispatch) => {
    dispatch(fetchResultRequest());
    fetchGet(
        `${SERVER_URL}/find?from=${values.from}&to=${values.to}`,
        dispatch,
        fetchSearchSuccess,
        (error) => console.log('RESULT ERROR', error)
    );
};

export const getHasResult = (state) => state.result !== null ;
