import React from "react";
import {reduxForm, Field} from 'redux-form';
import {handleFormSubmit} from "../actions/GraphActions";

let Form = props => {
    const {handleSubmit, socket: {socketState, searchState}} = props;
    const disabled = socketState !== 'CONNECTED' || searchState.length !== 0;
    return (
        <form className="main-form" onSubmit={handleSubmit}>
            <div>
                <h2>Choose data source</h2>
                <div className="icon-button">
                    <i className="fa fa-vk "/>
                    vkontakte
                </div>
                <div className="icon-button">
                    <i className="fa fa-upload"/>
                    your data
                </div>
            </div>
            <div>
                <i className="fa fa-long-arrow-right fa-2x"/>
            </div>
            <div>
                <h2>Enter IDs</h2>
                <div>
                    <Field name="from" component="input" type="text" placeholder="from"/>
                </div>
                <div>
                    <Field name="to" component="input" type="text" placeholder="to"/>
                </div>
            </div>
            <div>
                <i className="fa fa-long-arrow-right fa-2x"/>
            </div>
            <div>
                <button type="submit" disabled={disabled}>Check!</button>
            </div>
        </form>
    )
};

Form = reduxForm({
    form: 'mainForm',
    onSubmit: handleFormSubmit
})(Form);

export default Form;