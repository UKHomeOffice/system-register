import React from "react";
import PropTypes from "prop-types";

import NotificationBanner, {
  NotificationBannerHeading,
} from "../../NotificationBanner";
import useOnUnmount from "../../../utilities/useOnUnmount";

function UpdateSuccessMessage({ onDismiss }) {
  useOnUnmount(onDismiss);

  return (
    <NotificationBanner title="Success" type="success">
      <NotificationBannerHeading>
        Your update has been saved.
      </NotificationBannerHeading>
    </NotificationBanner>
  );
}

UpdateSuccessMessage.propTypes = {
  onDismiss: PropTypes.func.isRequired,
};

export default UpdateSuccessMessage;
