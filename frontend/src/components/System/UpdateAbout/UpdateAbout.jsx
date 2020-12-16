import React, { useCallback } from "react";
import { Field, Form, Formik } from "formik";
import { Button } from "govuk-react";

import "./UpdateAbout.css";

function UpdateAbout({ system, onSubmit }) {
  const handleSubmit = useCallback(async (values) => {
    console.log(values)
    await onSubmit(values);
  }, [onSubmit]);

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
            {({ values }) => (
              <Form>
                <div className="radioGroup">
                  <label><Field type="radio" name="criticality" value="unknown" />Unknown</label>
                  <label><Field type="radio" name="criticality" value="low" />Low</label>
                  <label><Field type="radio" name="criticality" value="medium" />Medium</label>
                  <label><Field type="radio" name="criticality" value="high" />High</label>
                  <label><Field type="radio" name="criticality" value="cni" />CNI</label>
                </div>
                <div className="form-controls">
                  <Button type="submit">Save</Button>
                </div>
              </Form>
            )}
          </Formik>
        </>
      ) : (
          <p>Loading system data...</p>
        )}
    </div>
  );
}

export default UpdateAbout;
