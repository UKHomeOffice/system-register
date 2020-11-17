import React from 'react'
import { useKeycloak } from '@react-keycloak/web'
import Button from '@govuk-react/button'
import './Auth.css'


const Auth = () => {
    const [keycloak] = useKeycloak();

    if (keycloak?.authenticated) {
        localStorage.setItem("bearer-token", keycloak.token)
    }

    const logout = () => keycloak.logout().then(() => localStorage.removeItem("bearer-token"))
    const login = () => keycloak.login().catch(e => console.error("Auth error: " + e))

    const renderAuthStatus = () => {
        if (keycloak?.authenticated) {
            return (
                <>
                   <span className="auth-welcome-message">Welcome {keycloak.tokenParsed.preferred_username}</span>
                    <Button onClick={logout}>Sign out</Button>
                </>
            )
        }
        else {
            return (
                <>
                    <Button onClick={login}>Sign in</Button>
                </>
            )
        }
    }

    return (
        <div className="auth">
            {renderAuthStatus()}
        </div>
    )
}

export default Auth