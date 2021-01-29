import React from 'react';
import { render, fireEvent, cleanup } from '@testing-library/react';
import SystemList from '../SystemList';
import renderer from 'react-test-renderer';
import { BrowserRouter } from 'react-router-dom';

const test_data = {
    systems: [
        {
            name: "System 1",
            portfolio: 'Portfolio 1',
            aliases: ['Alias A'],
            last_updated: {}
        },
        {
            name: "System 3",
            portfolio: 'Portfolio 2',
            aliases: ['Alias B', 'Alias C'],
            last_updated: {}
        },
        {
            name: "System 2",
            portfolio: 'Portfolio 2',
            aliases: [],
            last_updated: {}
        }
    ]
}

let _getByLabelText, _queryByText, _getByTestId = undefined

//tests
beforeEach(setup)
afterEach(cleanup)

describe('<SystemList />', () => {
    describe('searching by name', () => {
        it('renders search, filter and all systems when loaded', () => {
            expect(_getByLabelText('search')).toBeInTheDocument()
            expect(_getByTestId("select-portfolio")).toBeInTheDocument()
            expectAllSystems()
        })

        it('finds no systems when nothing matches the search string', () => {
            whenUserSearchesFor("System 99")
            expectNoSystems()
        })

        it('shows all systems when search string removed', () => {
            whenUserSearchesFor("System 1")
            expectSystems("System 1")
            whenUserSearchesFor("")
            expectAllSystems()
        })

        it('finds a system which fully match search string', () => {
            whenUserSearchesFor("System 1")
            expectSystems("System 1")
        })

        it('finds a system which partially matches search string', () => {
            whenUserSearchesFor("ystem 1")
            expectSystems("System 1")
        })

        it('finds multiple systems which match search string', () => {
            whenUserSearchesFor("System")
            expectSystems("System 1", "System 2", "System 3")
        })

        it('ignores case when searching', () => {
            whenUserSearchesFor("system 2")
            expectSystems("System 2")
        })
    })

    describe('searching by alias', () => {
        it('finds a system whos alias fully matches the search string', () => {
            whenUserSearchesFor("Alias A")
            expectSystems("System 1")
        })

        it('finds all system whos alias partialy matches the search string', () => {
            whenUserSearchesFor("Alias")
            expectSystems("System 1", "System 3")
        })

        it('finds no system when no aliases match the search string', () => {
            whenUserSearchesFor("Alias Z")
            expectNoSystems()
        })

        it('finds a system whos alias in lowercase matches the search string', () => {
            whenUserSearchesFor("alias a")
            expectSystems("System 1")
        })
    })

    describe('filtering by Portfolio', () => {
        it('filters systems when one system matches', () => {
            whenUserSelectsPortfolio("Portfolio 1")
            expectSystems("System 1")
        })

        it('filters systems when multiple systems match', () => {
            whenUserSelectsPortfolio("Portfolio 2")
            expectSystems("System 2", "System 3")
        })
    })

    describe('portfolio filter and search terms combined', () => {
        it('correctly conbines filters to return a system', () => {
            whenUserSelectsPortfolio("Portfolio 2")
            whenUserSearchesFor("3")
            expectSystems("System 3")
        })

        it('correctly conbines filters to return no systems', () => {
            whenUserSelectsPortfolio("Portfolio 2")
            whenUserSearchesFor("1")
            expectNoSystems()
        })
    })

    it('matches snapshot', () => {
        expect(renderer
            .create(<BrowserRouter><SystemList register={test_data} /></BrowserRouter>))
            .toMatchSnapshot()
    })
})

//dsl
function setup() {
    const { getByLabelText, queryByText, getByTestId } =
        render(<BrowserRouter><SystemList register={test_data} /></BrowserRouter>)
    _getByLabelText = getByLabelText
    _queryByText = queryByText
    _getByTestId = getByTestId
}

function expectSystems(...systemNames) {
    const visible = test_data.systems.filter(s => systemNames.includes(s.name))
    const invisible = test_data.systems.filter(s => !systemNames.includes(s.name))
    visible.forEach(s => expect(_queryByText(s.name)).toBeInTheDocument())
    invisible.forEach(s => expect(_queryByText(s.name)).not.toBeInTheDocument())
}

function expectNoSystems() { expectSystems() }
function expectAllSystems() { expectSystems(...test_data.systems.map(s => s.name)) }

function whenUserSearchesFor(str) {
    fireEvent.change(_getByLabelText('search'), { target: { value: str } })
}
function whenUserSelectsPortfolio(portfolio) {
    fireEvent.change(_getByTestId("select-portfolio"), { target: { value: portfolio } })
}

//mocks
jest.mock("@govuk-react/select", () => ({ value, onChange }) => {
    const options = [{ value: "Portfolio 1" }, { value: "Portfolio 2" }, { value: "Portfolio 3" }]
    function handleChange(event) {
        const option = options.find(
            (option) => option.value === event.currentTarget.value
        );
        onChange({ target: { ...option } });
    }
    return (
        <select data-testid="select-portfolio" value={value} onChange={handleChange}>
            {options.map(({ label, value }) => (
                <option key={value} value={value}>
                    {label}
                </option>
            ))}
        </select>
    );
});
