import React from 'react';
import App from '../App';
import { Router } from 'react-router-dom'
import { render, waitFor } from '@testing-library/react'
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
  })
);

describe("<App />", () => {
  beforeAll(() => server.listen({ onUnhandledRequest: "error" }));

  afterAll(() => server.close());

  afterEach(() => server.resetHandlers());

  it('renders system from the API', async () => {
    const history = createMemoryHistory()
    const { findByText } = render(
      <Router history={history}>
        <App />
      </Router>
    )

    expect(await findByText('System Register', { selector: '.systemCard *' })).toBeInTheDocument();
  })

  describe("routing", () => {
    it('navigates to the risk dashboard', async () => {
      const history = createMemoryHistory();
      const { getByText } = render(
        <Router history={history}>
          <App />
        </Router>
      )

      userEvent.click(getByText(/Risk Dashboard/i))

      await waitFor(() => {
        const dashboard = getByText('Aggregated risk by portfolio')
        expect(dashboard).toBeInTheDocument()
      })
    });
  })
})
