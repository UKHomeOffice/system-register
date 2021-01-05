import validateInfo, { containsForbiddenCharacters, isTooShort } from "../validators";

describe("UpdateInfo validators", () => {
  describe("validate system name", () => {
    it("returns an error message if name contains forbidden character", () => {
      const result = validateInfo("ca$h");

      expect(result).toContain("must not use the following special characters");
    });

    it("returns an error message if system name is too short", () => {
      const result = validateInfo("n");

      expect(result).toContain("must not be incomplete");
    });

    it("returns an error message if system name is empty", () => {
      const result = validateInfo("");

      expect(result).toContain("must not be incomplete");
    })

    it("returns undefined for valid values", () => {
      const result = validateInfo("valid system name", (value) => false);

      expect(result).toBeUndefined();
    });
  });

  describe("isTooShort", () => {
    it.each(["x", " x", "x "])
    ("rejects single character values", (value) => {
      const isInvalid = isTooShort(value);

      expect(isInvalid).toBeTruthy();
    });

    it.each(["", " "])
    ("does not permit empty values", (value) => {
      const isInvalid = isTooShort(value);

      expect(isInvalid).toBeTruthy();
    });

    it.each(["ab", "abc"])
    ("permits values longer than a single character", (value) => {
      const isInvalid = isTooShort(value);

      expect(isInvalid).toBeFalsy();
    });
  });

});
