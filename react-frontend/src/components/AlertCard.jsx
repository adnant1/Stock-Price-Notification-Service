"use client";

import { ArrowDown, ArrowUp, Edit, Trash2 } from "lucide-react";

export default function AlertCard({ alert, onEdit, onDelete }) {
  const {
    stockTicker,
    targetPrice,
    currentPrice,
    condition,
    triggered,
    approaching,
  } = alert;

  const formatPrice = (price) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
      minimumFractionDigits: 2,
    }).format(price);
  };

  const getStatusColor = () => {
    return triggered
      ? "bg-green-100 text-green-800 dark:bg-green-900/30 dark:text-green-400"
      : "bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400";
  };

  const getConditionIcon = () => {
    return condition === "above" ? (
      <ArrowUp className="w-4 h-4" />
    ) : (
      <ArrowDown className="w-4 h-4" />
    );
  };

  const getConditionColor = () => {
    return condition === "above"
      ? "text-green-600 dark:text-green-400"
      : "text-red-600 dark:text-red-400";
  };

  return (
    <div className="bg-white dark:bg-slate-800 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700 overflow-hidden">
      <div className="p-6">
        <div className="flex justify-between items-start mb-4">
          <div>
            <h3 className="text-xl font-bold text-slate-900 dark:text-white">
              {stockTicker}
            </h3>
            <div
              className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium mt-2 ${getStatusColor()}`}
            >
              {triggered ? "Triggered" : "Active"}
            </div>
          </div>
          <div className="flex gap-2">
            <button
              onClick={onEdit}
              className="p-2 rounded-md hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-600 dark:text-slate-400"
              aria-label="Edit alert"
            >
              <Edit className="w-4 h-4" />
            </button>
            <button
              onClick={onDelete}
              className="p-2 rounded-md hover:bg-slate-100 dark:hover:bg-slate-700 text-slate-600 dark:text-slate-400"
              aria-label="Delete alert"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>

        <div className="space-y-4">
          <div className="flex justify-between items-center">
            <div className="text-sm text-slate-500 dark:text-slate-400">
              Current Price
            </div>
            <div className="font-semibold text-slate-900 dark:text-white">
              {formatPrice(currentPrice)}
            </div>
          </div>

          <div className="flex justify-between items-center">
            <div className="text-sm text-slate-500 dark:text-slate-400">
              Target Price
            </div>
            <div
              className={`font-semibold flex items-center gap-1 ${getConditionColor()}`}
            >
              {getConditionIcon()}
              {formatPrice(targetPrice)}
            </div>
          </div>

          {approaching && (
            <div className="bg-yellow-100 dark:bg-yellow-900/30 border border-yellow-200 dark:border-yellow-800 rounded-md p-3 mt-4">
              <div className="flex items-center gap-2 text-yellow-800 dark:text-yellow-400 text-sm">
                <svg
                  className="w-4 h-4"
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
                <span>Price is approaching target!</span>
              </div>
            </div>
          )}
        </div>
      </div>

      <div className="bg-slate-50 dark:bg-slate-900/50 px-6 py-4 border-t border-slate-200 dark:border-slate-700">
        <div className="text-sm text-slate-500 dark:text-slate-400">
          Alert when price is {condition === "over" ? "above" : "below"}{" "}
          {formatPrice(targetPrice)}
        </div>
      </div>
    </div>
  );
}
