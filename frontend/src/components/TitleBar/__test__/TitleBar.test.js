import React from 'react';
import { render, cleanup } from '@testing-library/react';
import TitleBar from '../TitleBar';
import snapshotRenderer from 'react-test-renderer'
import { BrowserRouter } from 'react-router-dom';

afterEach(cleanup)

describe('<Title />', () => {
  it('renders without crashing', () => {
    render(<BrowserRouter><TitleBar /></BrowserRouter>)
  })

  it('renders title', () => {
    const { getByTestId } = render(<BrowserRouter><TitleBar /></BrowserRouter>);
    const title = getByTestId('title-bar-div');
    expect(title).toHaveTextContent('System Register')
  });

  it('matches snapshot', () => {
    expect(snapshotRenderer.create(<BrowserRouter><TitleBar /></BrowserRouter>))
      .toMatchSnapshot()
  })
})