import React from 'react';
import App from '../App';
import { Router } from 'react-router-dom'
import { render, waitFor, waitForElementToBeRemoved } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { createMemoryHistory } from 'history'
import { rest } from "msw";
import { setupServer } from "msw/node";

import data from '../../data/systems_dummy.json';

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(data)
    );
  }),
  rest.get("/config/keycloak", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json({
        host: "http://localhost:8081/auth/",
        realm: "local-realm",
        clientId: "system-register",
      })
    );
  })
);

describe("<App />", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('renders system from the API', async () => {
    const history = createMemoryHistory()
    const { findByText, getByTestId } = render(
      <Router history={history}>
        <App />
      </Router>
    )
    await waitForElementToBeRemoved(getByTestId(/auth-initialising-msg/))

    expect(await findByText('System Register', { selector: '.systemCard *' })).toBeInTheDocument();
  })

  it("re-renders from api when a system calls back to notify its been updated", () => {
    //TODO: write this test (JB)
  })

  describe("routing", () => {
    it('navigates to the risk dashboard', async () => {
      const history = createMemoryHistory();
      const { getByText, getByTestId } = render(
        <Router history={history}>
          <App />
        </Router>
      )
      await waitForElementToBeRemoved(getByTestId(/auth-initialising-msg/))
      userEvent.click(getByText(/Risk Dashboard/i))

      await waitFor(() => {
        const dashboard = getByText('Aggregated risk by portfolio')
        expect(dashboard).toBeInTheDocument()
      })
    });

    it("navigates to the About page", async () => {
      const history = createMemoryHistory();
      const { findByRole, findByText, getByTestId } = render(
        <Router history={history}>
          <App />
        </Router>
      );
      await waitForElementToBeRemoved(getByTestId("auth-initialising-msg"))
      const aboutLink = await findByText("About", { selector: "nav a" });

      userEvent.click(aboutLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent("About the System Register");
    });

    it("navigates to the Contact page", async () => {
      const history = createMemoryHistory();
      const { findByRole, findByText, getByTestId } = render(
        <Router history={history}>
          <App />
        </Router>
      );
      await waitForElementToBeRemoved(getByTestId("auth-initialising-msg"))
      const contactLink = await findByText("Contact", { selector: "nav a" });

      userEvent.click(contactLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent("Get in touch");
    });
  })
})
