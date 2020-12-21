import React from "react";
import "./SRFooter.css"
import crest from "./govuk-crest.png"
import ogl from "./ogl-symbol.png"
import {GridCol, GridRow} from 'govuk-react';


const SRFooter = () => {
  return (
    <div className="SRFooter">
      <div className="centerContent">
        <GridRow>
          <GridCol setWidth="85%">
            <ul className="footer-list">
              <li><a className="link" href="/about">About</a></li>
              <li><a className="link" href="/contact">Contact</a></li>
              <li><a className="link" href="/api/systems">API</a></li>
            </ul>
            <span >
              <img className="ogl" src={ogl} alt="OGL"/>
                <span>All content is available under the <a className="link"
                                                            href="https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/">Open Government Licence v3.0</a>, except where otherwise stated </span>
            </span>
          </GridCol>
          <GridCol setWidth="15%">
            <div className="copyright">
              <a
                href="https://www.nationalarchives.gov.uk/information-management/re-using-public-sector-information/uk-government-licensing-framework/crown-copyright/">
                <img className="crest" src={crest} alt="Crown copyright"/>
              </a>
            </div>
          </GridCol>
        </GridRow>
      </div>
    </div>
  )
};

export default SRFooter;