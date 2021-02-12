import getDisplayName from "../getDisplayName";

describe("getDisplayName", () => {
  test("returns component name", () => {
    const TheComponent = () => null;

    const name = getDisplayName(TheComponent);

    expect(name).toBe("TheComponent");
  });

  test("returns display name of component", () => {
    const AnotherComponent = () => null;
    AnotherComponent.displayName = "WithHoc(AnotherComponent)";

    const name = getDisplayName(AnotherComponent);

    expect(name).toBe("WithHoc(AnotherComponent)");
  });

  test("returns fallback for DOM element", () => {
    const HtmlComponent = "div";

    const name = getDisplayName(HtmlComponent);

    expect(name).toBe("Component");
  });
});
