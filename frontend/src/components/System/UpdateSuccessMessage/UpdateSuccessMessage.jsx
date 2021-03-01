import React from "react";

import NotificationBanner, {
  NotificationBannerHeading,
} from "../../NotificationBanner";

const UpdateSuccessMessage = () => (
  <NotificationBanner title="Success" type="success">
    <NotificationBannerHeading>
      Your update has been saved.
    </NotificationBannerHeading>
  </NotificationBanner>
);

UpdateSuccessMessage.propTypes = {};

export default UpdateSuccessMessage;
