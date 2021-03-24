import { validateAdditionalInformation } from "../validators";

describe("validate sunset additional information", () => {
  it.each(["x", " x", "x "])(
    "returns an error message if the additional information is too short: %p",
    (value) => {
      const result = validateAdditionalInformation(value);

      expect(result).toContain("must enter additional information");
    }
  );

  it.each(["info", ""])("returns undefined for valid values: %p", (value) => {
    const result = validateAdditionalInformation(value);

    expect(result).toBeUndefined();
  });
});
