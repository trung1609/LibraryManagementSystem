import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import "./App.css";
import Dashboard from "./pages/Dashboard/Dashboard";
import UserLayout from "./pages/UserLayout/UserLayout";

function App() {
  return (
    <Routes>
      <Route element={<UserLayout />}>
        <Route path="/" element={<Dashboard />} />
      </Route>
    </Routes>
  );
}

export default App;
