import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import Radio from "../../Radio"

import "./UpdateAbout.css";

function UpdateAbout({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async (values) => {
    const anythingChanged = values.criticality === system?.criticality
    anythingChanged ? onCancel() : await onSubmit(values);
  }, [onSubmit, system, onCancel]);

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
          <h2>What is the criticality of the system?</h2>

          <Formik
            initialValues={{
              criticality: system.criticality,
            }}
            onSubmit={handleSubmit}
          >
            <Form>
              {
                [
                  { value: "low", label: "Low" },
                  { value: "medium", label: "Medium" },
                  { value: "high", label: "High" },
                  { value: "cni", label: "CNI" },
                  { value: "unknown", label: "Unknown" },
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
