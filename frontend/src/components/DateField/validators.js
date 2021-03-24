import { DateTime } from "luxon";
import toSentenceCase from "../../utilities/toSentenceCase";

const parenthesisedRemarks = /\s*\([^)]*\)\s*/g;
const validNumber = /^\s*\d+\s*$/;

function validateDate({ day, month, year }) {
  const emptyFields = [];

  if (day.trim() === "") {
    emptyFields.push("day");
  }
  if (month.trim() === "") {
    emptyFields.push("month");
  }
  if (year.trim() === "") {
    emptyFields.push("year");
  }

  if (emptyFields.length === 1 || emptyFields.length === 2) {
    return `You must enter a ${emptyFields.join(" and ")}`;
  } else if (emptyFields.length === 3) {
    return;
  }

  if (!validNumber.test(day)) {
    return "Day is not a valid value";
  } else if (!validNumber.test(month)) {
    return "Month is not a valid value";
  } else if (!validNumber.test(year)) {
    return "Year is not a valid value";
  }

  const date = DateTime.local(+year, +month, +day);

  if (date.year >= 10000 || date.year < 0) {
    return `You specified ${date.year} as a year, which is invalid`;
  }

  if (!date.isValid) {
    return toSentenceCase(
      date.invalidExplanation.replace(parenthesisedRemarks, " ")
    );
  }
}

export { validateDate };
