import React from "react";
import {reduxForm, Field, change} from 'redux-form';
import {handleFormSubmit} from "../actions/GraphActions";
import "./Form.css"
import {connect} from "react-redux";
let Form = props => {
    const {handleSubmit, socket: {socketState, searchState}, setDataSource, formValues: {dataSource}} = props;
    const disabled = socketState !== 'CONNECTED' || searchState.length !== 0;
    return (
        <form className="main-form" onSubmit={handleSubmit}>
            <div>
                <h3>Choose data source</h3>
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
            <div className="arrow">
                <i className="fa fa-long-arrow-right fa-2x"/>
            </div>
            <div>
                <h3>Enter IDs</h3>
                <div>
                    <Field name="from" component="input" type="text" placeholder="from"/>
                </div>
                <div>
                    <Field name="to" component="input" type="text" placeholder="to"/>
                </div>
            </div>
            <div className="arrow">
                <i className="fa fa-long-arrow-right fa-2x"/>
            </div>
            <div>
                <button className="submit-button" type="submit" disabled={disabled}>Check!</button>
            </div>
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