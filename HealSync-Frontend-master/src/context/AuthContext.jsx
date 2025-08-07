import React, { createContext, useState, useContext, useEffect } from 'react';
import authService from '../services/authService';
import { getUser, setUser, removeUser } from '../utils/auth';

const AuthContext = createContext();

export const useAuth = () => {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
};

export const AuthProvider = ({ children }) => {
    const [user, setUserState] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const storedUser = getUser();
        if (storedUser) {
            setUserState(storedUser);
        }
        setLoading(false);
    }, []);

    const login = async (email, password) => {
        try {
            const response = await authService.login(email, password);
            
            if (response.success) {
                // For now, we'll create a basic user object
                // In a real app, you'd fetch user details from the backend
                const userDetails = { email, role: 'PATIENT' }; // Default role
                setUser(userDetails);
                setUserState(userDetails);
                return { success: true };
            }
            return response;
        } catch (error) {
            return { success: false, error: error.message };
        }
    };

    const register = async (userData) => {
        try {
            const response = await authService.register(userData);
            return response;
        } catch (error) {
            return { success: false, error: error.message };
        }
    };

    const logout = () => {
        authService.logout();
        removeUser();
        setUserState(null);
    };

    const value = {
        user,
        login,
        register,
        logout,
        loading,
        isAuthenticated: !!user,
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
