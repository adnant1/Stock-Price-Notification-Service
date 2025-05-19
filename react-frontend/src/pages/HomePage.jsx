"use client";

import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Navbar from "../components/Navbar";
import { ArrowRight } from "lucide-react";
import { useEffect } from "react";

export default function HomePage() {
  const { login, user } = useAuth();
  const navigate = useNavigate();

  // If user is already logged in, redirect to alerts page
  useEffect(() => {
    if (user) {
      navigate("/alerts");
    }
  }, [user]);

  return (
    <div className="min-h-screen bg-gradient-to-b from-slate-50 to-slate-100 dark:from-slate-900 dark:to-slate-800">
      <Navbar />

      <main className="container mx-auto px-4 py-16 md:py-24">
        <div className="max-w-4xl mx-auto">
          <div className="text-center mb-16">
            <h1 className="text-4xl md:text-6xl font-bold mb-6 bg-clip-text text-transparent bg-gradient-to-r from-cyan-500 to-blue-600">
              TickrAlert
            </h1>
            <p className="text-xl md:text-2xl text-slate-700 dark:text-slate-300 mb-8">
              Never miss a stock price movement again. Set alerts and stay ahead
              of the market.
            </p>

            <div className="flex flex-col sm:flex-row items-center justify-center gap-4">
              <button
                onClick={login}
                className="flex items-center justify-center gap-2 px-6 py-3 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium transition-colors"
              >
                <svg className="w-5 h-5" viewBox="0 0 24 24">
                  <path
                    fill="currentColor"
                    d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                  />
                  <path
                    fill="currentColor"
                    d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                  />
                  <path
                    fill="currentColor"
                    d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                  />
                  <path
                    fill="currentColor"
                    d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                  />
                  <path fill="none" d="M1 1h22v22H1z" />
                </svg>
                Login with Google
              </button>

              <button className="flex items-center justify-center gap-2 px-6 py-3 rounded-lg bg-white hover:bg-slate-100 text-slate-800 font-medium border border-slate-300 transition-colors dark:bg-slate-800 dark:hover:bg-slate-700 dark:text-white dark:border-slate-600">
                Learn more
                <ArrowRight className="w-4 h-4" />
              </button>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-16">
            <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
              <div className="w-12 h-12 bg-blue-100 dark:bg-blue-900 rounded-full flex items-center justify-center mb-4">
                <svg
                  className="w-6 h-6 text-blue-600 dark:text-blue-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M13 10V3L4 14h7v7l9-11h-7z"
                  />
                </svg>
              </div>
              <h3 className="text-xl font-semibold mb-2 text-slate-900 dark:text-white">
                Real-time Alerts
              </h3>
              <p className="text-slate-600 dark:text-slate-400">
                Get notified instantly when stocks hit your target price.
              </p>
            </div>

            <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
              <div className="w-12 h-12 bg-green-100 dark:bg-green-900 rounded-full flex items-center justify-center mb-4">
                <svg
                  className="w-6 h-6 text-green-600 dark:text-green-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"
                  />
                </svg>
              </div>
              <h3 className="text-xl font-semibold mb-2 text-slate-900 dark:text-white">
                Easy Setup
              </h3>
              <p className="text-slate-600 dark:text-slate-400">
                Create custom alerts in seconds with our intuitive interface.
              </p>
            </div>

            <div className="bg-white dark:bg-slate-800 p-6 rounded-xl shadow-sm border border-slate-200 dark:border-slate-700">
              <div className="w-12 h-12 bg-purple-100 dark:bg-purple-900 rounded-full flex items-center justify-center mb-4">
                <svg
                  className="w-6 h-6 text-purple-600 dark:text-purple-400"
                  fill="none"
                  stroke="currentColor"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    strokeWidth={2}
                    d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z"
                  />
                </svg>
              </div>
              <h3 className="text-xl font-semibold mb-2 text-slate-900 dark:text-white">
                Market Insights
              </h3>
              <p className="text-slate-600 dark:text-slate-400">
                Track your alerts and gain insights into market movements.
              </p>
            </div>
          </div>

          <div className="bg-white dark:bg-slate-800 rounded-xl shadow-md overflow-hidden border border-slate-200 dark:border-slate-700">
            <div className="p-8 md:p-12">
              <h2 className="text-2xl md:text-3xl font-bold mb-4 text-slate-900 dark:text-white">
                How TickrAlert Works
              </h2>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div>
                  <ol className="space-y-6">
                    <li className="flex gap-3">
                      <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 dark:bg-blue-900 flex items-center justify-center text-blue-600 dark:text-blue-400 font-bold">
                        1
                      </div>
                      <div>
                        <h3 className="font-semibold text-slate-900 dark:text-white">
                          Create an account
                        </h3>
                        <p className="text-slate-600 dark:text-slate-400">
                          Sign in with your Google account to get started.
                        </p>
                      </div>
                    </li>
                    <li className="flex gap-3">
                      <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 dark:bg-blue-900 flex items-center justify-center text-blue-600 dark:text-blue-400 font-bold">
                        2
                      </div>
                      <div>
                        <h3 className="font-semibold text-slate-900 dark:text-white">
                          Set up alerts
                        </h3>
                        <p className="text-slate-600 dark:text-slate-400">
                          Choose a stock ticker, target price, and condition.
                        </p>
                      </div>
                    </li>
                    <li className="flex gap-3">
                      <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 dark:bg-blue-900 flex items-center justify-center text-blue-600 dark:text-blue-400 font-bold">
                        3
                      </div>
                      <div>
                        <h3 className="font-semibold text-slate-900 dark:text-white">
                          Get notified
                        </h3>
                        <p className="text-slate-600 dark:text-slate-400">
                          Receive email alerts when your conditions are met.
                        </p>
                      </div>
                    </li>
                  </ol>
                </div>
                <div className="flex items-center justify-center">
                  <div className="w-full max-w-md bg-slate-100 dark:bg-slate-700 rounded-lg p-4 shadow-inner">
                    <div className="bg-white dark:bg-slate-800 rounded-md p-4 border border-slate-200 dark:border-slate-600 mb-3">
                      <div className="flex justify-between items-center mb-2">
                        <div className="font-semibold text-slate-900 dark:text-white">
                          AAPL
                        </div>
                        <div className="text-green-600 dark:text-green-400 text-sm font-medium">
                          Above $190.00
                        </div>
                      </div>
                      <div className="text-sm text-slate-500 dark:text-slate-400">
                        Alert when price exceeds target
                      </div>
                    </div>
                    <div className="bg-white dark:bg-slate-800 rounded-md p-4 border border-slate-200 dark:border-slate-600">
                      <div className="flex justify-between items-center mb-2">
                        <div className="font-semibold text-slate-900 dark:text-white">
                          TSLA
                        </div>
                        <div className="text-red-600 dark:text-red-400 text-sm font-medium">
                          Below $180.00
                        </div>
                      </div>
                      <div className="text-sm text-slate-500 dark:text-slate-400">
                        Alert when price falls below target
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </main>

      <footer className="bg-white dark:bg-slate-900 border-t border-slate-200 dark:border-slate-800 py-8">
        <div className="container mx-auto px-4">
          <div className="flex flex-col md:flex-row justify-between items-center">
            <div className="mb-4 md:mb-0">
              <div className="text-xl font-bold text-slate-900 dark:text-white">
                TickrAlert
              </div>
              <p className="text-sm text-slate-500 dark:text-slate-400">
                © {new Date().getFullYear()} TickrAlert. All rights reserved.
              </p>
            </div>
            <div className="flex gap-6">
              <a
                href="#"
                className="text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300"
              >
                Terms
              </a>
              <a
                href="#"
                className="text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300"
              >
                Privacy
              </a>
              <a
                href="#"
                className="text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-300"
              >
                Contact
              </a>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}
