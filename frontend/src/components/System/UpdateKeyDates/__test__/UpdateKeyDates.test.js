import React from "react";
import user from "@testing-library/user-event";
import { render, screen, waitFor } from "@testing-library/react";
import { DateTime } from "luxon";

import UpdateKeyDates from "../UpdateKeyDates";
import ValidationError from "../../../../services/validationError";

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

  beforeEach(() => {
    jest.resetAllMocks();
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
        const errors = screen.getAllByText(/must/, {
          selector: "label *, span",
        });
        expect(errors).toHaveLength(2);
      });
    });

    it("display a summary of any errors if fields contain invalid values", async () => {
      setUp({
        system: {
          name: "system name",
          sunset: {},
        },
      });
      const sunsetMonthField = screen.getByLabelText("Month");
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(sunsetMonthField, "1");
      user.click(saveButton);

      expect(
        await screen.findByText(/must/, { selector: "a" })
      ).toBeInTheDocument();
    });

    it("gives day field a name recognised by error-summary", () => {
      setUp({
        system: {
          name: "system name",
          sunset: {},
        },
      });
      const sunsetDayField = screen.getByLabelText("Day");

      expect(sunsetDayField).toHaveAttribute("name", "sunsetDate");
    });
  });

  describe("submission", () => {
    it("sends changed sunset data to the submit handler", async () => {
      setUp({
        system: {
          name: "system name",
          sunset: {},
        },
      });
      const sunsetDayField = screen.getByLabelText("Day");
      const sunsetMonthField = screen.getByLabelText("Month");
      const sunsetYearField = screen.getByLabelText("Year");
      const additionalSunsetInformationField = screen.getByLabelText(
        /additional information/i
      );
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(sunsetDayField, "1");
      overtype(sunsetMonthField, "4");
      overtype(sunsetYearField, "2021");
      overtype(additionalSunsetInformationField, "some info");
      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith({
          sunset: {
            date: DateTime.fromISO("2021-04-01"),
            additionalInformation: "some info",
          },
        });
      });
    });

    it("sends changed sunset data to the submit handler", async () => {
      setUp({
        system: {
          name: "system name",
          sunset: {
            date: "2020-05-01",
            additional_information: "some info",
          },
        },
      });
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith({});
      });
    });

    it("sends trims data before submission", async () => {
      setUp({
        system: {
          name: "the system",
          sunset: {},
        },
      });
      const sunsetDayField = screen.getByLabelText("Day");
      const sunsetMonthField = screen.getByLabelText("Month");
      const sunsetYearField = screen.getByLabelText("Year");
      const additionalSunsetInformationField = screen.getByLabelText(
        /additional information/i
      );
      const saveButton = screen.getByRole("button", { name: /save/i });

      overtype(sunsetDayField, "5");
      overtype(sunsetMonthField, "6");
      overtype(sunsetYearField, "1789");
      overtype(additionalSunsetInformationField, "\f text\t");
      user.click(saveButton);

      await waitFor(() => {
        expect(submitHandler).toBeCalledWith({
          sunset: {
            date: DateTime.fromISO("1789-06-05"),
            additionalInformation: "text",
          },
        });
      });
    });

    it("does not submit values if they are invalid", async () => {
      setUp({
        system: {
          name: "system name",
          sunset: {
            date: "2020-05-01",
            additional_information: "some info",
          },
        },
      });
      const sunsetDayField = screen.getByLabelText("Day");
      const saveButton = screen.getByRole("button", { name: /save/i });

      user.clear(sunsetDayField);
      user.click(saveButton);

      const errorMessage = await screen.findByText(/must/, {
        selector: "label *, span",
      });
      expect(errorMessage).toBeInTheDocument();
      expect(submitHandler).not.toBeCalled();
    });
  });

  it("displays errors returned by the API", async () => {
    submitHandler.mockRejectedValue(
      new ValidationError({
        sunsetDate: "sunset date validation error",
        sunsetAdditionalInformation:
          "additional sunset date information validation error",
      })
    );
    setUp({
      system: {
        name: "system name",
        sunset: {
          date: "2020-05-01",
          additional_information: "some info",
        },
      },
    });
    const sunsetMonthField = screen.getByLabelText("Month");
    const saveButton = screen.getByRole("button", { name: /save/i });
    overtype(sunsetMonthField, "4");
    user.click(saveButton);
    const errors = await screen.findAllByText(/validation error/, {
      selector: "label *, span",
    });
    expect(errors).toHaveLength(2);
    expect(errors[0]).toHaveTextContent("sunset date validation error");
    expect(errors[1]).toHaveTextContent(
      "additional sunset date information validation error"
    );
  });
});
