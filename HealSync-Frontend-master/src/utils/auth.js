const TOKEN_KEY = 'jwt_token';
const USER_KEY = 'user';

export const getToken = () => {
    return localStorage.getItem(TOKEN_KEY);
};

export const setToken = (token) => {
    localStorage.setItem(TOKEN_KEY, token);
};

export const removeToken = () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
};

export const getUser = () => {
    const userStr = localStorage.getItem(USER_KEY);
    return userStr ? JSON.parse(userStr) : null;
};

export const setUser = (user) => {
    localStorage.setItem(USER_KEY, JSON.stringify(user));
};

export const removeUser = () => {
    localStorage.removeItem(USER_KEY);
};

export const isAuthenticated = () => {
    return !!getToken();
};

export const getUserRole = () => {
    const user = getUser();
    return user ? user.role : null;
};
