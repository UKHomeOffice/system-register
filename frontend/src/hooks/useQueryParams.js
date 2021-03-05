import { parse } from "query-string";
import { useLocation } from "react-router-dom";

function useQueryParams() {
  const { search } = useLocation();
  return parse(search);
}

export default useQueryParams;
