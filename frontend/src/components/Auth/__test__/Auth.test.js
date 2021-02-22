import React from "react";
import { render, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import userEvent from "@testing-library/user-event";
import Auth from "../Auth";
import { useKeycloak } from "@react-keycloak/web";

jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

describe("<Auth />", () => {
  const login = jest.fn();
  const logout = jest.fn();

  beforeEach(() => {
    jest.resetAllMocks();

    useKeycloak.mockReturnValue({
      keycloak: {
        authenticated: true,
        login,
        logout,
        token: "bearer-token",
        tokenParsed: { preferred_username: "corey" },
      },
    });
    login.mockResolvedValue(null);
    logout.mockResolvedValue(null);
  });

  describe("when signed out", () => {
    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {
          authenticated: false,
          login,
        },
      });
    });

    it("renders without crashing", () => {
      const { getByText } = render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );
      const element = getByText("Sign in");
      expect(element).toBeInTheDocument();
    });

    it("on click, asks keycloak to authenticate", () => {
      const { getByText } = render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );
      const element = getByText("Sign in");
      userEvent.click(element);

      expect(login).toBeCalled();
    });
  });

  describe("when signed in", () => {
    it("has a sign out button", () => {
      const { getByRole } = render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );
      const element = getByRole("button", { name: "Sign out" });

      expect(element).toBeInTheDocument();
    });

    it("welcomes the user", () => {
      const { getByText } = render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );
      const message = getByText("Welcome corey");

      expect(message).toBeInTheDocument();
    });

    it("stores the bearer token in local storage", () => {
      render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );

      const token = localStorage.getItem("bearer-token");

      expect(token).toBe("bearer-token");
    });

    it("clears the bearer token on signing out", async () => {
      const { getByRole } = render(
        <MemoryRouter>
          <Auth />
        </MemoryRouter>
      );
      const signOutButton = getByRole("button", { name: "Sign out" });

      userEvent.click(signOutButton);

      await waitFor(() => {
        const token = localStorage.getItem("bearer-token");
        expect(token).toBeNull();
      });
    });
  });
});
