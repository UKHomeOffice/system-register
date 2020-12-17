import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import Radio from "../../Radio"

import "./UpdateAbout.css";

function UpdateAbout({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async (values) => {
    if(values.criticality === system.criticality) { //TODO when we have multiple commands might be better to have some kind of change log which is read by a command mapper, meaning if nothing changed, defualt is no commands
      handleCancel();
    }
    await onSubmit(values);
  }, [onSubmit]);

  const handleCancel = () => { onCancel() };
  return (
    <div className="centerContent">
      {system ? (
        <>
          <h1>{system.name}</h1>
          <p className="secondary">
            You can currently change the criticality only.
            We are working to make other fields editable.
          </p>

          <Formik
            initialValues={{
              criticality: system.criticality,
            }}
            onSubmit={handleSubmit}
          >
            <Form>
              {
                [
                  { value: "unknown", label: "Unknown" },
                  { value: "low", label: "Low" },
                  { value: "medium", label: "Medium" },
                  { value: "high", label: "High" },
                  { value: "cni", label: "CNI" }
                ].map(v => {
                  return (
                    <Radio name="criticality" key={v.value} value={v.value}>{v.label}</Radio>
                  )
                })
              }
              <div className="form-controls">
                <Button type="submit">Save</Button>
                <Button type="button" onClick={handleCancel} buttonColour="#f3f2f1" buttonTextColour="#0b0c0c">Cancel</Button>
              </div>
            </Form>
          </Formik>
        </>
      ) : (
          <p>Loading system data...</p>
        )}
    </div>
  );
}

export default UpdateAbout;
