import validateContact, { containsForbiddenCharacters, isTooShort } from "../validators";

describe("UpdateContacts validators", () => {
  describe("validateContact", () => {
    it("returns an error message if contact contains forbidden character", () => {
      const result = validateContact("ca$h");

      expect(result).toContain("must not use the following special characters");
    });

    it("returns an error message if contact is too short", () => {
      const result = validateContact("n");

      expect(result).toContain("must not be incomplete");
    });

    it("returns undefined for valid values", () => {
      const result = validateContact("owner");

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
    ("permits empty values", (value) => {
      const isInvalid = isTooShort(value);

      expect(isInvalid).toBeFalsy();
    });

    it.each(["ab", "abc"])
    ("permits values longer than a single character", (value) => {
      const isInvalid = isTooShort(value);

      expect(isInvalid).toBeFalsy();
    });
  });
});
