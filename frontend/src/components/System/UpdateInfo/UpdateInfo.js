import React, {useCallback} from "react";
import {Form, Formik} from "formik";
import {Button} from "govuk-react";

import TextField from "../../TextField";
import "./UpdateInfo.css";
import ErrorSummary from "../../ErrorSummary/ErrorSummary";

function UpdateInfo({system, onSubmit, onCancel}) {
  const handleSubmit = useCallback(async (values, formik) => {
   console.log("Arrrrghhhh")
  }, [system, onSubmit]);

  const handleCancel = () => {
    onCancel();
  };

  return (
    <div className="centerContent">
      {system ? (
        <Formik
          initialValues={system.name}
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
                name="systemName"
                hint="Please enter the new system name"
                inputClassName="width-two-thirds"
                // validate={validateName}
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
