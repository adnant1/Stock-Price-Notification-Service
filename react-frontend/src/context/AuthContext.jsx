"use client";

import { createContext, useState, useContext, useEffect } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Check if user is already logged in
    const token = localStorage.getItem("auth_token");
    const userData = localStorage.getItem("user_data");

    if (token && userData) {
      try {
        setUser(JSON.parse(userData));
      } catch (error) {
        console.error("Failed to parse user data:", error);
        localStorage.removeItem("auth_token");
        localStorage.removeItem("user_data");
      }
    }

    setLoading(false);
  }, []);

  // Handle OAuth redirect
  useEffect(() => {
    const handleOAuthRedirect = () => {
      const urlParams = new URLSearchParams(window.location.search);
      const token = urlParams.get("token");
      const userData = urlParams.get("user");

      if (token) {
        localStorage.setItem("auth_token", token);
        if (userData) {
          try {
            const parsedUserData = JSON.parse(atob(userData));
            setUser(parsedUserData);
            localStorage.setItem("user_data", JSON.stringify(parsedUserData));
          } catch (error) {
            console.error("Failed to parse user data from redirect:", error);
          }
        }

        // Clean up URL
        window.history.replaceState(
          {},
          document.title,
          window.location.pathname
        );
      }
    };

    handleOAuthRedirect();
  }, []);

  const login = () => {
    // Redirect to Google OAuth
    window.location.href = "/oauth2/authorization/google";
  };

  const logout = () => {
    localStorage.removeItem("auth_token");
    localStorage.removeItem("user_data");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
