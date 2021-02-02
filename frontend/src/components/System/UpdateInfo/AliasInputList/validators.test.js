import { validateAlias } from "./validators";

describe("ArrayInputList validators", () => {
  describe("validate alias", () => {
    it("returns an error message if alias contains forbidden character", () => {
      const result = validateAlias("ca$h");

      expect(result).toContain("must not use the following special characters");
    });

    it("returns an error message if alias is too short", () => {
      const result = validateAlias("x");

      expect(result).toContain("You must enter a complete system alias");
    });

    it("returns undefined for valid values", () => {
      const result = validateAlias("valid system alias");

      expect(result).toBeUndefined();
    });
  });
});
