import React from "react";

import Link from "../../Linking/Link";
import PageTitle from "../../PageTitle";

const DefaultError = () => (
  <div className="centerContent">
    <PageTitle>Something went wrong</PageTitle>

    <h1 style={{ marginBottom: "34px" }}>Something went wrong...</h1>
    <p>If you typed the web address, check it is correct.</p>
    <p>If you pasted web address, check you copied the entire address.</p>
    <p>
      If the web address is correct or you selected a link or button, contact
      the <Link to="/contact">System Register</Link> team if you need to speak
      to someone about the systems.
    </p>
  </div>
);

export default DefaultError;
