import React from 'react'
import { render, cleanup } from '@testing-library/react'
import System from '../System'
import renderer from 'react-test-renderer'

const test_system = {
    id: 444,
    name: "System X",
    aliases: ["sys X", "project Jupiter"],
    criticality: "high",
    investment_state: "evergreen",
    description: "The description",
    portfolio: 'Portfolio Y',
    last_updated: '2020-02-13T11:01:01',
    business_owner: "I own the business",
    service_owner: "I own the service",
    technical_owner: "I own the tech",
    product_owner: "I own the product",
    information_asset_owner: "I own the data",
    developed_by: "Dev Team",
    supported_by: "Support Team",
    risks: [
      {
        "name": "roadmap",
        "level": "high",
        "rationale": "there is no plan"
      },
      {
        "name": "tech_stack",
        "level": "low",
        "rationale": "Really good tech"
      }
    ]
}

const test_unknown_system = {
    id: 444,
    name: "System X",
    aliases: [],
    criticality: "",
    investment_state: "",
    description: "",
    portfolio: '',
    last_updated: '',
    business_owner: "",
    service_owner: "",
    technical_owner: "",
    product_owner: "",
    information_asset_owner: "",
    developed_by: "",
    supported_by: "",
    risks: [
      {
        "name": "roadmap",
        "level": null,
        "rationale": null
      },
      {
        "name": "change",
        "level": null,
        "rationale": null
      }
    ]
}

let _getByText = undefined
afterEach(cleanup)

describe('<System />', () => {
    describe('when the data is known', () => {
        beforeEach(() => setup(test_system))
        it('renders system name', () => {
            const element = _getByText('System X')
            expect(element).toBeInTheDocument()
        });

        it('renders system aliases', () => {
            const element = _getByText('Aliases: sys X, project Jupiter')
            expect(element).toBeInTheDocument()
        });

        it('renders system last updated', () => {
            const element = _getByText('Last Updated: February 2020')
            expect(element).toBeInTheDocument()
        });

        it('renders system portfolio', () => {
            const element = _getByText('Portfolio: Portfolio Y')
            expect(element).toBeInTheDocument()
        });

        it('renders criticality', () => {
            const element = _getByText('Criticality High')
            expect(element).toBeInTheDocument()
        });

        it('renders investment state', () => {
            const element = _getByText('Evergreen')
            expect(element).toBeInTheDocument()
        });

        it('renders system description', () => {
            const element = _getByText('The description')
            expect(element).toBeInTheDocument()
        });

        it('renders system business owner', () => {
            const element = _getByText('Business Owner: I own the business')
            expect(element).toBeInTheDocument()
        });

        it('renders system technical owner', () => {
            const element = _getByText('Technical Owner: I own the tech')
            expect(element).toBeInTheDocument()
        });

        it('renders system service owner', () => {
            const element = _getByText('Service Owner: I own the service')
            expect(element).toBeInTheDocument()
        });

        it('renders system product owner', () => {
            const element = _getByText('Product Owner: I own the product')
            expect(element).toBeInTheDocument()
        });

        it('renders system information asset owner', () => {
            const element = _getByText('Information Asset Owner: I own the data')
            expect(element).toBeInTheDocument()
        });

        it('renders developed by', () => {
            const element = _getByText('Developed By: Dev Team')
            expect(element).toBeInTheDocument()
        });

        it('renders supported by', () => {
            const element = _getByText('Supported By: Support Team')
            expect(element).toBeInTheDocument()
        });


        it('renders roadmap risk title', () => {
            const element = _getByText('Roadmap')
            expect(element).toBeInTheDocument()
        });
        it('renders tech stack risk title', () => {
            const element = _getByText('Tech Stack')
            expect(element).toBeInTheDocument()
        });
    })

    describe('when the data is unknown', () => {
        beforeEach(() => setup(test_unknown_system))

        it('renders system aliases as none', () => {
            const element = _getByText('Aliases: None')
            expect(element).toBeInTheDocument()
        });

        it('renders system last updated as never', () => {
            const element = _getByText('Last Updated:')
            toLookRisky(element, 'Never')
        });

        it('renders system portfolio as unknown', () => {
            const element = _getByText('Portfolio:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system business owner as unknown', () => {
            const element = _getByText('Business Owner:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system technical owner as unknown', () => {
            const element = _getByText('Technical Owner:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system service owner as unknown', () => {
            const element = _getByText('Service Owner:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system product owner as unknown', () => {
            const element = _getByText('Product Owner:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system information asset owner as unknown', () => {
            const element = _getByText('Information Asset Owner:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system developed by as unknown', () => {
            const element = _getByText('Developed By:')
            toLookRisky(element, 'Unknown')
        });

        it('renders system supported by as unknown', () => {
            const element = _getByText('Supported By:')
            toLookRisky(element, 'Unknown')
        });

        it('renders roadmap risk title', () => {
            const element = _getByText('Roadmap')
            expect(element).toBeInTheDocument()
        });
        it('renders change risk title', () => {
            const element = _getByText('Change')
            expect(element).toBeInTheDocument()
        });
    })

    it('matches snapshot with known data', () => {
        expect(renderer
            .create(<System system={test_system} />))
            .toMatchSnapshot()
    })

    it('matches snapshot with unknown data', () => {
        expect(renderer
            .create(<System system={test_unknown_system} />))
            .toMatchSnapshot()
    })
})

function toLookRisky(element, str) {
    const html = `<strong class="highRisk">${str}</strong>`
    expect(element).toContainHTML(html)
}

function setup(test_data) {
    const { getByText } = render(<System system={test_data} />)
    _getByText = getByText
}
