import React from "react";
import {reduxForm, Field, change} from 'redux-form';
import "./Form.css"
import {connect} from "react-redux";
import {handleFormSubmit} from "../../ducks/app";
import {getIsConnected} from "../../ducks/messageSocket";

//TODO  show server errors too
const validate = values => {
    const errors = {};
    const {from, to} = values;
    if (!from || !to) {
        errors.from = 'IDs shouldn\'t be empty'
    } else if (from === to) {
        errors.from = 'IDs shouldn\'t be same'
    }
    return errors
};


let Form = props => {
    console.log('FORM PROPS', props);
    const {formErrors, handleSubmit, connected, formValues: {from, to}, error} = props;

    let errorMessage = (formErrors) ? formErrors.from : '';
    if(error) errorMessage = error;
    if (!connected) errorMessage = 'Connecting to server...';


    return (
        <div>
            <form className="main-form content-wrapper" onSubmit={handleSubmit}>
                <h3>Enter IDs</h3>
                <div className="row">
                    <Field name="from" component="input" type="text" placeholder="from"/>
                    <Field name="to" component="input" type="text" placeholder="to"/>
                </div>

                <button className={`submit-button ${(errorMessage) ? 'disabled' : ''}`} type="submit"
                        disabled={errorMessage}>Check
                </button>
            </form>
            {errorMessage &&
            <div className="error-label"><i className="fa fa-exclamation-triangle"/>{errorMessage}</div>}
        </div>
    )
};


Form = connect(
    state => ({
        formValues: state.form.mainForm.values,
        formErrors: state.form.mainForm.syncErrors,
        connected: getIsConnected(state)
    })
)(Form);

Form = reduxForm({
    form: 'mainForm',
    onSubmit: handleFormSubmit,
    initialValues: {
        from: '',
        to: ''
    },
    validate
})(Form);

export default Form;