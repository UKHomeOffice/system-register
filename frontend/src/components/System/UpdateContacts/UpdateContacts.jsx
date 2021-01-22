import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { mapValues, omitBy } from "lodash-es";

import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import validateContact from "./validators";

import "./UpdateContacts.css";

const emptyIfUndefined = (value) => value != null ? value : "";

const ownersOf = (system) => ({
  productOwner: emptyIfUndefined(system.product_owner),
  technicalOwner: emptyIfUndefined(system.tech_owner),
  informationAssetOwner: emptyIfUndefined(system.information_asset_owner),
});

function UpdateContacts({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async (values, formik) => {
    const initialOwners = ownersOf(system);
    const changedOwners = omitBy(
      mapValues(values, (value) => value.trim()),
      (value, key) => value === initialOwners[key]);
    try {
      await onSubmit(changedOwners);
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
          initialValues={ownersOf(system)}
          validateOnChange={false}
          onSubmit={handleSubmit}
        >
          <>
            <ErrorSummary order={["technicalOwner", "productOwner", "informationAssetOwner"]}/>

            <h1>{system.name}</h1>
            <p className="secondary">
              You can currently change only technical owner, product owner or information asset owner information.
              We are working to make other fields editable.
            </p>

            <Form>
              <TextField
                name="technicalOwner"
                hint="Who is the technical owner for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Technical owner
              </TextField>

              <TextField
                name="productOwner"
                hint="Who is the primary contact for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Product owner
              </TextField>

              <TextField
                name="informationAssetOwner"
                hint="Who is the primary contact for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Information asset owner
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

export default UpdateContacts;
