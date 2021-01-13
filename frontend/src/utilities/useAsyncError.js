import { useCallback, useState } from "react";

const useAsyncError = () => {
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