import { containsForbiddenCharacters } from "../../../utilities/sharedValidators";

function isTooShort(value) {
  return value?.trim().length <= 1;
}


function validateInfo(value, duplicateCheckCallback) { //todo Team review
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "The system name must not be incomplete.";
  } else if (duplicateCheckCallback(value)) {
    return `There is already a system called ${value}`
  }
}

export default validateInfo;
export { containsForbiddenCharacters, isTooShort };
