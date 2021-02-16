import { useEffect } from "react";

const useOnUnmount = (onUnmount) => {
  useEffect(() => {
    return () => {
      onUnmount();
    };
  }, [onUnmount]);
};

export default useOnUnmount;
