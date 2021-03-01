import React, { useCallback } from "react";
import PropTypes from "prop-types";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import { defaultTo, mapValues, omitBy } from "lodash-es";

import ErrorSummary from "../../ErrorSummary/ErrorSummary";
import PageTitle from "../../PageTitle";
import Radio from "../../Radio";
import SecondaryButton from "../../SecondaryButton";
import TextField from "../../TextField";
import ValidationError from "../../../services/validationError";
import validateName from "./validators";

import "./UpdateAbout.css";

const emptyIfUndefined = (value) => (value != null ? value : "");

const infoAbout = (system) => ({
  portfolio: defaultTo(system.portfolio, "Unknown"),
  criticality: defaultTo(system.criticality, "unknown"),
  investmentState: defaultTo(system.investment_state, "unknown"),
  developedBy: emptyIfUndefined(system.developed_by),
  supportedBy: emptyIfUndefined(system.supported_by),
});

function UpdateAbout({ system, portfolios, onSubmit, onCancel }) {
  const handleSubmit = useCallback(
    async (values, formik) => {
      const initialInfo = infoAbout(system);
      const changedInfo = omitBy(
        mapValues(values, (value) => value.trim()),
        (value, key) => value === initialInfo[key]
      );

      try {
        await onSubmit(changedInfo);
      } catch (e) {
        if (e instanceof ValidationError) {
          formik.setErrors(e.errors);
        }
      }
    },
    [onSubmit, system]
  );
  const handleCancel = () => {
    onCancel();
  };

  return (
    <div className="centerContent">
      <PageTitle>{`Update about — ${system?.name}`}</PageTitle>

      {system ? (
        <Formik
          initialValues={infoAbout(system)}
          validateOnChange={false}
          onSubmit={handleSubmit}
        >
          <Form>
            <ErrorSummary
              order={[
                "portfolio",
                "criticality",
                "investmentState",
                "developedBy",
                "supportedBy",
              ]}
            />

            <h1>{system.name}</h1>
            <p className="update-about-secondary">
              You can update system portfolio, criticality, investment, support
              and development information. If you have any questions, please
              contact the System Register team via the ‘Contact’ tab in the
              navigation header.
            </p>

            <h2 className="update-about-radio-group-title">
              What portfolio does the system belong to?
            </h2>
            <br />
            {portfolios.map((v) => (
              <Radio name="portfolio" key={v} value={v}>
                {v}
              </Radio>
            ))}
            <Radio name="portfolio" value="Unknown">
              Unknown
            </Radio>

            <h2 className="update-about-radio-group-title">
              What is the criticality of the system?
            </h2>
            <p className="update-about-radio-group-hint">
              Please select the level of criticality, as per the system’s
              Service Criticality Assessment.
            </p>
            {[
              { value: "low", label: "Low" },
              { value: "medium", label: "Medium" },
              { value: "high", label: "High" },
              { value: "cni", label: "CNI" },
              { value: "unknown", label: "Unknown" },
            ].map((v) => {
              return (
                <Radio name="criticality" key={v.value} value={v.value}>
                  {v.label}
                </Radio>
              );
            })}
            <h2 className="update-about-radio-group-title">
              What is the investment state of the system?
            </h2>
            <p className="update-about-radio-group-hint">
              Please select the most applicable lifecycle stage.
            </p>
            {[
              {
                value: "evergreen",
                label: "Evergreen",
                hint:
                  "Constantly funded system or product without a financial cliff edge.",
              },
              {
                value: "invest",
                label: "Invest",
                hint:
                  "System or product in conception, development, launch or maturity phase where the investment is increasing capability.",
              },
              {
                value: "maintain",
                label: "Maintain",
                hint:
                  "System or product in conception, development, launch, maturity or plateau phase where the capability is not being increased.",
              },
              {
                value: "sunset",
                label: "Sunset",
                hint:
                  "System or product still in production but planned for end of life.",
              },
              {
                value: "decommissioned",
                label: "Decommissioned",
                hint:
                  "System or product is no longer in production and has been switched off.",
              },
              {
                value: "cancelled",
                label: "Cancelled",
                hint:
                  "System or product did not go ahead and was never implemented.",
              },
              {
                value: "unknown",
                label: "Unknown",
                hint:
                  "The investment state for this system or product is unknown.",
              },
            ].map((v) => {
              return (
                <Radio
                  name="investmentState"
                  key={v.value}
                  value={v.value}
                  hint={v.hint}
                >
                  {v.label}{" "}
                </Radio>
              );
            })}

            <TextField
              name="developedBy"
              hint="Please state the organisation, group or individuals developing the system (e.g. a specific portfolio, an outsourced company, Jane Bloggs, etc.)"
              inputClassName="update-about-two-thirds"
              placeholder="Unknown"
              validate={validateName}
            >
              Who develops the system?
            </TextField>

            <TextField
              name="supportedBy"
              hint="Please state the organisation, group or individuals supporting the system (e.g. a specific portfolio, an outsourced company, Jane Bloggs, etc.)"
              inputClassName="update-about-two-thirds"
              placeholder="Unknown"
              validate={validateName}
            >
              Who supports the system?
            </TextField>

            <div className="form-controls">
              <Button type="submit">Save</Button>
              <SecondaryButton onClick={handleCancel}>Cancel</SecondaryButton>
            </div>
          </Form>
        </Formik>
      ) : (
        <p>Loading system data...</p>
      )}
    </div>
  );
}

UpdateAbout.propTypes = {
  system: PropTypes.shape({
    name: PropTypes.string.isRequired,
    portfolio: PropTypes.string,
    criticality: PropTypes.oneOf(["low", "medium", "high", "cni", "unknown"]),
    investment_state: PropTypes.oneOf([
      "invest",
      "maintain",
      "sunset",
      "decommissioned",
      "cancelled",
      "unknown",
    ]),
    developed_by: PropTypes.string,
    supported_by: PropTypes.string,
  }),
  portfolios: PropTypes.arrayOf(PropTypes.string).isRequired,
  onSubmit: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
};

export default UpdateAbout;
