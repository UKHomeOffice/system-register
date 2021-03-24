import { isTooShort } from "../../../utilities/sharedValidators";

function validateAdditionalInformation(value) {
  if (isTooShort(value)) {
    return "You must enter additional information or leave blank if you do not know it.";
  }
}

export { validateAdditionalInformation };
