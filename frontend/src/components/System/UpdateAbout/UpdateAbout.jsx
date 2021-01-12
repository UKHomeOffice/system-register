import React, { useCallback } from "react";
import { Form, Formik } from "formik";
import { Button } from "govuk-react";
import Radio from "../../Radio"

import "./UpdateAbout.css";
import {omitBy} from "lodash-es";

const emptyIfUndefined = (value) => value != null ? value : "";

const infoAbout = (system) => ({
  criticality: emptyIfUndefined(system.criticality),
  investmentState: emptyIfUndefined(system.investment_state),
});

function UpdateAbout({ system, onSubmit, onCancel }) {
  const handleSubmit = useCallback(async (values) => {
        const initialInfo = infoAbout(system);
        const changedInfo = omitBy(
          values,
          (value, key) => value === initialInfo[key]);
          await onSubmit(changedInfo);
  }, [onSubmit, system]);

  const handleCancel = () => { onCancel() };
  return (
    <div className="centerContent">
      {system ? (
        <>
          <h1>{system.name}</h1>
          <p className="update-about-secondary">
            You can currently change only criticality.
            We are working to make other fields editable.
          </p>


          <Formik
            initialValues={{
              criticality: system.criticality,
              investmentState: system.investment_state,
            }}
            onSubmit={handleSubmit}
          >
            <Form>
              <h2 className="update-about-radio-group-title">What is the criticality of the system?</h2>
              <p className="update-about-radio-group-hint">Please select the level of criticality, as per the system's Service Criticality Assessment.</p>
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
              <h2 className="update-about-radio-group-title">What is the investment state of the system?</h2>
              <p className="update-about-radio-group-hint">Please select the most applicable lifecycle stage.</p>
              {
                [
                  { value: "evergreen",
                    label: "Evergreen",
                    hint:"Constantly funded system or product without a financial cliff edge."
                  },
                  { value: "invest",
                    label: "Invest",
                    hint:"System or product in conception, development, launch or maturity phase where the investment is increasing capability."
                  },
                  { value: "maintain",
                    label: "Maintain",
                    hint:"System or product in conception, development, launch, maturity or plateau phase where the capability is not being increased."
                  },
                  { value: "sunset",
                    label: "Sunset",
                    hint:"System or product still in production but planned for end of life."
                  },
                  { value: "decommissioned",
                    label: "Decommissioned",
                    hint:"System or product is no longer in production and has been switched off."
                  },
                  { value: "cancelled",
                    label: "Cancelled",
                    hint:"System or product did not go ahead and was never implemented."
                  },
                  { value: "unknown",
                    label: "Unknown",
                    hint:"The investment state for this system or product is unknown"
                  },
                ].map(v => {
                  return (
                    <Radio name="investmentState" key={v.value} value={v.value} hint={v.hint}>{v.label} </Radio>
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
