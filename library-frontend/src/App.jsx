import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./App.css";
import Dashboard from "./pages/Dashboard/Dashboard";
import UserLayout from "./pages/UserLayout/UserLayout";
import BookPage from "./pages/Book/BookPage";

function App() {
  return (
    <Routes>
      <Route element={<UserLayout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/books" element={<BookPage />} />
        <Route path="/my-loans" element={<Dashboard />} />
        <Route path="/my-reservations" element={<Dashboard />} />
        <Route path="/my-fines" element={<Dashboard />} />
        <Route path="/subscriptions" element={<Dashboard />} />
        <Route path="/wishlist" element={<Dashboard />} />
        <Route path="/profile" element={<Dashboard />} />
        <Route path="/settings" element={<Dashboard />} />
      </Route>
    </Routes>
  );
}

export default App;
