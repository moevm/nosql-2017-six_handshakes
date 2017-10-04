import React from "react";
import {reduxForm, Field, change} from 'redux-form';
import {handleFormSubmit} from "../actions/GraphActions";
import "./Form.css"
import {connect} from "react-redux";
let Form = props => {
    const {handleSubmit, socket: {socketState, searchState}, setDataSource, formValues: {dataSource, from, to}} = props;
    const disabled = !from || !to || socketState !== 'CONNECTED' || searchState.length !== 0;
    return (
        <form className="main-form" onSubmit={handleSubmit}>
            <h3>Choose data source</h3>
            <div className="row">
                <div className={`icon-button vk ${dataSource === 'VK' ? 'active' : ''}`}
                     onClick={() => setDataSource('VK')}>
                    <i className="fa fa-vk "/>
                    vkontakte
                </div>
                <div className={`icon-button ${dataSource === 'FILE' ? 'active' : ''}`}
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

            <button className={`submit-button ${(disabled) ? 'disabled' : ''}`} type="submit" disabled={disabled}>Check</button>
        </form>
    )
};


Form = connect(
    state => ({
        formValues: state.form.mainForm.values
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
        dataSource: 'VK'
    }
})(Form);

export default Form;