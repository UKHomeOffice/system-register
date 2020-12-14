import React from "react";
import { MemoryRouter } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";
import { useKeycloak } from "@react-keycloak/web";

import SecureRoute from "../SecureRoute";

jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

describe("SecureRoute", () => {
  beforeEach(() => {
    jest.resetAllMocks();
  });

  describe("when authenticated", () => {
    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {
          authenticated: true,
        },
        initialized: true,
      });
    });

    it("renders the route's descendent components", () => {
      renderWithHistory(
        <SecureRoute path="/">
          <p>restricted descendent content</p>
        </SecureRoute>
      );

      expect(screen.getByText("restricted descendent content")).toBeInTheDocument();
    });

    it("renders the route's component", () => {
      const ExampleComponent = () => <p>restricted component content</p>;
      renderWithHistory(<SecureRoute path="/" component={ExampleComponent} />);

      expect(screen.getByText("restricted component content")).toBeInTheDocument();
    });

    it("renders the route's render prop", () => {
      renderWithHistory(
        <SecureRoute path="/" render={
          () => <p>restricted render content</p>
        } />
      );

      expect(screen.getByText("restricted render content")).toBeInTheDocument();
    });
  });

  describe("when unauthenticated", () => {
    const login = jest.fn();

    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {
          authenticated: false,
          login,
        },
        initialized: true,
      });
    });

    it("redirects to the Keycloak login page", async () => {
      renderWithHistory(<SecureRoute path="/" component={null} />, { initialEntries: ["/initial/path"] });

      await waitFor(() => {
        expect(login).toBeCalled();
      });
    });
  });

  describe("when Keycloak is not yet initialised", () => {
    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {},
        initialized: false,
      });
    });

    it("displays a placeholder", () => {
      renderWithHistory(<SecureRoute path="/" component={null} />);

      expect(screen.getByText("Loading authentication settings...")).toBeInTheDocument();
    });
  });
});

function renderWithHistory(component, context = {}, options) {
  const props = {
    initialEntries: ["/"],
    initialIndex: 0,
    ...context,
  };

  return render(<MemoryRouter {...props}>{component}</MemoryRouter>, options);
}
