import { isEmpty, isTooShort } from "../../../../utilities/sharedValidators";

function validateRationale(rationale) {
  if (isEmpty(rationale) || isTooShort(rationale)) {
    return "You must enter a rationale for your chosen risk level.";
  }
}

export { validateRationale };
