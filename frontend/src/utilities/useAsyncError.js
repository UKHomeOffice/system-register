import { useCallback, useState } from "react";

const useAsyncError = () => {
    // eslint-disable-next-line 
    const [error, setError] = useState();
    return useCallback(
        e => {
            setError(() => {
                throw e;
            });
        },
        [setError],
    );
}

export default useAsyncError;