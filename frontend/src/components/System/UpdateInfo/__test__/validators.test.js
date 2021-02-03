import {validateName, validateDescription, validateAliases} from "../validators";

describe("UpdateInfo validators", () => {
  describe("validate system name", () => {
    it("returns an error message if name contains forbidden character", () => {
      const result = validateName("ca$h");

      expect(result).toContain("must not use the following special characters");
    });

    it("returns an error message if system name is too short", () => {
      const result = validateName("n");

      expect(result).toContain("You must enter a complete system name");
    });

    it("returns an error message if system name is empty", () => {
      const result = validateName("");

      expect(result).toContain("You must enter a system name");
    });

    it("returns error if system name already exists", () => {
      const result = validateName("I exisT", () => true);

      expect(result).toContain("There is already a system called");
    });

    it("does not return error if system name already exists, but system name has not been changed", () => {
      const result = validateName("I exisT", () => true, "I exisT");

      expect(result).toBeUndefined();
    });

    it("returns undefined for valid values", () => {
      const result = validateName("valid system name", () => false);

      expect(result).toBeUndefined();
    });
  });

  describe("validate system description", () => {
    it.each(["x", " x", "x "])("returns an error message if description is too short: %p", (value) => {
      const result = validateDescription(value);

      expect(result).toContain("must enter a description");
    });

    it.each(["description", ""])
    ("returns undefined for valid values: %p", (value) => {
      const result = validateDescription(value);

      expect(result).toBeUndefined();
    });
  });

  describe("validates system aliases", () => {
    it("returns undefined for valid values", () => {
      const values = {aliases: ["alias 1", "alias 2"]}
      const result = validateAliases(values);

      expect(result).toStrictEqual({});
    });

    it("returns an error message if there is a duplicate value", () => {
      const values = {aliases: ["duplicate alias", "unique alias", "duplicate alias"]}
      const result = validateAliases(values);

      expect(result).toStrictEqual({
        aliases: [
          "You have entered duplicate aliases. Please remove or amend the duplicate.",
          undefined,
          "You have entered duplicate aliases. Please remove or amend the duplicate."
        ]
      });
    })

    it("ignores empty strings", () => {
      const values = {aliases: ["", ""]};
      const result = validateAliases(values);

      expect(result).toStrictEqual({})
    })
  })
});
