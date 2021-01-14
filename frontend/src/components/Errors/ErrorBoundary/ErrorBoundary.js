import React from "react";
import DefaultError from "../DefaultError";
import PageNotFoundError from "../PageNotFoundError";

class ErrorBoundary extends React.Component {
    state = { error: false };

    static getDerivedStateFromError(error) {
        return { error };
    }

    componentDidCatch(error) {
        this.setState(error)
    }

    render() {
        if(this.state.error){
            if(this.state.error.message === "System not found"){
                return <PageNotFoundError />
            }
            else {
                return <DefaultError />
            }
        }
        return this.props.children
    }
}

export default ErrorBoundary