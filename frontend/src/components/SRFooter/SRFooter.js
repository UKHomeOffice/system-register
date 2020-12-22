import React from "react";
import "./SRFooter.css"
import ogl from "./ogl-symbol.png"
import config from "../../config/config";


const SRFooter = () => {
  return (
    <div className="SRFooter">
      <div className="centerContent">
        <div className="meta-flex">
          <div className="meta-item">
            <ul className="footer-list">
              <li><a className="link" href="/about">About</a></li>
              <li><a className="link" href="/contact">Contact</a></li>
              <li><a className="link" href={`${config.api.url}/systems`}>API</a></li>
            </ul>
            <div>
              <img className="ogl" src={ogl} alt="OGL"/>
              <span>All content is available under the
                <a className="link"
                   href="https://www.nationalarchives.gov.uk/doc/open-government-licence/version/3/">
                  Open Government Licence v3.0</a>, except where otherwise stated </span>
            </div>
          </div>
          <div className="copyright">
            <a className="crest link"
               href="https://www.nationalarchives.gov.uk/information-management/re-using-public-sector-information/uk-government-licensing-framework/crown-copyright/">
              © Crown copyright
            </a>
          </div>
        </div>
      </div>
    </div>
  )
};

export default SRFooter;