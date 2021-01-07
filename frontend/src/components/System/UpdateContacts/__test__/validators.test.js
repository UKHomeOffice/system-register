import validateContact from "../validators";

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
});
