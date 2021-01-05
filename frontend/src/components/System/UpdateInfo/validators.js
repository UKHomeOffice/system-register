import {containsForbiddenCharacters} from "../../../utilities/sharedValidators";

function isTooShort(value) {
  return value?.trim().length <= 1;
}

function validateInfo(value) {
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "The system name must not be incomplete.";
  }
}

export default validateInfo;
export { containsForbiddenCharacters, isTooShort };
