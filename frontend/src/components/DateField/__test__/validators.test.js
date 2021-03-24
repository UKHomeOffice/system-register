import { validateDate } from "../validators";

describe("date field validation", () => {
  it("doesn't return error text if the date is valid", () => {
    const errors = validateDate({
      day: "07",
      month: "03",
      year: "1986",
    });

    expect(errors).toBeUndefined();
  });

  it("doesn't return error text when all fields are blank", () => {
    const errors = validateDate({
      day: "",
      month: "",
      year: "",
    });

    expect(errors).toBeUndefined();
  });

  it.each([
    ["day", "", "03", "2021"],
    ["day", " ", "03", "2021"],
    ["month", "01", "", "2021"],
    ["month", "01", " ", "2021"],
    ["year", "01", "03", ""],
    ["year", "01", "03", " "],
  ])(
    "returns an error if one field has a missing value: %s",
    (field, day, month, year) => {
      const errors = validateDate({
        day,
        month,
        year,
      });

      expect(errors).toContain(`enter a ${field}`);
    }
  );

  it.each([
    ["day and month", "", "", "2021"],
    ["day and year", "", "03", ""],
    ["month and year", "01", "", ""],
  ])(
    "returns an error message if two fields are missing values",
    (fields, day, month, year) => {
      const errors = validateDate({
        day,
        month,
        year,
      });

      expect(errors).toContain(`enter a ${fields}`);
    }
  );

  it.each([
    ["day", "32", "03"],
    ["day", "29", "02"], //2021 is not a leap year
    ["day", "00", "12"],
    ["month", "01", "13"],
    ["month", "01", "00"],
  ])(
    "returns an error if value for %s is out of bounds",
    (field, day, month) => {
      const errors = validateDate({
        day,
        month,
        year: "2021",
      });

      expect(errors).toContain(field + ", which is invalid");
    }
  );

  it("returns an error if the year is out of bounds", () => {
    const errors = validateDate({
      day: "01",
      month: "01",
      year: "10000",
    });

    expect(errors).toContain(`year, which is invalid`);
  });

  it.each([".1", "1.", "1e1", "-1"])(
    "rejects 'e' and floating point numbers: %s",
    (year) => {
      const errors = validateDate({
        day: "01",
        month: "01",
        year,
      });

      expect(errors).toContain("is not a valid value");
    }
  );
});
