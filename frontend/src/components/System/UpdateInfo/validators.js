import { every, indexOf, isUndefined, lastIndexOf, map } from "lodash-es";

import { containsForbiddenCharacters, isEmpty, isTooShort } from "../../../utilities/sharedValidators";

function validateName(value, duplicateCheckCallback, initialValue) { //todo Team review
  if (initialValue === value) return;
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "You must enter a complete system name.";
  } else if (isEmpty(value)) {
    return "You must enter a system name.";
  } else if (duplicateCheckCallback(value)) {
    return `There is already a system called ${value}.`;
  }
}

function validateDescription(value) {
  if (isTooShort(value)) {
   return "You must enter a description or leave blank if you do not know it.";
  }
}

function containsDuplicates(value, index, aliases) {
  return indexOf(aliases, value) !== index || lastIndexOf(aliases, value) !== index;
}

function validateAliases({ aliases }) {
  const aliasErrors = map(
    aliases,
    (value, index) => {
      if (containsForbiddenCharacters(value)) {
        return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
      } else if (isTooShort(value)) {
        return "You must enter a complete system alias.";
      } else if (!isEmpty(value) && containsDuplicates(value, index, aliases)) {
        return "You have entered duplicate aliases. Please remove or amend the duplicate.";
      }
    }
  );

  return every(aliasErrors, isUndefined)
    ? {}
    : { aliases: aliasErrors };
}

export { validateName, validateDescription, validateAliases };
