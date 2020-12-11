import React from 'react'
import {cleanup, render} from '@testing-library/react'
import {screen} from '@testing-library/dom'
import System from '../System'
import {MemoryRouter, Route} from 'react-router-dom'

import api from '../../../services/api';

jest.mock('../../../services/api', () => ({
  getSystem: jest.fn(),
}))

const test_system = {
  name: "Test System",
  last_updated: {},
  risks: [],
  aliases: []
}

describe('<System />', () => {
  afterEach(cleanup)
  beforeEach(() => {
    api.getSystem.mockResolvedValue(test_system);
  });

  it('renders system name', async () => {
    setup();
    const element = await screen.findByText('Test System');
    expect(element).toBeInTheDocument();
  });

});

function setup() {
  render(
    <MemoryRouter initialEntries={['system/1']}>
      <Route path='system/:id'>
        <System/>
      </Route>
    </MemoryRouter>)
}
