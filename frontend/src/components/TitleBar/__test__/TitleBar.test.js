import React from 'react';
import { render, cleanup } from '@testing-library/react';
import TitleBar from '../TitleBar';
import snapshotRenderer from 'react-test-renderer'

afterEach(cleanup)

describe('<Title />', () => {
  it('renders without crashing', () => {
    render(<TitleBar />)
  })

  it('renders title', () => {
    const { getByTestId } = render(<TitleBar />);
    const title = getByTestId('title-bar-div');
    expect(title).toHaveTextContent('System Register')
  });

  it('matches snapshot', () => {
    expect(snapshotRenderer.create(<TitleBar />))
      .toMatchSnapshot()
  })
})