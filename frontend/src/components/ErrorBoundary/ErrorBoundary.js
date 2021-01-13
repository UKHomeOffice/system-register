import React from "react";

class ErrorBoundary extends React.Component {
    state = { error: false };

    static getDerivedStateFromError(error) {
        return { error };
    }

    componentDidCatch(error) {
        this.setState(error)
    }

    render() {
        return this.state.error ? <div className="centerContent"><h1>404 System not found</h1></div> : this.props.children;
        // return this.state.error ? this.props.fallback : this.props.children;
    }
}

export default ErrorBoundary