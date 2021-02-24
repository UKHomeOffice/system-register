import { validateName, validateDescription } from "../validators";

describe("AddSystem validators", () => {
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

    it("returns undefined for valid values", () => {
      const result = validateName("valid system name");

      expect(result).toBeUndefined();
    });
  });

  describe("validate system description", () => {
    it.each(["a", " a", "a "])(
      "returns an error message if description is too short: %p",
      (value) => {
        const result = validateDescription(value);

        expect(result).toContain("must enter a description");
      }
    );

    it.each(["description", ""])(
      "returns undefined for valid values: %p",
      (value) => {
        const result = validateDescription(value);

        expect(result).toBeUndefined();
      }
    );
  });
});
