import React from "react";
import PropTypes from "prop-types";
import { map } from "lodash-es";

import Radio from "../Radio";

import "./RadioGroup.css";

const makeRadio = (value, text, { hint, title } = {}) => ({
  value,
  text,
  hint,
  title,
});

// eslint-disable-next-line react/display-name,react/prop-types
const toRadioButton = (name) => ({ value, text, hint, title }, index) => (
  <Radio key={index} name={name} value={value} hint={hint} title={title}>
    {text}
  </Radio>
);

function RadioGroup({ children, hint, name, items }) {
  return (
    <fieldset className="radio-group">
      <legend>
        <h2 className="radio-group__heading">{children}</h2>
      </legend>

      {hint && <div className="radio-group__hint">{hint}</div>}

      <div className="radio-group__radios">
        {map(items, toRadioButton(name))}
      </div>
    </fieldset>
  );
}

RadioGroup.propTypes = {
  children: PropTypes.node.isRequired,
  hint: PropTypes.node,
  name: PropTypes.string.isRequired,
  items: PropTypes.arrayOf(
    PropTypes.shape({
      value: PropTypes.string.isRequired,
      text: PropTypes.node.isRequired,
      hint: PropTypes.node,
      title: PropTypes.string,
    }).isRequired
  ).isRequired,
};

export default RadioGroup;
export { makeRadio };
