import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { mapValues, omitBy } from "lodash-es";

import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import PageTitle, { FormikAwarePageTitle } from "../../PageTitle";
import SecondaryButton from "../../SecondaryButton";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import validateContact from "./validators";

import "./UpdateContacts.css";

const emptyIfUndefined = (value) => (value != null ? value : "");

const ownersOf = (system) => ({
  businessOwner: emptyIfUndefined(system.business_owner),
  productOwner: emptyIfUndefined(system.product_owner),
  technicalOwner: emptyIfUndefined(system.tech_owner),
  serviceOwner: emptyIfUndefined(system.service_owner),
  informationAssetOwner: emptyIfUndefined(system.information_asset_owner),
});

function UpdateContacts({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const initialOwners = ownersOf(system);
      const changedOwners = omitBy(
        mapValues(values, (value) => value.trim()),
        (value, key) => value === initialOwners[key]
      );
      try {
        await onSubmit(changedOwners);
      } catch (e) {
        if (e instanceof ValidationError) {
          formik.setErrors(e.errors);
        }
      }
    },
    [system, onSubmit]
  );

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
            <FormikAwarePageTitle>{`Update contacts — ${system?.name}`}</FormikAwarePageTitle>

            <ErrorSummary
              order={[
                "businessOwner",
                "technicalOwner",
                "serviceOwner",
                "productOwner",
                "informationAssetOwner",
              ]}
            />

            <h1>{system.name}</h1>
            <p className="update-contacts__secondary">
              You can provide named individuals as key points of contact for
              your system. An individual can be named multiple times if
              applicable.
            </p>

            <Form>
              <TextField
                name="businessOwner"
                hint="Who is the business owner for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Business owner
              </TextField>

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
                name="serviceOwner"
                hint="Who is the service owner for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Service owner
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
                hint="Who is the information asset owner for this system (e.g. Jane Bloggs)?"
                inputClassName="width-two-thirds"
                placeholder="Unknown"
                validate={validateContact}
              >
                Information asset owner
              </TextField>

              <div className="form-controls">
                <Button type="submit">Save</Button>
                <SecondaryButton onClick={handleCancel}>Cancel</SecondaryButton>
              </div>
            </Form>
          </>
        </Formik>
      ) : (
        <>
          <PageTitle>Loading system...</PageTitle>

          <p>Loading system data...</p>
        </>
      )}
    </div>
  );
}

UpdateContacts.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    business_owner: PropTypes.string,
    product_owner: PropTypes.string,
    tech_owner: PropTypes.string,
    service_owner: PropTypes.string,
    information_asset_owner: PropTypes.string,
  }),
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateContacts;
