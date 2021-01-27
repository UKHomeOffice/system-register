import validateName from "../validators";

describe("UpdateAbout validators", () => {
  describe("validateName", () => {
    it("returns an error message if name contains forbidden character", () => {
      const result = validateName("ca$h");

      expect(result).toContain("must not use the following special characters");
    });

    it("returns an error message if name is too short", () => {
      const result = validateName("n");

      expect(result).toContain("a full name or leave blank");
    });

    it.each(["name", ""])
    ("returns undefined for valid values: %p", (name) => {
      const result = validateName(name);

      expect(result).toBeUndefined();
    });
  });
});
