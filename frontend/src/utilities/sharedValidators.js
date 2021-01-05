function containsForbiddenCharacters(value) {
  return /[!£$%^*|<>~"=]/.test(value);
}

export {containsForbiddenCharacters}