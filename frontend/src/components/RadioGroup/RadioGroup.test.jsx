import React from "react";
import { render, screen, within } from "@testing-library/react";
import { Form, Formik } from "formik";
import { map } from "lodash-es";

import RadioGroup, { makeRadio } from ".";

const labelFor = (control) => control.closest("label");
const toRadioButtons = ([value, text, hint]) => makeRadio(value, text, hint);

describe("RadioGroup", () => {
  it("displays a heading for the group", () => {
    setUp({ items: [], heading: "the heading" });

    expect(screen.getByRole("heading")).toHaveTextContent(/^the heading$/);
  });

  it("displays hint text for the group", () => {
    setUp({ items: [], hint: "the hint" });

    expect(screen.getByText("the hint")).toBeVisible();
  });

  describe("radio buttons", () => {
    it("displays a list of radio buttons: %p", () => {
      const items = [
        ["value 1", "text 1"],
        ["value 2", "text 2"],
      ];

      setUp({ items: map(items, toRadioButtons) });

      const radioButtons = screen.getAllByRole("radio");
      expect(radioButtons).toHaveLength(2);

      items.forEach(([value, text], index) => {
        const radioButton = radioButtons[index];
        expect(labelFor(radioButton)).toHaveTextContent(
          new RegExp(`^${text}$`)
        );
        expect(radioButton.value).toBe(value);
      });
    });

    it("includes an optional hint with each radio button", () => {
      const items = [
        ["value 1", "text 1"],
        ["value 2", "text 2", "hint 2"],
      ];

      setUp({ items: map(items, toRadioButtons) });

      const radioButtons = screen.getAllByRole("radio");
      expect(
        within(labelFor(radioButtons[0])).queryByText(/hint/)
      ).not.toBeInTheDocument();
      expect(
        within(labelFor(radioButtons[1])).getByText("hint 2")
      ).toBeVisible();
    });

    it.each(["abc", "def"])(
      "preselects the radio button with matching value: %p",
      (value) => {
        const items = [
          ["abc", "pqr"],
          ["def", "stu"],
        ];

        setUp({ name: "group", value, items: map(items, toRadioButtons) });

        const radioButton = screen.getByDisplayValue(value);
        expect(radioButton).toBeChecked();
      }
    );
  });

  function setUp({
    items,
    value = "",
    name = "group name",
    heading = "radio group",
    hint = "group hint",
  }) {
    return render(
      <Formik initialValues={{ [name]: value }} onSubmit={() => {}}>
        <Form>
          <RadioGroup name={name} items={items} hint={hint}>
            {heading}
          </RadioGroup>
        </Form>
      </Formik>
    );
  }
});
