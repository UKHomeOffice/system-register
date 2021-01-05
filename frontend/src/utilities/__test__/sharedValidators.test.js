import {containsForbiddenCharacters} from "../sharedValidators";

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