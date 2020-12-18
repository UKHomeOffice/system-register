class ValidationError extends Error {
  constructor(errors) {
    super("Validation failed");
    this._errors = errors;
  }

  get errors() {
    return this._errors;
  }
}

export default ValidationError;
