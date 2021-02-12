const getDisplayName = (component) =>
  component.displayName || component.name || "Component";

export default getDisplayName;
