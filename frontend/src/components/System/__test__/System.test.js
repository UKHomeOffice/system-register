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
}));
jest.mock("@react-keycloak/web", () => ({
  useKeycloak: jest.fn(),
}));

const test_system = {
  name: "Test System",
  last_updated: {},
  risks: [],
  aliases: [],
};

describe('<System />', () => {
  beforeEach(() => {
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
    });
  });
});

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
