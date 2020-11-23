import React from 'react'
import { render, fireEvent } from '@testing-library/react'
import PortfolioHeatmap from '../PortfolioHeatmap'
import { BrowserRouter } from 'react-router-dom'

const test_data = [
    {
        id: 44,
        name: "VaporWare 3000",
        portfolio: 'Serentiy',
        aliases: ['Alias A'],
        "risks": [
            { "name": "roadmap", "level": 'low' }
        ]
    },
    {
        id: "1111",
        name: "Riskinator 5000",
        portfolio: 'No risk no reward',
        "risks": [
            { "name": "roadmap", "level": 'high' },
            { "name": "sunset", "level": 'high' }
        ]
    },
    {
        id: "1_test",
        name: "Riskinator 6000",
        portfolio: 'No risk no reward',
        "risks": [
            { "name": "roadmap", "level": 'high' },
            { "name": "sunset", "level": 'medium' }
        ]
    },
    {
        id: 2,
        name: "Systemio Budgeto",
        portfolio: 'Average Joes',
        "risks": [
            { "name": "roadmap", "level": 'low' },
            { "name": "sunset", "level": 'medium' }
        ]
    },
    {
        id: 3,
        name: "Systemio Budgeto v2",
        portfolio: 'Average Joes',
        "risks": [
            { "name": "roadmap", "level": 'high' },
            { "name": "sunset", "level": 'medium' }
        ]
    },
    {
        id: 45,
        name: "Systemio Budgeto v3",
        portfolio: 'Average Joes',
        "risks": [
            { "name": "roadmap", "level": null },
            { "name": "sunset", "level": 'medium' }
        ]
    },
    {
        id: 46,
        name: "Systemio Budgeto v4",
        portfolio: 'Average Joes',
        "risks": [
            { "name": "roadmap", "level": 'medium' },
            { "name": "sunset", "level": 'medium' }
        ]
    },
    {
        id: "1",
        name: "Mysterion",
        portfolio: 'Department of mystery',
        "risks": [
            { "name": "sunset", "level": 'unknown' },
            { "name": "architecture", "level": 'medium' },
            { "name": "tech_stack", "level": 'not_applicable' },
            { "name": "roadmap", "level": null }
        ]
    }
]

describe('<PortfolioHeatmap />', () => {
    describe('showing risk', () => {
        //TODO: mock api to resolve issues
        it('renders title', () => {
            const { getByText } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByText('Aggregated risk by portfolio')
            expect(element).toBeInTheDocument()
        });

        it('shows high risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-No risk no reward")
            expect(element).toHaveClass("highRisk")
        });

        it('shows low risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Serentiy")
            expect(element).toHaveClass("lowRisk")
        });

        it('shows medium risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-sunset-Average Joes")
            expect(element).toHaveClass("mediumRisk")
        });

        it('shows medium low risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Average Joes")
            expect(element).toHaveClass("mediumRisk")
        });

        it('shows medium high risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-sunset-No risk no reward")
            expect(element).toHaveClass("mediumHighRisk")
        });

        it('shows unknown risk as high risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-sunset-Department of mystery")
            expect(element).toHaveClass("unknownRisk")
        });

        it('shows not_applicable risk as low risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-tech_stack-Department of mystery")
            expect(element).toHaveClass("lowRisk")
        });

        it('shows undefined risk as unknown risk', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Department of mystery")
            expect(element).toHaveClass("unknownRisk")
        });

        it('includes unknown risks when unknown risk radio selected', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Department of mystery")
            const radioElement = getByTestId('radioUnknownRisk')
            fireEvent.click(radioElement)
            expect(element).toHaveClass("highRisk")
        });

        it('includes audit coverage when coverage radio selected', () => {
            const { getByTestId } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Department of mystery")
            const radioElement = getByTestId('radioCoverage')
            fireEvent.click(radioElement)
            expect(element).toHaveClass("blue1")
        });
    })

    describe('risk details section', () => {
        it('displays all systems when no cell selected', () => {
            const { getByText } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const expected = getByText('Riskinator 6000')
            expect(expected).toBeInTheDocument()
        })

        it('displays systems associated with selected cell', () => {
            const { getByTestId, getByText } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Department of mystery")
            fireEvent.click(element)
            const expectedSystem = getByText(/Mysterion/)
            expect(expectedSystem).toBeInTheDocument()
        })

        it('does not display systems not associated with selected cell', () => {
            const { getByTestId, queryByText } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Department of mystery")
            fireEvent.click(element)
            const expectedSystem = queryByText("VaporWare 3000")
            expect(expectedSystem).not.toBeInTheDocument()
        })

        it('orders systems from highest risk to lowest risk', () => {
            const { getByTestId, queryAllByText } = render(<BrowserRouter><PortfolioHeatmap systems={test_data} /></BrowserRouter>)
            const element = getByTestId("riskSquare-roadmap-Average Joes")
            fireEvent.click(element)
            const systems = queryAllByText(/Systemio Budgeto/)
            expect(systems[0]).toHaveTextContent("Systemio Budgeto v2")
            expect(systems[1]).toHaveTextContent("Systemio Budgeto v4")
            expect(systems[2]).toHaveTextContent("Systemio Budgeto")
            expect(systems[3]).toHaveTextContent("Systemio Budgeto v3")
        })
    })
})
