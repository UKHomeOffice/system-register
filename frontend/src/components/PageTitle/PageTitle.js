import { useLayoutEffect } from "react";
import PropTypes from "prop-types";

function PageTitle({ children }) {
  useLayoutEffect(() => {
    document.title = `${children} — System Register`;
  }, [children]);

  return null;
}

PageTitle.propTypes = {
  children: PropTypes.string.isRequired,
};

export default PageTitle;
