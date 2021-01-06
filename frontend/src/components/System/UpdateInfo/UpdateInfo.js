import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import TextField from "../../TextField";
import "./UpdateInfo.css";
import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import validateInfo from "../UpdateInfo/validators";
import ValidationError from "../../../services/validationError";
import { mapValues, omitBy } from "lodash-es";

function UpdateInfo({ system, onSubmit, onCancel, executeCheck }) {
    const handleSubmit = useCallback(async (values, formik) => {
        const initialInfo = { name: system.name };
        const changedInfo = omitBy(
            mapValues(values, (value) => value.trim()),
            (value, key) => value === initialInfo[key]);
        try {
            await onSubmit(changedInfo);
        } catch (e) {
            if (e instanceof ValidationError) {
                formik.setErrors(e.errors);
            }
        }
    }, [system, onSubmit]);

    const handleCancel = () => {
        onCancel();
    };

    return (
        <div className="centerContent">
            {system ? (
                <Formik
                    initialValues={{ name: system.name }}
                    validateOnChange={false}
                    onSubmit={handleSubmit}
                >
                    <>
                        <ErrorSummary />
                        <h1>{system.name}</h1>
                        <p className="secondary">
                            You can change the name of the system
            </p>

                        <Form>
                            <TextField
                                name="name"
                                hint="Please enter the new system name"
                                inputClassName="width-two-thirds"
                                validate={(value) => { return validateInfo(value, executeCheck, system.name) }}
                            >
                                System name
              </TextField>

                            <div className="form-controls">
                                <Button type="submit">Save</Button>
                                <Button
                                    type="button"
                                    onClick={handleCancel}
                                    buttonColour="#f3f2f1"
                                    buttonTextColour="#0b0c0c"
                                >
                                    Cancel
                </Button>
                            </div>
                        </Form>
                    </>
                </Formik>
            ) : (
                    <p>Loading system data...</p>
                )}
        </div>
    );
}

export default UpdateInfo;
