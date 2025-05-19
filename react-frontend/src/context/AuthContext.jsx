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
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get("token");
    const userDataEncoded = urlParams.get("user");

    if (token) {
      localStorage.setItem("auth_token", token);

      if (userDataEncoded) {
        try {
          const userData = JSON.parse(atob(userDataEncoded));
          setUser(userData);
          localStorage.setItem("user_data", JSON.stringify(userData));
        } catch (error) {
          console.error(
            "Failed to decode or parse user data from redirect:",
            error
          );
        }
      }

      // Clean up URL
      window.history.replaceState({}, document.title, window.location.pathname);
    }
  }, []);

  const login = () => {
    // Redirect to Google OAuth
    window.location.href = `${import.meta.env.VITE_BACKEND_URL}/login`;
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
