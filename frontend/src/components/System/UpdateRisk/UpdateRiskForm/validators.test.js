import { validateRationale } from "./validators";

describe("validateRationale", () => {
  it("returns an error message if the rationale is too short", () => {
    const result = validateRationale("r");

    expect(result).toContain(
      "You must enter a rationale for your chosen risk level."
    );
  });

  it.each(["", " "])(
    "returns an error message if the rationale is blank: %p",
    (value) => {
      const result = validateRationale(value);

      expect(result).toContain(
        "You must enter a rationale for your chosen risk level."
      );
    }
  );

  it("returns undefined for valid values", () => {
    const result = validateRationale("valid rationale");

    expect(result).toBeUndefined();
  });
});
