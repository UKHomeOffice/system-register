import { containsForbiddenCharacters, isTooShort } from "../../../utilities/sharedValidators";

function validateContact(value) {
  if (containsForbiddenCharacters(value)) {
    return "You must not use the following special characters: ! £ $ % ^ * | < > ~ \" =";
  } else if (isTooShort(value)) {
    return "The contact name must not be incomplete. Please enter a full contact name or leave blank if you do not know it.";
  }
}

export default validateContact;
