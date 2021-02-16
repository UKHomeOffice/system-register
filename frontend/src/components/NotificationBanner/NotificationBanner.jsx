import React from "react";
import PropTypes from "prop-types";

import "./NotificationBanner.css";

const typeClassNames = {
  default: "notification-banner--default",
  success: "notification-banner--success",
};

function NotificationBanner({ title, type = "default", children }) {
  return (
    <div
      role="alert"
      className={`notification-banner ${typeClassNames[type]}`}
      aria-labelledby="notification-banner-title"
    >
      <div className="notification-banner-header">
        <h2
          id="notification-banner-title"
          className="notification-banner-title"
        >
          {title}
        </h2>
      </div>
      <div className="notification-banner-content">{children}</div>
    </div>
  );
}

NotificationBanner.propTypes = {
  title: PropTypes.string.isRequired,
  type: PropTypes.oneOf(["default", "success"]),
  children: PropTypes.node.isRequired,
};

function NotificationBannerHeading({ children }) {
  return <div className="notification-banner-heading">{children}</div>;
}

NotificationBannerHeading.propTypes = {
  children: PropTypes.node.isRequired,
};

export default NotificationBanner;
export { NotificationBannerHeading };
