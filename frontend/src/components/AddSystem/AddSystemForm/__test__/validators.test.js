import {
  validateName,
  validateDescription,
  validateAliases,
} from "../validators";

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

  describe("validates system aliases", () => {
    it("returns undefined for valid values", () => {
      const values = { aliases: ["alias 1", "alias 2", ""] };
      const result = validateAliases(values);

      expect(result).toStrictEqual({});
    });

    it("returns an error message if there is a duplicate value", () => {
      const values = {
        aliases: ["duplicate alias", "unique alias", "duplicate alias"],
      };
      const result = validateAliases(values);

      expect(result).toStrictEqual({
        aliases: [
          "You have entered duplicate aliases. Please remove or amend the duplicate.",
          undefined,
          "You have entered duplicate aliases. Please remove or amend the duplicate.",
        ],
      });
    });

    it("returns an error message if alias contains forbidden characters", () => {
      const result = validateAliases({ aliases: ["ca$h"] });

      expect(result).toStrictEqual({
        aliases: expect.arrayContaining([
          expect.stringContaining(
            "must not use the following special characters"
          ),
        ]),
      });
    });

    it("returns an error message if alias is too short", () => {
      const result = validateAliases({ aliases: ["x"] });

      expect(result).toStrictEqual({
        aliases: expect.arrayContaining([
          expect.stringContaining("You must enter a complete system alias"),
        ]),
      });
    });

    it("prefers to reject invalid values over duplicates", () => {
      const result = validateAliases({ aliases: ["x", "x"] });

      expect(result).toStrictEqual({
        aliases: expect.arrayContaining([
          expect.stringContaining("You must enter a complete system alias"),
          expect.stringContaining("You must enter a complete system alias"),
        ]),
      });
    });
  });
});
