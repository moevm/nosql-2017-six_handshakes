import React from "react";
import {reduxForm, Field} from 'redux-form';
import {handleFormSubmit} from "../actions/GraphActions";

let Form = props => {
    const { handleSubmit, socket: {socketState, searchState} } = props;
    const disabled = socketState !== 'CONNECTED' || searchState !== 'NONE';
    return (
        <form onSubmit={ handleSubmit }>
            <div>
                <Field name="from" component="input" type="text" placeholder="from"/>
            </div>
            <div>
                <Field name="to" component="input" type="text" placeholder="to"/>
            </div>
            <button type="submit" disabled={disabled}>Check!</button>
        </form>
    )
};

Form =  reduxForm({
    form: 'mainForm',
    onSubmit: handleFormSubmit
})(Form);

export default Form;