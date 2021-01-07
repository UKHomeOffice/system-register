import { containsForbiddenCharacters, isEmpty, isTooShort } from "../sharedValidators";

describe("containsForbiddenCharacters", () => {
  it.each(["!", "£", "$", "%", "^", "*", "|", "<", ">", "~", "\""])
  ("rejects disallowed punctuation: %s", (character) => {
    const isInvalid = containsForbiddenCharacters("name" + character);

    expect(isInvalid).toBeTruthy();
  });

  it.each(["?", "'", "-", "a", "0", "Z"])
  ("permits other characters: %s", (character) => {
    const isInvalid = containsForbiddenCharacters(character + "name");

    expect(isInvalid).toBeFalsy();
  });
});

describe("isEmpty", () => {
  it.each(["", " ", "\t"])
  ("rejects empty values", (value) => {
    const isInvalid = isEmpty(value);

    expect(isInvalid).toBeTruthy();
  });

  it.each(["x", " x", "x "])
  ("accepts non-empty values", (value) => {
    const isInvalid = isEmpty(value);

    expect(isInvalid).toBeFalsy();
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
