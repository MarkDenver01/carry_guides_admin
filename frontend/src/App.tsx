// App.tsx
import { Routes, Route, useLocation } from 'react-router-dom';
import bg from './assets/bg.svg';
import logo from './assets/logo.svg';
import main from './assets/main_logo.svg';
import './App.css';

import Login from './page/login/Login.tsx';
import ProtectedRoute from './components/ProtectedRoute';
import DashboardLayout from './layout/dashboard/DashboardLayout.tsx';
import type { JSX } from 'react';

function App(): JSX.Element {
    const location = useLocation();
    const isLoginPage = location.pathname === '/';

    return (
        <div
            className={
                isLoginPage
                    ? 'relative min-h-screen w-full bg-cover bg-no-repeat bg-center flex items-center justify-center'
                    : 'min-h-screen w-full'
            }
            style={isLoginPage ? { backgroundImage: `url(${bg})` } : {}}
        >
            {isLoginPage && (
                <div className="absolute top-4 right-4 w-[140px] h-[140px] opacity-90">
                    <img src={logo} alt="Logo background" className="w-full h-full opacity-80" />
                    <img
                        src={main}
                        alt="Main logo overlay"
                        className="absolute top-1/2 left-1/2 w-[110px] h-[110px] transform -translate-x-1/2 -translate-y-1/2 opacity-90"
                    />
                </div>
            )}

            <div className={isLoginPage ? 'z-10' : ''}>
                <Routes>
                    <Route path="/" element={<Login />} />
                    <Route
                        path="/dashboard/*"
                        element={
                            <ProtectedRoute>
                                <DashboardLayout />
                            </ProtectedRoute>
                        }
                    />
                </Routes>
            </div>
        </div>
    );
}

export default App;
