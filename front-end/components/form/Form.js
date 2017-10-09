import React from "react";
import {reduxForm, Field, change} from 'redux-form';
import {handleFormSubmit} from "../../actions/SearchActions";
import "./Form.css"
import {connect} from "react-redux";

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
    console.log(props);
    const {errors, handleSubmit, socket: {socketState}, setDataSource, formValues: {dataSource, from, to}} = props;

    let errorMessage = (errors) ? errors.from : '';
    if (socketState !== 'CONNECTED') errorMessage = 'Connecting to server...';

    const vkDataSource = dataSource === 'VK';
    const fileDataSource = dataSource === 'FILE';

    return (
        <div>
            <form className="main-form content-wrapper" onSubmit={handleSubmit}>
                <h3>Choose data source</h3>
                <div className="row">
                    <div className={`icon-button vk ${vkDataSource ? 'active' : ''}`}
                         onClick={() => setDataSource('VK')}>
                        <i className="fa fa-vk "/>
                        vkontakte
                    </div>
                    <div className={`icon-button ${fileDataSource ? 'active' : ''}`}
                         onClick={() => setDataSource('FILE')}>
                        <i className="fa fa-upload"/>
                        your data
                    </div>
                </div>

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

/*
 <div className={`file-input ${fileDataSource ? 'active' : ''}`}>
 {fileDataSource && <Field name="csv" component="input" type="file"/>}
 </div>
 */

Form = connect(
    state => ({
        formValues: state.form.mainForm.values,
        errors: state.form.mainForm.syncErrors
    }),
    dispatch => ({
        setDataSource: (value) => dispatch(change('mainForm', 'dataSource', value))
    })
)(Form);

Form = reduxForm({
    form: 'mainForm',
    onSubmit: handleFormSubmit,
    initialValues: {
        from: '',
        to: '',
        dataSource: 'VK',
        // csv: undefined
    },
    validate
})(Form);

export default Form;