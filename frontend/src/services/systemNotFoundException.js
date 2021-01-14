class SystemNotFoundException extends Error {
    constructor() {
      super("System not found");
    }
  }
  
  export default SystemNotFoundException;
  