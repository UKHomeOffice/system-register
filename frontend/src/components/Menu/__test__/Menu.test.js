import React from 'react'
import { render } from '@testing-library/react'
import { createMemoryHistory } from 'history'
import renderer from 'react-test-renderer'
import { BrowserRouter, Router, MemoryRouter } from 'react-router-dom'
import Menu from '../Menu'
import config from "../../../config/config";


describe('<Menu />', () => {
    it('renders System Register link', () => {
        const { getByText } = render(<MemoryRouter><Menu /></MemoryRouter>)
        const element = getByText('System Register')
        expect(element).toBeInTheDocument()
        expect(element).toHaveAttribute("href", '/')
    });

    it('renders Risk Dashboard link', () => {
        const { getByText } = render(<MemoryRouter><Menu /></MemoryRouter>)
        const element = getByText('Risk Dashboard')
        expect(element).toBeInTheDocument()
        expect(element).toHaveAttribute("href", '/risk_dashboard')
    });

    it('renders API link', () => {
        const { getByText } = render(<MemoryRouter><Menu /></MemoryRouter>)
        const element = getByText('API')
        expect(element).toBeInTheDocument()
        expect(element).toHaveAttribute("href", `${config.api.url}/systems`)
    });

    it('renders About link', () => {
        const { getByText } = render(<MemoryRouter><Menu /></MemoryRouter>)
        const element = getByText('About')
        expect(element).toBeInTheDocument()
        expect(element).toHaveAttribute("href", '/about')
    });

    it('renders Contact link', () => {
        const { getByText } = render(<MemoryRouter><Menu /></MemoryRouter>)
        const element = getByText('Contact')
        expect(element).toBeInTheDocument()
        expect(element).toHaveAttribute("href", '/contact')
    });

    describe('when selecting Systems Register page', () => {
        it('should identify you are situated in the System Register page', () => {
            const history = createMemoryHistory()
            const route = '/'
            history.push(route)
            const { getByText } = render(<Router history={history}><Menu /></Router>)
            const element = getByText('System Register')
            expect(element).toHaveClass('selected')
        });

        it('should not identify you are situated in the Risk Dashboard page', () => {
            const history = createMemoryHistory()
            const dashboard_route = '/risk_dashboard'
            const home_route = '/'
            history.push(dashboard_route)
            history.push(home_route)
            const { getByText } = render(<Router history={history}><Menu /></Router>)
            const registerElement = getByText('System Register')
            const dashboardElement = getByText('Risk Dashboard')
            expect(registerElement).toHaveClass('selected')
            expect(dashboardElement).not.toHaveClass('selected')
        });
    })

    describe('when selecting Systems Register page', () => {
        it('should identify you are situated on the risk dashboard page', () => {
            const history = createMemoryHistory()
            const route = '/risk_dashboard'
            history.push(route)
            const { getByText } = render(<Router history={history}><Menu /></Router>)
            const element = getByText('Risk Dashboard')
            expect(element).toHaveClass('selected')
        })
    })

    it('matches snapshot', () => {
        const snapshot = renderer.create(<BrowserRouter><Menu /></BrowserRouter>)
        expect(snapshot).toMatchSnapshot()
    });
})