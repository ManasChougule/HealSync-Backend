import axios from 'axios';
import { getToken, setToken, removeToken } from '../utils/auth';

const API_URL = 'http://localhost:8080/auth';

class AuthService {
    async login(email, password) {
        try {
            const response = await axios.post(`${API_URL}/login`, {
                email,
                password
            });
            
            if (response.data && response.data.token) {
                setToken(response.data.token);
                return { success: true, token: response.data.token };
            }
            return { success: false, error: 'Invalid response' };
        } catch (error) {
            return { 
                success: false, 
                error: error.response?.data || 'Login failed' 
            };
        }
    }

    async register(userData) {
        try {
            const response = await axios.post(`${API_URL}/register`, userData);
            return { success: true, data: response.data };
        } catch (error) {
            return { 
                success: false, 
                error: error.response?.data || 'Registration failed' 
            };
        }
    }

    logout() {
        removeToken();
    }

    isAuthenticated() {
        return !!getToken();
    }

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        return userStr ? JSON.parse(userStr) : null;
    }

    setCurrentUser(user) {
        localStorage.setItem('user', JSON.stringify(user));
    }
}

export default new AuthService();
