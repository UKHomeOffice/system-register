import { useEffect, useState } from "react";
import { useHistory, useLocation } from "react-router-dom";

function BlurOnNavigate({ container }) {
  const [lastLocation, setLastLocation] = useState({
    hash: "",
    length: -1,
    pathname: "",
  });
  const { length } = useHistory();
  const { hash, pathname } = useLocation();

  useEffect(() => {
    const hasPathChanged = pathname !== lastLocation.pathname;
    const hasHistoryChanged = length !== lastLocation.length;
    const isFragmentTheSame = hash === lastLocation.hash;

    const hasLocationChanged =
      hasPathChanged || (hasHistoryChanged && isFragmentTheSame);

    if (hasLocationChanged && container.current?.focus) {
      setLastLocation({
        hash,
        length,
        pathname,
      });
      container.current.focus();
    }
  }, [container, length, hash, pathname, lastLocation]);

  return null;
}

export default BlurOnNavigate;
