import React from "react";
import SystemRegister from "../SystemRegister";
import { BrowserRouter, Router } from "react-router-dom";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { createMemoryHistory } from "history";
import { rest } from "msw";
import { setupServer } from "msw/node";

import data from "../../../data/systems_dummy.json";
import SystemNotFoundException from "../../../services/systemNotFoundException";
import { useKeycloak } from "@react-keycloak/web";

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(ctx.status(200), ctx.json(data));
  }),
  rest.get("/api/system/9999", () => {
    throw new SystemNotFoundException();
  })
);

jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

describe("<SystemRegister />", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  beforeEach(() => {
    useKeycloak.mockReturnValue({
      keycloak: {
        authenticated: true,
        tokenParsed: {},
      },
      initialized: true,
    });
  });

  it.each(["/", "/error"])(
    "contains a skip link at the start of the document",
    (path) => {
      render(
        <Router history={createMemoryHistory({ initialEntries: [path] })}>
          <SystemRegister />
        </Router>
      );

      const skipLink = screen.getByRole("link", {
        name: "Skip to main content",
      });
      expect(skipLink).toBeInTheDocument();
    }
  );

  it("renders system from the API", async () => {
    const { findByText } = render(
      <BrowserRouter>
        <SystemRegister />
      </BrowserRouter>
    );

    expect(
      await findByText("System Register", { selector: ".systemCard *" })
    ).toBeInTheDocument();
  });

  it("re-renders from api when a system calls back to notify its been updated", () => {
    //TODO: write this test (JB)
  });

  describe("routing", () => {
    it("shows <PageNotFound /> if route not valid", async () => {
      //TODO help writing this test
      const history = createMemoryHistory({ initialEntries: ["/nowhere"] });
      const { getByText } = render(
        <Router history={history}>
          <SystemRegister />
        </Router>
      );
      await waitFor(() => {
        const dashboard = getByText("Page not found");
        expect(dashboard).toBeInTheDocument();
      });
    });

    it("shows <PageNotFound /> if system does not exist", async () => {
      //TODO help writing this test
      const history = createMemoryHistory({ initialEntries: ["/system/9999"] });
      const { getByText } = render(
        <Router history={history}>
          <SystemRegister />
        </Router>
      );
      await waitFor(() => {
        try {
          const dashboard = getByText("Page not found");
          expect(dashboard).toBeInTheDocument();
        } catch (e) {
          // ignored
        }
      });
    });

    it("navigates to add-system page", async () => {
      const { getByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );

      userEvent.click(getByText(/Add a system/i));

      await waitFor(() => {
        const dashboard = getByText("Add a system to the register");
        expect(dashboard).toBeInTheDocument();
      });
    });

    it("navigates to the risk dashboard", async () => {
      const { getByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );
      userEvent.click(getByText(/Risk Dashboard/i));

      await waitFor(() => {
        const dashboard = getByText("Aggregated risk by portfolio");
        expect(dashboard).toBeInTheDocument();
      });
    });

    it("navigates to the About page", async () => {
      const { findByRole, findByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );
      const aboutLink = await findByText("About", { selector: "nav a" });

      userEvent.click(aboutLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent(
        "About the System Register"
      );
    });

    it("navigates to the Contact page", async () => {
      const { findByRole, findByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );
      const contactLink = await findByText("Contact", { selector: "nav a" });

      userEvent.click(contactLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent(
        "Get in touch"
      );
    });

    describe("when authorized", () => {
      it("navigates to the Add-System page", async () => {
        //ToDo: amend test to click on add system link in SR-315
        const history = createMemoryHistory({
          initialEntries: ["/add-system"],
        });
        const { findByRole } = render(
          <Router history={history}>
            <SystemRegister />
          </Router>
        );

        expect(await findByRole("heading", { level: 1 })).toHaveTextContent(
          "Add a system to the register"
        );
      });
    });
  });
});
