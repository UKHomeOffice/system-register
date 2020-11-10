import React from 'react'
import { useKeycloak } from '@react-keycloak/web';
import './Auth.css'

const Auth = () => {
    const [keycloak] = useKeycloak();

    if (keycloak?.authenticated) {
        localStorage.setItem("bearer-token", keycloak.token)
    }

    const logout = () => keycloak.logout().then(() => localStorage.removeItem("bearer-token"))
    const login = () => keycloak.login().catch(e => console.error("AUth error: " + e))

    const renderAuthStatus = () => {
        if (keycloak?.authenticated) {
            return (
                <>
                    Welcome {keycloak.tokenParsed.preferred_username} <button className="logout" onClick={logout}>Logout</button>
                </>
            )
        }
        else {
            return (
                <>
                    <button href="" className="login" onClick={login}>Login</button>
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