"use client";

import { useState, useEffect } from "react";
import { useAuth } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import AlertCard from "../components/AlertCard";
import CreateAlertModel from "../components/CreateAlertModel";
import EditAlertModel from "../components/EditAlertModel";
import { Plus } from "lucide-react";

export default function AlertsPage() {
  const { user } = useAuth();
  const [alerts, setAlerts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showCreateModel, setShowCreateModel] = useState(false);
  const [editingAlert, setEditingAlert] = useState(null);

  useEffect(() => {
    fetchAlerts();
  }, []);

  const fetchAlerts = async () => {
    setLoading(true);
    try {
      const token = localStorage.getItem("auth_token");
      const response = await fetch(
        `${import.meta.env.VITE_BACKEND_URL}/alerts/dashboard`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to fetch alerts");
      }

      const data = await response.json();
      setAlerts(data);
    } catch (error) {
      console.error("Error fetching alerts:", error);
      setError("Failed to load alerts. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateAlert = async (newAlert) => {
    try {
      // In a real app, you would send to your backend
      const token = localStorage.getItem("auth_token");
      const response = await fetch("/api/alerts", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(newAlert),
      });

      if (!response.ok) {
        throw new Error("Failed to create alert");
      }

      // For demo purposes, add to local state
      setAlerts([
        ...alerts,
        {
          ...newAlert,
          id: Date.now().toString(),
          status: "active",
          currentPrice: Math.random() * 100 + 100, // Random price for demo
        },
      ]);

      setShowCreateModel(false);
    } catch (error) {
      console.error("Error creating alert:", error);
      alert("Failed to create alert. Please try again.");
    }
  };

  const handleEditAlert = async (updatedAlert) => {
    try {
      // In a real app, you would send to your backend
      const token = localStorage.getItem("auth_token");
      const response = await fetch(`/api/alerts/${updatedAlert.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(updatedAlert),
      });

      if (!response.ok) {
        throw new Error("Failed to update alert");
      }

      // Update in local state
      setAlerts(
        alerts.map((alert) =>
          alert.id === updatedAlert.id ? { ...alert, ...updatedAlert } : alert
        )
      );

      setEditingAlert(null);
    } catch (error) {
      console.error("Error updating alert:", error);
      alert("Failed to update alert. Please try again.");
    }
  };

  const handleDeleteAlert = async (alertId) => {
    if (!confirm("Are you sure you want to delete this alert?")) {
      return;
    }

    try {
      // In a real app, you would send to your backend
      const token = localStorage.getItem("auth_token");
      const response = await fetch(`/api/alerts/${alertId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to delete alert");
      }

      // Remove from local state
      setAlerts(alerts.filter((alert) => alert.id !== alertId));
    } catch (error) {
      console.error("Error deleting alert:", error);
      alert("Failed to delete alert. Please try again.");
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
      <Navbar />

      <main className="container mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-8">
          <h1 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white">
            Your Stock Alerts
          </h1>

          <button
            onClick={() => setShowCreateModel(true)}
            className="flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium transition-colors"
          >
            <Plus className="w-5 h-5" />
            New Alert
          </button>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-t-2 border-b-2 border-blue-600"></div>
          </div>
        ) : error ? (
          <div className="bg-red-100 dark:bg-red-900/20 border border-red-200 dark:border-red-800 text-red-800 dark:text-red-400 p-4 rounded-lg">
            {error}
          </div>
        ) : alerts.length === 0 ? (
          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 p-8 text-center">
            <div className="w-16 h-16 bg-blue-100 dark:bg-blue-900/30 rounded-full flex items-center justify-center mx-auto mb-4">
              <svg
                className="w-8 h-8 text-blue-600 dark:text-blue-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
                xmlns="http://www.w3.org/2000/svg"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                />
              </svg>
            </div>
            <h2 className="text-xl font-semibold mb-2 text-slate-900 dark:text-white">
              No Alerts Yet
            </h2>
            <p className="text-slate-600 dark:text-slate-400 mb-6">
              Create your first stock price alert to get started.
            </p>
            <button
              onClick={() => setShowCreateModel(true)}
              className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium transition-colors"
            >
              Create Alert
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {alerts.map((alert) => (
              <AlertCard
                key={alert.id}
                alert={alert}
                onEdit={() => setEditingAlert(alert)}
                onDelete={() => handleDeleteAlert(alert.id)}
              />
            ))}
          </div>
        )}
      </main>

      {showCreateModel && (
        <CreateAlertModel
          onClose={() => setShowCreateModel(false)}
          onSubmit={handleCreateAlert}
        />
      )}

      {editingAlert && (
        <EditAlertModel
          alert={editingAlert}
          onClose={() => setEditingAlert(null)}
          onSubmit={handleEditAlert}
        />
      )}
    </div>
  );
}
