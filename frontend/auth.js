// auth.js - Shared authentication utilities
const API_BASE_URL = 'http://localhost:8080/api'; // UPDATE TO YOUR SERVER URL

// Check if user is authenticated
function isAuthenticated() {
    return localStorage.getItem('user') !== null;
}

// Get current user
function getCurrentUser() {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
}

// Get auth headers for API calls
function getAuthHeaders() {
    const token = localStorage.getItem('token');
    return {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token || ''}`
    };
}

// Logout function
function logout() {
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    window.location.href = 'login.html';
}

// Check user role
function hasRole(requiredRole) {
    const user = getCurrentUser();
    return user && user.role === requiredRole;
}

// Redirect if not authenticated
function requireAuth() {
    if (!isAuthenticated()) {
        window.location.href = 'login.html';
        return false;
    }
    return true;
}

// Redirect if not authorized for role
function requireRole(requiredRole) {
    if (!requireAuth()) return false;
    
    if (!hasRole(requiredRole)) {
        alert('Access denied. Insufficient permissions.');
        window.location.href = 'dashboard.html';
        return false;
    }
    return true;
}

// Make authenticated API call
async function makeAuthenticatedRequest(url, options = {}) {
    if (!isAuthenticated()) {
        window.location.href = 'login.html';
        throw new Error('Not authenticated');
    }

    const defaultOptions = {
        headers: getAuthHeaders()
    };

    const response = await fetch(`${API_BASE_URL}${url}`, {
        ...defaultOptions,
        ...options
    });

    if (response.status === 401) {
        logout();
        throw new Error('Session expired');
    }

    return response;
}