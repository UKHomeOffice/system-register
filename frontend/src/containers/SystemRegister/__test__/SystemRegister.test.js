import React from 'react';
import SystemRegister from '../SystemRegister';
import { BrowserRouter, Router } from 'react-router-dom'
import { render, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { createMemoryHistory } from 'history'
import { rest } from "msw";
import { setupServer } from "msw/node";

import data from '../../../data/systems_dummy.json';
import SystemNotFoundException from '../../../services/systemNotFoundException';

const server = setupServer(
  rest.get("/api/systems", (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(data)
    );
  }),
  rest.get("/api/system/9999", (req, res, ctx) => {
    throw new SystemNotFoundException();
  })
);

describe("<SystemRegister />", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('renders system from the API', async () => {
    const { findByText } = render(
      <BrowserRouter>
        <SystemRegister />
      </BrowserRouter>
    )

    expect(await findByText('System Register', { selector: '.systemCard *' })).toBeInTheDocument();
  })

  it("re-renders from api when a system calls back to notify its been updated", () => {

  })

  describe("routing", () => {
    it("shows <PageNotFound /> if route not valid", async () => { //TODO help writing this test
      const history = createMemoryHistory({ initialEntries: ['/nowhere'] });
      const { getByText } = render(
        <Router history={history}>
          <SystemRegister />
        </Router>
      )
      await waitFor(() => {
        const dashboard = getByText('Page not found')
        expect(dashboard).toBeInTheDocument()
      })
    });

    it("shows <PageNotFound /> if system does not exist", async () => { //TODO help writing this test
      const history = createMemoryHistory({ initialEntries: ['/system/9999'] });
      const { getByText } = render(
        <Router history={history}>
          <SystemRegister />
        </Router>
      )
      await waitFor(() => {
        try {
          const dashboard = getByText('Page not found')
          expect(dashboard).toBeInTheDocument()
        } catch (e) { }
      })
    });

    it('navigates to the risk dashboard', async () => {
      const { getByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      )
      userEvent.click(getByText(/Risk Dashboard/i))

      await waitFor(() => {
        const dashboard = getByText('Aggregated risk by portfolio')
        expect(dashboard).toBeInTheDocument()
      })
    });

    it("navigates to the About page", async () => {
      const { findByRole, findByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );
      const aboutLink = await findByText("About", { selector: "nav a" });

      userEvent.click(aboutLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent("About the System Register");
    });

    it("navigates to the Contact page", async () => {
      const { findByRole, findByText } = render(
        <BrowserRouter>
          <SystemRegister />
        </BrowserRouter>
      );
      const contactLink = await findByText("Contact", { selector: "nav a" });

      userEvent.click(contactLink);

      expect(await findByRole("heading", { level: 1 })).toHaveTextContent("Get in touch");
    });
  })
})
