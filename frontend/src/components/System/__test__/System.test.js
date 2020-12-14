import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route } from 'react-router-dom';
import { useKeycloak } from "@react-keycloak/web";

import System from '../System';
import api from '../../../services/api';

jest.mock('../../../services/api', () => ({
  getSystem: jest.fn(),
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
    setup("1");

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
      setup("1/update-contacts");

      const element = await screen.findByText('Test System');

      expect(element).toBeInTheDocument();
    });
  });
});

function setup(path) {
  render(
    <MemoryRouter initialEntries={[`/system/${path}`]}>
      <Route path='/system/:id' component={System} />
    </MemoryRouter>
  );
}
