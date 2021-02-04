import { containsForbiddenCharacters, isTooShort } from "../../../../utilities/sharedValidators";

function validateAlias(value) {
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "You must enter a complete system alias.";
  }
}

export { validateAlias };
