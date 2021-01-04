import React from 'react';
import user from "@testing-library/user-event";
import { useKeycloak } from "@react-keycloak/web";
import { render, screen, waitFor } from '@testing-library/react';
import { createMemoryHistory } from "history";
import { Route, Router } from 'react-router-dom';

import System from '../System';
import api from '../../../services/api';

jest.mock('../../../services/api', () => ({
  getSystem: jest.fn(),
  updateProductOwner: jest.fn(),
  updateCriticality: jest.fn(),
}));
jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

const test_system = {
  name: "Test System",
  criticality: "unknown",
  last_updated: {},
  risks: [],
  aliases: [],
};

describe('<System />', () => {
  beforeEach(() => {
    jest.resetAllMocks();
    api.getSystem.mockResolvedValue(test_system);
  });

  it('renders system view', async () => {
    renderWithHistory("1");

    const element = await screen.findByText('Test System');

    expect(element).toBeInTheDocument();
  });

  describe("when authorized", () => {
    beforeEach(() => {
      useKeycloak.mockReturnValue({
        keycloak: {
          authenticated: true,
        },
        initialized: true,
      });
    });

    it("shows an edit view for info", async () => {
      renderWithHistory("1/update-info");

      const element = await screen.findByText('Test System');

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for about", async () => {
      renderWithHistory("1/update-about");

      const element = await screen.findByText('Test System');

      expect(element).toBeInTheDocument();
    });

    it("shows an edit view for contacts", async () => {
      renderWithHistory("1/update-contacts");

      const element = await screen.findByText('Test System');

      expect(element).toBeInTheDocument();
    });

    describe("editing contacts", () => {
      it("returns to the system view after a successful update", async () => {
        api.updateProductOwner.mockResolvedValue({ ...test_system, product_owner: "updated owner" });
        const history = createMemoryHistory({
          initialEntries: ["/system/123/update-contacts"],
          initialIndex: 0,
        });
        renderWithHistory(null, { history });
        const productOwnerField = await screen.findByLabelText(/product owner/i);
        const saveButton = screen.getByRole("button", { name: /save/i });

        // noinspection ES6MissingAwait: there is no typing delay
        user.type(productOwnerField, "updated owner");
        user.click(saveButton);

        await waitFor(() => {
          expect(api.updateProductOwner).toBeCalledWith(
            "123",
            expect.objectContaining({
              productOwner: "updated owner",
            })
          );

          expect(history).toHaveProperty("index", 1);
          expect(history).toHaveProperty(
            "location.pathname",
            "/system/123"
          );
        });
      });

      it("returns to system view on cancel", async () => {
        await checkCancelButton("update-contacts");
      });

      it("does not send a request if field values are unchanged", async () => {
        const history = createMemoryHistory({
          initialEntries: ["/system/123/update-contacts"],
          initialIndex: 0,
        });
        renderWithHistory(null, { history });
        await screen.findByText("Test System");
        const saveButton = await screen.findByRole("button", { name: /save/i });

        user.click(saveButton);

        await waitFor(() => {
          expect(history).toHaveProperty("index", 1);
          expect(history).toHaveProperty(
            "location.pathname",
            "/system/123"
          );
        });
        expect(api.updateProductOwner).not.toBeCalled();
      });
    });

    describe("editing system info", () => {
      describe("system name", () => {
        it("returns to system view on cancel", async () => {
          await checkCancelButton("update-info");
        });

        it("returns to the system view after a successful update", async () => {
          api.updateSystemName.mockResolvedValue({ ...test_system, name: "updated system name" });
          const history = createMemoryHistory({
            initialEntries: ["/system/123/update-info"],
            initialIndex: 0,
          });
          renderWithHistory(null, { history });
          const systemNameField = await screen.findByLabelText(/system owner/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.type(systemNameField, "updated system name");
          user.click(saveButton);

          await waitFor(() => {
            expect(api.updateSystemName).toBeCalledWith(
              "123",
              expect.objectContaining({
                name: "updated system name",
              })
            );

            expect(history).toHaveProperty("index", 1);
            expect(history).toHaveProperty(
              "location.pathname",
              "/system/123"
            );
          });
        });
      })
    })

    describe("editing about", () => {
      describe("criticality", () => {
        it("returns to system view on cancel", async () => {
          await checkCancelButton("update-about");
        });

        it("returns to the system view after a successful update", async () => {
          api.updateCriticality.mockResolvedValue({ ...test_system, criticality: "high" });
          const history = createMemoryHistory({
            initialEntries: ["/system/123/update-about"],
            initialIndex: 0,
          });
          renderWithHistory(null, { history });
          const radioButton = await screen.findByLabelText(/high/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await waitFor(() => {
            expect(api.updateCriticality).toBeCalledWith(
              "123",
              expect.objectContaining({
                criticality: "high",
              })
            );

            expect(history).toHaveProperty("index", 1);
            expect(history).toHaveProperty(
              "location.pathname",
              "/system/123"
            );
          });
        });

        it("api is not called if criticality is unchanged", async () => {
          const history = createMemoryHistory({
            initialEntries: ["/system/123/update-about"],
            initialIndex: 0,
          });
          renderWithHistory(null, { history });
          const radioButton = await screen.findByLabelText(/unknown/i);
          const saveButton = screen.getByRole("button", { name: /save/i });

          // noinspection ES6MissingAwait: there is no typing delay
          user.click(radioButton);
          user.click(saveButton);

          await waitFor(() => {
            expect(history).toHaveProperty("index", 1);
            expect(history).toHaveProperty(
              "location.pathname",
              "/system/123"
            );
          });
          expect(api.updateCriticality).not.toBeCalled();
        });
      })
    });
  });
});

async function checkCancelButton(path) {
  const history = createMemoryHistory({
    initialEntries: [`/system/123/${path}`],
    initialIndex: 0,
  });
  renderWithHistory(null, { history });
  const cancelButton = await screen.findByRole("button", { name: /cancel/i });

  user.click(cancelButton);

  expect(history).toHaveProperty("index", 1);
  expect(history).toHaveProperty(
    "location.pathname",
    "/system/123"
  );

  expect(api.updateProductOwner).not.toBeCalled();
  expect(api.updateCriticality).not.toBeCalled(); //todo refactor to loop through all update api methods
}

function renderWithHistory(path, context = {}) {
  const { history } = {
    history: createMemoryHistory({ initialEntries: [`/system/${path}`] }),
    ...context,
  };

  return render(
    <Router history={history}>
      <Route path='/system/:id' component={System} />
    </Router>
  );
}
