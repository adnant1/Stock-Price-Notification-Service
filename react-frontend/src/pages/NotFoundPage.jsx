import { Link } from "react-router-dom";
import Navbar from "../components/Navbar";

export default function NotFoundPage() {
  return (
    <div className="min-h-screen bg-slate-50 dark:bg-slate-900">
      <Navbar />

      <main className="container mx-auto px-4 py-16 flex flex-col items-center justify-center">
        <div className="text-center">
          <h1 className="text-6xl md:text-8xl font-bold text-slate-300 dark:text-slate-700 mb-4">
            404
          </h1>
          <h2 className="text-2xl md:text-3xl font-bold text-slate-900 dark:text-white mb-4">
            Page Not Found
          </h2>
          <p className="text-slate-600 dark:text-slate-400 mb-8">
            The page you are looking for doesn't exist or has been moved.
          </p>
          <Link
            to="/"
            className="px-6 py-3 rounded-lg bg-blue-600 hover:bg-blue-700 text-white font-medium transition-colors inline-block"
          >
            Back to Home
          </Link>
        </div>
      </main>
    </div>
  );
}
