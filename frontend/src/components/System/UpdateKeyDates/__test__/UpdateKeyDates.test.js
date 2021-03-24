import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";

import UpdateKeyDates from "../UpdateKeyDates";

describe("UpdateKeyDates", () => {
  const cancelHandler = jest.fn();
  const submitHandler = jest.fn();

  function setUp(props = {}) {
    if (props.system && !props.system.name) {
      throw new Error();
    }
    const actualProps = {
      system: null,
      onSubmit: submitHandler,
      onCancel: cancelHandler,
      ...props,
    };
    return render(<UpdateKeyDates {...actualProps} />);
  }

  function overtype(field, value) {
    user.clear(field);
    // noinspection JSIgnoredPromiseFromCall
    user.type(field, value);
  }

  it("displays the name of the system", () => {
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: null,
          additional_information: null,
        },
      },
    });

    expect(screen.getByRole("heading", { level: 1 })).toHaveTextContent(
      "system name"
    );
  });

  it("has a page title", () => {
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: null,
          additional_information: null,
        },
      },
    });

    expect(document.title).toBe(
      "Update key dates — system name — System Register"
    );
  });

  it("displays a loading message if data is unavailable", () => {
    setUp({ system: null });

    expect(screen.getByText(/loading system data/i)).toBeInTheDocument();
    expect(document.title).toBe("Loading system... — System Register");
  });

  it("displays an additional information area pre-filled with existing system data", () => {
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: "2021-06-01",
          additional_information: "some sunset info",
        },
      },
    });

    expect(screen.getByText("some sunset info")).toBeInTheDocument();
  });

  it("displays a date field pre-filled with existing system data", () => {
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: "2021-06-01",
          additional_information: "some sunset info",
        },
      },
    });

    const day = screen.getByLabelText("Day");
    const month = screen.getByLabelText("Month");
    const year = screen.getByLabelText("Year");

    expect(day.value).toEqual("1");
    expect(month.value).toEqual("6");
    expect(year.value).toEqual("2021");
  });

  it("leaves the sunset date fields blank if there is no sunset date", () => {
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: null,
        },
      },
    });

    const day = screen.getByLabelText("Day");
    const month = screen.getByLabelText("Month");
    const year = screen.getByLabelText("Year");

    expect(day.value).toEqual("");
    expect(month.value).toEqual("");
    expect(year.value).toEqual("");
  });

  describe("validation", () => {
    it("displays error messages if fields contain invalid values", async () => {
      setUp({
        system: {
          name: "system name",
          sunset: {},
        },
      });
      const sunsetDayField = screen.getByLabelText("Day");
      const additionalSunsetInformation = screen.getByLabelText(
        /additional information for the sunset date/
      );
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(sunsetDayField, "1");
      overtype(additionalSunsetInformation, "x");
      user.click(saveButton);

      await waitFor(() => {
        const errors = screen.getAllByText(/must/);
        expect(errors).toHaveLength(2);
      });
    });
  });
});
