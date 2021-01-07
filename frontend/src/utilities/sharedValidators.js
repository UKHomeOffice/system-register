function containsForbiddenCharacters(value) {
  return /[!£$%^*|<>~"=]/.test(value);
}

function isEmpty(value) {
  return value?.trim().length === 0;
}

function isTooShort(value) {
  return value.trim().length === 1;
}

export { containsForbiddenCharacters, isEmpty, isTooShort };
