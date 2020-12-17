import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { mapValues, omitBy } from "lodash-es";

import TextField from "../../TextField";
import "./UpdateContacts.css";

const emptyIfUndefined = (value) => value != null ? value : "";

const ownersOf = (system) => ({
  productOwner: emptyIfUndefined(system.product_owner),
});

function UpdateContacts({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async (values) => {
    const initialOwners = ownersOf(system);
    const changedOwners = omitBy(
      mapValues(values, (value) => value.trim()),
      (value, key) => value === initialOwners[key]);
    await onSubmit(changedOwners);
  }, [system, onSubmit]);

  const handleCancel = () => {
    onCancel();
  };

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
          </Formik>
        </>
      ) : (
        <p>Loading system data...</p>
      )}
    </div>
  );
}

export default UpdateContacts;
