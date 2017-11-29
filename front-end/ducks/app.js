import {clearMessages} from "./messageSocket";
import {clearResult, fetchResult} from "./result";
import {reset} from "redux-form";

export const handleFormSubmit = (values, dispatch, props) => {
    return fetchResult(values, dispatch);
};

export const resetApp = () => dispatch => {
    dispatch(clearMessages());
    dispatch(clearResult());
    dispatch(reset("mainForm"));
};

