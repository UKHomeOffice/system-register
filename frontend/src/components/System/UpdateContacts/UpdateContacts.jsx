import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";

import TextField from "../../TextField";
import "./UpdateContacts.css";

const ownersOf = (system) => ({
  productOwner: system.product_owner,
});

function UpdateContacts({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async ({ productOwner }) => {
    await onSubmit({
      productOwner: productOwner.trim(),
    });
  }, [onSubmit]);
  const handleCancel = () => {onCancel()};

  return (
    <div className="centerContent">
      {system ? (
        <>
          <h1>{system.name}</h1>
          <p className="secondary">
            You can currently change the product owner information only.
            We are working to make other fields editable.
          </p>

          <Formik
            initialValues={ownersOf(system)}
            validateOnChange={false}
            onSubmit={handleSubmit}
          >
            <Form>
              <TextField
                name="productOwner"
                hint="Who is the primary contact for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
              >
                Product owner
              </TextField>

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

export default UpdateContacts;
