import React from 'react';
import App from '../App';
import renderer from 'react-test-renderer';
import { Router } from 'react-router-dom'
import { render, fireEvent } from '@testing-library/react'
import { createMemoryHistory } from 'history'

describe("<App />", () => {
    it('matches snapshot', () => {
      expect(renderer.create(<App />))
        .toMatchSnapshot()
      
    })

  describe("routing", () => {
    const history = createMemoryHistory()
    const { getByText } = render(
      <Router history={history}>
        <App />
      </Router>
    )

    fireEvent.click(getByText(/Risk Dashboard/i))

    const dashboard = getByText('Aggregated risk by portfolio')
    expect(dashboard).toBeInTheDocument()
  })
})
