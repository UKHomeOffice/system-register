import React from 'react'
import PhaseBanner from '@govuk-react/phase-banner'
import Page from '@govuk-react/page'

const Banner = ({ phase, children }) => {
    return (
        <Page.WidthContainer>
            <PhaseBanner level={phase}>
                {children}
            </PhaseBanner>
        </Page.WidthContainer>
    )
}

export default Banner