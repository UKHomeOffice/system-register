function toSentenceCase(value) {
  return value
    .replace(/[_\s]+/g, " ")
    .toLowerCase()
    .replace(
      /^(\s*)(\S)/,
      (_, leadingSpaces, initial) => leadingSpaces + initial.toUpperCase()
    );
}

export default toSentenceCase;
