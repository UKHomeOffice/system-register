import { containsForbiddenCharacters, isTooShort } from "../../../utilities/sharedValidators";

function validateName(value) {
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "Please enter a full name or leave blank if you do not know it.";
  }
}

export default validateName;
