import { validateName } from "../validators";

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

    it("returns error if system name already exists", () => {
      const result = validateName("I exisT", () => true);

      expect(result).toContain("There is already a system called");
    });
    it("returns undefined for valid values", () => {
      const result = validateName("valid system name", () => false);

      expect(result).toBeUndefined();
    });
  });
});
