import React from "react";
import TopNav from "@govuk-react/top-nav";

import Auth from "../Auth/Auth";

import "./TitleBar.css";

const TitleBar = () => {
  const serviceTitle = (<div className="title-bar-title">System Register</div>);

  return (
    <div className="title-bar-root">
      <TopNav className="top-nav" data-testid="title-bar-div" company="" serviceTitle={serviceTitle}/>
      <div className="title-bar-login">
        <Auth/>
      </div>
    </div>
  );
}

export default TitleBar;
