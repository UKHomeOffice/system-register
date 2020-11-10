import React from 'react'
import { render, getByLabelText } from '@testing-library/react'
import SystemCard from '../SystemCard'
import renderer from 'react-test-renderer'
import { BrowserRouter } from 'react-router-dom'

const up_to_date_system = {
    name: "System X",
    portfolio: 'Portfolio Y',
    last_updated: '2020-02-13T11:01:01'
}

const never_updated_system = {
    name: "System X",
    portfolio: 'Portfolio Y',
    last_updated: ''
}

describe('<SystemCard />', () => {
    it('renders name', () => {
        const { getByText } = render(
            <BrowserRouter>
                <SystemCard system={up_to_date_system} />
            </BrowserRouter>)
        const element = getByText(up_to_date_system.name)
        expect(element).toBeInTheDocument()
    });

    it('renders portfolio', () => {
        const { getByText } = render(
            <BrowserRouter>
                <SystemCard system={up_to_date_system} />
            </BrowserRouter>)
        const element = getByText(/Portfolio Y/)
        expect(element).toBeInTheDocument()
    });

    it('renders last_updated', () => {
        const { getByText } = render(
            <BrowserRouter>
                <SystemCard system={up_to_date_system} />
            </BrowserRouter>)
        const element = getByText(/February 2020/)
        expect(element).toBeInTheDocument()
    });

    it('renders last_updated as Never when falsy', () => {
        const { getByText } = render(
            <BrowserRouter>
                <SystemCard system={never_updated_system} />
            </BrowserRouter>)
        const element = getByText(/Never/) //todo need to be more specific in case
        expect(element).toBeInTheDocument()
    });

    it('matches snapshot', () => {
        const snapshot = renderer.create(<BrowserRouter><SystemCard system={up_to_date_system} /></BrowserRouter>)
        expect(snapshot).toMatchSnapshot()
    });
})