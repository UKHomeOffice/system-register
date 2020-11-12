import React from 'react'
import { cleanup, render } from '@testing-library/react'
import RiskDetails from '../RiskDetails'

afterEach(cleanup)

const test_risk_known = {
  name: 'tech_stack',
  level: 'low',
  rationale: 'Fairly modern tech stack'
}

const test_risk_unknown = {
  name: 'change',
  level: null,
  rationale: null
}

const test_risk_not_applicable = {
  name: 'sunset',
  level: 'not_applicable',
  rationale: null
}

describe('<RiskDetails />', () => {

  it('renders risk title correctly', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_known} />)
      const element = getByText('Tech Stack')
      expect(element).toBeInTheDocument()
  });
  
  it('renders level when value is known', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_known} />)
      const element = getByText('Risk: Low')
      expect(element).toBeInTheDocument()
  });

  it('renders level when value is not known', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_unknown} />)
      const element = getByText('Risk: Unknown')
      expect(element).toBeInTheDocument()
  });

  it('renders level when value is not applicable', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_not_applicable} />)
      const element = getByText('Risk: n/a')
      expect(element).toBeInTheDocument()
  });

  it('renders rationale when value is known', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_known} />)
      const element = getByText(/Rationale:/)
      expect(element).toHaveTextContent('Fairly modern tech stack')
  });

  it('renders rationale when value is not known', () => {
      const { getByText } = render(<RiskDetails risk={test_risk_unknown} />)
      const element = getByText(/Rationale:/)
      expect(element).toContainHTML(`Rationale: <strong class="highRisk">Unknown</strong>`)
  });

  it('renders rationale when value is not applicable', () => {
      const { queryByText } = render(<RiskDetails risk={test_risk_not_applicable} />)
      const element = queryByText(/Rationale:/)
      expect(element).not.toBeInTheDocument()
  });

})

