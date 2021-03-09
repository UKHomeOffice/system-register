import toSentenceCase from "../toSentenceCase";

describe("toSentenceCase", () => {
  it.each([
    [" many many words ", " Many many words "],
    ["Capitalised", "Capitalised"],
    [" kinda_ snake_Case_", " Kinda snake case "],
    ["i am a sentence", "I am a sentence"],
  ])("capitalises the first word in a string: %p", (value, expected) => {
    const actual = toSentenceCase(value);

    expect(actual).toStrictEqual(expected);
  });
});
