import {
  containsForbiddenCharacters,
  isEmpty,
  isTooShort,
} from "../../../utilities/sharedValidators";

function validateName(value) {
  if (containsForbiddenCharacters(value)) {
    return 'You must not use the following special characters: ! £ $ % ^ * | < > ~ " =';
  } else if (isTooShort(value)) {
    return "You must enter a complete system name.";
  } else if (isEmpty(value)) {
    return "You must enter a system name.";
  }
}

function validateDescription(value) {
  if (isTooShort(value)) {
    return "You must enter a description or leave blank if you do not know it.";
  }
}

export { validateName, validateDescription };
