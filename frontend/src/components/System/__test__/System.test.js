import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route } from 'react-router-dom';

import System from '../System';
import api from '../../../services/api';

jest.mock('../../../services/api', () => ({
  getSystem: jest.fn(),
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
    setup();

    const element = await screen.findByText('Test System');

    expect(element).toBeInTheDocument();
  });
});

function setup() {
  render(
    <MemoryRouter initialEntries={['system/1']}>
      <Route path='system/:id' component={System} />
    </MemoryRouter>
  );
}
