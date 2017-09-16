import React from "react";
import {reduxForm, Field} from 'redux-form';
import {handleFormSubmit} from "../actions/index";

let Form = props => {
    const { handleSubmit } = props;
    return (
        <form onSubmit={ handleSubmit }>
            <div>
                <Field name="from" component="input" type="text" placeholder="from"/>
            </div>
            <div>
                <Field name="to" component="input" type="text" placeholder="to"/>
            </div>
            <button type="submit">Check!</button>
        </form>
    )
};

Form =  reduxForm({
    form: 'mainForm',
    onSubmit: handleFormSubmit
})(Form);

export default Form;