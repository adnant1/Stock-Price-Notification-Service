"use client";

import { useState } from "react";
import { X } from "lucide-react";

export default function CreateAlertModel({
  onClose,
  onSubmit,
  externalErrors = {},
  onClearExternalError,
}) {
  const [formData, setFormData] = useState({
    stockTicker: "",
    targetPrice: "",
    condition: "above", // Set default to 'above'
  });
  const [errors, setErrors] = useState({});

  const mergedErrors = { ...errors, ...externalErrors };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    // Clear error when field is edited
    if (errors[name]) {
      setErrors({
        ...errors,
        [name]: null,
      });
    }
    if (externalErrors[name] && onClearExternalError) {
      onClearExternalError(name);
    }
  };

  const checkTickerExists = async (ticker) => {
    const url = `https://finnhub.io/api/v1/quote?symbol=${ticker}&token=${
      import.meta.env.VITE_FINNHUB_API_KEY
    }`;

    const maxRetries = 3;
    const delay = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

    for (let attempt = 1; attempt <= maxRetries; attempt++) {
      try {
        const res = await fetch(url);
        if (!res.ok) throw new Error(`HTTP ${res.status}`);

        const data = await res.json();
        return data.c && data.c > 0;
      } catch (error) {
        console.warn(`Attempt ${attempt} failed:`, error.message);
        if (attempt < maxRetries) {
          await delay(300 * attempt); // simple backoff
        }
      }
    }

    console.error(
      `Failed to validate ticker "${ticker}" after ${maxRetries} attempts.`
    );
    return false;
  };

  const validateForm = async () => {
    const newErrors = {};

    if (!formData.stockTicker) {
      newErrors.stockTicker = "Stock ticker is required";
    } else if (!/^[A-Z]{1,5}$/.test(formData.stockTicker)) {
      newErrors.stockTicker =
        "Enter a valid stock ticker (1-5 uppercase letters)";
    } else {
      const isValid = await checkTickerExists(formData.stockTicker);
      if (!isValid) {
        newErrors.stockTicker = "This stock ticker does not exist.";
      }
    }

    if (!formData.targetPrice) {
      newErrors.targetPrice = "Target price is required";
    } else if (
      isNaN(formData.targetPrice) ||
      Number.parseFloat(formData.targetPrice) <= 0
    ) {
      newErrors.targetPrice = "Enter a valid positive number";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const isValid = await validateForm();
    if (!isValid) return;

    onSubmit({
      stockTicker: formData.stockTicker,
      targetPrice: Number.parseFloat(formData.targetPrice),
      condition: formData.condition,
    });
  };

  return (
    <div className="fixed inset-0 bg-black/50 dark:bg-black/70 flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-slate-800 rounded-xl shadow-lg max-w-md w-full">
        <div className="flex justify-between items-center p-6 border-b border-slate-200 dark:border-slate-700">
          <h2 className="text-xl font-bold text-slate-900 dark:text-white">
            Create New Alert
          </h2>
          <button
            onClick={onClose}
            className="p-2 rounded-md hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-500 dark:text-slate-400"
            aria-label="Close"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="p-6">
          <div className="space-y-6">
            <div>
              <label
                htmlFor="ticker"
                className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1"
              >
                Stock Ticker
              </label>
              <input
                type="text"
                id="ticker"
                name="stockTicker"
                value={formData.stockTicker}
                onChange={handleChange}
                placeholder="e.g. AAPL"
                className={`w-full px-3 py-2 border ${
                  mergedErrors.stockTicker
                    ? "border-red-500 dark:border-red-500"
                    : "border-slate-300 dark:border-slate-600"
                } rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-700 dark:text-white`}
              />
              {mergedErrors.stockTicker && (
                <p className="mt-1 text-sm text-red-600 dark:text-red-400">
                  {mergedErrors.stockTicker}
                </p>
              )}
            </div>

            <div>
              <label
                htmlFor="targetPrice"
                className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1"
              >
                Target Price ($)
              </label>
              <input
                type="number"
                id="targetPrice"
                name="targetPrice"
                value={formData.targetPrice}
                onChange={handleChange}
                placeholder="0.00"
                step="0.01"
                min="0.01"
                className={`w-full px-3 py-2 border ${
                  mergedErrors.targetPrice
                    ? "border-red-500 dark:border-red-500"
                    : "border-slate-300 dark:border-slate-600"
                } rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-slate-700 dark:text-white`}
              />
              {mergedErrors.targetPrice && (
                <p className="mt-1 text-sm text-red-600 dark:text-red-400">
                  {mergedErrors.targetPrice}
                </p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-700 dark:text-slate-300 mb-1">
                Condition
              </label>
              <div className="flex gap-4">
                <label className="flex items-center">
                  <input
                    type="radio"
                    name="condition"
                    value="above"
                    checked={formData.condition === "above"}
                    onChange={handleChange}
                    className="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500 dark:border-slate-600 dark:bg-slate-700"
                  />
                  <span className="ml-2 text-slate-700 dark:text-slate-300">
                    Above target
                  </span>
                </label>
                <label className="flex items-center">
                  <input
                    type="radio"
                    name="condition"
                    value="below"
                    checked={formData.condition === "below"}
                    onChange={handleChange}
                    className="w-4 h-4 text-blue-600 border-slate-300 focus:ring-blue-500 dark:border-slate-600 dark:bg-slate-700"
                  />
                  <span className="ml-2 text-slate-700 dark:text-slate-300">
                    Below target
                  </span>
                </label>
              </div>
            </div>
          </div>

          <div className="mt-8 flex justify-end gap-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 rounded-lg border border-slate-300 dark:border-slate-600 text-slate-700 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-slate-700 transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium transition-colors"
            >
              Create Alert
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
